package com.csv.upload.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.csv.upload.util.Response;
import com.csv.upload.util.Util;
import com.mongodb.client.MongoCollection;

/**
 * 
 * @author Arun Natesn This rest API Controller
 *
 */
@RestController
@RequestMapping("/api/csv")
public class CsvController {
	@Autowired
	MongoTemplate mongoTemplate;

	MongoCollection<Document> collection;

	final String COLLECTION_NAME = "csv";

	@PostConstruct
	public void init() {
		collection = mongoTemplate.getCollection(COLLECTION_NAME);
	}

	/**
	 * 
	 * @param file
	 * @return
	 */
	@PostMapping("/upload")
	public ResponseEntity<Response> uploadCSV(@RequestParam("file") MultipartFile file) {
		BufferedReader br = null;
		long startTime = System.currentTimeMillis();
		long totalTimeTaken = 0;

		int totalRecords = 0, sucess = 0, failure = 0;
		Response response = new Response();
		final String FAILURE = "Failure";
		final String SUCESSS = "Sucess";

		int batchSize = 100; // Batch Size definition. This can be increase to

		try {

			String line = null;
			String SEPARATOR = ","; // separator
			String[] header = null;

			int passwordIndex = -1;

			Document document = null;
			List<Document> list = new ArrayList<>();

			InputStream is = file.getInputStream();
			br = new BufferedReader(new InputStreamReader(is));

			while ((line = br.readLine()) != null) {
				if (header == null) {
					// CSV Header Line.
					header = line.split(SEPARATOR);
					/*
					 * Find out password Index to mask.
					 */
					passwordIndex = Util.passwordIndex(header);
				} else {
					totalRecords++;
					if (line != null) {
						// Constructing Document from CSV Line
						document = getDocument(line, SEPARATOR, header, passwordIndex);
						list.add(document);

						try {
							if (collection != null) {
								int size = list.size();
								// Inserting to DB by Batch
								if (size % batchSize == 0) {
									collection.insertMany(list);
									list = new ArrayList<>();
									sucess = sucess + batchSize;
								}
							}
						} catch (Exception ex) {
							failure += batchSize;
							// Logger to be added to print the exception details.
						}

					}

				}
			}
			// The below code insert the remain batch data to DB
			int remainListSize = list.size();
			try {
				if (collection != null) {
					if (remainListSize > 0) {
						collection.insertMany(list);
						sucess = sucess + remainListSize;
					}
				}
			} catch (Exception ex) {
				failure += remainListSize;
				// Logger to be added trace the exception
			}
			totalTimeTaken = System.currentTimeMillis() - startTime;

		} catch (IOException e) {
			response.setStatus(FAILURE);
			response.setErrorMessage("Error occured during reading the CSV file.");
			return ResponseEntity.status(HttpStatus.OK).body(response);
		} finally {
			try {
				// Closing the file reader and null the MongoDB
				br.close();
				mongoTemplate = null;

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		response.setStatus(SUCESSS);
		response.setTotalRecords(totalRecords);
		response.setSucessRecords(sucess);
		response.setFailureRecords(failure);
		response.setToalTimeTaken(totalTimeTaken + " milliseconds.");
		return ResponseEntity.status(HttpStatus.OK).body(response);

	}

	/**
	 * 
	 * @param line
	 * @param SEPARATOR
	 * @param header
	 * @param passwordIndex
	 * @return
	 */
	private Document getDocument(String line, String SEPARATOR, String[] header, int passwordIndex) {
		String[] lineItem;
		Document document;
		document = new Document();
		lineItem = line.split(SEPARATOR);
		for (int i = 0; i < lineItem.length; i++) {
			if (i == passwordIndex) {
				document.put(header[i], Util.encriptPassword(lineItem[i]));
			} else {
				document.put(header[i], lineItem[i]);
			}
		}
		return document;
	}
}

package com.csv.upload.util;
/**
 * 
 * @author Arun Natesan
 *
 */
public class Response {
	private String status;
	private String errorMessage;
	private int totalRecords;
	private int sucessRecords;
	private int failureRecords;
	private String toalTimeTaken;
	public int getTotalRecords() {
		return totalRecords;
	}
	public void setTotalRecords(int totalRecords) {
		this.totalRecords = totalRecords;
	}
	public int getSucessRecords() {
		return sucessRecords;
	}
	public void setSucessRecords(int sucessRecords) {
		this.sucessRecords = sucessRecords;
	}
	public int getFailureRecords() {
		return failureRecords;
	}
	public void setFailureRecords(int failureRecords) {
		this.failureRecords = failureRecords;
	}
	public String getToalTimeTaken() {
		return toalTimeTaken;
	}
	public void setToalTimeTaken(String toalTimeTaken) {
		this.toalTimeTaken = toalTimeTaken;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}


}

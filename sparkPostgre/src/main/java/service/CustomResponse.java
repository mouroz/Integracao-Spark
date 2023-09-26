package service;

public class CustomResponse{
		//super simple class to storage both data (the html to be printed) and error (exceptions)
		private String data;
	 	private String errors[] = new String[10];
	 	//array that will contain all errors.
	 	private int numErrors = 0;
	 
	 	public void setData(String s) {
	 		this.data = s;
	 	}
	 	
	 	public void addError(String s) {
	 		errors[numErrors] = s;
	 		numErrors++;
	 	}
	 	
	 	public int getErrorNum(){
	 		return numErrors;
	 	}
	 	
	 	public String[] getAllErrors() {
	 		return errors;
	 	}
	 	
	 
}

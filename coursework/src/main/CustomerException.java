package main;
//creation of exception class for customer
public class CustomerException extends Exception{
	
	/*
	 * F21AS - Coursework group 4
	 * CustomerException class.
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	
	public CustomerException() {
		super();
	}
	public CustomerException(String message) {
		super(message);
	}

}

package main;

/*
 * F21AS - Coursework group 4
 * 
 * DuplicateItemException class
 * 
 * This exception is thrown if two items are added to the menu
 * with the same itemID.
 * 
 */

public class DuplicateItemException extends Exception {

	private static final long serialVersionUID = 1L;

	public DuplicateItemException(String id) { 
		super("Duplicate item id = " + id + " !!!");
	}
	

}

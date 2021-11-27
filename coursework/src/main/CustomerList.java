package main;
import java.util.HashMap;
import util.Name;

/*
 * F21AS - Coursework group 4
 * CustomerList class
 * 
 * Class to hold customers list (using Singleton Pattern).
 * 
 * Includes methods for adding/deleting and retrieving 
 * information about customers, stored in a HashMap.
 * 
 */


public class CustomerList {
	
	private static CustomerList custListInstance = new CustomerList();
	
	private HashMap<Integer, Name> customers; //stores elements of the form <customerID, Name>
	
	
	private CustomerList() {
		this.customers = new HashMap<Integer, Name>();
	}
	
	/*
	 * Given a custID and a name, it adds this customer to the HashMap (if custID is not used)
	 * Returns true/false if the addition was successful or not.
	 */
	public boolean addCustomer(Integer id, Name name) {
		if(!customers.containsKey(id)) {
			customers.put(id, name);
			return true;
		}
		return false;
	}
	
	/*
	 * Given a custID, it deletes their corresponding customer from the HashMap (if it exists)
	 * Returns true/false if the deletion was successful or not.
	 */
	public boolean deleteCustomer(Integer id) {
		if (!customers.containsKey(id)) {
			return false;
		}
		customers.remove(id);
		return true;
	}
	
	/* 
	 * Given a custId, returns true/false if the customer exists or not. 
	 */
	public boolean existingCustomer(Integer id) {
		return (customers.containsKey(id));
	}
	
	/*
	 * Returns the HashMap with all customers
	 */
	public HashMap<Integer,Name> getCustomers() {
		return customers;
	}
	
	/*
	 * Returns the instance of CustomerList.
	 */
	public static CustomerList getInstance() {
		return custListInstance;
	}
	
}

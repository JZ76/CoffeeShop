package main;
import java.util.*;

import interfaces.Observer;
import interfaces.Subject;
import util.Name;

/*
 * F21AS - Coursework group 4
 * 
 * OrderList class
 * 
 * Stores information about the list of placed orders using a RedBlackTree.
 * 
 */


public class OrderList {

    private RedBlackTree<Integer, ArrayList<Integer>> RBT = new RedBlackTree<Integer,ArrayList<Integer>>();
    private HashMap<Integer,String[]> map = new HashMap<>();
    private CustomerList cusList;
    private DiscountRules dis;
    private Menu m;

    public OrderList(RedBlackTree<Integer, ArrayList<Integer>> RBT,HashMap<Integer,String[]> map, CustomerList cusList, Menu m){
        this.RBT = RBT;
        this.map = map;
        this.cusList = cusList;
        this.m = m;
    }

    /*
     * creates a new (recurring) customer with id "customerID" and name "name"
     */
    public void createCustomer(int customerID, Name name){
        if (!RBT.contains(customerID)){
            ArrayList<Integer> a = new ArrayList<>();
            RBT.put(customerID,a);
        }
        cusList.addCustomer(customerID,name); //returns true if successful, false if customer already exists.
    }

    /*
     * associates an order to a customer
     */
    public void addOrderToCustomer(int customerID, int orderID){
        if (RBT.contains(customerID)) {
            ArrayList<Integer> a = RBT.get(customerID);
            if (!a.contains((Integer) orderID)) {
                a.add(orderID);
                RBT.put(customerID, a);
            }
        }
    }

    /*
     * adds items and timestamp to an order with a given orderID
     */
    public void addOrderInfo(int orderID, String[] items, Date timestamp){
        String[] s = new String[2 + items.length];
        s[0] = String.valueOf(timestamp);
        System.arraycopy(items, 0, s, 2, items.length);
        dis = new DiscountRules(items,m);
        s[1] = String.valueOf(dis.calculate());
        map.put(orderID,s);
    }

    /*
     * given a customerID it deletes it from the customerlist
     */
    public void deleteCustomer(int customerID){
        RBT.delete(customerID);
        cusList.deleteCustomer(customerID);
    }

    /*
     * given an orderID it deletes it from the orderlist
     */
    public void deleteOrder(int orderID){
        for (Map.Entry<Integer,Name> x : cusList.getCustomers().entrySet()){
            ArrayList<Integer> a = RBT.get(x.getKey());
            if (a.contains((Integer) orderID)) {
                a.remove((Integer) orderID);
                RBT.put(x.getKey(), a);
                break;
            }
        }
        map.remove(orderID);
    }

    /*
     * returns the list of orders placed by the customer with id "customerID"
     * returns null if the customerID does not exist.
     */

    public ArrayList<Integer> getOrders(int customerID) {
        return RBT.get(customerID);
    }

    /*
     * Returns the details of all placed orders (order ID, price with discount, timestamp)
     */
    public String[][] getAllOrderDetail(){
        String[][] ans = new String[map.size()][3];
        int i = 0;
        for (Map.Entry<Integer,String[]> entry : map.entrySet()){
            ans[i][0] = String.valueOf(entry.getKey());	//contains orderID
            ans[i][1] = entry.getValue()[1];			//contains total price (with discounts)
            ans[i][2] = entry.getValue()[0];			//contains timestamp
            i++;
        }
        return ans;
    }
    
    /*
     * returns array with all orders' IDs
     */
    public Integer[] getAllOrdersID() {
    	Integer[] orders = new Integer[map.size()];
    	int i = 0;
    	for (Integer orderID : map.keySet()) {
    		orders[i] = orderID;
    		++i;
    	}
    	return orders;
    }
    
    /*
     * Given an order ID returns the details of such order (each item ordered, quantity and price)
     */
    public String[][] getAnOrderDetail(Integer id){
        String[] temp = map.get(id);

        HashMap<String,Integer> t = new HashMap<>();
        for (int i = 2; i < temp.length; i++){
            t.put(temp[i],t.getOrDefault(temp[i],0)+1); 
        }
        String [][] ans = new String[t.size()][3];
        int i = 0;
        for (Map.Entry<String,Integer> entry : t.entrySet()) {
            ans[i][0] = entry.getKey();								//contains itemID
            ans[i][1] = String.valueOf(entry.getValue());			//contains quantity
            ans[i][2] = String.valueOf(m.getPrice(entry.getKey()));	//contains price
            i++;
        }
        return ans;
    }
    
    /*
     * returns list of items with the number of times each has been ordered
     */
    public String[][] getSummaryOfOrders() {
    	
    	Integer[] orders = getAllOrdersID();
    	HashMap<String, Integer> summaryMap = new HashMap<>();
    	
    	for (Integer id : orders) {
    		String[][] details = getAnOrderDetail(id);
    		
    		for (String[] item : details) {
    			String itemID = item[0];
    			Integer quantity = Integer.parseInt(item[1]);
    			
    			if (summaryMap.containsKey(itemID)) {
    				Integer previousQuantity = summaryMap.get(itemID);
    				summaryMap.put(itemID, previousQuantity + quantity);
    			}
    			else summaryMap.put(itemID, quantity);
    		}
    	}
    	
    	String[][] summary = new String[summaryMap.size()][3];
    	
    	int i = 0;
    	for (Map.Entry<String, Integer> item : summaryMap.entrySet()) {
			summary[i][0] = item.getKey();				//item id
			summary[i][1] = m.getName(item.getKey());	//item name
			summary[i][2] = item.getValue().toString();	//number of times ordered
			++i;
		}
    	
    	return summary;
    	
    }

    /*
	*	Returns list of customers with the number of orders they have placed and total money spent
	*/
    public String[][] getSummaryOfCustomers() {
    	
    	String[][] summary = new String[cusList.getCustomers().size()][4];
    	
    	int i = 0;
    	for(Map.Entry<Integer, Name> customer : cusList.getCustomers().entrySet()) {
    		summary[i][0] = String.valueOf(customer.getKey());	 //customerID
    		summary[i][1] = customer.getValue().getFullName();   //customer's name
    		summary[i][2] = String.valueOf(getOrders(customer.getKey()).size()); //number of orders
    		double total = 0;
    		for (Integer orderID : getOrders(customer.getKey())) { 
    			total += Double.parseDouble(map.get(orderID)[1]);
    		}
    		summary[i][3] = String.format("%.2f", total); //total money spent

    		++i;
    	}
    	
    	return summary;
    	
    }
    
    /* 
     * Given an orderID, returns the price paid for the order (with discount)
     */
    public double getOrderPrice(int orderID) {
    	return Double.parseDouble(map.get(orderID)[1]);
    }
    
    /*
     * Given an orderID, returns the number of items in it.
     */
    public int getNumberOfItems(int orderID) {
    	String[][] details = getAnOrderDetail(orderID);
    	int itemCount = 0;
    	for (String[] item : details) {
    		Integer quantity = Integer.parseInt(item[1]);
    		itemCount+= quantity;
    	}
    	return itemCount;
    }
    
    /*
     * Given an orderID, returns an array with quantity and name of each item in the order.
     */
    public String[] getOrderItemsDetail(int orderID) {
    	
    	String[][] details = getAnOrderDetail(orderID);
    	String[] itemsDetail = new String[details.length];
		
    	int i = 0;
		for (String[] item : details) {
			String itemID = item[0];
			Integer quantity = Integer.parseInt(item[1]);
			itemsDetail[i] = quantity + "x " + m.getName(itemID);
			++i;
		}
		
		return itemsDetail;
    }
    
    /* Given an orderID, returns a string with the full name of the customer who placed it
     * returns "unkown customer" if the order doesn't exist
     */
    public String getOrdersCustomer(int orderID) {
    	for(Map.Entry<Integer, Name> customer : cusList.getCustomers().entrySet()) {
    		int customerID = customer.getKey();
    		Name customerName = customer.getValue();
    		if (getOrders(customerID).contains(orderID)) {
    			return customerName.getFullName(); 
    		}
    	}
    	return "unkown customer";
    }
    
    /* 
     * Returns the total revenue fro this OrderList.
     */
    public double getTotalRevenue() {
    	double total = 0;
    	for (Integer orderID : getAllOrdersID()) {
    		total += getOrderPrice(orderID);
    	}
    	return total;
    }
    

    

}






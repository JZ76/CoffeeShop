package io;
import java.io.*;
import java.util.*;

import main.Category;
import main.DuplicateItemException;
import main.Item;
import main.Log;
import main.Menu;
import main.OrderList;
import util.Name;

import java.text.ParseException;
import java.text.SimpleDateFormat;


/*
 * F21AS - Coursework group 4
 * 
 * FileIO class
 * 
 * Contains methods for input/output from text files.
 * Includes: reading existing orders, reading items & establishing menu,
 * 			 writing the log to a file and writing the summary report.
 * 
 */


public class FileIO{
	
	/*
	 * Reads each order from the file "ExistingOrders.txt"
	 * links it with each customer and adds it to the orderList.
	 */
	public void readExistingOrders(OrderList ol) throws IOException {
		
		InputStream inputData = getClass().getResourceAsStream("/files/ExistingOrders.txt");
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputData));
		
		String line = reader.readLine();
		
		while(line != null) {
			int customerID, orderID;
			Name memberName;
			Date timestamp;
			String[] items;
			
			String orderDetails = line;
			try {
				String[] tokens = orderDetails.split(", ");
				customerID = Integer.parseInt(tokens[0]);
				memberName = new Name(tokens[1].trim());
				orderID = Integer.parseInt(tokens[2]);
				timestamp = new SimpleDateFormat("EEE dd-MM-yyyy HH:mm:ss", Locale.ENGLISH).parse(tokens[3]);
				items = new String[tokens.length-4];
				System.arraycopy(tokens, 4, items, 0, tokens.length - 4);
				
				ol.createCustomer(customerID, memberName);
				ol.addOrderToCustomer(customerID, orderID);
				ol.addOrderInfo(orderID, items, timestamp);
				
			} catch(IllegalFormatException | ParseException fe) {
				System.out.println("Data conversion error in '" + orderDetails + "'  - " + fe.getMessage());
			}
			
			line = reader.readLine();
			
		}
		reader.close();
	}
	
	/*
	 * Produces and prints a summary report showing 
	 * how many times each item has been ordered, 
	 * how many times each customer has come to the coffeeshop,
	 * and the total revenue.
	 */
	public void produceSummary (OrderList ol) {
		try {
			FileWriter output = new FileWriter("src/files/Summary.txt");
			
			output.write("Summary report\n");
			output.write("--------------\n\n");
			
			output.write("SUMMARY OF ORDERS:\n\n");
			output.write("ID\t\t\t\tItem\t\t \t#Orders\n");
			
			String[][] orderSummary = ol.getSummaryOfOrders();
			
			for(String[] item : orderSummary) {
				output.write(String.format("%-16s", item[0]) + String.format("%-20s", item[1]) + String.format("%-5s", item[2]) + "\n");
			}
			
			output.write("\n\nSUMMARY OF CUSTOMER ORDERS:\n\n");
			output.write("ID\t\tName\t\t\t #Orders  Total spent (with discounts)\n");
			
			String[][] customerSummary = ol.getSummaryOfCustomers();
			for(String[] cust : customerSummary) {
				output.write(String.format("%-8s", cust[0]) + String.format("%-20s", cust[1]) + String.format("%-6s", cust[2]) + cust[3]  + "\n");
			}
			
			output.write("\nTotal revenue: £" + ol.getTotalRevenue());
			
			output.close();
		}catch(IOException ie) {
			System.out.println("File cannot be written.");
		}
	}
	
	/*
	 * Reads the items and their characteristics from the file "Menu.txt"
	 * checks they are correct and adds them to the menu
	 */
	public void establishMenu(Menu menu) throws IOException {
		
		InputStream inputData = getClass().getResourceAsStream("/files/Menu.txt");
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputData));
		
		String line = reader.readLine();
		
		while(line != null) {
			String id, name, dsp;
			Category cat;
			double price;
			
			String data = line;
			try {
				String[] tokens = data.split(", ");
				id = tokens[0].trim();
				name = tokens[1].trim();
				cat = Category.valueOf(tokens[2].trim());
				price = Double.parseDouble(tokens[3]);
				dsp = tokens[4].trim();
				for(int i = 5; i<tokens.length; i++) {
					dsp += ", " + tokens[i].trim();
				}
				
				Item item = new Item(id, name, cat, dsp, price);
				menu.addToMenu(item);
			} catch(IllegalFormatException fe) {
				System.out.println("Data conversion error in '" + data + "'  - " + fe.getMessage());
			} catch (DuplicateItemException e) {
				System.out.println(e.getMessage() + " -  item will not be added");
			}
			
			line = reader.readLine();
		}
		
		reader.close();
	}
	
	/*
	 * Writes log to file "log.txt"
	 */
	public void genLog(Log log){
		try {
			new PrintWriter("src/files/log.txt").close(); //to delete contents of previous logs
			FileWriter output = new FileWriter("src/files/log.txt",true);
			String[] l = log.getLog();
			for (String s : l) {
				output.write(s);
				output.write("\n");
			}
			output.close();
		} catch(IOException ie) {
			System.out.println("File cannot be written.");
		}
	}

}

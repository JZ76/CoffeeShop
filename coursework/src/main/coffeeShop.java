package main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import io.FileIO;
import model.CustomersInLine;
import views.CustomersInLineGUI;
import controllers.CustomersInLineController;

/*
 * F21AS - Coursework group 4
 * 
 * CoffeeShop class
 * 
 * establishes menu, existing orders, log, worers 
 * 
 */


public class coffeeShop {
    public static void main(String[] args) throws ParseException, IOException {

		int numberOfWorkers = 4; //number of initial worker threads in the simulation.
		Locale.setDefault(new Locale("en", "UK"));
		
		RedBlackTree<Integer, ArrayList<Integer>> RBT = new RedBlackTree<Integer,ArrayList<Integer>>();
        HashMap<Integer,String[]> map = new HashMap<>();
        CustomerList cusList = CustomerList.getInstance();
        
        HashMap<String, String[]> food = new HashMap<>();
        HashMap<String, String[]> beverage = new HashMap<>();
        HashMap<String, String[]> desserts = new HashMap<>();
        
        Menu menu = new Menu(food, beverage, desserts);
        OrderList ol = new OrderList(RBT,map,cusList, menu);
        
        //Read existing orders and establish menu from a file
        FileIO fileIO = new FileIO();
        fileIO.establishMenu(menu);
        fileIO.readExistingOrders(ol);
		
        //create log
        Log log = Log.getInstance();
        
        //create new queue of customers waiting in the shop
		CustomersInLine line = new CustomersInLine(log, ol); //model
		
		//create & start workers and line producer
        Workers staff = new Workers(numberOfWorkers, line, ol);
        staff.startLineProducer(); 
        staff.startWorkers();
		
		//Displays GUI for the simulation
        CustomersInLineGUI view = new CustomersInLineGUI(staff, line); //view
		CustomersInLineController controller = new CustomersInLineController(view, staff, fileIO, log);
		
		//busy wait until the cafe has closed
		while (! line.getClosedCafe()) {
			try {Thread.sleep(1000);
			} catch (InterruptedException excep){System.out.println("Exception: "+ excep);};
		}
		//Generate summary report
        fileIO.produceSummary(ol);
		
        /*Order system graphic user interface (Stage1)
    	CafeGUI ui = new CafeGUI(400, 200, 1200, 800,menu,ol,fileIO);
   		ui.pack();
  		ui.setVisible(true);*/
        
    }
}

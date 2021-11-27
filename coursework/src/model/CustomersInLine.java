package model;

import java.text.SimpleDateFormat;
import java.util.*;

import interfaces.Observer;
import interfaces.Subject;
import main.Log;
import main.OrderList;

/*
 * F21AS - Coursework group 4
 * 
 * CustomersInLine class
 * 
 * Contains thread-safe methods to add and take orders to the line
 * and methods to be used by the DisplayGUI regarding current order processing.
 * 
 */

public class CustomersInLine implements Subject{

	private OrderList ol;
	private ArrayList<Integer> line; //stores the orders to be processed
	
	private boolean lineComplete;	 //is true if all orders have been added to the queue
	private boolean closedCafe;		 //is true if all orders have been added & processed

	//contains items of the form <ThreadName, orderID being processed by Thread>
		//orderID is -1 if thread is waiting for an order to process
		//orderID is -2 if thread is on a break
	private HashMap<String, Integer> staffStatus; 
	
	private int orderBeingAdded; //orderID that is currently being added to the queue (-1 if none is being added)
	private int requiredWorkers; //number of required worker threads (depends on line length): req_threads = Math.ceil(line_length/2)

	
	//used for the log
	private Log log;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //used for the timestamp in the log
	private Date date;
	
	
    public CustomersInLine(Log log, OrderList ol){
    	
    	this.staffStatus = new HashMap<String, Integer>();
		line = new ArrayList<Integer>();
		this.log = log;
		this.ol = ol;
		lineComplete = false;
		closedCafe = false; //initially the cafe is open
		requiredWorkers = 1; //one worker must always be at the counter (at least to close the cafe)
	}
    
    //Thread-safe method used by LineProducer to add orders to the queue
	synchronized public void put(Integer orderID) { 
				
		line.add(orderID); //add order to the queue
		orderBeingAdded = orderID; //stores which order is being added
		
		notifyObservers(); //notifies Observers of order added
		
		//updates the required workers because queue length has changed
		requiredWorkers = (int) Math.ceil((double) line.size()/2); 
		notifyAll(); //wakes up any waiting workers because they may be needed
		
		//store to the log
		Calendar calendar = Calendar.getInstance();
		date = calendar.getTime();
		String dateStringParse = sdf.format(date);
		log.add(dateStringParse + ": order " + orderID +" is added to the queue");
		
	}
    
	/* Returns the orderID to process or -1 if the cafe has closed.
	*/
	synchronized public Integer take() throws InterruptedException {
		
		Integer orderID;
		String threadName = Thread.currentThread().getName();
		
		//if this worker is not required (based on queue length) it takes a break
		while (requiredWorkers < Character.getNumericValue(threadName.charAt(7))) {
			staffStatus.put(threadName, -2);  //-2 means on a break 	
			wait();	//wait until requiredWorkers is updated or the cafe closes
			if (closedCafe) return -1; //return to terminate this thread
		 }
			
		//if the line is empty
		while (line.size() == 0) {
			if (lineComplete) {
				setClosedCafe(); //if no more orders will be added: the cafe closes
				notifyObservers();
				return -1;
			}
				
			staffStatus.put(threadName, -1);   //this thread is waiting to process an order
			notifyObservers(); 
			wait(); //wait until the line is not empty
		}	
			
		//otherwise, take order and remove it from line.
		orderID = line.get(0); 
		line.remove(0);
		
		staffStatus.put(threadName, orderID); //add information about this thread and orderID.
		notifyObservers(); //notify observers of an order being processed
		
		//update RequiredWorkers since queue length has changed
		requiredWorkers = (int) Math.ceil((double) line.size()/2);
		if (requiredWorkers == 0) requiredWorkers = 1;
		notifyAll(); //wake up any waiting workers that may be needed now
		
		//add information to the log
		Calendar calendar = Calendar.getInstance();
		date = calendar.getTime();
		String dateStringParse = sdf.format(date);
		log.add(dateStringParse + ": order " + orderID +" is processed by " + Thread.currentThread().getName());
		
		return orderID;
	}

	public void setLineComplete() { 
		lineComplete = true; 
		orderBeingAdded = -1; //no orders are being added anymore
	}

	private void setClosedCafe() {
		closedCafe = true;
		
		//store to the log
		Calendar calendar = Calendar.getInstance();
		date = calendar.getTime();
		String dateStringParse = sdf.format(date);
		log.add(dateStringParse + ": The Cafe is now closed");
		
		notifyObservers();		//Notify observers that simulation is over.
		notifyAll();			//workers are woken up to terminate
	}

	public int getRequiredWorkers() { return requiredWorkers;}
	synchronized public boolean getLineComplete() {return lineComplete;}
	synchronized public boolean getClosedCafe() {return closedCafe;}
	
	/* returns a string of the orders waiting to be processed.
	 * 	 with the format: "There are currently ... customers waiting in line:
	 	Customer0's Full Name (orderID): nb. of items.
	  	Customer1's Full Name (orderID): nb. of items.
	  	...
	 */
	public String ordersToString() {
		String result = "There are currently " + line.size() + " customers waiting in line:\n";
		for (Integer orderID : line) {
			result += " " + ol.getOrdersCustomer(orderID) 
			+ " (order " + orderID + ") : " + ol.getNumberOfItems(orderID) + " items.\n";
		}
		return result;
	}
	
	/*returns a sorted arrayList with what each worker is doing.
	Each cell has the format:
		Worker-i: OrderID
		1x Item0
		2x Item1
		...
		TOTAL: £XX.X
	*/
	public ArrayList<String> staffToString() {
		ArrayList<String> result = new ArrayList<String>();
		
		//translate each entry of the form <threadName, orderID> to a more detailed format
		for (Map.Entry<String, Integer> entry : staffStatus.entrySet()) {
			String threadName = entry.getKey();
			int orderID = entry.getValue();
			
			String s = threadName + ":";
			
			
			//if orderID is -2 worker is on a break
			if (orderID == -2 && !closedCafe) {
				s+= " is on a break";
			}
			
			
			//if orderID is -1 then the thread is not processing anything
			//otherwise, get the items and total price details:
			else if (orderID != -1 && !closedCafe) {
				s+= " order " +  orderID + "\n";
				String[] itemsDetail = ol.getOrderItemsDetail(orderID);
				for (String item : itemsDetail) {
					s += item + "\n";
				}
				s += "TOTAL: £" + ol.getOrderPrice(orderID);
			}
			
			result.add(s + "\n\n");
		}
		Collections.sort(result); //sort the ArrayList so that workers are always displayed in order
		return result;
	}
	
	/*
	 * returns a String with the order being added to the queue. (empty if none).
	 */
	public String lineProducerToString() {
		String result = " ";
		if (orderBeingAdded != -1) {
			result += "Line producer is adding order " + orderBeingAdded + " to the queue.\n";
		}
		else if (closedCafe) {
			result += "The cafe is now closed! Total revenue: £ " + ol.getTotalRevenue();
		}
		return result;
	}
		
	
    /**
     * Create list to hold any observers of subject (CustomersInLine)
     */
	private List<Observer> registeredObservers = new LinkedList<Observer>();
	/**
	 * register an observer with this subject
	 */
	@Override
	public void registerObserver(Observer obs) {
		registeredObservers.add(obs);
	}
    /**
     * De-register an observer with this subject
     */
	@Override
	public void removeObserver(Observer obs) {
		registeredObservers.remove(obs);
	}
    /**
     * Inform all observers that there has been an update
     */
	@Override
	public void notifyObservers() {
		for (Observer obs : registeredObservers)
			obs.update();
	}
	
}

package main;

import model.CustomersInLine;

/*
 * F21AS - Coursework group 4
 * 
 * Workers class
 * 
 * Creates and starts worker threads (that process orders in the queue) 
 * and a lineProducer thread (adds existing orders to the queue)
 * 
 */

public class Workers {
	
	private CustomersInLine line;
	private int numberOfWorkers;
	private OrderList ol;
	
	private int itemProcessTime  = 2000; //initial time to process an item of an order (in miliseconds)
	private int addOrderTime = 1000; //time between additions of orders to the line (in miliseconds)
	
	public Workers(int numberOfWorkers, CustomersInLine line, OrderList ol) {
		this.numberOfWorkers = numberOfWorkers;
		this.line = line;
		this.ol = ol;
	}
	
	
	class WorkerTask implements Runnable  { 
		
		public void run() { 
			
			//while the cafe is still open
			while(! line.getClosedCafe()) {		
				try {
					Integer orderID = line.take(); //try to take the order at begining of line
					if (orderID != -1) {
						//depending on the number of items an order takes longer to process
						int nbOfItems = ol.getNumberOfItems(orderID);
						Thread.sleep(itemProcessTime*nbOfItems);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}			
			}
		}
	}
	 
	//Creates and starts a new worker thread
	private void addAWorker() {
		++numberOfWorkers;
		Thread t = new Thread(new WorkerTask(), "Worker-" + numberOfWorkers);
		t.start();
	}
	
	//Creates and starts initial worker threads
	public void startWorkers() {
		
		for (int i = 1; i <= numberOfWorkers; ++i) {
			Thread t = new Thread(new WorkerTask(), "Worker-" + i);
			t.start();
			try {Thread.sleep(100);
			} catch (InterruptedException excep){System.out.println("Exception: "+ excep);};

		}
		
	}
	
	class ProducerTask implements Runnable  { 
		
		public void run() { 
			
			Integer [] orders = ol.getAllOrdersID(); //get all existing orders form orderList
			
			//for each existing order, add it to the queue
			for (int i = 0; i < orders.length; ++i) {
				Integer orderID = orders[i];
				
				try {
					line.put(orderID);
					Thread.sleep(addOrderTime); //wait between adding orders to the queue
				} catch (InterruptedException excep){System.out.println("Exception: "+ excep);};
				
				//if one more worker is required
				//requiredWorkers = Math.ceil(line_length/2);
				if (line.getRequiredWorkers() > numberOfWorkers) addAWorker();
				
			}
			
			line.setLineComplete(); //signals that all orders have been added to the queue
		}
	}
	
	//Creates and starts the thread that adds orders to the queue
	public void startLineProducer() {
		Thread lineProducer = new Thread(new ProducerTask(), "Line producer");
		lineProducer.start();
		
	}
	
	//sets new time per item (in ms) depending on slider from the GUI.
	public void setItemProcessPeriod (int newTime) {
		this.itemProcessTime = newTime;
	}

}

package views;
import java.awt.*;
import javax.swing.*;

import interfaces.Observer;
import model.CustomersInLine;

/*
 * F21AS - Coursework group 4
 * 
 * OrderDisplay class
 * 
 *  A display for the orders in line. An observer of the CustomersInLine.
 * 
 */


public class OrderDisplay extends JPanel implements Observer {
	
	private CustomersInLine cildata; //the customersInLine data
	private JTextArea cilText = new JTextArea(12, 5); //customers in line display
	private JPanel panel;
	private JScrollPane scrollPane;


	public OrderDisplay (CustomersInLine line) {
		this.cildata = line;
		line.registerObserver(this);
		
		panel = new JPanel();

		cilText.setEditable(false);
		cilText.setLineWrap(true);
		cilText.setWrapStyleWord(true);
		cilText.setAlignmentX(BOTTOM_ALIGNMENT);
		cilText.setMargin( new Insets(10,10,10,10) ); //adds margins to JTextArea

		Font timeFont = new Font("SansSerif", Font.BOLD, 12);
		cilText.setFont(timeFont);
		update();
		
		scrollPane = new JScrollPane(cilText);
		scrollPane.setViewportView(cilText);
		scrollPane.setPreferredSize(new Dimension(400,300));
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		panel.add(scrollPane);
		
		this.add(panel);
		
	}
	
	/**
	 * Tell the Observer to update itself
	 */
	public void update() {
		String text = cildata.ordersToString();
		cilText.setText(text);
	}

}

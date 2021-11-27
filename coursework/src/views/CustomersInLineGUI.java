package views;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import io.FileIO;
import main.Log;
import main.Workers;
import model.CustomersInLine;

/*
 * F21AS - Coursework group 4
 * 
 * CustomersInLineDisplayGUI class
 * 
 * JFrame for displaying customersInLine (heavily influenced by notes)
 * Shows what line producer, workers and the queue look like at every moment.
 * 
 */


public class CustomersInLineGUI extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	private JPanel mainPane;
	private JSlider timeSlider;
	
	private Workers staff;
	private CustomersInLine model;
	
	private static final int MIN_MS = 500; 	//minimum ms in time slider
	private static final int MAX_MS = 3500; //maximum ms in time slider
	private static final int INIT_MS = 2000; //default ms in time slider
	
	private static final Font TIME_FONT = new Font("SansSerif", Font.PLAIN, 12);

    //Creates the first JFrame, this will display what the line producer is doing and what orders are being processed/next in queue
	public CustomersInLineGUI(Workers staff, CustomersInLine model) {
		
		mainPane = new JPanel();
		mainPane.setLayout(new BoxLayout(mainPane, BoxLayout.PAGE_AXIS));
		this.add(mainPane);

		this.staff = staff;
		this.model = model;
		
		setNorthPanel();
		setCenterPanel();		
		
		this.setLocation(500, 200);
		this.setSize(new Dimension(500, 500));
		this.setVisible(true);
		this.pack();
		
	}
	
	//adds a central panel to JFrame containing Order & Worker Displays
	public void setCenterPanel() {
		
		JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.LEADING, 5, 5));
		
		centerPanel.add(new OrderDisplay(model));
		centerPanel.add(new WorkerDisplay(model));
		
		mainPane.add(centerPanel);
	}

	//adds a north panel to JFrame
	public void setNorthPanel() {

		//panel for slider to choose miliseconds per item.
		JPanel sliderPanel = new JPanel();
		sliderPanel.setLayout(new BoxLayout(sliderPanel, BoxLayout.PAGE_AXIS));

		JLabel msPerItemLabel = new JLabel ("Milliseconds per item:");
		msPerItemLabel.setHorizontalTextPosition(JLabel.CENTER);

		msPerItemLabel.setFont(TIME_FONT);

		timeSlider = new JSlider(JSlider.HORIZONTAL, MIN_MS, MAX_MS, INIT_MS);
		msPerItemLabel.setLabelFor(timeSlider);
		
		sliderPanel.add(msPerItemLabel);
		sliderPanel.add(timeSlider);
		
		timeSlider.setMajorTickSpacing(1000); //label each 1000 ms
		timeSlider.setMinorTickSpacing(500); //ticks drawn for each 500ms
		timeSlider.setPaintTicks(true);
		timeSlider.setPaintLabels(true);
		
		JPanel northPanel = new JPanel(new FlowLayout(FlowLayout.LEADING, 5, 5));
		northPanel.add(new LineProducerDisplay(model));
		northPanel.add(sliderPanel);
		mainPane.add(northPanel);
	}
	
	//return chosen time in timeSlider
	public int getMSPerItem() {
		int msPerItem = -1;
	    if (!timeSlider.getValueIsAdjusting()) msPerItem = (int)timeSlider.getValue();
	    return msPerItem;
	}
	
	//adds changeListener to timeSlider
	public void addChangeListener(ChangeListener cl) {
		timeSlider.addChangeListener(cl);
	}
	
	public void addWindowListener(WindowAdapter wa) {
		
		this.addWindowListener(wa);
		
		
	}


}

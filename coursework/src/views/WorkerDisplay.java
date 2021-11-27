package views;


import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import interfaces.Observer;
import model.CustomersInLine;

/*
 * F21AS - Coursework group 4
 * 
 * WorkerDisplay class
 * 
 * A display for what workers are doing/what orders they are working on. An observer of the CustomersInLine.
 * 
 */

public class WorkerDisplay extends JPanel implements Observer {
	
	private CustomersInLine wdata; //the customersInLine data
	private JTextArea wText = new JTextArea(); //Workers text
	private JScrollPane scrollPane;
	private JPanel panel;
	//JFrame frame;
	
	
	//This creates a new JFrame which contains the threads of what each worker is doing in 'real-time'.
	public WorkerDisplay (CustomersInLine line) {
		this.wdata = line;
		line.registerObserver(this);
		
		panel = new JPanel();

		wText.setEditable(false);
		wText.setLineWrap(true);
		wText.setWrapStyleWord(true);
		wText.setAlignmentX(BOTTOM_ALIGNMENT);
		wText.setMargin(new Insets(10,10,10,10));

		Font timeFont = new Font("SansSerif", Font.BOLD, 12);
		wText.setFont(timeFont);
		update();
		
		scrollPane = new JScrollPane(wText);
		scrollPane.setViewportView(wText);
		scrollPane.setPreferredSize(new Dimension(400,300));
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		
		panel.add(scrollPane);
		
		this.add(panel);
		
	}
	
	//Tell observer to update with what the worker is doing
	public void update() {
		wText.setText("");
	   for (String s : wdata.staffToString()){
		    wText.setText(wText.getText()+s);
	   }
	}
}


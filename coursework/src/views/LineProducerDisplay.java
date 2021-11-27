package views;
import java.awt.*;
import javax.swing.*;

import interfaces.Observer;
import model.CustomersInLine;


/*
 * F21AS - Coursework group 4
 * 
 * LineProducerDisplay class
 * 
 * A display for what line producers are doing. An observer of the CustomersInLine.
 * 
 */


public class LineProducerDisplay extends JPanel implements Observer {
	
	public static final Color LIGHT_BLUE = new Color(51,153,255);
	private CustomersInLine lpdata; //the customersInLine data
	private JTextField lpText = new JTextField(42); //line producer display

	//shows what the line producer is doing
	public LineProducerDisplay (CustomersInLine line) {
		this.lpdata = line;
		line.registerObserver(this);
		
		this.add(lpText);
		lpText.setEditable(false);
		lpText.setHorizontalAlignment(JTextField.CENTER);
		Font timeFont = new Font("SansSerif", Font.BOLD, 14);
		lpText.setFont(timeFont);
		update();
	}


	public void update() {
		String text = lpdata.lineProducerToString();
		lpText.setText(text);
		if (text.contains("closed")) { //change text to blue when cafe is closed
			lpText.setForeground(LIGHT_BLUE);
		}
	}
	
	

}
package controllers;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import io.FileIO;
import main.Log;
import main.Workers;
import model.CustomersInLine;
import views.CustomersInLineGUI;

public class CustomersInLineController {
	
	private CustomersInLineGUI view;
	private Workers staff;
	private FileIO io;
	private Log log;
	
	public CustomersInLineController (CustomersInLineGUI view, Workers staff, FileIO io, Log redoLog) {
		this.view = view;
		this.staff = staff;
		this.io = io;
		this.log = redoLog;
		
		view.addChangeListener(new SliderListener());
		view.addWindowListener(new WindowClosingListener());
	}
	
	public class SliderListener implements ChangeListener {
		
		public void stateChanged(ChangeEvent e) {

		    int msPerItem = view.getMSPerItem();
		    if (msPerItem != -1) staff.setItemProcessPeriod(msPerItem);

		}
	}
	
	public class WindowClosingListener implements WindowListener {

		@Override
		public void windowClosing(WindowEvent e) {
			
			int result = JOptionPane.showConfirmDialog(view, "Exit?", "Really?",
					JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
			
			//the programme will print log and exit.
			if(result == JOptionPane.OK_OPTION){
				io.genLog(log);
				view.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			}
			else { //otherwise nothing happens
				view.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			}
			
		}

		public void windowOpened(WindowEvent e) {}
		public void windowClosed(WindowEvent e) {}
		public void windowIconified(WindowEvent e) {}
		public void windowDeiconified(WindowEvent e) {}
		public void windowActivated(WindowEvent e) {}
		public void windowDeactivated(WindowEvent e) {}

		
	}

	
	
}

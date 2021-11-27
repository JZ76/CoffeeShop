package views;

import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.swing.*;

import io.FileIO;
import main.Menu;
import main.OrderList;

/*
 * F21AS - Coursework group 4
 * 
 * CafeGUI class
 * 
 * Manages GUI for placing orders (Stage1)
 * 
 */

public class CafeGUI extends JFrame implements ActionListener{

	JPanel headPanel, mainPanel, menuPanel, cartPanel, orderListPanel;
	JLabel head;
	JButton menu, checkout, orderList, food, beverage, dessert, back, next, check, cancel, add1, add2,add3;
	JTable orderDetails, orderOverview, orderNote;	//orderDetails(od)->cartPanel; orderOverview(oo)&orderNote-> orderListPanel
	JTextArea productDescription, receipt;
	JTextArea item1, item2, item3;
	JTextField item1Amount, item2Amount, item3Amount;
	JTable meun, order;
	Menu menu1;
	OrderList ol;
	ArrayList<String> temp = new ArrayList<>();
	ArrayList<String> redoLog;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	Date date;
	private String[] odColumnTitle = {"Order Details", "Amount", "Unit Price"};
	/*
	private String[][] odRowData = {
					{"Ham Sandwich", "1", "?2.00"},
					{"Coca-Cola", "1", "?5.50"}
					};

	 */
	private String[] ooColumnTitle = {"Order No.", "Total Price", "Time"};
	/*
	private String[][] ooRowData= { 
					{"Paid", "00001", "Mon 01-02-2021 14:00"},
					{"Pending", "00002", "Mon 01-02-2021 14:55"}
					};

	 */
	private int[] bound = {30,40,200,300};
	
	public CafeGUI(int x, int y, int w, int z,Menu menu,OrderList ol,FileIO io) {
		this.setTitle("21 Century Cafe Order System");
		this.setDefaultCloseOperation(CafeGUI.EXIT_ON_CLOSE);
		this.setLocation(x, y);
		this.setPreferredSize(new Dimension(w, z));
		this.getContentPane().setBackground(Color.ORANGE);
		this.setLayout(new BorderLayout(5,3));
		redoLog = new ArrayList<>();
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));


		this.menu1 = menu;
		this.ol = ol;
		setHeadPanel();
		setMainPanel();

		setOrderListPanel();
		setCartPanel();
		setMenuPanel();
	}

	public void setHeadPanel() {
		headPanel = new JPanel();
		headPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		headPanel.setBackground(Color.ORANGE);
		head = new JLabel(new ImageIcon(".\\Title.png"));
		head.setFont(new Font("Times New Roman", Font.PLAIN, 36));
		head.setForeground(Color.BLACK);
		head.setPreferredSize(new Dimension(344, 50));
		headPanel.add(head);
		this.add(headPanel, BorderLayout.NORTH);
	}
	
	public void setMainPanel() {
		mainPanel = new JPanel();
		mainPanel.setPreferredSize(new Dimension(50, 300));
		mainPanel.setLayout(new GridLayout(3, 1));
		//mainPanel.setBackground(Color.GREEN);
		menu = new JButton(new ImageIcon(".\\Menu.png"));
		//menu.addActionListener(new ActionListener());
		menu.setPreferredSize(new Dimension(30,30));
		menu.setBorder(null);
		checkout = new JButton(new ImageIcon(".\\Checkout.png"));
		checkout.setPreferredSize(new Dimension(30,30));
		checkout.setBorder(null);
		orderList = new JButton(new ImageIcon(".\\OrderList.png"));
		orderList.setPreferredSize(new Dimension(30,30));
		orderList.setBorder(null);
		menu.addActionListener(this);
		checkout.addActionListener(this);
		orderList.addActionListener(this);
		mainPanel.add(menu);
		mainPanel.add(checkout);
		mainPanel.add(orderList);
		this.add(mainPanel, BorderLayout.WEST);
	}
	
	public void setMenuPanel() {
		//menuPanel = new JPanel();
		//Image image = new ImageIcon(".\\menuPanelBack.png").getImage();
		menuPanel = new BackgroundPanel(new ImageIcon(".\\menuPanelBack.png").getImage());
		menuPanel.setLayout(new GridLayout(2, 1));
		
		JPanel catPanel = new JPanel();
		catPanel.setOpaque(false);
		catPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		menuPanel.add(catPanel);	//centrePanel(1, 1)
		food = new JButton(new ImageIcon(".\\Food.png"));
		//food.setContentAreaFilled(false);
		food.addActionListener(this);
		food.setBorder(null);
		catPanel.add(food);
		beverage = new JButton(new ImageIcon(".\\Beverage.png"));
		//beverage.setContentAreaFilled(false);
		beverage.setBorder(null);
		beverage.addActionListener(this);
		catPanel.add(beverage);
		dessert = new JButton(new ImageIcon(".\\Dessert.png"));
		//dessert.setContentAreaFilled(false);
		dessert.setBorder(null);
		dessert.addActionListener(this);
		catPanel.add(dessert);
				
		JPanel subMenu = new JPanel();
		subMenu.setOpaque(false);
		subMenu.setLayout(new GridLayout(2, 1));
		menuPanel.add(subMenu);	//centrePanel(2, 1)
		
		JPanel subMenuBlk1 = new JPanel();
		subMenuBlk1.setOpaque(false);
		subMenuBlk1.setLayout(new GridLayout(1, 2));
		JPanel itemsPanel = new JPanel();
		itemsPanel.setOpaque(false);
		itemsPanel.setLayout(new GridLayout(3, 1));
		//JLabel item1 = new JLabel("1st product");
		//JLabel item2 = new JLabel("2nd product");
		//JLabel item3 = new JLabel("3rd product");
		item1 = new JTextArea("1st product",3,50);
		item1.setEditable(false);
		item1.setLineWrap(true);
		item1.setWrapStyleWord(true);
		item2 = new JTextArea("2nd product", 3, 50);
		item2.setEditable(false);
		item2.setLineWrap(true);
		item2.setWrapStyleWord(true);
		item3 = new JTextArea("3rd product", 3, 50);
		item3.setEditable(false);
		item3.setLineWrap(true);
		item3.setWrapStyleWord(true);
		itemsPanel.add(item1);
		itemsPanel.add(item2);
		itemsPanel.add(item3);
		subMenuBlk1.add(itemsPanel);
		
		JPanel addToCartPanel = new JPanel();
		addToCartPanel.setOpaque(false);
		addToCartPanel.setLayout(new GridLayout(3,3));
		JLabel amount1 = new JLabel("Amount:");
		JLabel amount2 = new JLabel("Amount:");
		JLabel amount3 = new JLabel("Amount:");
		item1Amount = new JTextField("1", 2);
		item2Amount = new JTextField("1", 2);
		item3Amount = new JTextField("1", 2);
		add1 = new JButton("Add");
		add2 = new JButton("Add");
		add3 = new JButton("Add");
		add1.addActionListener(this);
		add2.addActionListener(this);
		add3.addActionListener(this);
		addToCartPanel.add(amount1);
		addToCartPanel.add(item1Amount);
		addToCartPanel.add(add1);
		addToCartPanel.add(amount2);
		addToCartPanel.add(item2Amount);
		addToCartPanel.add(add2);
		addToCartPanel.add(amount3);
		addToCartPanel.add(item3Amount);
		addToCartPanel.add(add3);
		subMenuBlk1.add(addToCartPanel);
		subMenu.add(subMenuBlk1);
		
		JPanel subMenuBlk2 = new JPanel();
		subMenuBlk2.setOpaque(false);
		subMenuBlk2.setLayout(new FlowLayout(FlowLayout.RIGHT));
		back = new JButton(new ImageIcon(".\\Back.png"));
		next = new JButton(new ImageIcon(".\\Next.png"));
		subMenuBlk2.add(back);
		subMenuBlk2.add(next);
		subMenu.add(subMenuBlk2);

		this.add(menuPanel, BorderLayout.CENTER);
	}
	
	public class BackgroundPanel extends JPanel {
		private static final long serialVersionUID = -6352788025440244338L;
		private Image img;

		public BackgroundPanel(Image img) {
			this.img = img;
		}

		protected void paintComponent(Graphics g) {
			g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
		}
	}

	public void setOrderListPanel() {
		orderListPanel = new JPanel();
		orderListPanel.setLayout(new FlowLayout());
		int[] bd1 = {6,8,50,75};
		int[] bd2 = {6,8,50,75};
		orderDetails = new JTable(){
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		order = new JTable(ol.getAllOrderDetail(),ooColumnTitle){
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		order.setRowSelectionAllowed(true);
		order.setColumnSelectionAllowed(true);

		order.setBounds(bd2[0], bd2[1], bd2[2], bd2[3]);
		order.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JScrollPane sp = new JScrollPane(order);
		JScrollPane sp1 = new JScrollPane(orderDetails);
		//setTable(orderListPanel, ooRowData, ooColumnTitle, bd1);
		JLabel arrow = new JLabel(new ImageIcon(".\\Arrow.png"));
		order.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				orderListPanel.remove(2);
				int y = order.getSelectedRow();
				String id = (String) order.getValueAt(y,0);
				orderDetails = new JTable(ol.getAnOrderDetail(Integer.valueOf(id)),odColumnTitle);
				orderDetails.setBounds(bd1[0], bd1[1], bd1[2], bd1[3]);
				JScrollPane sp1 = new JScrollPane(orderDetails);
				orderListPanel.add(sp1,BorderLayout.EAST);
			}
		});


		orderListPanel.add(sp,BorderLayout.WEST);

		orderListPanel.add(arrow, BorderLayout.CENTER);
		//setTable(orderListPanel, odRowData, odColumnTitle, bd2);
		orderListPanel.add(sp1,BorderLayout.EAST);
		this.add(orderListPanel);

	}

	public void setCartPanel() {
		cartPanel = new JPanel();
		cartPanel.setLayout(new GridLayout(1, 2));
		//setTable(cartPanel, odRowData, odColumnTitle, bound);
		//get data from "temp" and insert the data into
		String[][] orderItems = new String[temp.size()][3];
		for(int i = 0; i<temp.size(); i++) {
			orderItems[i][0] = temp.get(i);
			orderItems[i][1] = "Amount";
			orderItems[i][2] = "Unit Price";
		}
		setTable(cartPanel, orderItems, odColumnTitle, bound);
		JPanel shopDetail = new JPanel();
		shopDetail.setLayout(new GridLayout(2, 1));
		//receipt details
		String rd = "Date: OOOOOO\n"+
					"Time:\n"+
					"Buyer:\n"+
					"Items:\n"+
					"Total:\n";
		receipt = new JTextArea(rd, 7, 20);
		shopDetail.add(receipt);
		JPanel ckOrCancel = new JPanel();
		ckOrCancel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		cancel = new JButton("Cancel");
		check = new JButton("Check");
		ckOrCancel.add(cancel);
		cancel.addActionListener(this);
		ckOrCancel.add(check);
		check.addActionListener(this);
		shopDetail.add(ckOrCancel);
		cartPanel.add(shopDetail);

		this.add(cartPanel, BorderLayout.CENTER);
	}
	
	public void setTable(JPanel jp, String[][] rowData, String[] columnTitle, int[] bound) {
		JTable jt = new JTable(rowData, columnTitle);
		jt.setBounds(bound[0], bound[1], bound[2], bound[3]);
		JScrollPane sp = new JScrollPane(jt);
		jp.add(sp);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==menu){
			this.remove(cartPanel);
			this.remove(orderListPanel);
			this.add(menuPanel, BorderLayout.CENTER);
			validate();
			Calendar calendar = Calendar.getInstance();
			date = calendar.getTime();
			String dateStringParse = sdf.format(date);
			redoLog.add(dateStringParse + ": Customer click menu button");
		}else if (e.getSource()==checkout){
			this.remove(orderListPanel);
			this.remove(menuPanel);
			this.add(cartPanel, BorderLayout.CENTER);
			validate();
			Calendar calendar = Calendar.getInstance();
			date = calendar.getTime();
			String dateStringParse = sdf.format(date);
			redoLog.add(dateStringParse + ": Customer click check out button");
		}else if (e.getSource()==orderList){
			this.remove(menuPanel);
			this.remove(cartPanel);
			this.add(orderListPanel, BorderLayout.CENTER);
			validate();
			Calendar calendar = Calendar.getInstance();
			date = calendar.getTime();
			String dateStringParse = sdf.format(date);
			redoLog.add(dateStringParse + ": Customer click Order list button");
		}else if (e.getSource()==food){
			item1.setText(menu1.getDetailsFood(1));
			item2.setText(menu1.getDetailsFood(2));
			item3.setText(menu1.getDetailsFood(3));
			Calendar calendar = Calendar.getInstance();
			date = calendar.getTime();
			String dateStringParse = sdf.format(date);
			redoLog.add(dateStringParse + ": Customer swap list to food");
		}else if (e.getSource()==beverage){
			item1.setText(menu1.getDetailsBeverage(1));
			item2.setText(menu1.getDetailsBeverage(2));
			item3.setText(menu1.getDetailsBeverage(3));
			Calendar calendar = Calendar.getInstance();
			date = calendar.getTime();
			String dateStringParse = sdf.format(date);
			redoLog.add(dateStringParse + ": Customer swap list to beverage");
		}else if (e.getSource()==dessert){
			item1.setText(menu1.getDetailsDessert(1));
			item2.setText(menu1.getDetailsDessert(2));
			item3.setText(menu1.getDetailsDessert(3));
			Calendar calendar = Calendar.getInstance();
			date = calendar.getTime();
			String dateStringParse = sdf.format(date);
			redoLog.add(dateStringParse + ": Customer swap list to dessert");
		}else if (e.getSource()==add1){
			for (int i = 0; i < Integer.parseInt(item1Amount.getText()); i++) {
				temp.add(item1.getText().substring(1, item1.getText().indexOf(")")));
			}
			Calendar calendar = Calendar.getInstance();
			date = calendar.getTime();
			String dateStringParse = sdf.format(date);
			redoLog.add(dateStringParse + ": Customer add the first food");
		}else if (e.getSource()==add2){
			for (int i = 0; i < Integer.parseInt(item2Amount.getText()); i++) {
				temp.add(item2.getText().substring(1, item2.getText().indexOf(")")));
			}
			Calendar calendar = Calendar.getInstance();
			date = calendar.getTime();
			String dateStringParse = sdf.format(date);
			redoLog.add(dateStringParse + ": Customer add the second food");
		}else if (e.getSource()==add3){
			for (int i = 0; i < Integer.parseInt(item3Amount.getText()); i++) {
				temp.add(item3.getText().substring(1, item3.getText().indexOf(")")));
			}
			Calendar calendar = Calendar.getInstance();
			date = calendar.getTime();
			String dateStringParse = sdf.format(date);
			redoLog.add(dateStringParse + ": Customer add the third food");
		}else if(e.getSource()==cancel) {
			temp = new ArrayList<>();
			this.remove(cartPanel);
			this.remove(orderListPanel);
			this.add(menuPanel, BorderLayout.CENTER);
			validate();
			Calendar calendar = Calendar.getInstance();
			date = calendar.getTime();
			String dateStringParse = sdf.format(date);
			redoLog.add(dateStringParse + ": Customer click cancel button");
		}else if(e.getSource()==check) {
			
		}
	}
}

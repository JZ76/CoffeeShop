package tests;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.*;
import main.*;
import util.Name;



public class Tests {
	
	private OrderList ol;
	private Menu menu;
	private SimpleDateFormat sdf = new SimpleDateFormat("EEE dd-MM-yyyy HH:mm:ss", Locale.ENGLISH);
	
	
	@Before
	public void setUp() {

        RedBlackTree<Integer, ArrayList<Integer>> RBT = new RedBlackTree<Integer,ArrayList<Integer>>();
        HashMap<Integer,String[]> map = new HashMap<>();
        CustomerList cusList = CustomerList.getInstance();
        
        HashMap<String, String[]> food = new HashMap<>();
        HashMap<String, String[]> beverage = new HashMap<>();
        HashMap<String, String[]> desserts = new HashMap<>();
        
        menu = new Menu(food, beverage, desserts);
        ol = new OrderList(RBT,map,cusList, menu);
        
        try {
	        menu.addToMenu(new Item("FOOD001","Food1", Category.FOOD,"...", 2));
	        menu.addToMenu(new Item("FOOD002","Food2", Category.FOOD,"...", 6.65));
	        menu.addToMenu(new Item("BEVERAGE101", "Bev1", Category.BEVERAGE, "...", 2.35));
	        menu.addToMenu(new Item("BEVERAGE102", "Bev2", Category.BEVERAGE, "...", 3.75));
	        menu.addToMenu(new Item("BEVERAGE103", "Bev3", Category.BEVERAGE, "...", 0.55));
	        menu.addToMenu(new Item("DESSERT201", "Des1", Category.DESSERT, "...", 5.5));
	        menu.addToMenu(new Item("DESSERT202", "Des2", Category.DESSERT, "...", 6));
	        menu.addToMenu(new Item("DESSERT203", "Des3", Category.DESSERT, "...", 8.3));
        }	catch(DuplicateItemException e) {
        		System.out.println(e.getMessage() + " - item not added");
        }
        
        ol.createCustomer(100001, new Name("Cust 1"));
        ol.createCustomer(100002, new Name("Cust 2"));
        ol.createCustomer(100003, new Name("Cust 3"));
        
        
        ol.addOrderToCustomer(100001, 101);
        ol.addOrderToCustomer(100001, 102);
        ol.addOrderToCustomer(100001, 103);
        ol.addOrderToCustomer(100002, 104);
        ol.addOrderToCustomer(100002, 105);


	}
	
	
	//tests that multiple orders are added to the same customer and the method OrderList.getOrders(int custNo)
	@Test
	public void testCustomerOrderDetails() {
		
		ArrayList<Integer> ordersCust1 = new ArrayList<Integer>();
		ordersCust1.add(101);
		ordersCust1.add(102);
		ordersCust1.add(103);
		
		assertEquals (3, ol.getOrders(100001).size(), "Cust1 should have 3 orders");
		assertEquals (2, ol.getOrders(100002).size(), "Cust1 should have 2 orders");
		assertEquals (0, ol.getOrders(100003).size(), "Cust1 should have 0 orders");
		
		
		assertTrue ( "Cust1 should have order 101", ol.getOrders(100001).contains(101));
		assertTrue ( "Cust1 should have order 102", ol.getOrders(100001).contains(102));
		assertTrue ( "Cust1 should have order 103", ol.getOrders(100001).contains(103));
		assertTrue ( "Cust2 should have order 104", ol.getOrders(100002).contains(104));
		assertTrue ( "Cust2 should have order 105", ol.getOrders(100002).contains(105));
		
		
	}
	
	//tests the application of discounts to 5 different orders
	@Test
	public void testDiscount() throws ParseException {
		//discount1: 2 Food + 1 Dessert --> 40% discount
		//discount2	 1 Food + 2 Beverage + 3 Dessert --> 30% discount
        
        String[] items101 = {"FOOD001", "FOOD002", "FOOD002", "DESSERT201", "BEVERAGE101"}; //3F + 1B + 1D --> only discount 1 can be applied
        String[] items102 = {"FOOD001", "FOOD002", "BEVERAGE101", "BEVERAGE101", "DESSERT201", "DESSERT202", "DESSERT201"}; //2F + 1B + 3D --> discount 1 and 2 are applicable (should apply bigger disc: disc1)
        String[] items103 = {"FOOD001", "DESSERT201"}; //1F + 1D --> 0 discounts are applicable.
        String[] items104 = {"FOOD001", "FOOD002", "FOOD001", "DESSERT201", "DESSERT201", "DESSERT201", "DESSERT201", "BEVERAGE101", "BEVERAGE101"}; //3F + 4D + 2B --> discounts 1 and 2 should both be applied.
        String[] items105 = {"FOOD001", "FOOD001", "FOOD001", "FOOD001", "DESSERT201", "DESSERT201"}; //4F + 2D --> dic1 should be applied twice.
        
        ol.addOrderInfo(101, items101, sdf.parse("Fri 01-05-2021 12:08:43"));
        ol.addOrderInfo(102, items102, sdf.parse("Fri 01-05-2021 12:09:43"));
        ol.addOrderInfo(103, items103, sdf.parse("Fri 01-05-2021 12:10:43"));
        ol.addOrderInfo(104, items104, sdf.parse("Fri 01-05-2021 12:11:43"));
        ol.addOrderInfo(105, items105, sdf.parse("Fri 01-05-2021 12:12:43"));
        

        DiscountRules dis1 = new DiscountRules(items101, menu);
        DiscountRules dis2 = new DiscountRules(items102, menu);
        DiscountRules dis3 = new DiscountRules(items103, menu);
        DiscountRules dis4 = new DiscountRules(items104, menu);
        DiscountRules dis5 = new DiscountRules(items105, menu);
        
        
        assertEquals (dis1.calculate(), "15,63", "Discount for order 101 is wrong!"); //disc1 is applied
        assertEquals (dis2.calculate(), "21,85", "Discount for order 102 is wrong!"); //disc1 is applied because it is better than disc2 (both are possible)
        assertEquals (dis3.calculate(), "7,50" , "Discount for order 103 is wrong!"); //no discount is applied
        assertEquals (dis4.calculate(), "25,20", "Discount for order 104 is wrong!"); //both disc1 and disc2 are applied
        assertEquals (dis5.calculate(), "15,20", "Discount for order 105 is wrong!"); //disc1 can be applied twice but offers are only applied once per order --> disc1 applied once.
        
       
	}
	

	//test for summary report: to-do
	public  void testSummaryReport() {
		//...
	}
	
	//Tests that exceptions for incorrect new items are thrown
	@Test
	public void testItemExceptions() {
		
		try {
			Item itemBlankID = new Item("", "Name", Category.FOOD, "...", 2);
			fail("Empty Item ID - should throw exception!");
		} catch (IllegalStateException e) { assertTrue(e.getMessage().contains("id")); }
		
		try {
			Item itemBlankName = new Item("FOOD001", "", Category.FOOD, "...", 2);
			fail("Empty Name - should throw exception!");
		} catch (IllegalStateException e) { assertTrue(e.getMessage().contains("name")); }
		
		try {
			Item itemBlankDescription = new Item("FOOD001", "Name", Category.FOOD, "", 2);
			fail("Empty Description - should throw exception!");
		} catch (IllegalStateException e) { assertTrue(e.getMessage().contains("description")); }
		
		try {
			Item itemNegativePrice = new Item("FOOD001", "Name", Category.FOOD, "...", -2);
			fail("Negative price - should throw exception!");
		} catch (IllegalArgumentException e) { assertTrue(e.getMessage().contains("positive")); }
		
		try {
			Item item0Price = new Item("FOOD001", "Name", Category.FOOD, "...", 0);
			fail("Price is zero - should throw exception!");
		} catch (IllegalArgumentException e) { assertTrue(e.getMessage().contains("zero")); }
		
		
		assertThrows(DuplicateItemException.class,
		           () -> { menu.addToMenu(new Item("FOOD001", "name", Category.FOOD, "...", 2)); }
		);
		
	}
}

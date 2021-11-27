package main;

/*
 * F21AS - Coursework group 4
 * 
 * Item class
 * 
 * Holds information about a particular item in the menu.
 * 
 */

public class Item {
    
    private final  String 	id; 			//unique id of item (e.g. FOOD001)
    private final  String 	name;			//name of produce (e.g. Cheeseburger)
    private final  Category category;		//can be FOOD, BEVERAGE or DESSERT
    private String description;	//contains ingredients of item.
    private double price;			//holds price (in Pounds)

    //item constructor
    public Item(String id, String name, Category category, String description, double price){
        this.id 		 = id;
        this.name 		 = name;
        this.category 	 = category;
        this.description = description;
        this.price 		 = price;
        
        //exception will be thrown if item has no id
        if ((id.trim().length()==0)) {
        	throw new IllegalStateException("Item must have an id");
        } 
        //exception will be thrown if item has no name
        if ((name.trim().length()==0)) {
        	throw new IllegalStateException("Cannot have blank item name");
        } 
        //exception must be thrown if item has no description
        if ((description.trim().length()==0)) {
        	throw new IllegalStateException("Must have item description");
        } 
        
        //exception thrown if negative number is used for price
        if (price < 0 ) {
        	throw new IllegalArgumentException("Price must be a positive number");
        }
        
        //exception thrown if price is equal to zero
        if (price == 0 ) {
        	throw new IllegalArgumentException("Price of a product cannot be zero, we want to make money!");
        }
        
    }	

    //get methods
    public String getId() 	{return id;}
    public String getName() {return name;}
    public String getDescription() {return description;}
    public Category getCategory()  {return category;}
    public double getPrice() {return price;}
 
    //set methods
    public void setDescription(String description){
    	try {
    	     this.description = description;
        }
    	catch (IllegalStateException e) {
    		System.out.println("Description must be in String format.");
    	}
    }
    public void setPrice(double price) {
    	try {
    		this.price = price;}
    	catch (IllegalStateException e) {
    		System.out.println("Price must be a double e.g. 1.2");
    	}
    }
    
}

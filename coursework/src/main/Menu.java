package main;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * F21AS - Coursework group 4
 * 
 * Menu class
 * 
 * Holds information and methods regarding the items in the menu.
 * 
 */


public class Menu {
	//each of these Hashmaps contain elements of the type:
	//<itemID, [price, name, description]>
    private HashMap<String,String[]> food;
    private HashMap<String,String[]> beverage;
    private HashMap<String,String[]> desserts;
    
    public Menu(HashMap<String,String[]> food,HashMap<String,String[]> beverage,HashMap<String,String[]> desserts){
        this.food = food;
        this.beverage = beverage;
        this.desserts = desserts;
    }

    /* 
     * Adds an item to the menu. 
     * If the id is already being used it throws a DuplicateItemException
     * Otherwise, it adds it to the the corresponding HashMap (food, beverage or desserts)
     */
    public void addToMenu(Item item) throws DuplicateItemException{
        switch (item.getCategory()) {
            case FOOD: {
                String[] s = new String[3];
                s[0] = String.valueOf(item.getPrice()); //price of item
                s[1] = item.getName();	//name of item
                s[2] = item.getDescription(); //description
                if (food.containsKey(item.getId())) throw new DuplicateItemException(item.getId());
                else food.put(item.getId(), s);
                break;
            }
            case BEVERAGE : {
                String[] s = new String[3];
                s[0] = String.valueOf(item.getPrice());
                s[1] = item.getName();
                s[2] = item.getDescription();
                if (beverage.containsKey(item.getId())) throw new DuplicateItemException(item.getId());
                else beverage.put(item.getId(), s);
                break;
            }
            case DESSERT : {
                String[] s = new String[3];
                s[0] = String.valueOf(item.getPrice());
                s[1] = item.getName();
                s[2] = item.getDescription();
                if (desserts.containsKey(item.getId())) throw new DuplicateItemException(item.getId());
                else desserts.put(item.getId(), s);
                break;
            }
        }
    }

    
    /*
     * Given an item's id, it returns the price of it.
     * Returns 0 if the item is not in the menu.
     */
    public double getPrice(String id){
        Pattern foodp = Pattern.compile("^f.*d",Pattern.CASE_INSENSITIVE);
        Pattern beveragep = Pattern.compile("^b.*e",Pattern.CASE_INSENSITIVE);
        Pattern dessertsp = Pattern.compile("^d.*t",Pattern.CASE_INSENSITIVE);
        
        Matcher m1 = foodp.matcher(id);
        Matcher m2 = beveragep.matcher(id);
        Matcher m3 = dessertsp.matcher(id);
        
        if (m1.find()) return Double.parseDouble(food.get(id)[0]);
        if (m2.find()) return Double.parseDouble(beverage.get(id)[0]);
        if (m3.find()) return Double.parseDouble(desserts.get(id)[0]);
        
        return 0;
    }

    /*
     * Given a food's id, returns a String with its details in the form:
     * (id) name price description
     */
    public String getDetailsFood(int id) {
        int j = 1;
        String details="";
        for (Map.Entry<String,String[]> entry : food.entrySet()){
            if (j == id){
                details += "(" +entry.getKey() + ") "+ entry.getValue()[1] + " " +  entry.getValue()[0] + " " + entry.getValue()[2]+"\n";
            }
            j++;
        }
        return details;
    }
    
    /*
     * Given a beverage's id, returns a String with its details in the form:
     * (id) name price description
     */
    public String getDetailsBeverage(int id) {
        int j = 1;
        String details="";
        for (Map.Entry<String,String[]> entry : beverage.entrySet()){
            if (j == id){
                details += "(" +entry.getKey() + ") "+ entry.getValue()[1] + " " + entry.getValue()[0] + " " + entry.getValue()[2]+"\n";
            }
            j++;
        }
        return details;
    }
    
    /*
     * Given a dessert's id, returns a String with its details in the form:
     * (id) name price description
     */
    public String getDetailsDessert(int id) {
        int j = 1;
        String details="";
        for (Map.Entry<String,String[]> entry : desserts.entrySet()){
            if (j == id){
                details += "(" +entry.getKey() + ") "+ entry.getValue()[1] + " " + entry.getValue()[0] + " " + entry.getValue()[2]+"\n";
            }
            j++;
        }
        return details;
    }
    
    /*
     * Given an item's id, returns the name of the item in the menu.
     */
    public String getName(String id) {
    	
    	Pattern foodp = Pattern.compile("^f.*d",Pattern.CASE_INSENSITIVE);
        Pattern beveragep = Pattern.compile("^b.*e",Pattern.CASE_INSENSITIVE);
        Pattern dessertsp = Pattern.compile("^d.*t",Pattern.CASE_INSENSITIVE);
        
        Matcher m1 = foodp.matcher(id);
        Matcher m2 = beveragep.matcher(id);
        Matcher m3 = dessertsp.matcher(id);
        
        if (m1.find()) return food.get(id)[1];
        if (m2.find()) return beverage.get(id)[1];
        if (m3.find()) return desserts.get(id)[1];

        return "";
    }
    
}

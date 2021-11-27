package main;

import java.util.ArrayList;

/*
 * F21AS - Coursework group 4
 * 
 * Log class
 * 
 * Log to store what is happening during the simulation.
 * Uses the Singleton pattern.
 * 
 */


public class Log {
	
    private volatile static Log log;
    private volatile static ArrayList<String> l;
    
    private Log(){}
    
    public static Log getInstance(){
        if (log == null){
            synchronized (Log.class){
                if (log == null){
                    log = new Log();
                    l = new ArrayList<>();
                }
            }
        }
        return log;
    }
    
    public void add(String s){
        l.add(s);
    }
    
    public String[] getLog(){
        return (String[])l.toArray(new String[l.size()]);
    }
}

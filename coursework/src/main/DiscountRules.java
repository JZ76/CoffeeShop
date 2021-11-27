package main;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * F21AS - Coursework group 4
 * 
 * DiscountRules class
 * 
 * Processes and applies the chosen discount rules.
 * If several discounts are applicable, the one that benefits the customer the most is chosen.
 * 
 */

public class DiscountRules {
    private String[] items;
    private double price;
    private Menu m;
    private ArrayList<Double> foodArray = new ArrayList<>();
    private ArrayList<Double> beverageArray = new ArrayList<>();
    private ArrayList<Double> dessertsArray = new ArrayList<>();

    public DiscountRules(String[] items, Menu m){
        this.items = items;
        this.m = m;
    }

    private static void sort(ArrayList<Double> a, int low, int high) {
        if (high <= low) {
            return;
        }
        int j = partition(a, low, high);
        sort(a, low, j - 1);
        sort(a, j + 1, high);
    }

    private static int partition(ArrayList<Double> a, int low, int high) {
        int i = low;
        int j = high + 1;
        Double v = a.get(low);
        while (true) {
            while (a.get(++i).compareTo(v)<0) {
                if (i == high) {
                    break;
                }
            }
            while (v.compareTo(a.get(--j))<0) {
                if (j == low) {
                    break;
                }
            }
            if (i >= j) {
                break;
            }
            exchange(a, i, j);
        }
        exchange(a, low, j);
        return j;
    }

    private static void exchange(ArrayList<Double> a, int i, int j) {
        Double swap = a.get(i);
        a.set(i, a.get(j));
        a.set(j, swap);
    }

    public String calculate() {
        Pattern foodp = Pattern.compile("^f.*d",Pattern.CASE_INSENSITIVE);
        Pattern beveragep = Pattern.compile("^b.*e",Pattern.CASE_INSENSITIVE);
        Pattern dessertsp = Pattern.compile("^d.*t",Pattern.CASE_INSENSITIVE);
        double x = 0;
        for (String s : items) {
            Matcher m1 = foodp.matcher(s);
            Matcher m2 = beveragep.matcher(s);
            Matcher m3 = dessertsp.matcher(s);
            x = m.getPrice(s);
            if (m1.find()) {
                foodArray.add(x);
            } else if (m2.find()) {
                beverageArray.add(x);
            } else if (m3.find()) {
                dessertsArray.add(x);
            }
        }
        if (foodArray.size() > 1) {
            quickSort(foodArray);
        }
        if (beverageArray.size() > 1) {
            quickSort(beverageArray);
        }
        if (dessertsArray.size() > 1) {
            quickSort(dessertsArray);
        }

        int[][] rule = new int[2][4]; //add rules

        
        //discount 1
        rule[0][0] = 70; //discount rate
        rule[0][1] = 1; //how many food need to buy in an order
        rule[0][2] = 2; //how many beverage need to buy in an order
        rule[0][3] = 3; //how many desserts need to buy in an order

        
        //discount 2
        rule[1][0] = 60; //discount rate
        rule[1][1] = 2; //how many food need to buy in an order to apply this discount
        rule[1][2] = 0; //how many beverage need to buy in an order
        rule[1][3] = 1; //how many desserts need to buy in an order

        for (int[] ints : rule) {
            double disc = ints[0] * 1.0 / 100;
            double temp = 0.00;
            int fas = foodArray.size();
            int bas = beverageArray.size();
            int das = dessertsArray.size();
            for (int j = 1; j < 4; j++) {
                if (ints[1] > fas || ints[2] > bas || ints[3] > das) {
                    break;
                }
                if (j == 1) {
                    for (int c = fas - 1; c > fas - 1 - ints[1]; c--) {
                        temp += foodArray.get(c);
                        foodArray.remove(c);
                    }
                }
                if (j == 2) {
                    for (int c = bas - 1; c > bas - 1 - ints[2]; c--) {
                        temp += beverageArray.get(c);
                        beverageArray.remove(c);
                    }
                }
                if (j == 3) {
                    for (int c = das - 1; c > das - 1 - ints[3]; c--) {
                        temp += dessertsArray.get(c);
                        dessertsArray.remove(c);
                    }
                }
            }
            price += temp * disc;
        }

        if (foodArray.size() != 0) {
            for (double i : foodArray) {
                price += i;
            }
        }
        if (beverageArray.size() != 0) {
            for (double i : beverageArray) {
                price += i;
            }
        }
        if (dessertsArray.size() != 0) {
            for (double i : dessertsArray) {
                price += i;
            }
        }
        return String.format("%.2f", price);
    }

    private void quickSort(ArrayList<Double> a) {
        sort(a, 0, a.size() - 1);
    }
}

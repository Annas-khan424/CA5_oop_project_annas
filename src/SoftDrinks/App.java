package SoftDrinks;

import java.io.IOException;
import java.util.*;
import java.util.PriorityQueue;

/**
 * Annas Khan project
 *
 */

public class App {
    Drink drink;
    WholeSaler wholeSaler;

    public static void main(String[] args)
    {
        App app = new App();
        app.start();
    }

    public void start() {
        System.out.println("OOP Project(CA5) - Soft Drinks");
        ArrayList<Drink> drinks = new ArrayList<>();
        Map<String, WholeSaler> mapOfOrigin = new HashMap<>();
        Map<Integer, Drink> StockNumMap = new TreeMap<>();
        PriorityQueue<Drink> queue = new PriorityQueue<>();
        PriorityQueue<Drink> twoFieldQueue = new PriorityQueue<>(new StockBrandComparator());

        initialize(drinks, mapOfOrigin, StockNumMap, twoFieldQueue);

        try {
            displayMainMenu(drinks, mapOfOrigin, StockNumMap, queue, twoFieldQueue);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Program Over");

    }

    private void displayMainMenu(ArrayList<Drink> drinks, Map<String, WholeSaler> mapOfOrigin, Map<Integer, Drink> StockNumMap, PriorityQueue<Drink> queue, PriorityQueue<Drink> twoFieldQueue) throws IOException {

        final String MENU_ITEMS = "\nMAIN MENU\n"
                + "1. View All Drinks\n"
                + "2. Retrieve WholeSaler by Drink\n"
                + "3. display the objects from the TreeMap\n"
                + "4. PriorityQueue Sequence Simulation\n"
                + "5. PriorityQueue Two-Field (stockAva, Brands)\n"
                + "6. Exit\n"
                + "Enter Option [1,6]";

        final int VIEW_DRINKS = 1;
        final int RETRIEVE_WHOLESALER_DRINKS = 2;
        final int DISPLAY_OBJECTS_FROM_TREEMAP = 3;
        final int PRIORITYQUEUE_SEQUENCE_SIMULATION = 4;
        final int PRIORITYQUEUE_BRAND_STOCK = 5;
        final int EXIT = 6;

        Scanner keyboard = new Scanner(System.in);
        int option = 0;
        do {
            System.out.println("\n" + MENU_ITEMS);
            try {
                String usersInput = keyboard.nextLine();
                option = Integer.parseInt(usersInput);
                switch (option) {
                    case VIEW_DRINKS:
                        System.out.println(" ___ List of all Drinks ___  ");

                        for(Drink d: drinks) {
                            System.out.println("------------------------------");
                            System.out.println("\tID : " + d.get_id());
                            System.out.println("\tBrand : " + d.getBrand());
                            System.out.println("\tName : " + d.getName());
                            System.out.println("\tDrink size (ml) : " + d.getSize());
                            System.out.println("\tPrice : €" + d.getPrice());
                            System.out.println("\tStock Available : " + d.getStockAvailable());
                        }
                        break;
                    case RETRIEVE_WHOLESALER_DRINKS:
                        System.out.println(" ___ Find Wholesaler of a Drinks ___  ");
                        System.out.println("Enter Drinks ID (EX:1,2,3..) : ");
                        String id = keyboard.nextLine();

                        WholeSaler ws = mapOfOrigin.get(id);
                        System.out.println(ws);
                        break;
                    case DISPLAY_OBJECTS_FROM_TREEMAP:
                        System.out.println(" ___ Displaying Treemap Objects ___ ");

                        for (Map.Entry<Integer, Drink> entry : StockNumMap.entrySet()) {
                            System.out.println("Key: " + entry.getKey() + ".\t Value: " + entry.getValue());
                        }
                        break;
                    case PRIORITYQUEUE_SEQUENCE_SIMULATION:
                        System.out.println(" ___ Sequence Below ___  ");

                        // add two third-priority elements
                        queue.add(drinks.get(0));
                        queue.add(drinks.get(1));

                        // add two second-priority level items
                        queue.add(drinks.get(7));
                        queue.add(drinks.get(8));

                        // remove and display one element
                        System.out.println("Remove and display a single element");
                        System.out.println(queue.remove());

                        // add one top-priority element
                        queue.add(drinks.get(4));

                        // remove and display all elements in priority order
                        System.out.println("\nRemove and display all elements");
                        while ( !queue.isEmpty() ) {
                            System.out.println(queue.remove());
                        }
                        break;
                    case PRIORITYQUEUE_BRAND_STOCK:
                        System.out.println("Priority queue, sorting by brand name alphabetically \n");

                        while ( !twoFieldQueue.isEmpty() ) {
                            System.out.println(twoFieldQueue.remove());
                        }
                        break;
                    case EXIT:
                        System.out.println("Menu Exited");
                        break;
                    default:
                        System.out.print("Invalid option - please enter number in range [1,6]");
                        break;
                }

            } catch (InputMismatchException | NumberFormatException e) {
                System.out.print("Invalid option - please enter number in range [1,6]");
            }
        } while (option != EXIT);

        System.out.println("\nExiting Main Menu, goodbye.");

    }
    private void initialize( List list, Map<String, WholeSaler> mapOfOrigin, Map<Integer, Drink> StockNumMap, PriorityQueue<Drink> twoFieldQueue)
    {
        Drink d1 = new Drink("1","Coca-Cola","Coke",300,1.30,300);
        Drink d2 = new Drink("2","Coca-Cola","Coke-zero",300,1.30,670);
        Drink d3 = new Drink("3","Pepsi","Pepsi Wild Cherry",500,4.30,400);
        Drink d4 = new Drink("4","Red Bull","Red Bull Coconut",300,2.30,800);
        Drink d5 = new Drink("5","Sprite","Sprite Cherry",500,6.90,700);
        Drink d6 = new Drink("6","Pepsi","Pepsi",300,5.30,840);
        Drink d7 = new Drink("7","Monster","Zero-Sugar Ultra Red",500,3.40,20);
        Drink d8 = new Drink("8","Fanta","Fanta Lemon",350,2.89,50);
        Drink d9 = new Drink("9","Monster","Mango Loco ",500,3.00,100);
        Drink d10 = new Drink("10","Schweppes","Russchian Pink Soda",200,2.00,10);

        WholeSaler ws1 = new WholeSaler("3928436", "995 Anderson Rest,Missouri", "USA");
        WholeSaler ws2 = new WholeSaler("9562098", "207 O'Connell Divide,Maryland", "England");
        WholeSaler ws3 = new WholeSaler("0765463", "IAS Colony, HYD", "India");
        WholeSaler ws4 = new WholeSaler("3928436", "1 Bridge street, london", "UK");
        WholeSaler ws5 = new WholeSaler("9562098", "0 Quai de Nesle,Paris", "France");
        WholeSaler ws6 = new WholeSaler("0765463", "Eintrachtstr.243,Rheinland-Pfalz", "Germany");

        list.add(d1);
        list.add(d2);
        list.add(d3);
        list.add(d4);
        list.add(d5);
        list.add(d6);
        list.add(d7);
        list.add(d8);
        list.add(d9);
        list.add(d10);

        mapOfOrigin.put(d1.get_id(), ws1);
        mapOfOrigin.put(d2.get_id(), ws4);
        mapOfOrigin.put(d3.get_id(), ws2);
        mapOfOrigin.put(d4.get_id(), ws1);
        mapOfOrigin.put(d5.get_id(), ws3);
        mapOfOrigin.put(d6.get_id(), ws1);
        mapOfOrigin.put(d7.get_id(), ws5);
        mapOfOrigin.put(d8.get_id(), ws3);
        mapOfOrigin.put(d9.get_id(), ws2);
        mapOfOrigin.put(d10.get_id(), ws6);

        StockNumMap.put(d1.getStockAvailable(), d1);
        StockNumMap.put(d2.getStockAvailable(), d2);
        StockNumMap.put(d3.getStockAvailable(), d3);
        StockNumMap.put(d4.getStockAvailable(), d4);
        StockNumMap.put(d5.getStockAvailable(), d5);
        StockNumMap.put(d6.getStockAvailable(), d6);
        StockNumMap.put(d7.getStockAvailable(), d7);
        StockNumMap.put(d8.getStockAvailable(), d8);
        StockNumMap.put(d9.getStockAvailable(), d9);
        StockNumMap.put(d10.getStockAvailable(), d10);

        twoFieldQueue.add(d1);
        twoFieldQueue.add(d2);
        twoFieldQueue.add(d3);
        twoFieldQueue.add(d4);
        twoFieldQueue.add(d5);
        twoFieldQueue.add(d6);
        twoFieldQueue.add(d7);
        twoFieldQueue.add(d8);
        twoFieldQueue.add(d9);
        twoFieldQueue.add(d10);
    }



}

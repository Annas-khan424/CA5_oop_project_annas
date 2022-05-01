package SoftDrinks.BusinessClasses;

import java.io.IOException;
import java.util.*;

import SoftDrinks.DAOs.MySqlDrinkDao;
import SoftDrinks.DAOs.DrinkDaoInterface;
import SoftDrinks.DTOs.Drink;
import SoftDrinks.DTOs.WholeSaler;
import SoftDrinks.Exceptions.DaoException;

import java.util.PriorityQueue;

/**
 * Annas Khan project
 *
 */

public class App {

    List<Drink> drinks;
    Map<Integer, WholeSaler> mapOfOrigin;
    Map<Integer, Drink> StockNumMap;
    PriorityQueue<Drink> queue;
    PriorityQueue<Drink> twoFieldQueue;
    PriorityQueue<Drink> queueDBFiltered;
    List<Drink> listDBFiltered;
    DrinkDaoInterface IDrinkDao = new MySqlDrinkDao();



    public static void main(String[] args)
    {
        App app = new App();
        app.start();
    }

    public void start() {
        System.out.println("OOP Project(CA5) - Soft Drinks");

        this.drinks = new ArrayList<>();
        this.mapOfOrigin = new HashMap<Integer, WholeSaler>();
        this.StockNumMap = new TreeMap<>();
        this.queue = new PriorityQueue<>();
        this.twoFieldQueue = new PriorityQueue<>(new StockBrandComparator());
        initialize();

        try {
            displayMainMenu();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Program Over");

    }

    private void displayMainMenu() throws IOException {

        final String MENU_ITEMS = "\nMAIN MENU\n"
                + "1. View All Drinks\n"
                + "2. Retrieve WholeSaler by Drink\n"
                + "3. display the objects from the TreeMap\n"
                + "4. PriorityQueue Sequence Simulation\n"
                + "5. PriorityQueue Two-Field (stockAva, Brands)\n"
                + "6. Find all Drink from database\n"
                + "7. Find one Drink from database by ID\n"
                + "8. Delete one Drink from database by ID\n"
                + "9. Add Drink to database\n"
                + "10. List Drink filtered by price\n"
                + "11. Find all Drink from database as JSON\n"
                + "12. Find one Drink from database by ID as JSON\n"
                + "13. Exit\n"
                + "Enter Option [1,13]";


        final int VIEW_DRINKS = 1;
        final int RETRIEVE_WHOLESALER_DRINKS = 2;
        final int DISPLAY_OBJECTS_FROM_TREEMAP = 3;
        final int PRIORITYQUEUE_SEQUENCE_SIMULATION = 4;
        final int PRIORITYQUEUE_BRAND_STOCK = 5;
        final int PULL_ALL_DRINK_FROM_DB = 6;
        final int PULL_DRINK_FROM_DB_BY_ID = 7;
        final int DELETE_DRINK_FROM_DB_BY_ID = 8;
        final int ADD_DRINK_TO_DB = 9;
        final int LIST_FILTERED = 10;
        final int PULL_ALL_DRINK_FROM_DB_JSON = 11;
        final int PULL_DRINK_FROM_DB_BY_ID_JSON = 12;
        final int EXIT = 13;

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
                        displayList(drinks);
                        break;
                    case RETRIEVE_WHOLESALER_DRINKS:
                        System.out.println(" ___ Find Wholesaler of a Drinks ___  ");
                        System.out.println("Enter Drinks ID (EX : 1-10 ");
                        String id = keyboard.nextLine();
                        WholeSaler ws = mapOfOrigin.get(id);
                        System.out.println(ws);
                        break;
                    case DISPLAY_OBJECTS_FROM_TREEMAP:
                        System.out.println(" ___ Displaying Treemap Objects ___ ");
                        displayStockAmountMap();
                        break;
                    case PRIORITYQUEUE_SEQUENCE_SIMULATION:
                        System.out.println(" ___ Sequence Below ___  ");
                        priorityQueueSequence();
                        break;
                    case PRIORITYQUEUE_BRAND_STOCK:
                        System.out.println("Priority queue, sorting by brand name alphabetically \n");
                        displayTwoFieldQueue();
                        break;
                    case PULL_ALL_DRINK_FROM_DB:
                        findAllDrink();
                        break;
                    case PULL_DRINK_FROM_DB_BY_ID:
                        System.out.println("Please enter Drink ID: ");
                        String idTag = keyboard.nextLine();
                        findDrinkByID(idTag);
                        break;
                    case DELETE_DRINK_FROM_DB_BY_ID:
                        System.out.println("Please enter Drink ID: ");
                        String idForDeletion = keyboard.nextLine();
                        deleteDrinkByID(idForDeletion);
                        break;
                    case ADD_DRINK_TO_DB:
                        addDrinkToDB();
                        break;
                    case LIST_FILTERED:
                        System.out.println("Please enter maximum Drink price: ");
                        float filterPrice = keyboard.nextFloat();
                        keyboard.nextLine();
                        listFilteredDrink(filterPrice);
                        break;
                    case PULL_ALL_DRINK_FROM_DB_JSON:
                        findAllDrinkJSON();
                        break;
                    case PULL_DRINK_FROM_DB_BY_ID_JSON:
                        System.out.println("Please enter Drink ID: ");
                        String idTag2 = keyboard.nextLine();
                        findDrinkByIDJSON(idTag2);
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
    private void initialize()
    {
        Drink d1 = new Drink(1,"Coca-Cola","Coke                 ",300,(float)1.30,200);//260
        Drink d2 = new Drink(2,"Coca-Cola","Coke-zero            ",300,(float)1.30,670);//871
        Drink d3 = new Drink(3,"Pepsi    ","Pepsi Wild Cherry    ",500,(float)4.30,400);//1720
        Drink d4 = new Drink(4,"Red Bull ","Red Bull Coconut     ",300,(float)2.30,800);//1840
        Drink d5 = new Drink(5,"Sprite   ","Sprite Cherry        ",500,(float)6.90,700);//4830
        Drink d6 = new Drink(6,"Pepsi    ","Pepsi                ",300,(float)5.30,840);//4452
        Drink d7 = new Drink(7,"Monster  ","Zero-Sugar Ultra Red ",500,(float)3.40,20);//68
        Drink d8 = new Drink(8,"Fanta    ","Fanta Lemon          ",350,(float)2.89,50);//144.5
        Drink d9 = new Drink(9,"Monster  ","Mango Loco           ",500,(float)3.00,100);//300
        Drink d10 = new Drink(10,"Schweppes","Russchian Pink Soda ",200,(float)2.00,10);//200

        WholeSaler ws1 = new WholeSaler("3928436", "995 Anderson Rest,Missouri", "USA");
        WholeSaler ws2 = new WholeSaler("9562098", "207 O'Connell Divide,Maryland", "England");
        WholeSaler ws3 = new WholeSaler("0765463", "IAS Colony, HYD", "India");
        WholeSaler ws4 = new WholeSaler("3928436", "1 Bridge street, london", "UK");
        WholeSaler ws5 = new WholeSaler("9562098", "0 Quai de Nesle,Paris", "France");
        WholeSaler ws6 = new WholeSaler("0765463", "Eintrachtstr.243,Rheinland-Pfalz", "Germany");

        drinks.add(d1);
        drinks.add(d2);
        drinks.add(d3);
        drinks.add(d4);
        drinks.add(d5);
        drinks.add(d6);
        drinks.add(d7);
        drinks.add(d8);
        drinks.add(d9);
        drinks.add(d10);

        mapOfOrigin.put(d1.get_id(), ws2);
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
    public void displayList(List <Drink> list)
    {
        for(Drink d: drinks) {
            System.out.println("------------------------------");
            System.out.println("\tID : " + d.get_id());
            System.out.println("\tBrand : " + d.getBrand());
            System.out.println("\tName : " + d.getName());
            System.out.println("\tDrink size (ml) : " + d.getSize());
            System.out.println("\tPrice : €" + d.getPrice());
            System.out.println("\tStock Available : " + d.getStockAvailable());
        }
    }

    public void displayStockAmountMap()
    {
        for (Map.Entry<Integer, Drink> entry : StockNumMap.entrySet()) {
            System.out.println("Key: " + entry.getKey() + ".\t Value: " + entry.getValue());
        }
    }
    public void priorityQueueSequence()
    {
        // add two third-priority elements
        queue.add(drinks.get(6));
        queue.add(drinks.get(7));

        // add two second-priority level items
        queue.add(drinks.get(9));
        queue.add(drinks.get(0));

        // remove and display one element
        System.out.println("Remove and display a single element");
        Drink d = queue.remove();
        System.out.println(d.toString() + "  -  Total Price for Stock Available: €" + (d.getPrice()*d.getStockAvailable()));

        // add one top-priority element
        queue.add(drinks.get(4));

        // remove and display all elements in priority order
        System.out.println("\nRemove and display all elements");
        while ( !queue.isEmpty() ) {
            Drink r = queue.remove();
            System.out.println(r.toString() + "\t-\tTotal Price for Stock Available: €" + (Double.valueOf(Math.round((r.getPrice()*r.getStockAvailable()) * 100)) / 100) );
        }
    }

    public void displayTwoFieldQueue()
    {
        while ( !twoFieldQueue.isEmpty() ) {
            System.out.println(twoFieldQueue.remove());
        }
    }

    public void findAllDrink()
    {
        try
        {
            System.out.println("\nCall findAllDrink()");
            List<Drink> Drinks = IDrinkDao.findAllDrink();

            if( Drinks.isEmpty() )
                System.out.println("There are no Users");
            else {
                for (Drink drink : Drinks)
                    System.out.println(drink.toString());
            }

        }
        catch( DaoException e )
        {
            e.printStackTrace();
        }
    }

    public void findAllDrinkJSON()
    {
        try
        {
            System.out.println("\nCall findAllDrinkJSON()");
            String jsonString = IDrinkDao.findAllDrinkJSON();

            if(jsonString.equals("null"))
                System.out.println("No Drink found");
            else {
                System.out.println(jsonString);
            }

        }
        catch( DaoException e )
        {
            e.printStackTrace();
        }
    }

    public void findDrinkByID(String id)
    {
        try
        {
            System.out.println("findDrinkByID()");
            Drink drink = IDrinkDao.findDrinkByID(id);

            if(drink == null)
                System.out.println("No drink exists with ID: " + id);
            else {
                System.out.println(drink);
            }

        }
        catch( DaoException e )
        {
            e.printStackTrace();
        }
    }

    public void listFilteredDrink(float x)
    {
        System.out.println("Showing all Drink sub " + x + " Euro, ordered by (price/ml), high->low");

        try {
            listDBFiltered = IDrinkDao.findAllDrinkSubXEuro(x);
            ArrayList<Drink> fetchedFilteredArrList = new ArrayList<Drink>();
            fetchedFilteredArrList.addAll(listDBFiltered);

            for(int i = 0; i < fetchedFilteredArrList.size(); i++)
            {
                queueDBFiltered.add(fetchedFilteredArrList.get(i));
            }
            while ( !queueDBFiltered.isEmpty() ) {
                Drink r = queueDBFiltered.remove();
                System.out.println(r.toString() + "\t-\tPrice per ml: €" + Double.valueOf(Math.round((r.getPrice()*r.getStockAvailable()) * 100)) / 100);
            }

        }
        catch( DaoException e )
        {
            e.printStackTrace();
        }
    }


    public void findDrinkByIDJSON(String id)
    {
        try
        {
            System.out.println("findDrinkByIDJSON()");
            String jsonString = IDrinkDao.findDrinkByIDJSON(id);

            if(jsonString.equals("null"))
                System.out.println("No Drink found");
            else {
                System.out.println(jsonString);
            }

        }
        catch( DaoException e )
        {
            e.printStackTrace();
        }
    }

    public void deleteDrinkByID(String id)
    {
        try
        {
            System.out.println("deleteDrinkeByID()");
            Drink drink1 = IDrinkDao.deleteDrinkByID(id);
            IDrinkDao.deleteDrinkByID(id);
            Drink drink2 = IDrinkDao.deleteDrinkByID(id);

            if(drink1 != null && drink2 == null)
            {
                System.out.println("Drink Deleted Successfully");
            }
            else if(drink1 == null)
            {
                System.out.println("The Drink with id = " + id + " wasn't in the database");
            }
            else {
                System.out.println("Drink deletion has failed");
            }

        }
        catch( DaoException e )
        {
            e.printStackTrace();
        }
    }

    public void addDrinkToDB()
    {
        String brand = "";
        String name = "";
        int enteredSize = 0;
        float enteredPrice = -1;
        int enteredStockAvalible = -1;
        Scanner kb = new Scanner(System.in);


        while(brand == "") {
            System.out.println("Please enter Drink Brand: ");
            brand = kb.nextLine();
        };

        while(name == "") {
            System.out.println("Please enter Drink Name: ");
            name = kb.nextLine();
        };

        while(enteredSize < 1) {
            System.out.println("Please enter Drink Size (ml): ");
            enteredSize = kb.nextInt();
        };

        while(enteredPrice <= 0) {
            System.out.println("Please enter Drink price: ");
            enteredPrice = kb.nextFloat();
        };
        ;

        while(enteredStockAvalible <= 0) {
            System.out.println("Please enter Drink Stock Avalible: ");
            enteredStockAvalible = kb.nextInt();
        };

        try {
            IDrinkDao.addDrink(brand, name, enteredSize, enteredPrice,  enteredStockAvalible);
            System.out.println("Added Successfully");
        }
        catch( DaoException e )
        {
            e.printStackTrace();
        }

    }




}

package SoftDrinks.Multitheard;

import SoftDrinks.BusinessClasses.StockBrandComparator;
import SoftDrinks.DAOs.MySqlDrinkDao;
import SoftDrinks.DAOs.DrinkDaoInterface;
import SoftDrinks.DTOs.DataInfo;
import SoftDrinks.DTOs.Drink;
import SoftDrinks.DTOs.WholeSaler;
import SoftDrinks.Exceptions.DaoException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;





public class Server {

    List<Drink> drinks;
    Map<Integer, WholeSaler> mapOfOrigin;
    Map<Integer, Drink> StockNumMap;
    PriorityQueue<Drink> queue;
    PriorityQueue<Drink> queueDBFiltered;
    PriorityQueue<Drink> twoFieldQueue;
    List<Drink> listDBFiltered;
    DrinkDaoInterface IDrinkDao = new MySqlDrinkDao();



    public static void main(String[] args)
    {
        Server server = new Server();
        server.start();
    }

    public void start() {
        System.out.println("OOP Project(CA5) - Soft Drinks");

        this.drinks = new ArrayList<>();
        this.mapOfOrigin = new HashMap<Integer, WholeSaler>();
        this.StockNumMap = new TreeMap<>();
        this.queue = new PriorityQueue<>();
        this.queueDBFiltered = new PriorityQueue<>();
        this.twoFieldQueue = new PriorityQueue<>(new StockBrandComparator());
        initialize();
        try
        {
            ServerSocket ss = new ServerSocket(8080);  // set up ServerSocket to listen for connections on port 8080

            System.out.println("Server: Server started. Listening for connections on port 8080...");

            int clientNumber = 0;  // a number for clients that the server allocates as clients connect

            while (true)    // loop continuously to accept new client connections
            {
                Socket socket = ss.accept();    // listen (and wait) for a connection, accept the connection,
                // and open a new socket to communicate with the client
                clientNumber++;

                System.out.println("Server: Client " + clientNumber + " has connected.");

                System.out.println("Server: Port# of remote client: " + socket.getPort());
                System.out.println("Server: Port# of this server: " + socket.getLocalPort());

                Thread t = new Thread(new ClientHandler(socket, clientNumber, IDrinkDao)); // create a new ClientHandler for the client,
                t.start();                                                  // and run it in its own thread

                System.out.println("Server: ClientHandler started in thread for client " + clientNumber + ". ");
                System.out.println("Server: Listening for further connections...");
            }
        } catch (IOException e)
        {
            System.out.println("Server: IOException: " + e);
        }
        System.out.println("Server: Server exiting, Goodbye!");
    }

    public class ClientHandler implements Runnable   // each ClientHandler communicates with one Client
    {


        BufferedReader socketReader;
        PrintWriter socketWriter;
        Socket socket;
        int clientNumber;

        public ClientHandler(Socket clientSocket, int clientNumber, DrinkDaoInterface IDrinkDao)
        {
            try
            {
                InputStreamReader isReader = new InputStreamReader(clientSocket.getInputStream());
                this.socketReader = new BufferedReader(isReader);

                OutputStream os = clientSocket.getOutputStream();
                this.socketWriter = new PrintWriter(os, true); // true => auto flush socket buffer

                this.clientNumber = clientNumber;  // ID number that we are assigning to this client

                this.socket = clientSocket;  // store socket ref for closing

            } catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }

        @Override
        public void run()
        {

            String message;
            try
            {
                while ((message = socketReader.readLine()) != null)
                {
                    System.out.println("Server: (ClientHandler): Read command from client " + clientNumber + ": " + message);

                    if (message.equals("DisplayAllDrink"))
                    {
                        socketWriter.println(findAllDrinkJSON());  // sends current time to client
                    }
                    else if (message.startsWith("DisplayDrinkById"))
                    {
                        String[] tokens = message.split(" ");
                        String param1 = tokens[0];
                        String param2 = tokens[1];
                        System.out.println("param1 == " + param1 + "\tparam2 == " + param2);

                        try {
                            String res = findDrinkByIDJSON(param2);

                            Integer.parseInt(param2);

                            System.out.println("res == " + res);

                            socketWriter.println(findDrinkByIDJSON(param2));

                            message = message.substring(5);
                            socketWriter.println(message);
                        }
                        catch (Exception e)
                        {
                            socketWriter.println("No Drink found");
                        }


                    }
                    else if (message.startsWith("AddDrinkToDb"))
                    {

                        System.out.println("Arriving from server: " + message);

                        String msg = socketReader.readLine();

                        Gson gsonParser = new Gson();
                        Drink drink1 = gsonParser.fromJson(msg, Drink.class);
                        try {
                            IDrinkDao.addDrink(drink1.getBrand(), drink1.getName(), drink1.getSize(), drink1.getPrice(), drink1.getStockAvailable());
                            String g = drink1.getName() + " " + drink1.getBrand() + " " + drink1.getSize();
                            String result = IDrinkDao.findDrinkByNameBrandSizeJSON(g);
                            socketWriter.println(result);
                        }
                        catch( DaoException e )
                        {
                            e.printStackTrace();
                            socketWriter.println("Please try again add  Unsuccessful ");
                        }
                    }
                    else if (message.startsWith("DelDrinkByID"))
                    {
                        String[] tokens = message.split(" ");
                        String param1 = tokens[0];
                        String param2 = tokens[1];
                        System.out.println("param1 == " + param1 + "\tparam2 == " + param2);

                        String postActionMsg = deleteDrinkByID(param2);

                        socketWriter.println(postActionMsg);
                    }
                    else if (message.equals("GetSummaryData"))
                    {
                        String serialized = findAllDrinkJSON();

                        Type perfList = new TypeToken<ArrayList<Drink>>(){}.getType();
                        List<Drink> gottenList = new Gson().fromJson(serialized, perfList);

                        String summaryObject = getStats(gottenList);

                        socketWriter.println(summaryObject);
                    }

//                        String res = findDrinkByIDJSON(param2);
//                        System.out.println("res == " + res);
//
//                        socketWriter.println(findDrinkByIDJSON(param2));
//                   }
//                    else if (message.startsWith("Triple"))
//                    {
//                        message = message.substring(7);
//                        int res = Integer.parseInt(message);
//                        res = res * 3;
//                        socketWriter.println(res);  // send message to client
//                    }
//                    else if (message.startsWith("Add"))
//                    {
//                        String[] tokens = message.split(" ");
//                        String param1 = tokens[1];
//                        String param2 = tokens[2];
//                        int p1 = Integer.parseInt(param1);
//                        int p2 = Integer.parseInt(param2);
//                        int ans = p1 + p2;
//                        socketWriter.println(ans);
//
//                    }
//                    else
                    else
                    {
                        socketWriter.println("incorrect command couldn't understand");
                    }
                }

                socket.close();

            } catch (IOException | DaoException ex)
            {
                ex.printStackTrace();
            }
            System.out.println("Server: (ClientHandler): Handler for Client " + clientNumber + " is terminating .....");
        }
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

    public String displayList(List <Drink> list)
    {
        Gson gson = new Gson();

        String jsonString = gson.toJson(list);
        return jsonString;
    }
    public void displayStockNumMap()
    {
        for (Map.Entry<Integer, Drink> entry : StockNumMap.entrySet()) {
            System.out.println("Key: " + entry.getKey() + ".\t Value: " + entry.getValue());
        }
    }

    public void displayTwoFieldQueue()
    {
        while ( !twoFieldQueue.isEmpty() ) {
            System.out.println(twoFieldQueue.remove());
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


    public String findAllDrinkJSON() throws DaoException {
//        try
//        {
            System.out.println("\nCall findAllDrinkJSON()");
            String jsonString = IDrinkDao.findAllDrinkJSON();


               return jsonString;
           // }

//        }
//        catch( DaoException e )
//        {
//            e.printStackTrace();
//        }

        //return "No Drink found";
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

    public String findDrinkByIDJSON(String id)
    {
        try
        {
            System.out.println("findDrinkByIDJSON()");
            String jsonString = IDrinkDao.findDrinkByIDJSON(id);

            if(jsonString.equals("null"))
                System.out.println("No Drink found");
            else {
               return jsonString;
            }

        }
        catch( DaoException e )
        {
            e.printStackTrace();
        }
        return "No Drink found";
    }

    public void listFilteredDrinks(float x)
    {
        System.out.println("Showing all Drinks sub " + x + " Euro, ordered by (price/ml), hi->lo");

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
                System.out.println(r.toString() + "\t-\tPrice per ml: €" + (Double.valueOf(Math.round((r.getPrice()/r.getSize()) * 100)) / 100) );
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
    public String deleteDrinkByID(String id)
    {
        try
        {
            System.out.println("deleteDrinkByID()");
            Drink drink1 = IDrinkDao.findDrinkByID(id);
            IDrinkDao.deleteDrinkByID(id);
            Drink drink2 = IDrinkDao.findDrinkByID(id);
            String returnVal= "";

            if(drink2 != null && drink2 == null)
            {
                returnVal = "Drink Deleted Successfully";
                System.out.println(returnVal);
            }
            else if(drink2 == null)
            {
                returnVal = "The Drink with id = " + id + " is not in the database";
                System.out.println(returnVal);
            }
            else {
                returnVal = "Drink deletion has failed";
                System.out.println(returnVal);
            }
            return returnVal;

        }
        catch( DaoException e )
        {
            e.printStackTrace();
            return "NA";
        }
    }
    public String getStats(List<Drink> gottenList)
    {
        Gson gson = new Gson();

        int totalStock = 0;
        int totalProduct = 0;
        for(Drink p: gottenList)
        {
            totalStock = totalStock + p.getStockAvailable();
            totalProduct++;
        }

        DataInfo data = new DataInfo(totalStock, totalProduct);

        String jsonString = gson.toJson(data);

        return jsonString;
    }

}

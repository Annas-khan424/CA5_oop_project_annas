package SoftDrinks.Multitheard;

import SoftDrinks.BusinessClasses.App;
import SoftDrinks.BusinessClasses.StockBrandComparator;
import SoftDrinks.DAOs.MySqlDrinkDao;
import SoftDrinks.DAOs.DrinkDaoInterface;
import SoftDrinks.DTOs.Drink;
import SoftDrinks.DTOs.WholeSaler;
import SoftDrinks.Exceptions.DaoException;

import java.io.*;
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


                        String res = findDrinkByIDJSON(param2);
                        System.out.println("res == " + res);

                        socketWriter.println(findDrinkByIDJSON(param2));

                        message = message.substring(5); // strip off the 'Echo ' part
                        socketWriter.println(message);  // send message to client
                    }
                    else if (message.startsWith("Triple"))
                    {
                        message = message.substring(7);
                        int res = Integer.parseInt(message);
                        res = res * 3;
                        socketWriter.println(res);  // send message to client
                    }
                    else if (message.startsWith("Add"))
                    {
                        String[] tokens = message.split(" ");
                        String param1 = tokens[1];
                        String param2 = tokens[2];
                        int p1 = Integer.parseInt(param1);
                        int p2 = Integer.parseInt(param2);
                        int ans = p1 + p2;
                        socketWriter.println(ans);

                    }
                    else
                    {
                        socketWriter.println("incorrect command couldn't understand");
                    }
                }

                socket.close();

            } catch (IOException ex)
            {
                ex.printStackTrace();
            }
            System.out.println("Server: (ClientHandler): Handler for Client " + clientNumber + " is terminating .....");
        }
    }
    private void initialize()
    {
        Drink d1 = new Drink(1,"Coca-Cola","Coke                 ",300,1.30,200);//260
        Drink d2 = new Drink(2,"Coca-Cola","Coke-zero            ",300,1.30,670);//871
        Drink d3 = new Drink(3,"Pepsi    ","Pepsi Wild Cherry    ",500,4.30,400);//1720
        Drink d4 = new Drink(4,"Red Bull ","Red Bull Coconut     ",300,2.30,800);//1840
        Drink d5 = new Drink(5,"Sprite   ","Sprite Cherry        ",500,6.90,700);//4830
        Drink d6 = new Drink(6,"Pepsi    ","Pepsi                ",300,5.30,840);//4452
        Drink d7 = new Drink(7,"Monster  ","Zero-Sugar Ultra Red ",500,3.40,20);//68
        Drink d8 = new Drink(8,"Fanta    ","Fanta Lemon          ",350,2.89,50);//144.5
        Drink d9 = new Drink(9,"Monster  ","Mango Loco           ",500,3.00,100);//300
        Drink d10 = new Drink(10,"Schweppes","Russchian Pink Soda ",200,2.00,10);//200

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
    public String findAllDrinkJSON()
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

        return "No Drink found";
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
                System.out.println(jsonString);
            }

        }
        catch( DaoException e )
        {
            e.printStackTrace();
        }
        return "No Drink found";
    }
    public void listFilteredPerfumes(float x)
    {
        System.out.println("Showing all perfumes sub " + x + " Euro, ordered by (price/ml), hi->lo");

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


}

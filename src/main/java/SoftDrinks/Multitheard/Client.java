package SoftDrinks.Multitheard;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import SoftDrinks.DTOs.DataInfo;
import SoftDrinks.DTOs.Drink;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Client {
    List<Drink> drinks;
    Gson gsonParser = new Gson();

    public static void main(String[] args)
    {
        Client client = new Client();
        client.start();
    }

    public void start()
    {
        Scanner in = new Scanner(System.in);
        try {
            Socket socket = new Socket("localhost", 8080);  // connect to server socket
            System.out.println("Client: Port# of this client : " + socket.getLocalPort());
            System.out.println("Client: Port# of Server :" + socket.getPort() );

            System.out.println("Client message: The Client is running and has connected to the server");

            this.drinks = new ArrayList<>();

            final String MENU_ITEMS = "\nMAIN MENU COMMANDS\n"
                    + "Please Enter the same keyword given below to run this program \n"
                    + "1. DisplayAllDrink\t\tView All Drinks\n"
                    + "2. DisplayDrinkById\t\tSample: DisplayDrinkById 11\n"
                    + "3. AddDrinkToDb\t\t\n"
                    + "4. deleteDrinkByID\t\texample: deleteDrinkByID 2\n"
                    + "5. GetSummaryData\n"
                    + "6. Exit\n"
                    + "Enter Option [1,6]";
            System.out.println("\n" + MENU_ITEMS);
            String command = in.nextLine();

            OutputStream os = socket.getOutputStream();
            PrintWriter socketWriter = new PrintWriter(os, true);   // true => auto flush buffers

            socketWriter.println(command);

            Scanner socketReader = new Scanner(socket.getInputStream());  // wait for, and retrieve the reply

            while(!command.equals("Exit")) {

                if (command.equals("DisplayAllDrink"))  //we expect the server to return a time
                {
                    String res = socketReader.nextLine();
                    System.out.println(res);
                    Gson gsonParser = new Gson();

                    Drink[] drinks = gsonParser.fromJson(res,Drink[].class);

                    if (drinks.length<1){
                        System.out.println("no drinks found");
                    }else {
                        for (Drink drink:drinks){
                            System.out.println(drink);
                        }
                    }



//                    Type perfList = new TypeToken<ArrayList<Drink>>(){}.getType();
//                    List<Drink> gottenList = new Gson().fromJson(res, perfList);
//
//                    displayList(gottenList);
                }
                else if(command.startsWith("DisplayDrinkById"))
                {
                    String res = socketReader.nextLine();
                    if(res.equals("No Drink found"))
                    {
                        System.out.println(res);
                    }
                    else
                    {
                        Gson gsonParser = new Gson();
                        Drink result = gsonParser.fromJson(res, Drink.class);
                        display(result);
                    }
                }
                else if(command.equals("AddDrinkToDb"))
                {
                    String details = enterDrinkJSON();
                    socketWriter.println(details);
                    String res = socketReader.nextLine();

                    Drink result = gsonParser.fromJson(res, Drink.class);
                    System.out.println("Coming from server: Successfully added");
                    display(result);
                }

                else if(command.startsWith("DelDrinkByID"))
                {
                    String res = socketReader.nextLine();

                    if(res == "NA")
                    {
                        System.out.println("Sorry there is some error in server");
                    }
                    else
                    {
                        System.out.println(res);
                    }

                }
                else if(command.equals("GetSummaryData"))
                {
                    socketWriter.println(command);
                    String res = socketReader.nextLine();

                    DataInfo data = gsonParser.fromJson(res, DataInfo.class);
                    System.out.println("Coming from server:");
                    displayData(data);
                }

                System.out.println("Please enter correct command");
                command = in.nextLine();
                socketWriter.println(command);

            }
            socketWriter.close();
            socketReader.close();
            socket.close();

        } catch (IOException e) {
            System.out.println("Client message: IOException: "+e);
        }


    }
    public void displayData(DataInfo d)
    {
        System.out.println("------------------------------");
        System.out.println("Total Stock : " + d.getTotalStock());
        System.out.println("Amount of different products : " + d.getTotalProducts());
        System.out.println("------------------------------");
    }

    public void displayList(List <Drink> list)
    {
        for(Drink d: list) {
            System.out.println("------------------------------");
            System.out.println("\tID : " + d.get_id());
            System.out.println("\tBrand : " + d.getBrand());
            System.out.println("\tName : " + d.getName());
            System.out.println("\tDrink size (ml) : " + d.getSize());
            System.out.println("\tPrice : €" + d.getPrice());
            System.out.println("\tStock Available : " + d.getStockAvailable());
        }
    }

    public void display(Drink d)
    {
        System.out.println("------------------------------");
        System.out.println("\tID : " + d.get_id());
        System.out.println("\tBrand : " + d.getBrand());
        System.out.println("\tName : " + d.getName());
        System.out.println("\tDrink size (ml) : " + d.getSize());
        System.out.println("\tPrice : €" + d.getPrice());
        System.out.println("\tStock Available : " + d.getStockAvailable());
        System.out.println("------------------------------");
    }
    public String enterDrinkJSON()
    {

        Drink drink1 = new Drink();

        String brand = "";
        String name = "";
        int enteredSize = 0;
        float enteredPrice = -1;
        int enteredStockAval = -1;
        Scanner kb = new Scanner(System.in);
        String exportString = "AddDrinkToDb";


        while(brand == "") {
            System.out.println("Please enter Drink Brand: ");
            brand = kb.nextLine();
        };
        drink1.setBrand(brand);

        while(name == "") {
            System.out.println("Please enter Drink Name: ");
            name = kb.nextLine();
        };
        drink1.setName(name);

        while(enteredSize <= 0) {
            System.out.println("Please enter Drink Size (ml): ");
            String temp = kb.next();
            try
            {
                enteredSize = Integer.parseInt(temp);
            }
            catch (Exception e)
            {
                System.out.println("Please Try Again");
            }
        };

        drink1.setSize(enteredSize);

        while(enteredPrice <= 0) {
            System.out.println("Please enter Drink price: ");
            String temp = kb.next();
            try
            {
                enteredPrice = Float.parseFloat(temp);
            }
            catch (Exception e)
            {
                System.out.println("Please Try Again");
            }
        };

        drink1.setPrice(enteredPrice);


        while(enteredStockAval <= 0) {
            System.out.println("Please enter Drink Stock Available: ");
            String temp = kb.next();
            try
            {
                enteredStockAval = Integer.parseInt(temp);
            }
            catch (Exception e)
            {
                System.out.println("Please Try Again");
            }
        };
        drink1.setStockAvailable(enteredStockAval);

        Gson gsonParser = new Gson();   // instantiate a Gson Parser

        String result = gsonParser.toJson(drink1);

        return result;
    }


}

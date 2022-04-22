package SoftDrinks.Multitheard;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import SoftDrinks.DTOs.Drink;

public class Client {
    List<Drink> drinks;

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
                    + "1. DisplayAllDrink\t\tView All Drinks\n"
                    + "2. DisplayDrinkById\t\texample: DisplayDrinkById 13\n"
                    + "3. Exit\n"
                    + "Enter Option [1,3]";
            System.out.println("\n" + MENU_ITEMS);
            String command = in.nextLine();

            OutputStream os = socket.getOutputStream();
            PrintWriter socketWriter = new PrintWriter(os, true);   // true => auto flush buffers

            socketWriter.println(command);

            Scanner socketReader = new Scanner(socket.getInputStream());  // wait for, and retrieve the reply

//            boolean continueLoop=true;
            while(!command.equals("Exit")) {

                if (command.equals("DisplayAllDrink"))  //we expect the server to return a time
                {
                    String res = socketReader.nextLine();

                    Type perfList = new TypeToken<ArrayList<Drink>>(){}.getType();
                    List<Drink> gottenList = new Gson().fromJson(res, perfList);

                    displayList(gottenList);
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
//                    System.out.println("Client message: Response from server: " + input);
                }
                else if(command.startsWith("Triple"))
                {
                    String input = socketReader.nextLine();
                    System.out.println("Client message: Response from server: " + input);
                }
                else if(command.startsWith("Add"))
                {
                    String input = socketReader.nextLine();
                    System.out.println("Client message: Response from server: " + input);
                }
                else// the user has entered the Echo command or an invalid command
                {
                    String input = socketReader.nextLine();
                    System.out.println("Client message: Response from server: \"" + input + "\"");
                }

                System.out.println("Please enter new command");
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


}

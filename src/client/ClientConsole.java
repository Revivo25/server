//This file contains material supporting section 3.7 of the textbook:
//"Object Oriented Software Engineering" and is issued under the open-source
//license found at www.lloseng.com 
package client;

import java.io.*;
import java.util.ArrayList;

import common.*;

/**
* This class constructs the UI for a chat client.  It implements the
* chat interface in order to activate the display() method.
* Warning: Some of the code here is cloned in ServerConsole 
*
* @author Fran&ccedil;ois B&eacute;langer
* @author Dr Timothy C. Lethbridge  
* @author Dr Robert Lagani&egrave;re
* @version July 2000
*/
public class ClientConsole implements ChatIF 
{
//Class variables *************************************************

/**
* The default port to connect on.
*/
final public static int DEFAULT_PORT = 5555;

//Instance variables **********************************************

/**
* The instance of the client that created this ConsoleChat.
*/
ChatClient client;


//Constructors ****************************************************

/**
* Constructs an instance of the ClientConsole UI.
*
* @param host The host to connect to.
* @param port The port to connect on.
*/
public ClientConsole(String host, int port) 
{
 try 
 {
   client= new ChatClient(host, port, this);
 } 
 catch(IOException exception) 
 {
   System.out.println("Error: Can't setup connection!"
             + " Terminating client.");
   System.exit(1);
 }
}


//Instance methods ************************************************

/**
* This method waits for input from the console.  Once it is 
* received, it sends it to the client's message handler.
*/
public void accept() {
    try {
        BufferedReader fromConsole = new BufferedReader(new InputStreamReader(System.in));
        String message;

        while (true) {
            System.out.print("> ");
            message = fromConsole.readLine();

            if (message.equalsIgnoreCase("fetch")) {
                client.handleMessageFromClientUI("fetch");

            } else if (message.startsWith("update")) {
                // Format: update orderNumber newParkingSpace yyyy-MM-dd
                String[] parts = message.split(" ");
                if (parts.length == 4) {
                    ArrayList<String> updateData = new ArrayList<>();
                    updateData.add(parts[1]); // orderNumber
                    updateData.add(parts[2]); // newParkingSpace
                    updateData.add(parts[3]); // newOrderDate
                    client.handleMessageFromClientUI(updateData);
                } else {
                    System.out.println("Invalid update format. Use: update <orderNumber> <newParkingSpace> <yyyy-MM-dd>");
                }

            } else if (message.startsWith("add")) {
                // Format: add orderNumber parkingSpace orderDate confirmationCode subscriberId placingDate
                String[] parts = message.split(" ");
                if (parts.length == 7) {
                    ArrayList<String> addData = new ArrayList<>();
                    for (int i = 1; i <= 6; i++) {
                        addData.add(parts[i]);
                    }
                    client.handleMessageFromClientUI(addData);
                } else {
                    System.out.println("Invalid add format. Use: add <orderNumber> <parkingSpace> <orderDate> <confirmationCode> <subscriberId> <placingDate>");
                }

            } else {
                System.out.println("Unknown command. Try: fetch, update, or add");
            }
        }
    } catch (Exception ex) {
        System.out.println("Unexpected error while reading from console!");
    }
}



/**
* This method overrides the method in the ChatIF interface.  It
* displays a message onto the screen.
*
* @param message The string to be displayed.
*/
public void display(String message) 
{
 System.out.println("> " + message);
}


//Class methods ***************************************************

/**
* This method is responsible for the creation of the Client UI.
*
* @param args[0] The host to connect to.
*/
public static void main(String[] args) 
{
 String host = "";
 int port = 0;  //The port number

 try
 {
   host = args[0];
 }
 catch(ArrayIndexOutOfBoundsException e)
 {
   host = "localhost";
 }
 ClientConsole chat= new ClientConsole(host, DEFAULT_PORT);
 chat.accept();  //Wait for console data
}
}
//End of ConsoleChat class
package client;

import ocsf.client.*;
import common.*;
import GuiClient.OrderFormController;

import java.io.*;
import java.util.ArrayList;
import server.Order;

public class ChatClient extends AbstractClient {
    ChatIF clientUI;

    // הפניה ל־OrderFormController
    private static OrderFormController orderFormController;

    public static void setOrderFormController(OrderFormController controller) {
        orderFormController = controller;
    }

    public static OrderFormController getOrderFormController() {
        return orderFormController;
    }

    public ChatClient(String host, int port, ChatIF clientUI) throws IOException {
        super(host, port);
        this.clientUI = clientUI;
        openConnection();
    }

    @Override
    public void handleMessageFromServer(Object msg) {
        if (msg instanceof String) {
            clientUI.display((String) msg);

        } else if (msg instanceof Order) {
            clientUI.display("Received updated order:\n" + msg.toString());

        } else if (msg instanceof ArrayList) {
            ArrayList<?> list = (ArrayList<?>) msg;
            if (!list.isEmpty() && list.get(0) instanceof Order) {
                OrderFormController controller = getOrderFormController();
                if (controller != null) {
                    try {
                        @SuppressWarnings("unchecked")
                        ArrayList<Order> orderList = (ArrayList<Order>) list;
                        controller.setOrders(orderList);
                    } catch (Exception e) {
                        clientUI.display("Failed to update table: " + e.getMessage());
                    }
                } else {
                    clientUI.display("Controller not initialized.");
                }
            } else {
                clientUI.display("Received unknown or empty list.");
            }

        } else {
            clientUI.display("Received unknown object type from server.");
        }
    }

    public void handleMessageFromClientUI(Object info) {
        try {
            sendToServer(info);
        } catch(IOException e) {
            System.out.println("Could not send message to server. Terminating client.");
            quit();
        }
    }

    public void quit() {
        try {
            closeConnection();
        } catch(IOException e) {}
        System.exit(0);
    }

    public void accept(Object msg) {
        try {
            openConnection();
            sendToServer(msg);
        } catch (Exception e) {
            System.out.println("Could not send message to server: " + e.getMessage());
        }
    }
}

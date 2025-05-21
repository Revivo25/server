package client;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import common.ChatIF;
import GuiClient.OrderFormController; // ← תיקון שם החבילה

public class ClientUI extends Application {
    public static ChatClient chat;
    final public static int DEFAULT_PORT = 5555;

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            chat = new ChatClient("10.0.0.16", DEFAULT_PORT, null);
        } catch (Exception e) {
            System.out.println("Failed to connect to server: " + e.getMessage());
            return;
        }

        // Load FXML from GuiClient
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GuiClient/OrderForm.fxml")); // ← תיקון הנתיב
        Parent root = loader.load();
        OrderFormController controller = loader.getController();
        ChatClient.setOrderFormController(controller);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Order Management Client");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

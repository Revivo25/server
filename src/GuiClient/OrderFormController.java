package GuiClient;

import server.Order;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.sql.Date;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;

import client.ClientUI;

public class OrderFormController {

    @FXML private TextField txtOrderNumber;
    @FXML private TextField txtParkingSpace;
    @FXML private DatePicker dateOrderDate;
    @FXML private Button btnUpdate;
    @FXML private Button btnClose;

    @FXML private TableView<Order> orderTable;
    @FXML private TableColumn<Order, Integer> colOrderNumber;
    @FXML private TableColumn<Order, Integer> colParkingSpace;
    @FXML private TableColumn<Order, Date> colOrderDate;
    @FXML private TableColumn<Order, Integer> colConfirmationCode;
    @FXML private TableColumn<Order, Integer> colSubscriberId;
    @FXML private TableColumn<Order, Date> colPlacingDate;

    private ObservableList<Order> orderList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colOrderNumber.setCellValueFactory(new PropertyValueFactory<>("orderNumber"));
        colParkingSpace.setCellValueFactory(new PropertyValueFactory<>("parkingSpace"));
        colOrderDate.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        colConfirmationCode.setCellValueFactory(new PropertyValueFactory<>("confirmationCode"));
        colSubscriberId.setCellValueFactory(new PropertyValueFactory<>("subscriberId"));
        colPlacingDate.setCellValueFactory(new PropertyValueFactory<>("placingDate"));

        orderTable.setItems(orderList);

        orderTable.setOnMouseClicked(e -> handleRowSelect());
    }

    @FXML
    private void handleUpdate(ActionEvent event) {
        try {
            int orderNumber = Integer.parseInt(txtOrderNumber.getText().trim());
            int parkingSpace = Integer.parseInt(txtParkingSpace.getText().trim());
            Date orderDate = Date.valueOf(dateOrderDate.getValue());

            ArrayList<String> data = new ArrayList<>();
            data.add(String.valueOf(orderNumber));
            data.add(String.valueOf(parkingSpace));
            data.add(orderDate.toString());

            ClientUI.chat.accept(data);
            System.out.println("Update sent to server.");
        } catch (Exception e) {
            System.out.println("Error sending update: " + e.getMessage());
        }
    }

    @FXML
    void handleClose(ActionEvent event) {
        ((Stage)((Node)event.getSource()).getScene().getWindow()).close();
    }

    @FXML
    private void handleLoadOrders() {
        ClientUI.chat.accept("fetch");
    }

    public void setOrders(ArrayList<Order> orders) {
        orderList.setAll(orders);
    }

    private void handleRowSelect() {
        Order selected = orderTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            txtOrderNumber.setText(String.valueOf(selected.getOrderNumber()));
            txtParkingSpace.setText(String.valueOf(selected.getParkingSpace()));
            dateOrderDate.setValue(selected.getOrderDate().toLocalDate());
        }
    }
}

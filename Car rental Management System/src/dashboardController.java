import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
// import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
// import javafx.scene.image.Image;
// import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
// import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class dashboardController implements Initializable{

    @FXML
    private TextField availableCar_brand;

    @FXML
    private TextField availableCar_carId;

    @FXML
    private Button availableCar_clearBtn;

    @FXML
    private TableColumn<carData, String> availableCar_col_brand;

    @FXML
    private TableColumn<carData, String> availableCar_col_carId;

    @FXML
    private TableColumn<carData, String> availableCar_col_model;

    @FXML
    private TableColumn<carData, String> availableCar_col_price;

    @FXML
    private TableColumn<carData, String> availableCar_col_status;

    @FXML
    private Button availableCar_deleteBtn;

    // @FXML
    // private ImageView availableCar_imageView;

    // @FXML
    // private Button availableCar_importBtn;

    @FXML
    private Button availableCar_insertBtn;

    @FXML
    private TextField availableCar_model;

    @FXML
    private TextField availableCar_price;

    // @FXML
    // private TextField availableCar_search;

    @FXML
    private ComboBox<?> availableCar_status;

    @FXML
    private TableView<carData> availableCar_tableView;

    @FXML
    private Button availableCar_updateBtn;

    @FXML
    private Button availableCars_btn;

    @FXML
    private AnchorPane availableCars_form;

    @FXML
    private Button close;

    @FXML
    private Label home_availableCars;

    @FXML
    private Button home_btn;

    // @FXML
    // private LineChart<?, ?> home_customerChart;

    @FXML
    private AnchorPane home_form;

    // @FXML
    // private BarChart<?, ?> home_incomeChart;

    @FXML
    private Label home_totalCustomers;

    @FXML
    private Label home_totalIncome;

    @FXML
    private FontAwesomeIcon logoutBtn;

    @FXML
    private AnchorPane main_form;

    @FXML
    private Button minimize;

    @FXML
    private Button rentBtn;

    @FXML
    private Button rentCar_btn;

    @FXML
    private AnchorPane rentCar_home;

    @FXML
    private TextField rent_amount;

    @FXML
    private Label rent_balance;

    @FXML
    private ComboBox<?> rent_brand;

    @FXML
    private ComboBox<?> rent_carId;

    @FXML
    private TableColumn<?, ?> rent_col_brand;

    @FXML
    private TableColumn<carData, String> rent_col_carId;

    @FXML
    private TableColumn<carData, String> rent_col_modal;

    @FXML
    private TableColumn<carData, String> rent_col_price;
    
    @FXML
    private TableColumn<carData, String> rent_col_status;
    
    @FXML
    private DatePicker rent_dateRented;

    @FXML
    private DatePicker rent_dateReturn;

    @FXML
    private TextField rent_YourName;

    // @FXML
    // private ComboBox<?> rent_gender;

    // @FXML
    // private TextField rent_lastName;

    @FXML
    private ComboBox<?> rent_model;

    @FXML
    private TableView<carData> rent_tableView;

    @FXML
    private Label rent_total;

    @FXML
    private AnchorPane rentcar_form;

    @FXML
    private Label userName;


    // database tools
    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;
    private Statement statement;

    public void homeAvailableCars(){
        String sql = "SELECT COUNT(id) FROM car WHERE status = 'Available'";
        connect = database.connectDb();
        int countAC = 0;
        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()) {
                countAC = result.getInt("COUNT(id)");
            }

            home_availableCars.setText(String.valueOf(countAC));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void homeTotalIncome(){
        String sql = "SELECT SUM(total) FROM customer";
        
        double sumIncome = 0;
        
        connect = database.connectDb();
        
        try{
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            
            while(result.next()){
                sumIncome = result.getDouble("SUM(total)");
            }
            home_totalIncome.setText("$" + String.valueOf(sumIncome));
        }catch(Exception e){e.printStackTrace();}
    }

    public void homeTotalCustomers(){
        
        String sql = "SELECT COUNT(id) FROM customer";
        
        int countTC = 0;
        
        connect = database.connectDb();
        
        try{
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            
            while(result.next()){
                countTC = result.getInt("COUNT(id)");
            }
            home_totalCustomers.setText(String.valueOf(countTC));
        }catch(Exception e){e.printStackTrace();}
        
    }
    
    public void availableCarAdd() {
        String sql = "INSERT INTO car (car_id, brand, model, price, status, date) VALUES (?, ?, ?, ?, ?, ?)";

        connect = database.connectDb();

        try {
            Alert alert;

            if (availableCar_carId.getText().isEmpty()
                    || availableCar_brand.getText().isEmpty()
                    || availableCar_model.getText().isEmpty()
                    || availableCar_status.getSelectionModel().getSelectedItem() == null
                    || availableCar_price.getText().isEmpty()) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            } else {
                prepare = connect.prepareStatement(sql);
                prepare.setString(1, availableCar_carId.getText());
                prepare.setString(2, availableCar_brand.getText());
                prepare.setString(3, availableCar_model.getText());
                prepare.setDouble(4, Double.parseDouble(availableCar_price.getText()));
                prepare.setString(5, (String) availableCar_status.getSelectionModel().getSelectedItem());

                // Setting current date
                Date currentDate = Date.valueOf(java.time.LocalDate.now());
                prepare.setDate(6, currentDate);

                prepare.executeUpdate();

                alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Information Message");
                alert.setHeaderText(null);
                alert.setContentText("Successfully Added!");
                alert.showAndWait();

                availableCarShowListData();
                availableCarClear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void availableCarClear() {
        availableCar_carId.setText("");
        availableCar_brand.setText("");
        availableCar_model.setText("");
        availableCar_status.getSelectionModel().clearSelection();
        availableCar_price.setText("");

        // getData.path = "";
        // availableCars_imageView.setImage(null);
    }

    public void availableCarDelete() {

        String sql = "DELETE FROM car WHERE car_id = '" + availableCar_carId.getText() + "'";
        connect = database.connectDb();

        try {
            Alert alert;
            if (availableCar_carId.getText().isEmpty()
                    || availableCar_brand.getText().isEmpty()
                    || availableCar_model.getText().isEmpty()
                    || availableCar_status.getSelectionModel().getSelectedItem() == null
                    || availableCar_price.getText().isEmpty()
                    // || getData.path == null || getData.path == ""
                    ) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            } else {
                alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to DELETE Car ID: " + availableCar_carId.getText() + "?");
                Optional<ButtonType> option = alert.showAndWait();

                if (option.get().equals(ButtonType.OK)) {
                    statement = connect.createStatement();
                    statement.executeUpdate(sql);

                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Deleted!");
                    alert.showAndWait();

                    availableCarShowListData();
                    availableCarClear();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void availableCarUpdate() {
        String sql = "UPDATE car SET brand = ?, model = ?, status = ?, price = ? WHERE car_id = ?";
    
        connect = database.connectDb();
    
        try {
            Alert alert;
    
            // Validate that all fields are filled
            if (availableCar_carId.getText().isEmpty()
                    || availableCar_brand.getText().isEmpty()
                    || availableCar_model.getText().isEmpty()
                    || availableCar_status.getSelectionModel().getSelectedItem() == null
                    || availableCar_price.getText().isEmpty()) {
    
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
                return;
            }
    
            // Validate that the price is a valid number
            double price;
            try {
                price = Double.parseDouble(availableCar_price.getText());
            } catch (NumberFormatException e) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Price must be a valid number");
                alert.showAndWait();
                return;
            }
    
            // Confirm update action
            alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Message");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to UPDATE Car ID: " + availableCar_carId.getText() + "?");
            Optional<ButtonType> option = alert.showAndWait();
    
            if (option.isPresent() && option.get().equals(ButtonType.OK)) {
                // Use PreparedStatement to safely execute the SQL query
                prepare = connect.prepareStatement(sql);
                prepare.setString(1, availableCar_brand.getText());
                prepare.setString(2, availableCar_model.getText());
                prepare.setString(3, availableCar_status.getSelectionModel().getSelectedItem().toString());
                prepare.setDouble(4, price);
                prepare.setString(5, availableCar_carId.getText());
    
                prepare.executeUpdate();
    
                alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Information Message");
                alert.setHeaderText(null);
                alert.setContentText("Successfully Updated!");
                alert.showAndWait();
    
                availableCarShowListData();
                availableCarClear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    



    private String[] listStatus = {"Available", "Not Available"};
    public void availableCarStatusList() {
        List<String> listS = new ArrayList<>();
        for (String data : listStatus) {
            listS.add(data);
        }
        ObservableList listData = FXCollections.observableArrayList(listS);
        availableCar_status.setItems(listData);
    }
    
    public ObservableList<carData> availableCarListData() {
        ObservableList<carData> listData = FXCollections.observableArrayList();
        String sql = "SELECT * FROM car";
        connect = database.connectDb();
        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            
            carData carD;
            while (result.next()) {
                carD = new carData(
                        result.getInt("car_id"),
                        result.getString("brand"),
                        result.getString("model"),
                        result.getDouble("price"),
                        result.getString("status"),
                        result.getDate("date")); // Corrected column name

                listData.add(carD);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }

    private ObservableList<carData> availableCarList;
    public void availableCarShowListData(){
        availableCarList =  availableCarListData();
        availableCar_col_carId.setCellValueFactory(new PropertyValueFactory<>("carId"));
        availableCar_col_brand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        availableCar_col_model.setCellValueFactory(new PropertyValueFactory<>("model"));
        availableCar_col_price.setCellValueFactory(new PropertyValueFactory<>("price"));
        availableCar_col_status.setCellValueFactory(new PropertyValueFactory<>("status"));

        availableCar_tableView.setItems(availableCarList); 
    }

    public void availableCarSelect(){
        carData carD = availableCar_tableView.getSelectionModel().getSelectedItem();
        int num = availableCar_tableView.getSelectionModel().getSelectedIndex();

        if((num - 1) < - 1){
            return;
        }

        availableCar_carId.setText(String.valueOf(carD.getCarId()));
        availableCar_brand.setText(carD.getBrand());
        availableCar_model.setText(carD.getModel());
        availableCar_price.setText(String.valueOf(carD.getPrice()));

        // String url = "file:" + carD.getImage();
        // image = new Image(url, 116, 153, false, true);
        // availableCar_imageView(image);
    }

    public void renttPay(){
        String sql = "INSERT INTO CUSTOMER" + "(customer_id, yourName, car_id, brand" + ", model, total, date_rented, date_return)" + "VALUES(?,?,?,?,?,?,?,?)";
        connect = database.connectDb();
        
        try{
            Alert alert;
            
            if(rent_YourName.getText().isEmpty()  || rent_carId.getSelectionModel().getSelectedItem() == null
                    || rent_brand.getSelectionModel().getSelectedItem() == null  || rent_model.getSelectionModel().getSelectedItem() == null
                    || totalP == 0 || rent_amount.getText().isEmpty()){
                alert = new Alert(AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Something wrong :3");
                alert.showAndWait();
            } else {
                alert = new Alert(AlertType.CONFIRMATION);
                alert.setHeaderText(null);
                alert.setContentText("Are you sure?");
                Optional<ButtonType> option = alert.showAndWait();
                
                if(option.get().equals(ButtonType.OK)){
                
                    prepare = connect.prepareStatement(sql);
                    prepare.setString(1, String.valueOf(customerId));
                    prepare.setString(2, rent_YourName.getText());
                    prepare.setString(3, (String)rent_carId.getSelectionModel().getSelectedItem());
                    prepare.setString(4, (String)rent_brand.getSelectionModel().getSelectedItem());
                    prepare.setString(5, (String)rent_model.getSelectionModel().getSelectedItem());
                    prepare.setString(6, String.valueOf(totalP));
                    prepare.setString(7, String.valueOf(rent_dateRented.getValue()));
                    prepare.setString(8, String.valueOf(rent_dateReturn.getValue()));

                    prepare.executeUpdate();
                    
                    // SET THE  STATUS OF CAR TO NOT AVAILABLE 
                    String updateCar = "UPDATE car SET status = 'Not Available' WHERE car_id = '"
                            +rent_carId.getSelectionModel().getSelectedItem()+"'";
                    
                    statement = connect.createStatement();
                    statement.executeUpdate(updateCar);
                    
                    alert = new Alert(AlertType.INFORMATION);
                    alert.setHeaderText(null);
                    alert.setContentText("Successful!");
                    alert.showAndWait();
                    
                    rentCarShowListData();
                    rentClear();
                } // NOW LETS PROCEED TO DASHBOARD FORM : ) 
            }
        }catch(Exception e){e.printStackTrace();}
        
    }

    public void rentClear(){
        totalP = 0;
        rent_YourName.setText("");
        amount = 0;
        balance = 0;
        rent_balance.setText("$0.0");
        rent_total.setText("$0.0");
        rent_amount.setText("");
        rent_carId.getSelectionModel().clearSelection();
        rent_brand.getSelectionModel().clearSelection();
        rent_model.getSelectionModel().clearSelection();
    }

    private int customerId;
    public void rentCustomerId(){
        String sql = "SELECT id FROM customer";
        
        connect = database.connectDb();
        
        try{
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            
            while(result.next()){
                // GET THE LAST id and add + 1
                customerId = result.getInt("id") + 1;
            }
            
        }catch(Exception e){e.printStackTrace();}
    }

    private double balance;
    private double amount;
    public void rentAmount(){
        Alert alert;
        if (totalP == 0 || rent_amount.getText().isEmpty()) {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Invalid: 3");
            alert.showAndWait();

            rent_amount.setText("");

        } else {
            amount = Double.parseDouble(rent_amount.getText());

            if(amount >=  totalP){
                balance = (amount - totalP);
                rent_balance.setText("$" + String.valueOf(balance));
            }else{
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Invalid: 3");
                alert.showAndWait();

                rent_amount.setText("");

            }
        }
    }

    public ObservableList<carData> rentCarListData(){
        ObservableList<carData> listData = FXCollections.observableArrayList();
        String sql = "SELECT * FROM car";

        connect = database.connectDb();
        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            carData carD;

            while (result.next()) {
                carD = new carData(
                    result.getInt("car_id"), 
                    result.getString("brand"), 
                    result.getString("model"), 
                    result.getDouble("price"), 
                    result.getString("status"), 
                    result.getDate("date"));

                    listData.add(carD);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }

    // public void 
    private int countDate;
    public void rentDate(){
        Alert alert;
        if (rent_carId.getSelectionModel().getSelectedItem() == null || rent_brand.getSelectionModel().getSelectedItem() == null 
        || rent_model.getSelectionModel().getSelectedItem() == null) {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Something wrong: 3");
            alert.showAndWait();

            rent_dateReturn.setValue(null);
            rent_dateRented.setValue(null);
        }else{
            if (rent_dateReturn.getValue().isAfter(rent_dateRented.getValue())) {
                // COUNT THE DAY
                countDate = rent_dateReturn.getValue().compareTo(rent_dateRented.getValue());
            }else{
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Something wrong: 3");
                alert.showAndWait();

                // INCREASE OF 1 DAY ONCE THE USER CLICKED THE SAME DAY
                rent_dateReturn.setValue(rent_dateReturn.getValue().plusDays(1));
            }
        }
    }

    private double totalP;
    public void rentDisplayTotal(){
        rentDate();
        double price = 0;
        String sql = "SELECT price, model FROM car WHERE model = '"+rent_model.getSelectionModel().getSelectedItem()+"'";
        connect = database.connectDb();
        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            if (result.next()) {
                price = result.getDouble("price");
            }

            // price * day you want to use car
            totalP = (price * countDate);
            // DISPLAY TOTAL
            rent_total.setText("$" + String.valueOf(totalP));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void rentCarCarId(){
        String sql = "SELECT * FROM car WHERE status = 'Available' OR status = 'Not Available'";
        connect = database.connectDb();
        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            ObservableList listData = FXCollections.observableArrayList();

            while (result.next()) {
                listData.add(result.getString("car_id"));
            }

            rent_carId.setItems(listData);
            rentCarBrand();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void rentCarBrand(){
        String sql = "SELECT * FROM car WHERE car_id = '"+rent_carId.getSelectionModel().getSelectedItem()+"'";
        connect = database.connectDb();
        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            ObservableList listData = FXCollections.observableArrayList();

            while (result.next()) {
                listData.add(result.getString("brand"));
            }

            rent_brand.setItems(listData);
            rentCarModel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void rentCarModel(){
        String sql = "SELECT * FROM car WHERE brand = '"+rent_brand.getSelectionModel().getSelectedItem()+"'";
        connect = database.connectDb();
        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            ObservableList listData = FXCollections.observableArrayList();

            while (result.next()) {
                listData.add(result.getString("model"));
            }

            rent_model.setItems(listData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private ObservableList<carData> rentCarList;
    public void rentCarShowListData(){
        rentCarList = rentCarListData();

        rent_col_carId.setCellValueFactory(new PropertyValueFactory<>("carId"));
        rent_col_brand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        rent_col_modal.setCellValueFactory(new PropertyValueFactory<>("model"));
        rent_col_price.setCellValueFactory(new PropertyValueFactory<>("price"));
        rent_col_status.setCellValueFactory(new PropertyValueFactory<>("status"));

        rent_tableView.setItems(rentCarList);
    }

    public void displayUserName(){
        String user = getData.userName;
        userName.setText(user.substring(0, 1).toUpperCase() + user.substring( 1));
    }

    private double x = 0;
    private double y = 0;

    public void logout(){
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Message");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to logout");
        Optional<ButtonType> option = alert.showAndWait();

        try {
        if(option.get().equals(ButtonType.OK)){
            // HIDE YOUR LOGIN FORM
            logoutBtn.getScene().getWindow().hide();

            // LINK YOUR LOGIN FORM
           Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();

            root.setOnMousePressed((MouseEvent event) -> {
                x = event.getSceneX();
                y = event.getSceneY();
            });

            root.setOnMouseDragged((MouseEvent event) -> {
                stage.setX(event.getScreenX() - x);
                stage.setY(event.getScreenY() - y);

                stage.setOpacity(.8);
            });

            root.setOnMouseReleased((MouseEvent event) -> {
                stage.setOpacity(1.0);
            });

            stage.initStyle(StageStyle.TRANSPARENT);

            stage.initStyle(StageStyle.TRANSPARENT);

            stage.setScene(scene);
            stage.show();
        }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void switchForm(ActionEvent event) {
        if (event.getSource() == home_btn) {
            home_form.setVisible(true);
            availableCars_form.setVisible(false);
            rentcar_form.setVisible(false);

            home_btn.setStyle("-fx-background-color: linear-gradient(to bottom right, #686f86, #8e9296)");
            availableCars_btn.setStyle("-fx-background-color: transparent");
            rentCar_btn.setStyle("-fx-background-color: transparent");

            homeAvailableCars();
            homeTotalCustomers();
            homeTotalIncome();

        } else if (event.getSource() == availableCars_btn) {
            home_form.setVisible(false);
            availableCars_form.setVisible(true);
            rentcar_form.setVisible(false);

            availableCars_btn.setStyle("-fx-background-color: linear-gradient(to bottom right, #686f86, #8e9296)");
            home_btn.setStyle("-fx-background-color: transparent");
            rentCar_btn.setStyle("-fx-background-color: transparent");

            // Update table and status list
            availableCarShowListData();
            availableCarStatusList();

        } else if (event.getSource() == rentCar_btn) {
            home_form.setVisible(false);
            availableCars_form.setVisible(false);
            rentcar_form.setVisible(true);

            rentCar_btn.setStyle("-fx-background-color: linear-gradient(to bottom right, #686f86, #8e9296)");
            home_btn.setStyle("-fx-background-color: transparent");
            availableCars_btn.setStyle("-fx-background-color: transparent");

            rentCarShowListData();
            rentCarCarId();
            rentCarBrand();
            rentCarModel();
        }
    }

    public void close(){
        System.exit(0);
    }

    public void minimize(){
        Stage stage = (Stage)main_form.getScene().getWindow();
        stage.setIconified(true);
        
    }
    public void initialize(URL location, ResourceBundle resource){
        displayUserName();

        homeAvailableCars();
        homeTotalCustomers();
        homeTotalIncome();

        // TO DISPLAY THE DATA OF TABLE VIEW
        availableCarShowListData();
        availableCarStatusList();

        rentCarShowListData();
        rentCarCarId();
        rentCarBrand();
        rentCarModel();
    }
} 

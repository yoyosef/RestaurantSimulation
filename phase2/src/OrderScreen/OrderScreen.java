package OrderScreen;

import ComplementScreen.ComplementScreen;
import Restaurant.Dish;
import Restaurant.Restaurant;
import Restaurant.Server;
import Restaurant.Table;
import Restaurant.MenuItem;
import Restaurant.ModelControllerInterface;
import TablesScreen.TablesScreen;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.scene.layout.VBox;
import notification.Notification;


public class OrderScreen implements EventHandler<ActionEvent>, Initializable, ModelControllerInterface {

  public Button buttonSend;
  public Button buttonBack;
  public ComboBox customerNumberDropDown;

  public TableColumn customerNumberColumn;
    public Button addComplimentsButton;
    private Server server;
  private Table table;
  private Restaurant restaurant;
  public Label tableOrderTitle;
  public Label menuLabel;
  public Button addDish;
  public Button removeDish;
  public TableView menuTableView;
  public TableView orderTableView;
  public TableColumn menuIdColumn;
  public TableColumn menuDishColumn;
  public TableColumn menuPriceColumn;
  public TableColumn menuIngredientsColumn;
  public Button openBillScreen;
  public TableColumn commentColumn;
  public TableColumn idColumn;
  public TableColumn nameColumn;
  public Button addCommentButton;
  public Pane paneBox;

  private double menuSelectedDishId;
  private String menuSelectedDishName;
  private int menuSelectedDishCustomerNum;
  private ArrayList<Dish> dishes;

  private Notification notification;
  @FXML private Pane notificationArea;
  @FXML private VBox vBox;




  @Override
  public void initialize(URL location, ResourceBundle resources) {
    getIdColumn().setCellValueFactory(new PropertyValueFactory<Dish, Double>("id"));
    getNameColumn().setCellValueFactory(new PropertyValueFactory<Dish, String>("name"));
    getCustomerNumberColumn().setCellValueFactory(new PropertyValueFactory<Dish, Integer>("customerNum"));

    menuIdColumn.setCellValueFactory(new PropertyValueFactory<MenuItem, Double>("id"));
    menuDishColumn.setCellValueFactory(new PropertyValueFactory<MenuItem, String>("name"));
    menuPriceColumn.setCellValueFactory(new PropertyValueFactory<MenuItem, Double>("price"));

    this.setRowAction();
    System.out.println(getOrderTableView());
    notification = new Notification();
    notificationArea.getChildren().setAll(notification);
    
  }

  public void setRowAction() {
    getOrderTableView().setRowFactory(tv -> {
      TableRow<Dish> row = new TableRow<>();
      row.setOnMouseClicked(event -> {
        if (!row.isEmpty() && event.getClickCount() == 2) {
          Dish rowData = row.getItem();
          System.out.println("Click on: " + rowData.getName());
          String finalString =
              "The price of this dish is: " + rowData.getPrice() + System.lineSeparator() +
                  "The ingredients of this dish are: " + System.lineSeparator() + rowData
                  .getIngredientString()
                  + "Comment: " + rowData.getComment();
          Alert alert = new Alert(Alert.AlertType.NONE, finalString,
              ButtonType.OK);
          alert.setTitle("Dish details");
          alert.showAndWait();
          System.out.println(rowData.getPrice());
        }
      });
      return row;
    });
  }

  /**
   * Opens the bill screen GUI when the print bill button is clicked
   */
  public void openBillScreen() throws IOException {
    System.out.println("Loading Bill Screen.....");
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
        "../BillScreen/bill.fxml"));
    Parent root1 = fxmlLoader.load();
    paneBox.getChildren().setAll(root1);

  }

  /**
   * Gives the data of the selected cell which is the price of the dish in this case
   */
  public void getSelectedCellPrice(ActionEvent actionEvent) {
    TablePosition pos = (TablePosition) orderTableView.getSelectionModel().getSelectedCells()
        .get(3);
    int row = pos.getRow();

// Item here is the table view type:
    Object item = orderTableView.getItems().get(row);

    TableColumn col = pos.getTableColumn();

// this gives the value in the selected cell:
    String data = (String) col.getCellObservableValue(item).getValue();
    System.out.println(data);
  }

  /**
   * Prints the bill for the table.
   */
  public void printTableBill(ActionEvent actionEvent) {
  }

  public void setOrderTable(ArrayList<Dish> orderedDishes) {
    ObservableList<Dish> dishes = FXCollections.observableArrayList();
    dishes.addAll(orderedDishes);
    this.idColumn.getTableView().setItems(dishes);
  }

  public void rowSelectedId(javafx.scene.input.MouseEvent mouseEvent) {
    this.menuSelectedDish = (Dish) menuTableView.getSelectionModel().getSelectedItem();
    menuSelectedDishId = menuSelectedDish.getId();
    menuSelectedDishName = menuSelectedDish.getName();
    menuSelectedDishCustomerNum = menuSelectedDish.getCustomerNum();
  }

  /**
   * This method assigns a specific comment given by a customer to the selected dish
   * @param comment the comment of the customer
   */
  public void addCommentToDish(String comment){
      Dish dish = (Dish) orderTableView.getSelectionModel().getSelectedItem();
      dish.setComment(comment);
  }

  public void openCommentDialog(){
    Dialog dialog = new TextInputDialog();
    dialog.setTitle("Comment Dialog");
    dialog.setHeaderText(
            "Enter your comment for the dish"
                    + System.lineSeparator()
                    + "Example: Make the falafel crispy on the outside");
    Optional<String> result = dialog.showAndWait();
    String entered = "";

    while (result.isPresent() && ((result.get()).equals("") || !validCommentEntry(result.get()))) {
      result = dialog.showAndWait();
    }
    if (result.isPresent()) {
      entered = result.get();
      addCommentToDish(entered);
      System.out.println(entered);
    }
  }

  private boolean validCommentEntry(String comment){
    try{
      return comment instanceof String;
    }
    catch (Exception e){
      return false;
    }
  }

  /**
   * Removes dishes from a specific customer's order
   */
  public void removeDishFromOrder(){
    MenuItem dish = (MenuItem) orderTableView.getSelectionModel().getSelectedItem();
    table.removeDish((Dish) dish);
    updateScreen();
  }

  /**
   * Adds dishes to a specific customer's order
   */
  public void addDishToOrder() {
    // TODO: Create a way to give customer number
    // TODO: Add Compliments Somehow
    try{
      MenuItem dish = (MenuItem) menuTableView.getSelectionModel().getSelectedItem();
      String customer =  (String) customerNumberDropDown.getValue();
      int customerNumber = (int) customer.charAt(9);
      System.out.println(customer);
      System.out.println(customerNumber);
      server.addOrder(getTable(), customerNumber-48, dish);
      updateScreen();
    }
    catch(NullPointerException e){
      System.out.println("Choose a customer from the drop down menu");
    }

  }

  /**
   * Adds the number of customers at the table to the drop down menu so each customer can have his/her own order.
   * @param table the table at which the customers are seated
   */
  public void addOptionsToComboBox(Table table){
      this.table = table;
      ArrayList<String> customerLabels = new ArrayList<>();
      int tableSize = setTableOccupied();
      table.setOccupied(tableSize);
      for(int k = 1; k <= table.getTableSize(); k++){
          customerLabels.add("Customer " + k);
      }
      ObservableList<String> labels = FXCollections.observableArrayList();
      labels.addAll(customerLabels);
      customerNumberDropDown.setItems(labels);
  }

  /**
   * Checks if the number of customers at a table entered is a valid input value
   * @param customerInput the number of customers that have entered
   * @return returns true if it is a valid input else false
   */
  private boolean validCustomerEntry(String customerInput) {
    try {
      int numCustomers = Integer.parseInt(customerInput);
      return numCustomers > 1 && numCustomers < 50;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  public int setTableOccupied(){
    Dialog dialog = new TextInputDialog();
    dialog.setTitle("Table Dialog");
    dialog.setHeaderText(
            "Enter the number of people"
                    + System.lineSeparator()
                    + "Format: a whole number between 1 and 50"
                    + System.lineSeparator()
                    + "Example: 8");
    Optional<String> result = dialog.showAndWait();
    String entered = "";

    while (result.isPresent() && ((result.get()).equals("") || !validCustomerEntry(result.get()))) {
      result = dialog.showAndWait();
    }
    if (result.isPresent()) {
       entered = result.get();
       table.setOccupied(Integer.parseInt(entered));
       System.out.println(entered);
    }
    int numberOfPeople = Integer.parseInt(entered);
    return numberOfPeople;
  }



  public void backButtonAction() {
    try {

      System.out.println("Loading Serving Table.....");
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
          "../TablesScreen/TablesScreen.fxml"));
      Parent root1 = fxmlLoader.load();
      TablesScreen controller = fxmlLoader.getController();
      controller.setServer(getServer());
      controller.setRestaurant(getRestaurant());
      controller.updateScreen();
      vBox.getChildren().setAll(root1);
    } catch (IOException e) {
    }

  }

  /**
   *
   * Opens the compliment menu with only the ingredients that are allowed to be added or removed to the selected dish
   */
  public void openComplimentMenu(){
      try{
          Dish dish = getOrderTableView().getSelectionModel().getSelectedItem();
          if (dish != null) {

              FXMLLoader loader = new FXMLLoader(getClass().getResource("../ComplementScreen/complements.fxml"));
              Parent root = loader.load();
              vBox.getChildren().setAll(root);

          }

      } catch (IOException e){
          e.printStackTrace();
      } catch (NullPointerException e){
          System.out.println("Choose a dish to add compliments");
      }
  }





  public void updateScreen() {
    tableOrderTitle.setText("Table" + table.getTableID() + " Order");

    //TODO: Disable or not show certain MenuItems that have not enough ingredients
    menuTableView.setItems(getMenuItem());
    orderTableView.setItems(getOrderDish());
  }

  @Override
  public void openNotification(String message) {
    notification.pushNotification(message);

  }

  public ObservableList<MenuItem> getMenuItem() {
    ObservableList<MenuItem> menu = FXCollections.observableArrayList();
    menu.addAll(restaurant.getMenu().getMenuItems());
    return menu;
  }

  public ObservableList<Dish> getOrderDish() {
    ObservableList<Dish> orderedDishes = FXCollections.observableArrayList();
    orderedDishes.addAll(table.getTableOrder());
    return orderedDishes;
  }

  public void sendOrder(){
    server.passOrder(table, restaurant.getServingTable());
  }

  //-----------------------GETTERS AND SETTERS BELOW

  public Server getServer() {
    return server;
  }

  public void setServer(Server server) {
    this.server = server;
    server.setScreen(this);
    System.out.println("New Screen has been changed");
  }

  public Table getTable() {
    return table;
  }

  public void setTable(Table table) {
    this.table = table;
  }

  public Restaurant getRestaurant() {
    return restaurant;
  }

  public void setRestaurant(Restaurant restaurant) {
    this.restaurant = restaurant;
  }

  public ArrayList<Dish> getDishes() {
    return dishes;
  }

  public void setDishes(ArrayList<Dish> dishes) {
    this.dishes = dishes;
    getOrderTableView().setItems(getOrderDish());
  }


  public TableView<Dish> getOrderTableView() {
    return orderTableView;
  }

  public TableView<Dish> getMenuTableView() {
    return menuTableView;
  }

  @Override
  public void handle(ActionEvent event) {

  }

  public TableColumn getMenuIdColumn() {
    return menuIdColumn;
  }

  public void setMenuIdColumn(TableColumn menuIdColumn) {
    this.menuIdColumn = menuIdColumn;
  }

  public TableColumn getMenuDishColumn() {
    return menuDishColumn;
  }

  public void setMenuDishColumn(TableColumn menuDishColumn) {
    this.menuDishColumn = menuDishColumn;
  }

  public TableColumn getMenuPriceColumn() {
    return menuPriceColumn;
  }

  public void setMenuPriceColumn(TableColumn menuPriceColumn) {
    this.menuPriceColumn = menuPriceColumn;
  }

  public TableColumn getMenuIngredientsColumn() {
    return menuIngredientsColumn;
  }

  public void setMenuIngredientsColumn(TableColumn menuIngredientsColumn) {
    this.menuIngredientsColumn = menuIngredientsColumn;
  }

  public TableColumn getIdColumn() {
    return idColumn;
  }

  public void setIdColumn(TableColumn idColumn) {
    this.idColumn = idColumn;
  }

  public TableColumn getNameColumn() {
    return nameColumn;
  }

  public void setNameColumn(TableColumn nameColumn) {
    this.nameColumn = nameColumn;
  }

  public Dish getMenuSelectedDish() {
    return menuSelectedDish;
  }

  public void setMenuSelectedDish(Dish menuSelectedDish) {
    this.menuSelectedDish = menuSelectedDish;
  }

  private Dish menuSelectedDish;

  public double getMenuSelectedDishId() {
    return menuSelectedDishId;
  }

  public void setMenuSelectedDishId(double menuSelectedDishId) {
    this.menuSelectedDishId = menuSelectedDishId;
  }

  public String getMenuSelectedDishName() {
    return menuSelectedDishName;
  }

  public void setMenuSelectedDishName(String menuSelectedDishName) {
    this.menuSelectedDishName = menuSelectedDishName;
  }

  public int getMenuSelectedDishCustomerNum() {
    return menuSelectedDishCustomerNum;
  }

  public void setMenuSelectedDishCustomerNum(int menuSelectedDishCustomerNum) {
    this.menuSelectedDishCustomerNum = menuSelectedDishCustomerNum;
  }

  public TableColumn getCustomerNumberColumn() {
    return customerNumberColumn;
  }

  public void setCustomerNumberColumn(TableColumn customerNumberColumn) {
    this.customerNumberColumn = customerNumberColumn;
  }

  public void rowSelectedCheckIngredients(MouseEvent mouseEvent) {
      MenuItem dish = (MenuItem) menuTableView.getSelectionModel().getSelectedItem();
    try {
        if(!restaurant.getInventory().hasEnoughIngredients(dish.getIngredientAmounts())){
            addDish.setDisable(true);
        }
        else{
            addDish.setDisable(false);
        }
    }
    catch(NullPointerException e){
        System.out.println("No row selected");
    }
  }
}


package Restaurant;

import MenuDishes.Dish;
import java.util.ArrayList;

/** The table is where the customers sit and order food and also where their cooked food arrives. */
public class Table {

  private String tableID; // the id number of the table
  private int tableSize; //Number of people sitting at the table
  private boolean isOccupied; // if the table already has customers
  private ArrayList<Dish> tableOrder; // the dishes the table ordered


  /**
   * Creates a new table.
   *
   * @param tableID table identifier
   */
  public Table(String tableID) {
    this.tableID = tableID;
    this.isOccupied = false;
    this.tableOrder = new ArrayList<Dish>();
  }

  /**
   * When a server seats customers to a table, the server must specify how many people are eat at
   * that table. Table then becomes occupied.
   * @param size
   */
  public void setOccupied(int size) {
    this.isOccupied = true;
    this.tableSize = size;
  }

  /**
   * Customers have cleared their bills. TableOrder has been erased and the table is no longer
   * occupied.
   */
  public void clearTable() {
    this.tableOrder = new ArrayList<>();
    this.isOccupied = false;
    this.tableSize = 0;
  }
  /**
   * Set the table order to this array of dishes.
   *
   * @param tableOrder set table order to this array of dishes
   */
  public void setTableOrder(ArrayList<Dish> tableOrder) {
    this.tableOrder = tableOrder;
  }

  /**
   * Adds a single dish to array of orders.
   *
   * @param dish
   */
  public void addSingleOrder(Dish dish) {
    this.tableOrder.add(dish);
  }

  /**
   * Returns the table order.
   *
   * @return the entire table order of dishes
   */
  public ArrayList<Dish> getTableOrder() {
    return tableOrder;
  }

  /**
   * Removes the served dish.
   *
   * @param dish to be removed from the list.
   */
  public void removeDish(Dish dish) {
    tableOrder.remove(dish);
  }

  /**
   * Removes the served dish and reduces the number of dishes served by 1.
   *
   * @param index to be removed from the list.
   */
  public Dish removeDish(int index) {
    return tableOrder.remove(index);
  }

  /** Returns the dishes ordered by a specific customerNumber on this table. */
  public ArrayList<Dish> getCustomerOrder(int customerNumber) {
    ArrayList<Dish> customerDishes = new ArrayList<>();
    for (Dish dish : tableOrder) {
      if (dish.getCustomerNum() == customerNumber) {
        customerDishes.add(dish);
      }
    }
    return customerDishes;
  }

  /**
   * get dish from table's order.
   *
   * @param index index of dish
   * @return dish at index
   */
  public Dish getDish(int index) {
    return getTableOrder().get(index);
  }

  /**
   * Get name id of table.
   *
   * @return string of table id
   */
  public String getTableID() {
    return this.tableID;
  }

  public int getTableSize() {
    return tableSize;
  }

  public void setTableSize(int tableSize) {
    this.tableSize = tableSize;
  }

  public boolean getIsOccupied() {
    return isOccupied;
  }
}

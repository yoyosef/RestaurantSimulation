import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

/*
 * A Manager manages the overall restaurant and gets the ingredients if there
 * is a shortage.
 */
public class Manager implements IWorker{
    private boolean isOccupied;
    private String name;
    private String requestIngredients = "request.txt";
    public Manager(String name, Restaurant restaurant){
        this.name = name;
    }
    /**
     * Calls a server or a cook to collect the received ingredients.
     */
    public IWorker callWorker(Restaurant restaurant){
        for(IWorker worker: restaurant.getWorkers()){
            if(!worker.isOccupied()){
                return worker;
            }
        }
        return null; // NEEDS TO BE CHECKED AGAIN.
    }
    /**
     * Reads the file about the new ingredients to be purchased and default value is 20.
     */
    public String SendMail(){
        try (BufferedReader fileReader = new BufferedReader(new FileReader(requestIngredients))) {
            String line = fileReader.readLine();
            if (line != null) {
                return line;
            }
            return null;
        }
        catch (Exception e) {
            return null;
        }
    }
    /**
     * Reads the file about the new ingredients to be purchased and requests the distributor the amount given.
     */
    public String SendMail(int amount){
        try (BufferedReader fileReader = new BufferedReader(new FileReader(requestIngredients))) {
            String line = fileReader.readLine();
            if (line != null) {
                return line + " " + amount;
            }
            return null;
        }
        catch (Exception e) {
            return null;
        }
    }
    public boolean isOccupied(){
        return isOccupied;
    }
    /**
     * The Manager can check the inventory.
     */
    public void checkInventory(Inventory inventory){}
}

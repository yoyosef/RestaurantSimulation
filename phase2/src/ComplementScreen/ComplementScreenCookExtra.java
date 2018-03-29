package ComplementScreen;

import Restaurant.Cook;
import Restaurant.Dish;
import Restaurant.DishIngredient;
import Restaurant.Restaurant;
import java.util.HashMap;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class ComplementScreenCookExtra extends ComplementScreenController {

  private Restaurant restaurant;

  public void addIfIngredientsEnough() {

    HashMap<String, DishIngredient> dishIngredientsCopy = dish.cloneIngredients();
    Cook cook = new Cook("jeff");
    HashMap<String, DishIngredient> differences = dish
        .getPosDifBetweenTwoIngredientsList(dishIngredientsCopy);
    Dish newIngredients = dish.clone();
    newIngredients.setIngredients(differences);
    if (cook.canBePrepared(newIngredients, restaurant.getInventory())) {
      for (String key : differences.keySet()) {
        restaurant.getInventory().removeStock(key, differences.get(key).getAmount());
      }
    } else {
      Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
          "Sorry, there is not enough ingredients in stock right now to fulfill this order.",
          ButtonType.OK);
      alert.showAndWait();
      dish.setIngredients(this.getIngredientsCopy());
    }
  }


  @Override
  public void acceptEvent() {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you are finished?",
        ButtonType.YES, ButtonType.CANCEL);
    alert.showAndWait();
    if (alert.getResult() == ButtonType.YES) {
      this.addIfIngredientsEnough();
      this.closeWindow(accept);
    }
  }
}
package dao;

import entity.FoodItem;
import java.util.List;

public interface FoodDAOInterface {

    List<FoodItem> getFoodItems() throws Exception;

    void addFoodItem(FoodItem theFoodItem) throws Exception;

    FoodItem getFoodItem(String theFoodItemId) throws Exception;

    void updateFoodItem(FoodItem theFoodItem) throws Exception;

    void deleteFoodItem(String theFoodItemId) throws Exception;
}















package dao;

import entity.FoodItem;

import java.util.ArrayList;
import java.util.List;

public class FoodListDAO implements FoodDAOInterface {
    private static List<FoodItem> foodItems;
    private static FoodListDAO instance;

    private FoodListDAO() {
        foodItems = new ArrayList<>();
        foodItems.add(new FoodItem(1, "Burger", 1, 20, "https://burgersushipoint.com.ua/wp-content/uploads/2021/02/burger-sushi-point_meny_17-2-500x500.jpg", 1));
        foodItems.add(new FoodItem(2, "Cheese Burger", 1, 15, "https://simply-delicious-food.com/wp-content/uploads/2015/07/Bacon-and-cheese-burgers-3-500x500.jpg", 5));
        foodItems.add(new FoodItem(3, "Potato free", 1, 30, "https://i.pinimg.com/736x/31/7f/f6/317ff60cbdfe3d07e29062f278c529c0.jpg", 2));
    }

    public static FoodListDAO getInstance() {
        if (instance == null) {
            return instance = new FoodListDAO();
        } else return instance;
    }

    @Override
    public List<FoodItem> getFoodItems() throws Exception {
        return foodItems;

    }

    @Override
    public void addFoodItem(FoodItem theFoodItem) throws Exception {
        foodItems.add(theFoodItem);
    }

    @Override
    public FoodItem getFoodItem(String theFoodItemId) throws Exception {
        long studentId = Integer.parseInt(theFoodItemId);
        for (FoodItem foodItem : foodItems) {
            if (foodItem.getId() == studentId) {
                return foodItem;
            }
        }
        return null;
    }

    @Override
    public void updateFoodItem(FoodItem theFoodItem) throws Exception {
        for (FoodItem foodItem : foodItems) {
            if (foodItem.getId() == theFoodItem.getId()) {
                foodItem = theFoodItem;
            }
        }

    }

    @Override
    public void deleteFoodItem(String theFoodItemId) throws Exception {
        int id = Integer.parseInt(theFoodItemId);
        FoodItem foodItemToDelete = null;
        for (FoodItem foodItem : foodItems) {
            if (foodItem.getId() == id) {
                foodItemToDelete = foodItem;
            }
        }
        if (foodItemToDelete != null) {
            foodItems.remove(foodItemToDelete);
        }
    }
}
















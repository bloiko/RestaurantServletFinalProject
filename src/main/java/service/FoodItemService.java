package service;

import dao.FoodJDBCDAO;
import entity.Category;
import entity.FoodItem;
import entity.Item;
import exception.CannotFetchItemsException;
import exception.DBException;

import java.util.List;

public class FoodItemService {
    private FoodJDBCDAO foodItemDAO;

    public FoodItemService() throws DBException {
        foodItemDAO = FoodJDBCDAO.getInstance();
    }

    public List<Item> addFoodItemToCart(List<Item> cart, String foodId) throws DBException {
        int index = isExisting(foodId, cart);
        if (cart.isEmpty() || index == -1) {
            cart.add(new Item(1, foodItemDAO.getFoodItem(foodId), 1));
        } else {
            int quantity = cart.get(index).getQuantity() + 1;
            cart.get(index).setQuantity(quantity);
        }
        return cart;
    }

    private int isExisting(String id, List<Item> cart) {
        int foodId = Integer.parseInt(id);
        for (int i = 0; i < cart.size(); i++) {
            if (cart.get(i).getFoodItem().getId() == foodId) {
                return i;
            }
        }
        return -1;
    }

    public List<Category> getCategories() throws DBException {
        return foodItemDAO.getCategories();
    }
    public List<FoodItem> getFoodItems() throws CannotFetchItemsException {
        try {
            return foodItemDAO.getFoodItems();
        } catch (DBException e) {
            throw new CannotFetchItemsException("Items fetch failed");
        }
    }
}

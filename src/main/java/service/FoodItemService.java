package service;

import dao.FoodDAO;
import entity.Category;
import entity.FoodItem;
import entity.Item;
import exception.CannotFetchItemsException;
import exception.DBException;

import java.util.List;

public class FoodItemService {
    private FoodDAO foodItemDAO;

    public FoodItemService() throws DBException {
        foodItemDAO = FoodDAO.getInstance();
    }

    public FoodItemService(FoodDAO foodItemDAO) {
        this.foodItemDAO = foodItemDAO;
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
    public int isExisting(String id, List<Item> cart) {
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
    public List<FoodItem> getFoodItems(int page, int number, String sortBy, String order, String filter) throws DBException {
        int offset = (page - 1) * number;
        int itemsLimit = number;
        if (sortBy == null && filter != null) {
            return foodItemDAO.getFoodItemsWithSkipLimitFilter(itemsLimit, offset, filter);
        } else if (sortBy == null && filter == null) {
            return foodItemDAO.getFoodItemsWithSkipAndLimit(itemsLimit, offset);
        } else if (filter == null && sortBy != null) {
            return foodItemDAO.getFoodItemsWithSkipLimitAndOrder(itemsLimit, offset, sortBy, order);
        } else {
            return foodItemDAO.getFoodItemsWithFilterSkipLimitAndOrder(filter, itemsLimit, offset, sortBy, order);
        }
    }
}

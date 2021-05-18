package service;

import entity.Item;

import java.util.List;

public class CartService {
    public List<Item> removeItemFromCart(List<Item> cart, String itemId) {
        int index = isExisting(Integer.parseInt(itemId), cart);
        cart.remove(index);
        return cart;
    }

    private int isExisting(int id, List<Item> cart) {
        for (int i = 0; i < cart.size(); i++) {
            if (cart.get(i).getFoodItem().getId() == id) {
                return i;
            }
        }
        return -1;
    }
}

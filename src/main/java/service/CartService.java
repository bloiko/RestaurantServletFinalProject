package service;

import entity.Item;

import java.util.List;

public class CartService {
    public List<Item> removeItemFromCart(List<Item> cart, String itemId) {
        int index = isExisting(Integer.parseInt(itemId), cart);
        if (index != -1) {
            cart.remove(index);
        }
        return cart;
    }

    public int isExisting(int id, List<Item> cart) {
        for (int i = 0; i < cart.size(); i++) {
            if (cart.get(i).getFoodItem().getId() == id) {
                return i;
            }
        }
        return -1;
    }
}

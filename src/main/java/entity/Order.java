package entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@NoArgsConstructor
public class Order {
    private int id;
    private Timestamp orderDate;
    private User user;
    private List<Item> items;
    private OrderStatus status;
    public void addFoodItem(Item item){
        items.add(item);
    }
}

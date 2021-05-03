package entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class Order {
    private int id;
    private Date orderDate;
    private List<Item> items;
    private OrderStatus status;
    public void addFoodItem(Item item){
        items.add(item);
    }
}

package database.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
/**
 * Order entity.
 *
 * @author B.Loiko
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order implements Serializable {
    private int id;
    private Timestamp orderDate;
    private User user;
    private List<Item> items;
    private OrderStatus status;
    public void addFoodItem(Item item){
        items.add(item);
    }
}

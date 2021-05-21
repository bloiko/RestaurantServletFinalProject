package entity;

import lombok.*;
/**
 * Item entity.
 *
 * @author B.Loiko
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    private int id;
    private FoodItem foodItem;
    private int quantity;
}

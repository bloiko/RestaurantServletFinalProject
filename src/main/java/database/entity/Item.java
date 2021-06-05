package database.entity;

import lombok.*;

import java.io.Serializable;

/**
 * Item entity.
 *
 * @author B.Loiko
 *
 */
@Data
@AllArgsConstructor
public class Item implements Serializable {
    private int id;
    private FoodItem foodItem;
    private int quantity;
}

package entity;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    private int id;
    private FoodItem foodItem;
    private int quantity;
}

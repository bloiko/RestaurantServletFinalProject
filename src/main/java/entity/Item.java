package entity;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    private FoodItem foodItem;
    private int quantity;
}

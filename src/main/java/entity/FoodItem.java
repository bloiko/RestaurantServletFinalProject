package entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoodItem {
    private int id;
    private String name;
    private int quantity;
    private int price;
    private String image;
    private int itemCategory;
}

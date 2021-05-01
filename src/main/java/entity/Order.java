package entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class Order {
    private int id;
    private Date orderDate;
    private List<FoodItem> foodItems;
    public void addFoodItem(FoodItem foodItem){
        foodItems.add(foodItem);
    }
}

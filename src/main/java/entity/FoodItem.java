package entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * FoodItem entity.
 *
 * @author B.Loiko
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoodItem {
    private int id;
    private String name;
    private int price;
    private String image;
    private Category category;
}

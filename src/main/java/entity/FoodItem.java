package entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * FoodItem entity.
 *
 * @author B.Loiko
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoodItem implements Serializable {
    private int id;
    private String name;
    private int price;
    private String image;
    private Category category;
}

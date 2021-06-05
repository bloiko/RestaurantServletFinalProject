package database.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
/**
 * Category entity.
 *
 * @author B.Loiko
 *
 */
@Data
@AllArgsConstructor
public class Category implements Comparable<Category> {
    private int id;
    private String name;
    private String nameUa;

    @Override
    public int compareTo(Category o) {
        return this.name.compareTo(o.name);
    }
}

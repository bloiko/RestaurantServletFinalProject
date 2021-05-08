package entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category implements Comparable<Category> {
    private int id;
    private String name;

    @Override
    public int compareTo(Category o) {
        return this.name.compareTo(o.name);
    }
}

package org.wsh.datastructure;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class Skill implements Serializable {
    private int id;
    private String name;
    private double price;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Skill skill = (Skill) o;

        return id == skill.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}

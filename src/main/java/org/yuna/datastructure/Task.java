package org.wsh.datastructure;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
public class Task implements Serializable {
    private int id;
    // 坐标
    private Location l;
    // 半径
    private double r;
    // 额外预算
    private double b;
    // 技能要求
    private List<Skill> sList;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}

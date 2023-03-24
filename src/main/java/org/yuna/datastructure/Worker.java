package org.wsh.datastructure;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
public class Worker implements Serializable {
    private int id;
    // 坐标
    private Location l;
    // 技能
    private List<Skill> sList;
    

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Worker worker = (Worker) o;
        return id == worker.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

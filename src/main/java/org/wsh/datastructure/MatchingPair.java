package org.wsh.datastructure;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.wsh.datastructure.Task;
import org.wsh.datastructure.Worker;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class MatchingPair implements Serializable {
    private Task task;
    private List<Worker> workers;
    private double vwsPrice;

    public Map<Integer, Integer> getSkillTW() {
        return skillTW;
    }

    public void setSkillTW(Map<Integer, Integer> skillTW) {
        this.skillTW = skillTW;
    }

    private Map<Integer,Integer> skillTW = new HashMap<>();

    public void record(Integer sid, Integer wid){
        skillTW.put(sid,wid);
    }

    public MatchingPair(Task task, List<Worker> workers) {
        this.task = task;
        this.workers = workers;
    }
}

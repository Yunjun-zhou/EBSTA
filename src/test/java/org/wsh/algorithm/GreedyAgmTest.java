package org.wsh.algorithm;

import org.junit.jupiter.api.Test;
import org.wsh.datastructure.MatchingPair;
import org.wsh.datastructure.Task;
import org.wsh.datastructure.Worker;
import org.wsh.utils.DatasetUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GreedyAgmTest {

    @Test
    void calculate() {
        Map<Integer, Task> integerTaskMap = null;
        try {
            integerTaskMap = DatasetUtils.taskMap();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<Integer, Worker> integerWorkerMap = null;
        try {
            integerWorkerMap = DatasetUtils.workerMap();
        } catch (IOException e) {
            e.printStackTrace();
        }
        GreedyAgm greedyAgm = new GreedyAgm();
        List<MatchingPair> results = greedyAgm.calculate(new ArrayList<>(integerTaskMap.values()), new ArrayList<>(integerWorkerMap.values()));
        System.out.println(results.size());
        System.out.println(results.stream().mapToDouble(MatchingPair::getVwsPrice).sum());
        results.forEach(System.out::println);
    }
}
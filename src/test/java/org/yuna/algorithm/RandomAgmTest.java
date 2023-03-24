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

class RandomAgmTest {

    @Test
    void calculate() {
        try {
            Map<Integer, Task> integerTaskMap = DatasetUtils.taskMap();
            Map<Integer, Worker> integerWorkerMap = DatasetUtils.workerMap();
            RandomAgm randomAgm = new RandomAgm();
            List<MatchingPair> results = randomAgm.calculate(new ArrayList<>(integerTaskMap.values()), new ArrayList<>(integerWorkerMap.values()));
            System.out.println(results.size());
            System.out.println(results.stream().mapToDouble(MatchingPair::getVwsPrice).sum());
            results.forEach(System.out::println);

//            System.out.println(results);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
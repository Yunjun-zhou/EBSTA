package org.wsh.algorithm;

import org.junit.jupiter.api.Test;
import org.wsh.datastructure.MatchingPair;
import org.wsh.datastructure.Task;
import org.wsh.datastructure.Worker;
import org.wsh.utils.DatasetUtils;
import org.wsh.utils.PlatformRevenueUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GameAgmTest {

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
        GameAgm gameAgm = new GameAgm();
        List<MatchingPair> results = gameAgm.calculate(new ArrayList<>(integerTaskMap.values()), new ArrayList<>(integerWorkerMap.values()));
        System.out.println(results.size());
        System.out.println(PlatformRevenueUtils.getVwsPriceSum(results));
        results.forEach(System.out::println);
    }
}
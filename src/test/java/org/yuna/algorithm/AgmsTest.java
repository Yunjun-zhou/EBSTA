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

public class AgmsTest {
    @Test
    void randomcalculate() {
        try {
            System.out.println("==============================random============================");
            Map<Integer, Task> integerTaskMap = DatasetUtils.taskMap();
            Map<Integer, Worker> integerWorkerMap = DatasetUtils.workerMap();
            RandomAgm randomAgm = new RandomAgm();
            List<MatchingPair> results = randomAgm.calculate(new ArrayList<>(integerTaskMap.values()), new ArrayList<>(integerWorkerMap.values()));
            System.out.println("匹配数："+results.size());
            System.out.println("平台收益："+results.stream().mapToDouble(MatchingPair::getVwsPrice).sum());
//            results.forEach(System.out::println);
//            System.out.println(results);
            System.out.println("=================================================================");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void greedycalculate() {
        System.out.println("===========================greedy==============================");
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
        System.out.println("匹配数："+results.size());
        System.out.println("平台收益："+results.stream().mapToDouble(MatchingPair::getVwsPrice).sum());
//        results.forEach(System.out::println);
        System.out.println("=================================================================");
    }

    @Test
    void gamecalculate() {
        System.out.println("=============================game==============================");
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
        System.out.println("匹配数："+results.size());
        System.out.println("平台收益："+PlatformRevenueUtils.getVwsPriceSum(results));
//        results.forEach(System.out::println);
        System.out.println("=================================================================");
    }
}

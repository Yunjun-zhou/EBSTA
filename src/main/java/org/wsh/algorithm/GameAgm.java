package org.wsh.algorithm;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.RandomUtil;
import org.apache.commons.lang3.SerializationUtils;
import org.wsh.datastructure.MatchingPair;
import org.wsh.datastructure.Skill;
import org.wsh.datastructure.Task;
import org.wsh.datastructure.Worker;
import org.wsh.utils.DistanceUtils;
import org.wsh.utils.ExtraCostUtils;
import org.wsh.utils.PlatformRevenueUtils;

import java.util.*;
import java.util.stream.Collectors;

public class GameAgm {

    public List<MatchingPair> calculate(List<Task> tasks, List<Worker> workers) {
        List<Task> tasks_p = new ArrayList<>();
        List<Worker> workers_p = new ArrayList<>();
        tasks.forEach(task -> tasks_p.add(SerializationUtils.clone(task)));
        workers.forEach(worker -> workers_p.add(SerializationUtils.clone(worker)));
        List<MatchingPair> matchingPairs = randomA(tasks_p, workers_p);

        System.out.println("----random----");
        System.out.println(matchingPairs.size());
        System.out.println(PlatformRevenueUtils.getVwsPriceSum(matchingPairs));
        System.out.println("----random----");

        List<MatchingPair> matchingPairs_p = new ArrayList<>();
        int epoll = 0;
        do {
            matchingPairs.forEach(m -> matchingPairs_p.add(SerializationUtils.clone(m)));
            workers.forEach(worker -> {
                Task task_star = getPriceIncrease(tasks, worker, matchingPairs);
                Task task_star_p = SerializationUtils.clone(task_star);
                if (task_star != null) {
                    if (!matchingPairs.stream().map(MatchingPair::getTask).collect(Collectors.toList()).contains(task_star)) {
                        List<Worker> vws = new ArrayList<>();
                        extracted(workers_p, matchingPairs, worker, task_star_p, vws);
                    } else {
                        MatchingPair matchingPair = matchingPairs.stream().filter(m -> m.getTask().getId() == task_star.getId()).findFirst().get();
                        workers_p.addAll(matchingPair.getWorkers());
                        matchingPairs.remove(matchingPair);
                        List<Worker> vws = new ArrayList<>();
                        extracted(workers_p, matchingPairs, worker, task_star_p, vws);
                    }
                }
            });
            System.out.println(epoll++);
            System.out.println(PlatformRevenueUtils.getVwsPriceSum(matchingPairs) - PlatformRevenueUtils.getVwsPriceSum(matchingPairs_p));
        } while (PlatformRevenueUtils.getVwsPriceSum(matchingPairs) - PlatformRevenueUtils.getVwsPriceSum(matchingPairs_p) < 0.00001);
        return matchingPairs;
    }

    private void extracted(List<Worker> workers_p, List<MatchingPair> matchingPairs, Worker worker, Task task_star_p, List<Worker> vws) {
        vws.add(worker);
        task_star_p.getSList().removeAll(worker.getSList().stream().filter(s -> task_star_p.getSList().contains(s)).collect(Collectors.toList()));
        task_star_p.setB(task_star_p.getB() - ExtraCostUtils.getExtraCost(task_star_p, worker));
        MatchingPair matchingPair = new MatchingPair(task_star_p, null);
        while (!task_star_p.getSList().isEmpty()) {
            double Rt = task_star_p.getR() + task_star_p.getB();
            List<Worker> W_star = workers_p.stream().filter(w -> task_star_p.getSList().stream().anyMatch(s -> w.getSList().contains(s)) && DistanceUtils.getDistance(task_star_p.getL(), w.getL()) < Rt).collect(Collectors.toList());
            if (CollectionUtil.isEmpty(W_star)) {
                workers_p.addAll(vws);
                vws = new ArrayList<>();
                break;
            }
            Worker w = W_star.stream().max(Comparator.comparingDouble(cw -> PlatformRevenueUtils.getWorkerMaxRevenue(task_star_p, cw))).get();
            vws.add(w);
            task_star_p.setB(task_star_p.getB() - ExtraCostUtils.getExtraCost(task_star_p, w));
            List<Skill> skillList = w.getSList().stream().filter(s -> task_star_p.getSList().contains(s)).collect(Collectors.toList());
            skillList.stream().forEach(s -> matchingPair.record(s.getId(), w.getId()));
            task_star_p.getSList().removeAll(skillList);
            workers_p.remove(w);
        }
        if (!vws.isEmpty()) {
            matchingPair.setWorkers(vws);
            matchingPair.setVwsPrice(PlatformRevenueUtils.getPlatformRevenue(matchingPair));
            matchingPairs.add(matchingPair);
        }
    }

    public static Task getPriceIncrease(List<Task> tasks, Worker worker, List<MatchingPair> M) {
        return tasks.stream().max(Comparator.comparingDouble(task -> {
            MatchingPair m = M.stream().filter(t -> task.getId() == t.getTask().getId()).findFirst().orElse(null);
            return PlatformRevenueUtils.getWorkerMaxRevenue(task, worker) - (m != null ? PlatformRevenueUtils.getWorkerRealRevenue(m, worker) : 0d);
        })).orElse(null);
    }

    private List<MatchingPair> randomA(List<Task> tasks, List<Worker> workers) {
        List<MatchingPair> m = new ArrayList<>();
        tasks.forEach(t -> {
            List<Worker> vws = new ArrayList<>();
            MatchingPair matchingPair = new MatchingPair(t, null);
            double p = 0;
            for (Skill s : t.getSList()) {
                double Rt = t.getR() + t.getB();
                List<Worker> W_star = workers.stream().filter(w -> w.getSList().contains(s) && DistanceUtils.getDistance(t.getL(), w.getL()) < Rt).collect(Collectors.toList());
                if (CollectionUtil.isEmpty(W_star)) {
                    workers.addAll(vws);
                    vws = new ArrayList<>();
                    break;
                }
                Worker w = W_star.stream().max(Comparator.comparingDouble(cw -> PlatformRevenueUtils.getPlatformRevenue(t, cw, s))).get();
                vws.add(w);
                matchingPair.record(s.getId(), w.getId());
                t.setB(t.getB() - ExtraCostUtils.getExtraCost(t, w));
                workers.remove(w);
            }
            if (!vws.isEmpty()) {
                matchingPair.setWorkers(vws);
                matchingPair.setVwsPrice(PlatformRevenueUtils.getPlatformRevenue(matchingPair));
                m.add(matchingPair);
            }
        });
        return m;
    }
}

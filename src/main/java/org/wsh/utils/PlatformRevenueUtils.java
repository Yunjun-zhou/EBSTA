package org.wsh.utils;

import org.wsh.datastructure.MatchingPair;
import org.wsh.datastructure.Skill;
import org.wsh.datastructure.Task;
import org.wsh.datastructure.Worker;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class PlatformRevenueUtils {
    private final static double alpha = 0.8;
    private final static double beta = 0.0003;

    public static double getPlatformRevenue(Task t, Worker w, Skill s) {
        return alpha * s.getPrice() - (DistanceUtils.getDistance(t.getL(), w.getL()) < t.getR() ? 0d : beta * ExtraCostUtils.getExtraCost(t, w));
    }

    public static double getPlatformRevenue(Skill s) {
        //return alpha * s.getPrice() - (DistanceUtils.getDistance(t.getL(), w.getL()) < t.getR() ? 0d : beta * ExtraCostUtils.getExtraCost(t, w));
        return alpha * s.getPrice();
    }

    public static double getPlatformRevenue(Task t, Worker w, List<Skill> skillList) {
        double skillrevenue=0;
        for(Skill s: skillList){
            skillrevenue+=s.getPrice();
        }
        return alpha * skillrevenue - (DistanceUtils.getDistance(t.getL(), w.getL()) < t.getR() ? 0d : beta * ExtraCostUtils.getExtraCost(t, w));
    }


    public static double getPlatformRevenue(MatchingPair matchingPair){
        AtomicReference<Double> temp = new AtomicReference<>(0d);
        Map<Integer, Integer> skillTW = matchingPair.getSkillTW();
        double extracostrevenue = 0;
        try {
            for(Worker w: matchingPair.getWorkers()){
                extracostrevenue += DistanceUtils.getDistance(matchingPair.getTask().getL(), w.getL()) < matchingPair.getTask().getR() ? 0d : beta * ExtraCostUtils.getExtraCost(matchingPair.getTask(), w);
            }
            Map<Integer, Skill> skillMap = DatasetUtils.skillMap();
            skillTW.forEach((k,v)-> temp.updateAndGet(v1 -> v1 + getPlatformRevenue(skillMap.get(k))));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return temp.get() - extracostrevenue;
    }

    public static double getWorkerMaxRevenue(Task t, Worker w) {
        List<Skill> skills = t.getSList().stream().filter(s -> w.getSList().contains(s)).collect(Collectors.toList());
        double skillrevenue=0;
        for(Skill s: skills){
            skillrevenue += alpha * s.getPrice();
        }
        double extracostrevenue = DistanceUtils.getDistance(t.getL(), w.getL()) < t.getR() ? 0d : beta * ExtraCostUtils.getExtraCost(t, w);
        return skillrevenue - extracostrevenue;
    }

    public static double getWorkerRealRevenue(MatchingPair m, Worker w) {
        AtomicReference<Double> temp = new AtomicReference<>((double) 0);
        m.getSkillTW().forEach((k, v) -> {
            if (v.equals(w.getId())) {
                temp.updateAndGet(v1 -> v1 + getPlatformRevenue(w.getSList().stream().filter(s -> s.getId() == k).findFirst().get()));
                //System.out.println(w.getSList().stream().filter(s -> s.getId() == k).findFirst().get());
            }
        });
        double extracostrevenue = DistanceUtils.getDistance(m.getTask().getL(), w.getL()) < m.getTask().getR() ? 0d : beta * ExtraCostUtils.getExtraCost(m.getTask(), w);
        return temp.get() - extracostrevenue;
    }

    public static double getVwsPriceSum(List<MatchingPair> list){
        return list.stream().mapToDouble(MatchingPair::getVwsPrice).sum();
    }

}

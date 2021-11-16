package org.wsh.algorithm;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.RandomUtil;
import org.wsh.datastructure.MatchingPair;
import org.wsh.datastructure.Skill;
import org.wsh.datastructure.Task;
import org.wsh.datastructure.Worker;
import org.wsh.utils.DistanceUtils;
import org.wsh.utils.ExtraCostUtils;
import org.wsh.utils.PlatformRevenueUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class GreedyAgm {
    public List<MatchingPair> calculate(List<Task> tasks, List<Worker> workers) {
        tasks.sort(Comparator.comparingDouble(t -> t.getSList().stream().mapToDouble(Skill::getPrice).sum() / t.getSList().size()));
        List<MatchingPair> m = new ArrayList<>();
        tasks.forEach(t -> {
            List<Worker> vws = new ArrayList<>();
            double p = 0;
            while (!t.getSList().isEmpty()) {
                double Rt = t.getR() + t.getB();
                List<Worker> W_star = workers.stream().filter(w -> t.getSList().stream().anyMatch(s -> w.getSList().contains(s)) && DistanceUtils.getDistance(t.getL(), w.getL()) < Rt).collect(Collectors.toList());
                if (CollectionUtil.isEmpty(W_star)) {
                    workers.addAll(vws);
                    vws = new ArrayList<>();
                    break;
                }
                Worker w = W_star.stream().max(Comparator.comparingLong(cw -> cw.getSList().stream().filter(s -> t.getSList().contains(s)).count())).get();
                vws.add(w);
                p += w.getSList().stream().filter(s -> t.getSList().contains(s)).mapToDouble(s->PlatformRevenueUtils.getPlatformRevenue(t,w,s)).sum();
                t.setB(t.getB() - ExtraCostUtils.getExtraCost(t, w));
                t.getSList().removeAll(w.getSList().stream().filter(s->t.getSList().contains(s)).collect(Collectors.toList()));
                workers.remove(w);
            }
            if (!vws.isEmpty()) {
                MatchingPair matchingPair = new MatchingPair(t, vws);
                matchingPair.setVwsPrice(p);
                m.add(matchingPair);
            }
        });
        return m;
    }
}

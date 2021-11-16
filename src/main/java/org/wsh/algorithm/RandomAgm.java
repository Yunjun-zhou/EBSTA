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
import java.util.List;
import java.util.stream.Collectors;

public class RandomAgm {
    public List<MatchingPair> calculate(List<Task> tasks, List<Worker> workers) {
        List<MatchingPair> m = new ArrayList<>();
        tasks.forEach(t -> {
            List<Worker> vws = new ArrayList<>();
            double p = 0;
            for (Skill s : t.getSList()) {
                double Rt = t.getR() + t.getB();
                List<Worker> W_star = workers.stream().filter(w -> w.getSList().contains(s) && DistanceUtils.getDistance(t.getL(), w.getL()) < Rt).collect(Collectors.toList());
                if (CollectionUtil.isEmpty(W_star)) {
                    workers.addAll(vws);
                    vws = new ArrayList<>();
                    break;
                }
                Worker w = W_star.get(RandomUtil.randomInt(0, W_star.size()));
                vws.add(w);
                p += PlatformRevenueUtils.getPlatformRevenue(t, w, s);
                t.setB(t.getB() - ExtraCostUtils.getExtraCost(t, w));
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

package org.wsh.utils;

import org.wsh.datastructure.Task;
import org.wsh.datastructure.Worker;

public class ExtraCostUtils {
    public static double getExtraCost(Task t, Worker w) {
        return DistanceUtils.getDistance(t.getL(), w.getL()) - t.getR();
    }
}

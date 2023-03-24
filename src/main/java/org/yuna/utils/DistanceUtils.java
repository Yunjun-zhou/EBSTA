package org.wsh.utils;

import org.wsh.datastructure.Location;

public class DistanceUtils {

    private static double EARTH_RADIUS = 6371.009;

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * 通过经纬度获取距离(单位：米)
     * @param l1
     * @param l2
     * @return
     */
    public static double getDistance(Location l1,
                                     Location l2) {
        double radLat1 = rad(l1.getLat());
        double radLat2 = rad(l2.getLat());
        double a = radLat1 - radLat2;
        double b = rad(l1.getLng()) - rad(l2.getLng());
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = s * 1000;
        return s;
    }
}

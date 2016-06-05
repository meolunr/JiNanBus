package com.leon.jinanbus.utils;

import android.location.Location;

/**
 * 位置工具类
 * <p/>
 * Modify from 2016.5.1 By Leon
 */
public class LocationUtils {
    private static final double pi = 3.14159265358979324;
    private static final double x_pi = pi * 3000.0 / 180.0;
    private static final double a = 6378245.0;
    private static final double ee = 0.00669342162296594323;

    /**
     * 计算两点之间的距离，单位（米）
     *
     * @param startLat 起点纬度
     * @param startLng 起点经度
     * @param endLat   终点纬度
     * @param endLng   终点纬度
     * @return 两点距离
     */
    public static float distanceBetween(double startLat, double startLng, double endLat, double endLng) {
        float[] result = new float[1];
        Location.distanceBetween(startLat, startLng, endLat, endLng, result);

        return result[0];
    }

    /**
     * 将世界标准坐标转换为百度坐标
     *
     * @param lat 纬度
     * @param lng 精度
     */
    public static double[] inter2Baidu(double lat, double lng) {
        double[] wgs2gcj = inter2China(lat, lng);
        double[] gcj2bd = china2Baidu(wgs2gcj[0], wgs2gcj[1]);
        return gcj2bd;
    }

    /**
     * 将中国国测局坐标转换为百度坐标
     *
     * @param lat 纬度
     * @param lng 精度
     */
    public static double[] china2Baidu(double lat, double lng) {
        double x = lng, y = lat;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * x_pi);
        double bd_lon = z * Math.cos(theta) + 0.0065;
        double bd_lat = z * Math.sin(theta) + 0.006;
        return new double[]{bd_lat, bd_lon};
    }

    /**
     * 将世界标准坐标转换为中国国测局坐标
     *
     * @param lat 纬度
     * @param lng 精度
     */
    public static double[] inter2China(double lat, double lng) {
        double dLat = transformLat(lng - 105.0, lat - 35.0);
        double dLon = transformLng(lng - 105.0, lat - 35.0);
        double radLat = lat / 180.0 * pi;
        double magic = Math.sin(radLat);
        magic = 1 - ee * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
        dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
        double mgLat = lat + dLat;
        double mgLon = lng + dLon;
        double[] loc = {mgLat, mgLon};
        return loc;
    }

    private static double transformLat(double lat, double lng) {
        double ret = -100.0 + 2.0 * lat + 3.0 * lng + 0.2 * lng * lng + 0.1 * lat * lng + 0.2 * Math.sqrt(Math.abs(lat));
        ret += (20.0 * Math.sin(6.0 * lat * pi) + 20.0 * Math.sin(2.0 * lat * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(lng * pi) + 40.0 * Math.sin(lng / 3.0 * pi)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(lng / 12.0 * pi) + 320 * Math.sin(lng * pi / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    private static double transformLng(double lat, double lng) {
        double ret = 300.0 + lat + 2.0 * lng + 0.1 * lat * lat + 0.1 * lat * lng + 0.1 * Math.sqrt(Math.abs(lat));
        ret += (20.0 * Math.sin(6.0 * lat * pi) + 20.0 * Math.sin(2.0 * lat * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(lat * pi) + 40.0 * Math.sin(lat / 3.0 * pi)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(lat / 12.0 * pi) + 300.0 * Math.sin(lat / 30.0 * pi)) * 2.0 / 3.0;
        return ret;
    }
}
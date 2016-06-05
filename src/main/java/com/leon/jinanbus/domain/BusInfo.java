package com.leon.jinanbus.domain;

import java.util.List;

public class BusInfo {
    public Status status;

    public List<Result> result;

    public static class Status {
        public int code;
    }

    public static class Result {
        public String busId;
        public double lng;
        public double lat;
        public double velocity;
        public String isArrvLft;
        public int stationSeqNum;
        public String buslineId;
        public String actTime;
        public String cardId;
        public String orgName;
        public boolean isArriveDest;
        public int dualSerialNum;

        public boolean isLuxury;
        public int arriveNum;
        public float stationRatio;
    }
}

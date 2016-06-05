package com.leon.jinanbus.domain;

import java.util.List;

public class BusLine {
    public Status status;
    public Result result;

    public static class Status {
        public int code;
    }

    public static class Result {
        public String id;
        public int area;
        public String localLineId;
        public String endStationName;
        public String lineName;
        public String linePoints;
        public String startStationName;
        public String state;
        public String stationList;
        public String ticketPrice;
        public String operationTime;
        public String owner;
        public String updateTime;
        public String descrip;

        public List<Stations> stations;

        public float[] stationDistances;

        public static class Stations {
            public String id;
            public int area;
            public double lat;
            public double lng;
            public String state;
            public String stationName;
            public String updateTime;
        }
    }
}

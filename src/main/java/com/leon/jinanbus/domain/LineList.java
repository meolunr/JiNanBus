package com.leon.jinanbus.domain;

import java.util.List;

public class LineList {
    public Status status;
    public List<Result> result;

    public static class Status {
        public int code;
    }

    public static class Result {
        public String id;
        public String endStationName;
        public String lineName;
        public String startStationName;

        // public String updateTime;
        // public String localLineId;
    }
}
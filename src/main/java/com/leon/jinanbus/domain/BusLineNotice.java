package com.leon.jinanbus.domain;

import java.util.List;

public class BusLineNotice {
    public List<Notice> results;

    public static class Notice {
        public String title;
        public String detail;

        // public String createdAt;
        // public String objectId;
        // public String updatedAt;
    }
}

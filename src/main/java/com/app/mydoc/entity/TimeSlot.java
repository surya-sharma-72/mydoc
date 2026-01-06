package com.app.mydoc.entity;

public enum TimeSlot {

    MORNING_10_00("10:00 AM - 11:00 AM"),
    MORNING_11_00("11:00 AM - 12:00 PM"),
    MORNING_12_00("12:00 PM - 01:00 PM"),

    EVENING_03_00("03:00 PM - 04:00 PM"),
    EVENING_04_00("04:00 PM - 05:00 PM"),
    EVENING_05_00("05:00 PM - 06:00 PM"),
    EVENING_06_00("06:00 PM - 07:00 PM"),
    EVENING_07_00("07:00 PM - 08:00 PM");

    private final String display;

    TimeSlot(String display) {
        this.display = display;
    }

    public String getDisplay() {
        return display;
    }
}

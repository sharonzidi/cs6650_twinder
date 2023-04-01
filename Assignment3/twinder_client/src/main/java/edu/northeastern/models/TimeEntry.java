package edu.northeastern.models;

public class TimeEntry {
    private final long startTime;
    private final long endTime;

    public TimeEntry(long startTime, long endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    @Override
    public String toString() {
        return "TimeEntry{" +
                "startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}

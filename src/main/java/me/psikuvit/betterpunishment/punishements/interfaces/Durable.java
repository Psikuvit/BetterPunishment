package me.psikuvit.betterpunishment.punishements.interfaces;

public interface Durable {

    long getDuration();
    void setDuration(long duration);
    long getRemaining();
    long getEndTime();
}

package com.magalhaes.bowltoten.model;

public class Frame {

    private int frameNumber;
    private String firstRoll;
    private String secondRoll;
    private String extraRoll;
    private Integer frameScore;

    public Frame(int frameNumber, String firstRoll, String secondRoll, String extraRoll) {
        this.frameNumber = frameNumber;
        this.firstRoll = firstRoll;
        this.secondRoll = secondRoll;
        this.extraRoll = extraRoll;
    }

    public int getFrameNumber() {
        return frameNumber;
    }

    public void setFrameNumber(int frameNumber) {
        this.frameNumber = frameNumber;
    }

    public String getFirstRoll() {
        return firstRoll;
    }

    public void setFirstRoll(String firstRoll) {
        this.firstRoll = firstRoll;
    }

    public String getSecondRoll() {
        return secondRoll;
    }

    public void setSecondRoll(String secondRoll) {
        this.secondRoll = secondRoll;
    }

    public String getExtraRoll() {
        return extraRoll;
    }

    public void setExtraRoll(String extraRoll) {
        this.extraRoll = extraRoll;
    }

    public int getFrameScore() {
        return frameScore;
    }

    public void setFrameScore(int frameScore) {
        this.frameScore = frameScore;
    }

    public Boolean isStrike() {
        try {
            return Integer.valueOf(firstRoll) == 10;
        } catch (Exception e) {
            return false;
        }
    }

    public Boolean isSpare() {
        try {
            return !isStrike() && Integer.valueOf(firstRoll) + Integer.valueOf(secondRoll) == 10;
        } catch (Exception e) {
            return false;
        }
    }

    public Boolean canRollExtra() {
        return frameNumber == 10 && isSpare() || frameNumber == 10 && isStrike();
    }
}

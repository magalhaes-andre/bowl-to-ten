package com.magalhaes.bowltoten.service;


import com.magalhaes.bowltoten.model.Frame;
import com.magalhaes.bowltoten.model.Player;

import java.lang.management.PlatformLoggingMXBean;
import java.util.*;

public class ScoringService {

    public List<Player> calculateFramesPerPlayer(Map<String, List<String>> playersAndRolls) {
        List<Player> players = new ArrayList<>();
        playersAndRolls.forEach((playerName, rollList) -> {
            Player player = new Player(playerName);
            List<Frame> frames = new ArrayList<>();
            int frameCounter = 1;
            for (int iteration = 0; iteration < rollList.size(); iteration++) {
                if (rollList.get(iteration).equalsIgnoreCase("f")) {
                    frames.add(new Frame(frameCounter, "F", rollList.get(iteration + 1), null));
                    iteration++;
                } else {
                    if (frameCounter == 10) {
                        Frame tenthFrame = new Frame(frameCounter, rollList.get(iteration), rollList.get(iteration + 1), rollList.get(iteration + 2));
                        frames.add(tenthFrame);
                        iteration = rollList.size();
                    } else if (Integer.valueOf(rollList.get(iteration)) == 10) {
                        frames.add(new Frame(frameCounter, rollList.get(iteration), "0", null));
                    } else {
                        frames.add(new Frame(frameCounter, rollList.get(iteration), rollList.get(iteration + 1), null));
                        iteration++;
                    }
                }

                frameCounter++;
            }
            player.setFrames(frames);
            players.add(player);
        });

        return calculateFrameScores(players);
    }

    private List<Player> calculateFrameScores(List<Player> players) {
        players.forEach(player -> {
            for (int iteration = 0; iteration < player.getFrames().size(); iteration++) {
                Frame currentFrame = player.getFrames().get(iteration);

                if (currentFrame.getFrameNumber() == 10) {
                    calculateTenthFrame(iteration, player);
                } else {
                    if (currentFrame.isStrike()) {
                        calculateFrameScoreOnStrike(iteration, player);
                    }

                    if (currentFrame.isSpare()) {
                        calculateFrameScoreOnSpare(iteration, player);
                    }

                    if (!currentFrame.isStrike() && !currentFrame.isSpare()) {
                        calculateFrameScoreWithoutSpareOrStrike(iteration, player);
                    }
                }
            }
        });
        return players;
    }

    private void calculateFrameScoreWithoutSpareOrStrike(int counterIteration, Player player) {
        Frame currentFrame = player.getFrames().get(counterIteration);
        Frame previousFrame;
        int firstRoll = getValueFromRoll(currentFrame.getFirstRoll());
        int secondRoll = getValueFromRoll(currentFrame.getSecondRoll());
        if (counterIteration == 0) {
            currentFrame.setFrameScore(firstRoll + secondRoll);
        } else {
            previousFrame = player.getFrames().get(counterIteration - 1);
            currentFrame.setFrameScore(previousFrame.getFrameScore() + firstRoll + secondRoll);
        }
    }

    private void calculateFrameScoreOnSpare(int counterIteration, Player player) {
        Frame currentFrame = player.getFrames().get(counterIteration);
        Frame nextFrame = player.getFrames().get(counterIteration + 1);
        Frame previousFrame;
        int bonusPinfall = getValueFromRoll(nextFrame.getFirstRoll());
        if (counterIteration == 0) {
            currentFrame.setFrameScore(10 + bonusPinfall);
        } else {
            previousFrame = player.getFrames().get(counterIteration - 1);
            currentFrame.setFrameScore(previousFrame.getFrameScore() + 10 + bonusPinfall);
        }
    }

    private void calculateFrameScoreOnStrike(int counterIteration, Player player) {
        Frame currentFrame = player.getFrames().get(counterIteration);
        Frame nextFrame = player.getFrames().get(counterIteration + 1);
        Frame previousFrame;
        int firstBonusPinfall;
        int secondBonusPinfall;
        if (nextFrame.isStrike()) {
            if (currentFrame.getFrameNumber() == 8 ) {
                firstBonusPinfall = getValueFromRoll(nextFrame.getFirstRoll());
                secondBonusPinfall = getValueFromRoll(player.getFrames().get(counterIteration + 1).getFirstRoll());
            } else if (currentFrame.getFrameNumber() == 9) {
                firstBonusPinfall = getValueFromRoll(nextFrame.getFirstRoll());
                secondBonusPinfall = getValueFromRoll(nextFrame.getSecondRoll());
            } else {
                firstBonusPinfall = getValueFromRoll(nextFrame.getFirstRoll());
                secondBonusPinfall = getValueFromRoll(player.getFrames().get(counterIteration + 2).getFirstRoll());
            }
        } else {
            firstBonusPinfall = getValueFromRoll(nextFrame.getFirstRoll());
            secondBonusPinfall = getValueFromRoll(nextFrame.getSecondRoll());
        }
        if (counterIteration == 0) {
            currentFrame.setFrameScore(10 + firstBonusPinfall + secondBonusPinfall);
        } else {
            previousFrame = player.getFrames().get(counterIteration - 1);
            currentFrame.setFrameScore(previousFrame.getFrameScore() + 10 + firstBonusPinfall + secondBonusPinfall);
        }
    }

    private void calculateTenthFrame(int counterIteration, Player player) {
        Frame currentFrame = player.getFrames().get(counterIteration);
        int previousFrameScore = player.getFrames().get(counterIteration - 1).getFrameScore();
        int firstRoll = getValueFromRoll(currentFrame.getFirstRoll());
        int secondRoll = getValueFromRoll(currentFrame.getSecondRoll());
        int extraRoll = getValueFromRoll(currentFrame.getExtraRoll());
        if (currentFrame.canRollExtra()) {
            currentFrame.setFrameScore(previousFrameScore + firstRoll + secondRoll + extraRoll);
        }
    }

    private int getValueFromRoll(String rollValue) {
        return rollValue.equalsIgnoreCase("f") ? 0 : Integer.valueOf(rollValue);
    }
}

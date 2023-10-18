package com.magalhaes.bowltoten.service;


import com.magalhaes.bowltoten.model.Frame;
import com.magalhaes.bowltoten.model.Player;

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

    public List<Player> calculateFrameScores(List<Player> players) {
        players.forEach(player -> {
            for (int iteration = 0; iteration < player.getFrames().size(); iteration++) {
                Frame currentFrame = player.getFrames().get(iteration);
                Frame nextFrame = iteration + 1 == player.getFrames().size() ? null : player.getFrames().get(iteration + 1);
                int firstBonusPinfall = 0;
                int secondBonusPinfall = 0;

                if (currentFrame.getFrameNumber() == 10) {
                    int previousFrameScore = player.getFrames().get(iteration - 1).getFrameScore();
                    int firstRoll = currentFrame.getFirstRoll().equalsIgnoreCase("F") ? 0 : Integer.valueOf(currentFrame.getFirstRoll());
                    int secondRoll = currentFrame.getSecondRoll().equalsIgnoreCase("F") ? 0 : Integer.valueOf(currentFrame.getSecondRoll());
                    int extraRoll = currentFrame.getExtraRoll().equalsIgnoreCase("F") ? 0 : Integer.valueOf(currentFrame.getExtraRoll());
                    if (currentFrame.canRollExtra()) {
                        currentFrame.setFrameScore(previousFrameScore + firstRoll + secondRoll + extraRoll);
                    }
                } else {
                    if (currentFrame.isStrike()) {
                        if (nextFrame.isStrike()) {
                            if (currentFrame.getFrameNumber() + 2 > 10) {
                                firstBonusPinfall = nextFrame.getFirstRoll().equalsIgnoreCase("f") ? 0 : Integer.valueOf(nextFrame.getFirstRoll());
                                secondBonusPinfall = player.getFrames().get(iteration + 1).getSecondRoll().equalsIgnoreCase("f") ? 0 : Integer.valueOf(player.getFrames().get(iteration + 1).getFirstRoll());
                            } else {
                                firstBonusPinfall = nextFrame.getFirstRoll().equalsIgnoreCase("f") ? 0 : Integer.valueOf(nextFrame.getFirstRoll());
                                secondBonusPinfall = player.getFrames().get(iteration + 2).getFirstRoll().equalsIgnoreCase("f") ? 0 : Integer.valueOf(player.getFrames().get(iteration + 2).getFirstRoll());
                            }
                        } else {
                            firstBonusPinfall = nextFrame.getFirstRoll().equalsIgnoreCase("f") ? 0 : Integer.valueOf(nextFrame.getFirstRoll());
                            secondBonusPinfall = nextFrame.getSecondRoll().equalsIgnoreCase("f") ? 0 : Integer.valueOf(nextFrame.getSecondRoll());
                        }
                        if (iteration == 0) {
                            currentFrame.setFrameScore(10 + firstBonusPinfall + secondBonusPinfall);
                        } else {
                            Frame previousFrame = player.getFrames().get(iteration - 1);
                            currentFrame.setFrameScore(previousFrame.getFrameScore() + 10 + firstBonusPinfall + secondBonusPinfall);
                        }
                    }

                    if (currentFrame.isSpare()) {
                        int bonusPinfall = nextFrame.getFirstRoll().equalsIgnoreCase("f") ? 0 : Integer.valueOf(nextFrame.getFirstRoll());
                        if (iteration == 0) {
                            currentFrame.setFrameScore(10 + bonusPinfall);
                        } else {
                            Frame previousFrame = player.getFrames().get(iteration - 1);
                            currentFrame.setFrameScore(previousFrame.getFrameScore() + 10 + bonusPinfall);
                        }
                    }

                    if (!currentFrame.isStrike() && !currentFrame.isSpare()) {
                        int firstRoll = currentFrame.getFirstRoll().equalsIgnoreCase("f") ? 0 : Integer.valueOf(currentFrame.getFirstRoll());
                        int secondRoll = currentFrame.getSecondRoll().equalsIgnoreCase("f") ? 0 : Integer.valueOf(currentFrame.getSecondRoll());
                        if (iteration == 0) {
                            currentFrame.setFrameScore(firstRoll + secondRoll);
                        } else {
                            Frame previousFrame = player.getFrames().get(iteration - 1);
                            currentFrame.setFrameScore(previousFrame.getFrameScore() + firstRoll + secondRoll);
                        }
                    }
                }
            }
        });
        return players;
    }
}

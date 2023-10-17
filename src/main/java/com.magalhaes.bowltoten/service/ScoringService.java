package com.magalhaes.bowltoten.service;


import com.magalhaes.bowltoten.model.Frame;
import com.magalhaes.bowltoten.model.Player;

import java.util.*;

public class ScoringService {

    Map<String, List<String>> playersAndRolls;

    public ScoringService(Map<String, List<String>> playersAndRolls) {
        this.playersAndRolls = playersAndRolls;
    }

    public List<Player> calculateFramesPerPlayer() {
        List<Player> players = new ArrayList<>();
        playersAndRolls.forEach((playerName, rollList) -> {
            Player player = new Player(playerName);
            List<Frame> frames = new ArrayList<>();
            int frameCounter = 1;
            for (int iteration = 0; iteration < rollList.size(); iteration++) {
                if (rollList.get(iteration).equalsIgnoreCase("f")) {
                    frames.add(new Frame(frameCounter, "F", rollList.get(iteration + 1), null));
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

        return players;
    }
}

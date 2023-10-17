package com.magalhaes.bowltoten.service;


import com.magalhaes.bowltoten.errors.NotationErrorException;
import com.magalhaes.bowltoten.model.Match;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotationReadService {

    private final String TAB_REGEX = "\t";
    private final String FAULT_FLAG = "F";

    public Match buildMatch(List<String> lines) {
        Match match = new Match();
        Map<String, List<String>> playersAndRolls = new HashMap<>();
        lines.forEach(line -> {
            String playerName = line.split(TAB_REGEX)[0];
            String rollValue = line.split(TAB_REGEX)[1];
            if(!rollIsFault(rollValue)){
                checkForErrors(rollValue, line);
            }
                if (playersAndRolls.containsKey(playerName)) {
                    playersAndRolls.get(playerName).add(rollValue);
                } else {
                    List<String> startingList = new ArrayList<>();
                    startingList.add(rollValue);
                    playersAndRolls.put(playerName, startingList);
                }
            }
        );

        ScoringService scoringService = new ScoringService();
        scoringService.calculateFramesPerPlayer(playersAndRolls).forEach(player -> {
            System.out.println("Player Name: " + player.getName());
            player.getFrames().forEach(frame -> {
                System.out.println("Frame Number: " +frame.getFrameNumber());
                System.out.println("FR: " + frame.getFirstRoll() + TAB_REGEX + "SR: " + frame.getSecondRoll() + TAB_REGEX + "ER: " + frame.getExtraRoll());
            });
        });

        match.setPlayers(scoringService.calculateFramesPerPlayer(playersAndRolls));
        return match;
    }

    private void checkForErrors(String rollValue, String line) {
        if (rollIsNotANumberOrFault(rollValue, line) || rollGreaterThenTen(rollValue) || negativeRoll(rollValue)) {
            throw new NotationErrorException("Invalid values for roll on file in the following line: " + line);
        }
    }

    private Boolean rollIsNotANumberOrFault(String rollValue, String line) {
        try {
            Integer.valueOf(rollValue);
            return false;
        } catch (NumberFormatException exception){
            if(rollIsFault(rollValue)){
                return false;
            }
            throw new NotationErrorException("File format is not according to expected, issue is on line: " + line);
        }
    }
    private Boolean rollGreaterThenTen(String rollValue) {
        return Integer.valueOf(rollValue) > 10;
    }

    private Boolean negativeRoll(String rollValue) {
        return Integer.valueOf(rollValue) < 0;
    }

    private Boolean rollIsFault(String rollValue) {
        return rollValue.equalsIgnoreCase(FAULT_FLAG);
    }
}

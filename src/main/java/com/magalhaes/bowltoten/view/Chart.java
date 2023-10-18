package com.magalhaes.bowltoten.view;

import com.magalhaes.bowltoten.model.Frame;
import com.magalhaes.bowltoten.model.Match;
import dnl.utils.text.table.TextTable;

import java.nio.channels.Pipe;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Chart {
    private Match match;
    private String[] columnNames = {"Frame", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
    private Object[][] data;

    private final String STRIKE = "_ | X";
    private final String SPARE = " | /";
    private final String PIPE = " | "; // My keyboard's pipe key is not working.

    public Chart(Match match) {
        this.match = match;
        this.data = calculateData();
    }

    private Object[][] calculateData() {
        int columnsToTen = 11;
        int numberOfRows = match.getPlayers().size() * 3;
        String[][] data = new String[numberOfRows][columnsToTen];
        AtomicInteger currentLine = new AtomicInteger();
        match.getPlayers().forEach(player -> {
            data[currentLine.get()][0] = player.getName();
            data[currentLine.get() + 1][0] = "Pinfalls";
            data[currentLine.get() + 2][0] = "Score";
            for (int iteration = 0; iteration < player.getFrames().size(); iteration++) {
                Frame currentFrame = player.getFrames().get(iteration);
                if (currentFrame.getFrameNumber() != 10 && currentFrame.isStrike()) {
                    data[currentLine.get() + 1][currentFrame.getFrameNumber()] = STRIKE;
                } else if (currentFrame.getFrameNumber() != 10 && currentFrame.isSpare()) {
                    data[currentLine.get() + 1][currentFrame.getFrameNumber()] = currentFrame.getFirstRoll() + SPARE;
                } else if (currentFrame.getFrameNumber() == 10 || currentFrame.canRollExtra()) {
                    String firstRoll = Integer.valueOf(currentFrame.getFirstRoll()) == 10 ? "X" : currentFrame.getFirstRoll();
                    String secondRoll = Integer.valueOf(currentFrame.getSecondRoll()) == 10 ? "X" : currentFrame.getSecondRoll();
                    String extraRoll = Integer.valueOf(currentFrame.getExtraRoll()) == 10 ? "X" : currentFrame.getExtraRoll();
                    data[currentLine.get() + 1][currentFrame.getFrameNumber()] = firstRoll + PIPE + secondRoll + PIPE + extraRoll;
                } else {
                    data[currentLine.get() + 1][currentFrame.getFrameNumber()] = currentFrame.getFirstRoll() + PIPE + currentFrame.getSecondRoll();
                }
                data[currentLine.get() + 2][currentFrame.getFrameNumber()] = String.valueOf(currentFrame.getFrameScore());
            }
            currentLine.addAndGet(3);
        });

        return data;
    }

    public void printTable() {
        TextTable table = new TextTable(columnNames, data);
        table.printTable();
    }
}

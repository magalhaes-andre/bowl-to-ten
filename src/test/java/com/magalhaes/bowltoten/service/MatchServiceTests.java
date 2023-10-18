package com.magalhaes.bowltoten.service;

import com.magalhaes.bowltoten.errors.BadFileException;
import com.magalhaes.bowltoten.errors.NotationErrorException;
import com.magalhaes.bowltoten.input.InputFileImpl;
import com.magalhaes.bowltoten.model.Match;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MatchServiceTests {

    private MatchService service;
    private InputFileImpl inputFile;
    private final int TENTH_FRAME_INDEX = 9;
    private final int MAX_SCORE = 300;
    private final int FRAMES_TOTAL = 10;
    private final String INVALID_SCORES_PATH = "src/test/resources/negative/invalid-score.txt";
    private final String NEGATIVE_SCORES_PATH = "src/test/resources/negative/negative.txt";
    private final String PERFECT_SCORES_PATH = "src/test/resources/positive/perfect.txt";
    private final String SCORES_PATH = "src/test/resources/positive/scores.txt";
    private final String EMPTY_PATH = "src/test/resources/negative/empty.txt";

    @BeforeEach
    private void setUp() {
        service = new MatchService();
    }
    @Test()
    public void invalidScoresWillThrowNotationErrorException() {
        inputFile = new InputFileImpl(INVALID_SCORES_PATH);
        assertThrows(NotationErrorException.class, () -> {
            service.buildMatch(inputFile.buildNotation());
        });
    }

    @Test()
    public void negativeScoresWillThrowNotationErrorException() {
        inputFile = new InputFileImpl(NEGATIVE_SCORES_PATH);
        assertThrows(NotationErrorException.class, () -> {
            service.buildMatch(inputFile.buildNotation());
        });
    }

    @Test()
    public void blankFileWillThrowBadFileException() {
        inputFile = new InputFileImpl(EMPTY_PATH);
        assertThrows(BadFileException.class, () -> {
            service.buildMatch(inputFile.buildNotation());
        });
    }

    @Test()
    public void perfectScoreWillReadCorrectly() {
        inputFile = new InputFileImpl(PERFECT_SCORES_PATH);
        Match match = service.buildMatch(inputFile.buildNotation());
        assertEquals(1, match.getPlayers().size(), "Should be a 1 player match.");
        assertEquals(FRAMES_TOTAL, match.getPlayers().get(0).getFrames().size(), "Player should have 10 frames played exactly.");
        assertEquals(MAX_SCORE, match.getPlayers().get(0).getFrames().get(TENTH_FRAME_INDEX).getFrameScore(), "Player should have 300 points in perfect score match.");
    }

    @Test()
    public void matchWillReadCorrectly() {
        inputFile = new InputFileImpl(SCORES_PATH);
        final int JEFF_FINAL_SCORE = 167;
        final int JOHN_FINAL_SCORE = 151;
        Match match = service.buildMatch(inputFile.buildNotation());
        assertEquals(2, match.getPlayers().size(), "Should be a 2 player match.");
        assertEquals(FRAMES_TOTAL, match.getPlayers().get(0).getFrames().size(), "Player should have 10 frames played exactly.");
        assertEquals(FRAMES_TOTAL, match.getPlayers().get(1).getFrames().size(), "Player should have 10 frames played exactly.");
        assertEquals(JEFF_FINAL_SCORE, match.getPlayers().get(0).getFrames().get(TENTH_FRAME_INDEX).getFrameScore(), "Jeff should have " + JEFF_FINAL_SCORE + " in his final framescore");
        assertEquals(JOHN_FINAL_SCORE, match.getPlayers().get(1).getFrames().get(TENTH_FRAME_INDEX).getFrameScore(), "John should have " + JOHN_FINAL_SCORE + " in his final framescore");
    }
}

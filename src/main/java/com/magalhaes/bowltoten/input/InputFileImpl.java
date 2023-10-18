package com.magalhaes.bowltoten.input;

import com.magalhaes.bowltoten.errors.BadFileException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class InputFileImpl implements Input {
    private String fileName;

    public InputFileImpl(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public List<String> buildNotation() {
       return readLines(fileName);
    }

    private List<String> readLines(String filePath) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.lines().forEach(line -> lines.add(line));
        } catch (Exception exception) {
            throw new BadFileException("There was an error when reading the file with the following path: " + filePath);
        } finally {
            if (lines.isEmpty()) {
                throw new BadFileException("The file provided is empty");
            }
            return lines;
        }
    }

}

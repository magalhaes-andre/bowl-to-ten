package com.magalhaes.bowltoten.model;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private String name;
    private List<Frame> frames;

    public Player(String name) {
        this.name = name;
        frames = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setFrames (List<Frame> frames) {
        this.frames = frames;
    }

    public List<Frame> getFrames() {
        return frames;
    }
}

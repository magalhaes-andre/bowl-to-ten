package com.magalhaes.bowltoten;


import com.magalhaes.bowltoten.input.Input;
import com.magalhaes.bowltoten.input.InputFile;
import com.magalhaes.bowltoten.model.Match;
import com.magalhaes.bowltoten.service.NotationReadService;
import com.magalhaes.bowltoten.view.Chart;

public class Application {

    public static void main(String[] args) {
        Input input = new InputFile("src/test/resources/positive/scores.txt");
        NotationReadService notationReadService = new NotationReadService();
        Match match = notationReadService.buildMatch(input.buildNotation());
        Chart chart = new Chart(match);
        chart.printTable();
    }
}

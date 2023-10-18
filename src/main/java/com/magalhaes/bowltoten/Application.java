package com.magalhaes.bowltoten;


import com.magalhaes.bowltoten.input.Input;
import com.magalhaes.bowltoten.input.InputFileImpl;
import com.magalhaes.bowltoten.model.Match;
import com.magalhaes.bowltoten.service.MatchService;
import com.magalhaes.bowltoten.view.Chart;

public class Application {

    public static void main(String[] args) {
        if (checkForArguments(args)) {
            Input input = new InputFileImpl(args[0]);
            MatchService matchService = new MatchService();
            Match match = matchService.buildMatch(input.buildNotation());
            Chart chart = new Chart(match);
            chart.printTable();
        }
    }

    private static Boolean checkForArguments(String[] args) {
        if (args.length == 0) {
            System.out.println("[ERROR] Please paste an absolute path to the file you'd like to read in this program");
            return false;
        } else {
            System.out.println("[INFO] Reading file with the following path: " + args[0]);
            return true;
        }
    }
}

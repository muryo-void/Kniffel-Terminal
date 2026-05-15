package kniffel.com;

import java.util.List;
import java.util.Arrays;

public class GameController {

    private enum ScreenState { START, SETUP, GAME, SCORE_ENTRY, END, HIGHSCORES, EXIT }

    private ScreenState currentState = ScreenState.START;
    private Player[] players;
    private int playersCount = 1;
    private int activePlayer = 0;
    private int currentRound = 1;
    
    private DiceController dice;
    private int selIdx = 0;
    private boolean firstStart = true;

    public GameController() {
        dice = new DiceController(); 
    }

    public void startGame() throws InterruptedException {
        while(currentState != ScreenState.EXIT) {
            switch(currentState) {
                case START -> handleStartScreen();
                case SETUP -> handleSetupScreen();
                case GAME -> handleGameScreen();
                case SCORE_ENTRY -> handleScoreScreen();
                case END -> handleEndScreen();
                case HIGHSCORES -> handleHighscores();
            }
        }
        InputHandler.close();
    }

    private void handleStartScreen() throws InterruptedException {
        ConsoleRender.drawStartScreen(firstStart);
        firstStart = false; // Tippanimation nur beim ersten Mal
        String in = InputHandler.readInput().toUpperCase();
        if (in.equals("S")) {
            currentRound = 1;
            activePlayer = 0;
            currentState = ScreenState.SETUP;
        } else if (in.equals("H")) currentState = ScreenState.HIGHSCORES;
        else if (in.equals("Q")) currentState = ScreenState.EXIT;
    }

    private void handleSetupScreen() {
        String[] names = {"", "", "", ""};
        int field = 0;
        ConsoleRender.drawSetupFrame(); // Rahmen nur 1x malen!
        while (currentState == ScreenState.SETUP) {
            ConsoleRender.updateSetup(playersCount, field, names);
            String input = InputHandler.readInput(); 
            switch (input) {
                case "1","2","3","4" -> {
                    playersCount = Integer.parseInt(input);
                    field = 0; 
                }
                case "UP" -> { if (field > 0) field--; }
                case "DOWN" -> { if (field < playersCount - 1) field++; }
                case "\r", "\n" -> {
                    players = new Player[playersCount];
                    for (int i = 0; i < playersCount; i++) {
                        players[i] = new Player(names[i].isEmpty() ? "P" + (i+1) : names[i]);
                    }
                    dice.reset();
                    currentState = ScreenState.GAME;
                }
                case "ESC" -> currentState = ScreenState.START;
                case "\b", "\177" -> {
                    if (!names[field].isEmpty()) names[field] = names[field].substring(0, names[field].length()-1);
                }
                default -> {
                    if (input.length() == 1 && names[field].length() < 10) names[field] += input;
                }
            }
        }
    }

    private void handleGameScreen() throws InterruptedException {
        ConsoleRender.drawDashboardFrame(false); // Spiel-Rahmen 1x laden
        
        // Automatisches Würfeln + Animation zu Beginn jedes Zugs!
        if (dice.getLeft() == 3) {
            ConsoleRender.updateDashboard(players, activePlayer, currentRound, dice, false, selIdx);
            ConsoleRender.animateDice(dice);
            dice.roll();
        }

        while(currentState == ScreenState.GAME) {
            ConsoleRender.updateDashboard(players, activePlayer, currentRound, dice, false, selIdx);
            String in = InputHandler.readInput().toUpperCase(); 
            switch (in) {
                case "1","2","3","4","5" -> dice.toggle(Integer.parseInt(in) - 1);
                case "R" -> {
                    if (dice.getLeft() > 0) {
                        ConsoleRender.animateDice(dice);
                        dice.roll();
                    }
                }
                case "\r", "\n" -> { selIdx = 0; currentState = ScreenState.SCORE_ENTRY; }
                case "ESC" -> currentState = ScreenState.START;
            }
        }
    }

    private void handleScoreScreen() throws InterruptedException {
        Scorecard.Cat[] cats = Scorecard.Cat.values();
        ConsoleRender.drawDashboardFrame(true); // Rechten Rand für die Scores clearen
        
        while(currentState == ScreenState.SCORE_ENTRY) {
            ConsoleRender.updateDashboard(players, activePlayer, currentRound, dice, true, selIdx);
            
            String in = InputHandler.readInput();
            switch (in) {
                case "UP" -> { if (selIdx > 0) selIdx--; }
                case "DOWN" -> { if (selIdx < cats.length - 1) selIdx++; }
                case "\r", "\n" -> {
                    Player p = players[activePlayer];
                    if (!p.getSc().has(cats[selIdx])) {
                        int val = p.getSc().calc(cats[selIdx], dice.getV());
                        p.getSc().set(cats[selIdx], val);
                        checkNextTurn();
                    }
                }
                case "ESC" -> currentState = ScreenState.GAME; // Geht zurück, ohne neu zu würfeln!
            }
        }
    }

    private void checkNextTurn() {
        activePlayer++;
        if (activePlayer >= playersCount) {
            activePlayer = 0;
            currentRound++;
        }
        if (currentRound > 13) {
            currentState = ScreenState.END;
        } else {
            dice.reset();
            currentState = ScreenState.GAME;
        }
    }

    private void handleEndScreen() {
        for (Player p : players) {
            HighscoreManager.save(p.getName(), p.getSc().total());
        }
        ConsoleRender.drawEndScreen(players);
        while(currentState == ScreenState.END) {
            String in = InputHandler.readInput().toUpperCase();
            if (in.equals("N")) currentState = ScreenState.SETUP;
            else if (in.equals("ESC")) currentState = ScreenState.START;
        }
    }

    private void handleHighscores() {
        List<String> tops = HighscoreManager.getTop();
        ConsoleRender.drawHighscoreScreen(tops);
        InputHandler.readInput();
        currentState = ScreenState.START;
    }
}
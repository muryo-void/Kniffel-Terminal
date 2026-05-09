package kniffel.com;
import java.util.List;

public class GameController {

    private enum ScreenState { START, SETUP, GAME, SCORE_ENTRY, END, HIGHSCORES, EXIT }

    private ScreenState currentState = ScreenState.START;
    private Player[] players;
    private int playersCount = 1;
    private int activePlayer = 0;
    private int currentRound = 1;
    
    private DiceController dice;
    private int selIdx = 0;

    // --- KONSTRUKTOR ---
    public GameController() {
        dice = new DiceController(); 
    }

    public void startGame() {
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

    private void handleStartScreen() {
        ConsoleRender.drawStartScreen();
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
        while (currentState == ScreenState.SETUP) {
            ConsoleRender.drawSetupScreen(playersCount, field, names);
            String input = InputHandler.readInput(); 
            switch (input) {
                case "1","2","3","4" -> playersCount = Integer.parseInt(input);
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

    private void handleGameScreen() {
        dice.roll();
        while(currentState == ScreenState.GAME) {
            ConsoleRender.drawGameScreen(players[activePlayer], currentRound, dice);
            String in = InputHandler.readInput().toUpperCase(); 
            switch (in) {
                case "1","2","3","4","5" -> dice.toggle(Integer.parseInt(in) - 1);
                case "R" -> dice.roll();
                case "\r", "\n" -> { selIdx = 0; currentState = ScreenState.SCORE_ENTRY; }
                case "Q" -> currentState = ScreenState.START;
            }
        }
    }

    private void handleScoreScreen() {
        Scorecard.Cat[] cats = Scorecard.Cat.values();
        Player p = players[activePlayer];
        ConsoleRender.drawScoreScreen(p, dice.getV(), selIdx);
        
        String in = InputHandler.readInput();
        switch (in) {
            case "UP" -> { if (selIdx > 0) selIdx--; }
            case "DOWN" -> { if (selIdx < cats.length - 1) selIdx++; }
            case "\r", "\n" -> {
                if (!p.getSc().has(cats[selIdx])) {
                    int val = p.getSc().calc(cats[selIdx], dice.getV());
                    p.getSc().set(cats[selIdx], val);
                    dice.reset();
                    checkNextTurn();
                }
            }
            case "Q", "q" -> currentState = ScreenState.GAME;
        }
    }

    private void checkNextTurn() {
        activePlayer++;
        if (activePlayer >= playersCount) {
            activePlayer = 0;
            currentRound++;
        }
        currentState = (currentRound > 13) ? ScreenState.END : ScreenState.GAME;
    }

    private void handleEndScreen() {
        // Scores werden gespeichert
        for (Player p : players) {
            HighscoreManager.save(p.getName(), p.getSc().total());
        }

        // Gewinner
        Player win = players[0];
        for (Player p : players) {
            if (p.getSc().total() > win.getSc().total()) win = p;
        }

        ConsoleRender.drawEndScreen(win.getName());
        
        String in = InputHandler.readInput().toUpperCase();
        if (in.equals("N")) currentState = ScreenState.SETUP;
        else if (in.equals("Q")) currentState = ScreenState.START;
    }

    private void handleHighscores() {
        List<String> tops = HighscoreManager.getTop();
        ConsoleRender.drawHighscoreScreen(tops);
        InputHandler.readInput();
        currentState = ScreenState.START;
    }
}
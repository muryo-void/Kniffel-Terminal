package kniffel.com;

public class GameController {

    private enum ScreenState { START, SETUP, GAME, SCORE_ENTRY, END, HIGHSCORES, EXIT }

    private ScreenState currentState = ScreenState.START;
    private Player[] players;
    private int playersCount = 1;
    private int activePlayer = 0;
    private int currentRound = 1;
    private int rollsLeft = 3;

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
        String input = InputHandler.readInput().toUpperCase();
        if (input.equals("S")) {
            currentRound = 1;
            activePlayer = 0;
            currentState = ScreenState.SETUP;
        } else if (input.equals("H")) currentState = ScreenState.HIGHSCORES;
        else if (input.equals("Q")) currentState = ScreenState.EXIT;
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
                case "\r", "\n" -> { // Start
                    players = new Player[playersCount];
                    for (int i = 0; i < playersCount; i++) {
                        players[i] = new Player(names[i].isEmpty() ? "Spieler " + (i+1) : names[i]);
                    }
                    rollsLeft = 3;
                    currentState = ScreenState.GAME;
                }
                case "ESC" -> currentState = ScreenState.START;
                case "\b", "\177" -> { // Backspace
                    if (!names[field].isEmpty()) names[field] = names[field].substring(0, names[field].length()-1);
                }
                default -> {
                    if (input.length() == 1 && names[field].length() < 15) names[field] += input;
                }
            }
        }
    }

    private void handleGameScreen() {
        ConsoleRender.drawGameScreen(players[activePlayer].getName(), currentRound, rollsLeft);
        String input = InputHandler.readInput().toUpperCase(); 
        if (input.equals("R") && rollsLeft > 0) rollsLeft--;
        else if (input.equals("\r") || input.equals("\n")) currentState = ScreenState.SCORE_ENTRY;
        else if (input.equals("Q")) currentState = ScreenState.START;
    }

    private void handleScoreScreen() {
        ConsoleRender.drawScoreScreen(players[activePlayer].getName());
        String input = InputHandler.readInput();
        if (input.equals("\r") || input.equals("\n")) checkNextTurn();
        else if (input.equalsIgnoreCase("Q")) currentState = ScreenState.GAME;
    }

    private void checkNextTurn() {
        activePlayer++;
        rollsLeft = 3;
        if (activePlayer >= playersCount) {
            activePlayer = 0;
            currentRound++;
        }
        currentState = (currentRound > 13) ? ScreenState.END : ScreenState.GAME;
    }

    private void handleEndScreen() {
        ConsoleRender.drawEndScreen(players[0].getName());
        String input = InputHandler.readInput().toUpperCase();
        if (input.equals("N")) currentState = ScreenState.SETUP;
        else if (input.equals("Q")) currentState = ScreenState.START;
    }

    private void handleHighscores() {
        ConsoleRender.drawStub("HIGHSCORES\n[Taste] Zurück");
        InputHandler.readInput();
        currentState = ScreenState.START;
    }
}
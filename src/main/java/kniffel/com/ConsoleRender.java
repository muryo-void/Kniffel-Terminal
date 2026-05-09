package kniffel.com;

public class ConsoleRender {

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void drawStub(String text) {
        clearScreen();
        System.out.println(text);
    }

    public static void drawStartScreen() {
        clearScreen();
        System.out.println("--- KNIFFEL START ---");
        System.out.println("[S] SPIELEN\n[H] HIGHSCORES\n[Q] BEENDEN");
    }

    public static void drawSetupScreen(int count, int field, String[] names) {
        clearScreen();
        System.out.println("--- SETUP ---");
        System.out.println("Spieler: " + count + " (1-4)");
        for(int i = 0; i < count; i++) {
            System.out.println((i == field ? " > " : "   ") + "P" + (i+1) + ": " + names[i]);
        }
        System.out.println("\n[ENTER] Start  [ESC] Zurück");
    }

    public static void drawGameScreen(String name, int round, int rolls) {
        clearScreen();
        System.out.println("RUNDE: " + round + "/13 | SPIELER: " + name);
        System.out.println("WÜRFE: " + rolls);
        System.out.println("\n[R] WÜRFELN  [ENTER] SCORE");
    }

    public static void drawScoreScreen(String name) {
        clearScreen();
        System.out.println("--- SCORE EINTRAGEN ---");
        System.out.println("Spieler: " + name);
        System.out.println("\n[ENTER] OK  [Q] ZURÜCK");
    }

    public static void drawEndScreen(String winner) {
        clearScreen();
        System.out.println("--- GAME OVER ---");
        System.out.println("Gewinner: " + winner);
        System.out.println("\n[N] NEU  [Q] HAUPTMENÜ");
    }
}
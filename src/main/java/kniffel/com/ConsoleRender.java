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

   public static void drawGameScreen(Player p, int round, DiceController d) {
        clearScreen();
        System.out.println("RUNDE: " + round + "/13 | SPIELER: " + p.getName());
        System.out.print("WÜRFEL: ");
        int[] v = d.getV();
        boolean[] h = d.getH();
        for(int i=0; i<5; i++) System.out.print(h[i] ? "["+v[i]+"] " : v[i]+" ");
        System.out.println("\nWürfe übrig: " + d.getLeft());
        System.out.println("\n[1-5] Halten  [R] Würfeln  [ENTER] Score");
    }

    public static void drawScoreScreen(Player p, int[] v, int sel) {
        clearScreen();
        System.out.println("SCORE EINTRAGEN: " + p.getName());
        Scorecard.Cat[] cats = Scorecard.Cat.values();
        for(int i=0; i<cats.length; i++) {
            String m = (i == sel) ? "> " : "  ";
            Integer s = p.getSc().get(cats[i]);
            // Zeigt Punkte oder eine Vorschau in Klammern
            String val = (s != null) ? s.toString() : "(" + p.getSc().calc(cats[i], v) + ")";
            System.out.printf("%s%-10s: %s\n", m, cats[i], val);
        }
        System.out.println("\n[UP/DOWN] Wahl  [ENTER] Bestätigen");
    }

    public static void drawHighscoreScreen(java.util.List<String> scores) {
        clearScreen();
        System.out.println("--- TOP 5 HIGHSCORES ---");
        if (scores.isEmpty()) {
            System.out.println("\nNoch keine Einträge!");
        } else {
            for (int i = 0; i < scores.size(); i++) {
                String[] parts = scores.get(i).split(":");
                System.out.printf("%d. %-10s : %s Pkt\n", (i+1), parts[0], parts[1]);
            }
        }
        System.out.println("\n[Taste] Zurück zum Menü");
    }

    public static void drawEndScreen(String winner) {
        clearScreen();
        System.out.println("--- GAME OVER ---");
        System.out.println("Gewinner: " + winner);
        System.out.println("\n[N] NEU  [Q] HAUPTMENÜ");
    }
}
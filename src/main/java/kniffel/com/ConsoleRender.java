package kniffel.com;

import java.util.List;
import java.util.Arrays;

public class ConsoleRender {

    public static final String RST = "\033[0m";
    public static final String RED = "\033[31m";
    public static final String GRN = "\033[32m";
    public static final String YEL = "\033[33m";
    public static final String CYA = "\033[36m";
    public static final String WHT = "\033[37m";
    public static final String GRY = "\033[90m";

    private static final String[] CAT_NAMES = {
        "Eins", "Zwei", "Drei", "Vier", "Fünf", "Sechs",
        "3er Pasch", "4er Pasch", "Full House  ", "kl. Straße", "gr. Straße", "Kniffel", "Chance"
    };

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void moveCursor(int row, int col) {
        // Die Koordinaten sind nun so gesetzt, dass row 1 und col 1 immer leer bleiben
        System.out.print("\033[" + row + ";" + col + "H");
    }

    public static void typeText(String color, String text, int speed) throws InterruptedException {
        System.out.print(color);
        for (char c : text.toCharArray()) {
            System.out.print(c);
            System.out.flush();
            Thread.sleep(speed);
        }
        System.out.print(RST);
    }

    private static void drawMainFrame() {
        clearScreen();
        // Startet bei Zeile 2, Spalte 2
        moveCursor(2, 2);
        System.out.print(CYA + "╔" + "═".repeat(100) + "╗" + RST);
        for(int i=0; i<23; i++) {
            moveCursor(3 + i, 2);
            System.out.print(CYA + "║" + " ".repeat(100) + "║" + RST);
        }
        moveCursor(26, 2);
        System.out.print(CYA + "╚" + "═".repeat(100) + "╝" + RST);
    }

    public static void drawStartScreen(boolean animate) throws InterruptedException {
        drawMainFrame();
        String[] title = {
            "██╗  ██╗███╗  ██╗██╗███████╗███████╗███████╗██╗",
            "██║ ██╔╝████╗ ██║██║██╔════╝██╔════╝██╔════╝██║",
            "█████╔╝ ██╔██╗██║██║█████╗  █████╗  █████╗  ██║",
            "██╔═██╗ ██║╚████║██║██╔══╝  ██╔══╝  ██╔══╝  ██║",
            "██║ ╚██╗██║ ╚███║██║██║     ██║     ███████╗███████╗",
            "╚═╝  ╚═╝╚═╝  ╚══╝╚═╝╚═╝     ╚═╝     ╚══════╝╚══════╝"
        };
        for(int i=0; i<title.length; i++) {
            moveCursor(5 + i, 30); // Vorher 4, 29
            if(animate) typeText(YEL, title[i], 2); else System.out.print(YEL + title[i] + RST);
        }

        moveCursor(13, 33); // Vorher 12, 32
        if(animate) { 
            Thread.sleep(500);
            typeText(WHT, "T  E  R  M  I  N  A  L  ", 40); 
            Thread.sleep(300);
            typeText(WHT, "  E  D  I  T  I  O  N", 40);
            Thread.sleep(500);
        } else System.out.print(WHT + "T  E  R  M  I  N  A  L    E  D  I  T  I  O  N" + RST);
        
        moveCursor(17, 44); if(animate) typeText(GRN, "[S] SPIELEN", 15); else System.out.print(GRN + "[S] SPIELEN" + RST);
        moveCursor(19, 44); if(animate) typeText(YEL, "[H] HIGHSCORES", 15); else System.out.print(YEL + "[H] HIGHSCORES" + RST);
        moveCursor(21, 44); if(animate) typeText(RED, "[Q] BEENDEN", 15); else System.out.print(RED + "[Q] BEENDEN" + RST);
        
        moveCursor(25, 36); // Vorher 24, 35
        if(animate) {
            Thread.sleep(300);
            typeText(GRY, "Developed by Adrian Pinggera | v1.0" + RST, 30);
        } else System.out.print(GRY + "Developed by Adrian Pinggera | v1.0" + RST);
        moveCursor(26, 1); System.out.flush();
    }

    public static void drawSetupFrame() {
        drawMainFrame();
        moveCursor(3, 41); System.out.print(WHT + "S P I E L E R   S E T U P" + RST);
        moveCursor(4, 2); System.out.print(CYA + "╠" + "═".repeat(100) + "╣" + RST);
        moveCursor(9, 7); System.out.print(GRY + "────────────────────────────────────────────────────" + RST);
        moveCursor(24, 2); System.out.print(CYA + "╠" + "═".repeat(100) + "╣" + RST);
        moveCursor(25, 5); System.out.print(YEL + "[1-4] SPIELERZAHL");
        moveCursor(25, 32); System.out.print("[↑↓] NAVIGIEREN");
        moveCursor(25, 58); System.out.print(WHT + "[ENTER] WEITER");
        moveCursor(25, 86); System.out.print(GRN + "[ESC] ZURÜCK" + RST);
    }

    public static void updateSetup(int count, int field, String[] names) {
        moveCursor(7, 7); // Vorher 6, 6
        System.out.print(WHT + "Anzahl Spieler:   ");
        for(int i=1; i<=4; i++) {
            if(i == count) System.out.print(YEL + "[ " + i + " ]  " + RST);
            else System.out.print(WHT + "[ " + i + " ]  " + RST);
        }
        System.out.print("   "); 

        for (int i=0; i<4; i++) {
            moveCursor(11 + i*2, 7); // Vorher 10 + i*2, 6
            String col = (i < count) ? WHT : GRY;
            String mark = (i == field) ? "> " : "  "; 
            String n = names[i].isEmpty() ? "___" : names[i];
            if (i == field && i < count) n += "█";
            
            System.out.print(col + "Spieler " + (i+1) + ":  " + mark + String.format("%-11s", n) + RST);
        }
        moveCursor(26, 1); System.out.flush();
    }

    public static void drawDashboardFrame(boolean isScoreMenu) {
        clearScreen();
        moveCursor(2, 2);
        System.out.print(CYA + "╔" + "═".repeat(42) + " K N I F F E L " + "═".repeat(43) + "╗" + RST);
        for(int i=0; i<22; i++) {
            moveCursor(3+i, 2);
            if(i == 0)
                System.out.print(CYA + "║" + " ".repeat(100) + "║" + RST);
            else if(i == 1) {
                System.out.print(CYA + "╠" + "═".repeat(32) + "╦" + "═".repeat(67) + "╣" + RST);
            } else if(i == 21) {
                System.out.print(CYA + "╠" + "═".repeat(32) + "╩" + "═".repeat(67) + "╣" + RST);
            } else {
                System.out.print(CYA + "║" + " ".repeat(32) + "║" + " ".repeat(67) + "║" + RST);
            }
        }
        moveCursor(25, 2);
        System.out.print(CYA + "║" + " ".repeat(100) + "║" + RST);
        moveCursor(26, 2);
        System.out.print(CYA + "╚" + "═".repeat(100) + "╝" + RST);

        moveCursor(6, 4); System.out.print(GRY + "────────────────────────────" + RST);
        moveCursor(13, 4); System.out.print(GRY + "─────────────────────────────" + RST);
        moveCursor(21, 4); System.out.print(GRY + "─────────────────────────────" + RST);

        if (isScoreMenu) {
            moveCursor(25, 5); System.out.print(WHT + "[↑↓] NAVIGIEREN");
            moveCursor(25, 43); System.out.print("[ENTER] EINTRAGEN");
            moveCursor(25, 79); System.out.print("[ESC] ZURÜCK" + RST);
        } else {
            moveCursor(25, 5); System.out.print(WHT + "[1-5] HALTEN");
            moveCursor(25, 27); System.out.print("[R] WÜRFELN");
            moveCursor(25, 49); System.out.print("[ENTER] KATEGORIE WÄHLEN");
            moveCursor(25, 86); System.out.print("[ESC] ZURÜCK" + RST);
        }
    }

    public static void updateDashboard(Player[] players, int active, int round, DiceController d, boolean isScoreMenu, int selIdx) {
        moveCursor(3, 5); // Vorher 2, 4
        System.out.print(WHT + "Runde " + round + "/13" + RST);
        String rightHead = "[ WÜRFE ÜBRIG: " + d.getLeft() + " ]";
        moveCursor(3, 102 - rightHead.length()); // Vorher 101...
        System.out.print(YEL + rightHead + RST);

        moveCursor(5, 4); System.out.print(WHT + "Kat." + RST);
        for(int i=0; i<players.length; i++) {
            String shortName = players[i].getName().length() > 2 ? players[i].getName().substring(0,2) : players[i].getName();
            if(i == active) shortName = CYA + shortName + RST;
            moveCursor(5, 15 + i*5); System.out.print(shortName + "  ");
        }

        Scorecard.Cat[] cats = Scorecard.Cat.values();
        int row = 7;
        for (int c = 0; c < cats.length; c++) {
            if(c == 6) row++; 
            moveCursor(row, 4); System.out.print(WHT + String.format("%-7s", CAT_NAMES[c]) + RST);
            for(int i=0; i<players.length; i++) {
                Integer s = players[i].getSc().get(cats[c]);
                String val = (s != null) ? s.toString() : "-";
                moveCursor(row, 15 + i*5); System.out.printf(WHT + "%2s" + RST, val);
            }
            row++;
        }
        
        moveCursor(22, 4); System.out.print(YEL + "Gesamt" + RST);
        for(int i=0; i<players.length; i++) {
            String col = (i == active) ? YEL : WHT;
            moveCursor(22, 13 + i*5); System.out.print(col + String.format("%2d", players[i].getSc().total()) + RST);
        }

        if (isScoreMenu) updateScoreRightPane(players[active], d.getV(), selIdx);
        else updateDiceRightPane(d);
        
        moveCursor(26, 1); System.out.flush();
    }

    private static void updateDiceRightPane(DiceController d) {
        int[] v = d.getV();
        boolean[] h = d.getH();
        for(int i=0; i<5; i++) {
            String[] face = getDiceFace(v[i], h[i]);
            for(int line=0; line<5; line++) {
                moveCursor(10 + line, 41 + i*11); // Vorher 9 + line, 40 + i*11
                System.out.print(face[line]);
            }
        }
        StringBuilder held = new StringBuilder("Gehalten: ");
        for(int i=0; i<5; i++) if(h[i]) held.append("[").append(i+1).append("] ");
        moveCursor(17, 41); System.out.print(YEL + String.format("%-30s", held.toString()) + RST);
    }

    private static void updateScoreRightPane(Player p, int[] v, int selIdx) {
        moveCursor(6, 41); System.out.print(WHT + "KATEGORIE WÄHLEN  —  Würfel: " + v[0]+" "+v[1]+" "+v[2]+" "+v[3]+" "+v[4]);
        moveCursor(7, 41); System.out.print(GRY + "──────────────────────────────────────────────────────" + RST);
        
        Scorecard.Cat[] cats = Scorecard.Cat.values();
        int row = 9;
        for(int i=0; i<cats.length; i++) {
            if(i == 6) row++;
            moveCursor(row, 41);
            String mark = (i == selIdx) ? "> " : "  "; 
            String col = (i == selIdx) ? CYA : WHT;
            Integer s = p.getSc().get(cats[i]);
            String val = (s != null) ? GRN + String.format("%3d", s) + RST : GRY + "(" + String.format("%2d", p.getSc().calc(cats[i], v)) + ")" + RST;
            
            System.out.printf("%s%s%-12s %s.............................. %5s  " + RST, col, mark, CAT_NAMES[i], GRY, val);
            row++;
        }
    }

    private static String[] getDiceFace(int val, boolean held) {
        String c = held ? GRN : WHT;
        String[] top = {"       ", " ●     ", " ●     ", " ●   ● ", " ●   ● ", " ●   ● "};
        String[] mid = {"   ●   ", "       ", "   ●   ", "       ", "   ●   ", " ●   ● "};
        String[] bot = {"       ", "     ● ", "     ● ", " ●   ● ", " ●   ● ", " ●   ● "};
        int i = val - 1;
        return new String[]{
            c + "╭───────╮" + RST, 
            c + "│" + top[i] + "│" + RST, 
            c + "│" + mid[i] + "│" + RST, 
            c + "│" + bot[i] + "│" + RST, 
            c + "╰───────╯" + RST
        };
    }

    public static void animateDice(DiceController d) throws InterruptedException {
        for(int k=0; k<6; k++) {
            int[] tempV = d.getV().clone();
            for(int i=0; i<5; i++) if(!d.getH()[i]) tempV[i] = (int)(Math.random()*6)+1;
            
            for(int i=0; i<5; i++) {
                String[] face = getDiceFace(tempV[i], d.getH()[i]);
                for(int line=0; line<5; line++) {
                    moveCursor(10 + line, 41 + i*11); 
                    System.out.print(face[line]);
                }
            }
            moveCursor(26, 1); System.out.flush();
            Thread.sleep(70);
        }
    }

    public static void drawEndScreen(Player[] players) {
        drawMainFrame();
        moveCursor(3, 41); System.out.print(YEL + "★  S P I E L   E N D E  ★" + RST);
        moveCursor(4, 2); System.out.print(CYA + "╠" + "═".repeat(100) + "╣" + RST);

        List<Player> sorted = Arrays.asList(players);
        sorted.sort((a,b) -> b.getSc().total() - a.getSc().total());

        for(int i=0; i<sorted.size(); i++) {
            moveCursor(9 + i*2, 36);
            System.out.printf(WHT + "%d. %-15s ..... %3d Punkte" + RST, (i+1), sorted.get(i).getName(), sorted.get(i).getSc().total());
        }

        moveCursor(24, 2); System.out.print(CYA + "╠" + "═".repeat(100) + "╣" + RST);
        moveCursor(25, 26); System.out.print(WHT + "[N] NOCHMAL SPIELEN");
        moveCursor(25, 61); System.out.print("[ESC] ZURÜCK" + RST);
        moveCursor(26, 1); System.out.flush();
    }

    public static void drawHighscoreScreen(List<String> scores) {
        drawMainFrame();
        moveCursor(3, 39); System.out.print(YEL + "★  HIGHSCORE TOP 5  ★" + RST);
        moveCursor(4, 2); System.out.print(CYA + "╠" + "═".repeat(100) + "╣" + RST);

        if(scores.isEmpty()) {
            moveCursor(11, 41); System.out.print(WHT + "Noch keine Einträge!" + RST);
        } else {
            for(int i=0; i<scores.size(); i++) {
                String[] parts = scores.get(i).split(":");
                moveCursor(9 + i*2, 33);
                System.out.printf(WHT + "%d.  %-15s %5s Pkt" + RST, (i+1), parts[0], parts[1]);
            }
        }

        moveCursor(24, 2); System.out.print(CYA + "╠" + "═".repeat(100) + "╣" + RST);
        moveCursor(25, 38); System.out.print(WHT + "[Belibige Taste] ZURÜCK" + RST);
        moveCursor(26, 1); System.out.flush();
    }
}
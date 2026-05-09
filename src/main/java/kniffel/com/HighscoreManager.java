package kniffel.com;

import java.io.*;
import java.util.*;

public class HighscoreManager {
    
    private static final String PATH = "data/highscores.txt";

    public static void save(String name, int score) {
        try {
            // Ordner erstellen, falls er fehlt
            File dir = new File("data");
            if (!dir.exists()) dir.mkdir();

            PrintWriter pw = new PrintWriter(new FileWriter(PATH, true));
            pw.println(name + ":" + score);
            pw.close();
        } catch (IOException e) {
        }
    }

    public static List<String> getTop() {
        List<String> list = new ArrayList<>();
        File file = new File(PATH);
        
        if (!file.exists()) return list;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    list.add(line);
                }
            }
        } catch (IOException e) {
            return list;
        }

        // Sortieren
        list.sort((a, b) -> {
            try {
                int s1 = Integer.parseInt(a.split(":")[1]);
                int s2 = Integer.parseInt(b.split(":")[1]);
                return Integer.compare(s2, s1);
            } catch (Exception e) {
                return 0;
            }
        });

        // Top 5 
        if (list.size() > 5) return list.subList(0, 5);
        return list;
    }
}
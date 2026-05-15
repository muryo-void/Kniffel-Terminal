package kniffel.com;

import java.io.*;
import java.util.*;

public class HighscoreManager {

    private static final String PATH = "data/highscores.txt";

    public static void save(String name, int score, String date) {
        try {
            File dir = new File("data");
            if (!dir.exists()) dir.mkdir();
            PrintWriter pw = new PrintWriter(new FileWriter(PATH, true));
            pw.print("\n" + name + ":" + score + ":" + date);
            pw.close();
        } catch (IOException e) { }
    }

    public static void save(String name, int score) {
        save(name, score, "");
    }

    public static List<String> getTop() {
        List<String> list = new ArrayList<>();
        File file = new File(PATH);
        if (!file.exists()) return list;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null)
                if (!line.trim().isEmpty()) list.add(line);
        } catch (IOException e) {
            return list;
        }

        list.sort((a, b) -> {
            try {
                return Integer.compare(Integer.parseInt(b.split(":")[1]), Integer.parseInt(a.split(":")[1]));
            } catch (Exception e) { return 0; }
        });

        return list.size() > 5 ? list.subList(0, 5) : list;
    }
}

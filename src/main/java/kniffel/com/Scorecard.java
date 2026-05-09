package kniffel.com;
import java.util.*;

public class Scorecard {
    public enum Cat { 
        EINS, ZWEI, DREI, VIER, FUNF, SECHS, 
        PASCH3, PASCH4, FULLH, STRKL, STRGR, KNIFFEL, CHANCE 
    }
    private Map<Cat, Integer> map = new HashMap<>();

    public int calc(Cat c, int[] dice) {
        int[] cnt = new int[7];
        int sum = 0;
        for (int d : dice) { cnt[d]++; sum += d; }

        return switch (c) {
            case EINS, ZWEI, DREI, VIER, FUNF, SECHS -> cnt[c.ordinal()+1] * (c.ordinal()+1);
            case PASCH3 -> checkP(cnt, 3) ? sum : 0;
            case PASCH4 -> checkP(cnt, 4) ? sum : 0;
            case FULLH -> (checkP(cnt, 3) && checkP(cnt, 2)) ? 25 : 0;
            case STRKL -> checkS(cnt, 4) ? 30 : 0;
            case STRGR -> checkS(cnt, 5) ? 40 : 0;
            case KNIFFEL -> checkP(cnt, 5) ? 50 : 0;
            case CHANCE -> sum;
        };
    }

    private boolean checkP(int[] c, int n) {
        for (int x : c) if (x >= n) return true;
        return false;
    }

    private boolean checkS(int[] c, int len) {
        int s = 0;
        for (int i = 1; i <= 6; i++) {
            if (c[i] > 0) s++; else s = 0;
            if (s >= len) return true;
        }
        return false;
    }

    public void set(Cat c, int v) { map.put(c, v); }
    public boolean has(Cat c) { return map.containsKey(c); }
    public Integer get(Cat c) { return map.get(c); }
}
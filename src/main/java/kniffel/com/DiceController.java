package kniffel.com;

public class DiceController {
    private int[] v = new int[5]; // Würfelwerte
    private boolean[] h = new boolean[5]; // "Held" (festgehalten)
    private int left = 3; // Würfe übrig

    public DiceController() { reset(); }

    public void reset() {
        for (int i = 0; i < 5; i++) { v[i] = 1; h[i] = false; }
        left = 3;
    }

    public void roll() {
        if (left > 0) {
            for (int i = 0; i < 5; i++) {
                if (!h[i]) v[i] = (int)(Math.random() * 6) + 1;
            }
            left--;
        }
    }

    public void toggle(int i) { if (i >= 0 && i < 5) h[i] = !h[i]; }
    public int[] getV() { return v; }
    public boolean[] getH() { return h; }
    public int getLeft() { return left; }
}
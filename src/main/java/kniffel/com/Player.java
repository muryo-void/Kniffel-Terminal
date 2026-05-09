package kniffel.com;

public class Player {
    private String name;
    private Scorecard sc;

    public Player(String name) { 
        this.name = name; 
        this.sc = new Scorecard(); 
    }
    public String getName() { return name; }
    public Scorecard getSc() { return sc; }
}
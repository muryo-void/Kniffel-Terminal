package kniffel.com;

public class kniffel {
    public static void main(String[] args) throws InterruptedException {
        // Terminal Setup
        System.out.print("\033[8;40;102t\033[?25l"); 
        System.out.flush();
        
        // Cursor wieder an
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.print("\033[?25h");
            System.out.flush();
        }));

        GameController controller = new GameController();
        controller.startGame();
    }
}
package kniffel.com;

public class kniffel {
    public static void main(String[] args) throws InterruptedException {
        System.out.print("\033[3m\033[?25l");
        System.out.flush();

        Thread.sleep(150);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.print("\033[?25h");
            System.out.flush();
        }));

        GameController controller = new GameController();
        controller.startGame();
    }
}

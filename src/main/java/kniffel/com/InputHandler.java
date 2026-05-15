package kniffel.com;

import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.NonBlockingReader;
import java.io.IOException;

public class InputHandler {
    private static Terminal terminal;
    private static NonBlockingReader reader;

    static {
        try {
            terminal = TerminalBuilder.builder().system(true).build();
            terminal.enterRawMode();
            reader = terminal.reader();
        } catch (IOException e) {
            System.err.println("Terminal konnte nicht initialisiert werden.");
        }
    }

    public static char readChar() {
        try { return (char) reader.read(); } 
        catch (IOException e) { return '\0'; }
    }

    public static String readInput() {
        try {
            int code = reader.read();
            if (code == -1) return "";

            // Wenn es ein Escape-Zeichen ist (27)
            if (code == 27) {
                // Schau kurz nach, ob weitere Zeichen folgen (Pfeiltasten-Sequenz)
                int next1 = reader.read(10L); // 10ms warten
                int next2 = reader.read(10L);
            
                if (next1 == '[') {
                    return switch (next2) {
                        case 'A' -> "UP";
                        case 'B' -> "DOWN";
                        case 'C' -> "RIGHT";
                        case 'D' -> "LEFT";
                        default -> "ESC_SEQ"; 
                    };
                }
                return "ESC"; // Nur die ESC-Taste gedrückt
            }

            return String.valueOf((char) code);
        } catch (IOException e) {
            return "";
        }
    }
    

    public static char peekChar() {
        try {
            int code = reader.read(1L); 
            if (code >= 0) return (char) code;
            return '\0'; 
        } catch (IOException e) { return '\0'; }
    }

    public static void close() {
        try { if (terminal != null) terminal.close(); } 
        catch (IOException e) {}
    }
}
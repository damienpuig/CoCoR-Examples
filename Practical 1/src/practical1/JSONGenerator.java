package practical1;

import java.io.FileNotFoundException;
import java.io.PrintStream;

/**
 * @author Damien
 */

// Generate a the binary tree in a JSON format
public class JSONGenerator {

    PrintStream s;

    public JSONGenerator(String fileName) throws FileNotFoundException {
        s = new PrintStream(fileName);
    }

    /*-- Methods used in grammar.atg to print nodes --*/
    public void PrintOpenJson() {
        s.print("{");
    }

    public void PrintCloseJson() {
        s.print("}");
    }

    public void PrintChildInit() {
        s.print(", \"partners\":\" [");
    }

    public void PrintNode(String name) {
        s.print("\"city\": \"" + name + "\"");
    }

    public void PrintChildClose() {
        s.print("]");
    }
}

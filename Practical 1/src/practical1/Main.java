/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package practical1;

import java.io.FileNotFoundException;

/**
 * @author Damien
 */

public class Main {

    public static void main(String[] args) {
        // Read command-line arguments
        String inFileName = args[0];
        String outFileName = args[1];

        // Create and initialize scanner and parser
        Scanner scanner = new Scanner(inFileName);
        Parser parser = new Parser(scanner);
        try {
            parser.Jg = new JSONGenerator(outFileName);
            // Start the parser
            parser.Parse();
        } catch (FileNotFoundException e) {
            System.out.println("-- file " + outFileName + " not found");
        }
    }
}

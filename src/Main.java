import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        while (true) {
            System.out.print("Enter string: ");
            Scanner in = new Scanner(System.in);
            PrecedenceGrammar precedenceGrammar = new PrecedenceGrammar(in.nextLine() + "#");
            System.out.println(precedenceGrammar.isValid());
        }
    }
}

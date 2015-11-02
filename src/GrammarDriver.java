import java.io.IOException;

/**
 * Created by jmitch on 10/20/2015.
 */
public class GrammarDriver {

    public static void main(String[] args){

        Grammar grammar1 = null;
        Grammar grammar2 = null;

        try {
            grammar1 = new Grammar("grammar_1.txt");
            grammar2 = new Grammar("grammar_2.txt");
        } catch (IOException e){
            System.out.println("Error parsing in grammar: "+e.toString());
            return;
        }

        System.out.println("Grammar 1: ");
        System.out.println("FIRST SETS: ");
        System.out.println(grammar1.getFirstSetsAsString());

        System.out.println("Grammar 1: ");
        System.out.println("FOLLOW SETS: ");
        System.out.println(grammar1.getFollowSetsAsString());

        System.out.println("Grammar 2: ");
        System.out.println("FIRST SETS: ");
        System.out.println(grammar2.getFirstSetsAsString());

        System.out.println("Grammar 2: ");
        System.out.println("FOLLOW SETS: ");
        System.out.println(grammar2.getFollowSetsAsString());


    }

}

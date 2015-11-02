import java.io.IOException;

/**
 * Created by jmitch on 10/20/2015.
 */
public class GrammarDriver {

    public static void main(String[] args){
        if(args==null || args[0] == null){
            System.out.println("Syntax Error: Expected File Name as argument.");
            return;
        }

        Grammar grammar1 = null;
        Grammar grammar2 = null;

        try {
            grammar1 = new Grammar(args[0]);
        } catch (IOException e){
            System.out.println("Error parsing in grammar: "+e.toString());
            return;
        }

        //System.out.println(grammar.toString());
        System.out.println("FIRST SETS: ");
        System.out.println(grammar1.getFirstSetsAsString());

        System.out.println("FOLLOW SETS: ");
        System.out.println(grammar1.getFollowSetsAsString());
       // System.out.println(grammar.reportRulesLinks());


    }

}

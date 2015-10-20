/**
 * Created by jmitch on 10/20/2015.
 */
public class GrammarDriver {

    public static void main(String[] args){
        if(args==null || args[0] == null){
            System.out.println("Syntax Error: Expected File Name as argument.");
            return;
        }
        Grammar grammar = Grammar.instantiate(args[0]);
        System.out.println(grammar.toString());


    }

}

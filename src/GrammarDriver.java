/**
 * Created by jmitch on 10/20/2015.
 */
public class GrammarDriver {

    public static void main(String[] args){
        /*if(args==null || args[0] == null){
            System.out.println("Syntax Error: Expected File Name as argument.");
            return;
        }*/
        Grammar.instantiate("grammar_1.txt");
    }

}

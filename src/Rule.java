import java.util.LinkedList;
import java.util.Set;

/**
 * Created by jmitch on 10/20/2015.
 */
public class Rule {

    private String ruleName;
    private LinkedList<String> ruleParams;



    private Set<String> firstSet;
    private Set<String> followSet;

    public Rule(String parsedLine){

       // System.out.println(parsedLine);
        ruleName = parsedLine.substring(0,parsedLine.indexOf("-")-1);

    }

    public String getRuleName() {
        return ruleName;
    }

    public LinkedList<String> getRuleParams() {
        return ruleParams;
    }

    public Set<String> getFirstSet() {
        return firstSet;
    }

    public Set<String> getFollowSet() {
        return followSet;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        return ruleName;




        //return builder.toString();
    }
}

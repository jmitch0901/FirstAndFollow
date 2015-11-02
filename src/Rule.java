
import java.util.*;

/**
 * Created by jmitch on 10/20/2015.
 */
public class Rule {

    private Symbol ruleName;
    private List<Symbol> ruleResult;

    private Set<String> firstSet;

    protected static String startingSymbol = null;

    private Set<String> followSet;

    public Rule(String parsedLine){

        ruleResult = new LinkedList<Symbol>();
        followSet = new HashSet<String>();

        StringTokenizer tokenizer = new StringTokenizer(parsedLine," >",false);
        ruleName = new Symbol(tokenizer.nextToken());


        while (tokenizer.hasMoreTokens()){
            ruleResult.add(new Symbol(tokenizer.nextToken()));
        }

        firstSet = new HashSet<String>();


        if(startingSymbol==null){
            startingSymbol = ruleName.getSymbol();
        }

        if(startingSymbol.equalsIgnoreCase(ruleName.getSymbol())){
            followSet.add("$");
        }
    }

    public Set<String> getFollow(){
        followSet.remove(Symbol.LAMBA);
        return followSet;
    }

    public void setFollow(Set<String> follow){
        followSet.addAll(follow);
    }


    public Symbol getStartSymbol(){
        return ruleName;
    }

    public List<Symbol> getRuleResult(){
        return ruleResult;
    }


    public String getRuleName() {
        return ruleName.getSymbol();
    }

    public String getRuleReport(){
        StringBuilder b = new StringBuilder();
        for(Symbol s : ruleResult){
            b.append("\t" + s.getRulesLinksReport() + "\n");
        }

        return b.toString();
    }



    public Set<String> getFirst(){

        if(ruleName.equals(ruleResult.get(0))){ //To eliminate left-hand recursion
            return firstSet;
        }

        firstSet = ruleResult.get(0).getFirst();
        return firstSet;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append(ruleName.getSymbol()+" -> ");
        for(Symbol s : ruleResult){
            builder.append(s.getSymbol()+" ");
        }

        return builder.toString();
    }

    /**
     * A class that represents a terminal/non-terminal symbol.
     *
     */
    protected class Symbol{

        public static final String LAMBA = "_LAMBDA_";

        private String symbol;
        private boolean isTerminal;

        private List<Rule> linkedRules;

        public Symbol(String symbol){
            this.symbol=symbol;
            isTerminal = true;

            for(char c : symbol.toCharArray()){
                isTerminal &= !(Character.isUpperCase(c));
            }

        }

        public Set<String> getFirst(){

            Set<String> s = new HashSet<String>();

            if(isTerminal){
                s.add(symbol);
                return s;
            }

            if(symbol.equalsIgnoreCase(LAMBA)) {
                s.add(LAMBA);
                return s;
            }

            for(Rule r : linkedRules){
                s.addAll(r.getFirst());
            }

            return s;
        }


        public void linkRule(List<Rule> r){
            this.linkedRules=r;
        }

        public String getSymbol() {
            return symbol;
        }

        public boolean isTerminal() {
            return isTerminal;
        }

        public String getRulesLinksReport(){
            if(isTerminal)
                return "N/A";

            StringBuilder builder = new StringBuilder();
            builder.append("SYMBOL "+symbol+" goes to:\n ");
            if(linkedRules!=null) {
                for (Rule r : linkedRules) {
                    builder.append("\t\t" + r.toString() + "\n");
                }
            }

            return builder.toString();
        }

        public boolean equals(Symbol s){
            return this.symbol.equals(s.symbol);
        }

        @Override
        public String toString() {
            //System.out.println();
            return symbol+" isTerminal? :"+isTerminal;
        }
    }
}

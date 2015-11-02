

import java.util.*;

/**
 * Created by jmitch on 10/20/2015.
 */
public class Rule {

    private Symbol ruleName;
    private List<Symbol> ruleResult;

    private Set<String> firstSet;
    private Set<String> followSet;

    protected static String startingSymbol = null;

    public Rule(String parsedLine){

        ruleResult = new LinkedList<>();

        StringTokenizer tokenizer = new StringTokenizer(parsedLine," >",false);
        ruleName = new Symbol(tokenizer.nextToken());



        while (tokenizer.hasMoreTokens()){
            ruleResult.add(new Symbol(tokenizer.nextToken()));
        }

        firstSet = new HashSet<>();


        if(startingSymbol==null){
            startingSymbol = ruleName.getSymbol();
        }

        if(startingSymbol.equalsIgnoreCase(ruleName.getSymbol())){
            followSet = new HashSet<>();
            followSet.add("$");
        }
    }

    public List<Symbol> getSymbolList(){
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

    public Set<String> getFollow(){
        return followSet;
    }

    public Set<String> getFirst(){

        if(ruleName.equals(ruleResult.get(0))){ //To eliminate left-hand recursion
            return firstSet;
        }

        firstSet = ruleResult.get(0).getFirst();
        return firstSet;
    }

    public void initFollowsForRules(Grammar.OnFollowSetUpdatedCallbacks callbacks){
        for(int i = 0; i < ruleResult.size()-1; i++){
            Symbol firstS = ruleResult.get(i);
            Symbol nextS = ruleResult.get(i+1);

            Set<String> nextFirstSet = nextS.getFirst();

            Set<String> followSet = new HashSet<>();

            if(nextFirstSet.contains(Symbol.LAMBA)){
                List<Rule> rules = nextS.getLinkedRules();
                for(Rule r : rules){
                    if(r.hasFollowSet()){
                        followSet.addAll(r.getFollow());
                    } else {
                        callbacks.onRuleNeedingUpdate(r.ruleName.getSymbol(),firstS.getSymbol());
                    }
                }
            }


            followSet.addAll(nextS.getFirst());
            followSet.remove(Symbol.LAMBA);

            callbacks.onFollowSetUpdated(firstS.getSymbol(),followSet);
        }

        Symbol endingSymbol = ruleResult.get(ruleResult.size()-1);

        if(!endingSymbol.isTerminal()){
            List<Rule> rules = endingSymbol.getLinkedRules();
            if(rules==null) return;
            for(Rule r : rules){
                if(this.hasFollowSet()){
                    callbacks.onFollowSetUpdated(endingSymbol.getSymbol(),this.getFollow());
                } else {
                    callbacks.onRuleNeedingUpdate(ruleName.getSymbol(),r.ruleName.getSymbol());
                }
            }
        }
    }

    public void addToFollowSet(Set<String> followSet){
        this.followSet = new HashSet<>();
        this.followSet.addAll(followSet);
    }

    public boolean hasFollowSet(){
        return this.followSet!=null && !this.followSet.isEmpty();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append(ruleName.getSymbol()+" -> ");
        for(Symbol s : ruleResult){
            builder.append(s.getSymbol()+" ");
        }

       // return ruleName.toString();
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

            Set<String> s = new HashSet<>();

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
            //return linkedRules.get(0).getFirst();
        }

        public List<Rule> getLinkedRules(){
            return linkedRules;
        }


        public boolean isStartingSymbol(){
            return this.symbol.equalsIgnoreCase(startingSymbol);
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




            /*if(nextSymbol.isTerminal()) {
                followSymbols.add(nextSymbol.getSymbol());
           } else {
                //DOUBLE CHECK THIS! Do I add the first for ALL symbols following????

                for(int j = i; j < ruleResult.size(); j++){
                    followSymbols.addAll(ruleResult.get(j).getFirst());
                }
                //followSymbols.addAll(nextSymbol.getFirst());
                followSymbols.remove(Symbol.LAMBA);
            }*/

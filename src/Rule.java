

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

    public Set<String> getFirst(){

        if(ruleName.equals(ruleResult.get(0))){ //To eliminate left-hand recursion
            return firstSet;
        }

        firstSet = ruleResult.get(0).getFirst();
        return firstSet;
    }


    public void findAndStoreFollows(OnFollowSetUpdatedListener listener){
        for(int i = 0; i < ruleResult.size()-1; i++){
            Symbol thisSymbol = ruleResult.get(i);
            Symbol nextSymbol = ruleResult.get(i+1);




        }

        Symbol finalSymbol = ruleResult.get(ruleResult.size()-1);

        if(finalSymbol.isTerminal()){
            return;
        }



    }

    /*public void findAndStoreFollows(OnFollowSetUpdatedListener listener){

        for(int i =0; i < ruleResult.size()-1; i++){ //-1 because the last symbol is special

            Symbol thisSymbol = ruleResult.get(i);
            Symbol nextSymbol = ruleResult.get(i+1);

            Set<String> followSymbols = new HashSet<>();

            if(thisSymbol.isStartingSymbol()){
                followSymbols.add("$");
            }


            Set<String> nextFirst = nextSymbol.getFirst();
            boolean containsLambda = nextFirst.contains(Symbol.LAMBA);

            if(containsLambda){
                //followSymbols.addAll(this.getFollow());
                followSymbols.remove(Symbol.LAMBA);
            } else {
                followSymbols.addAll(nextSymbol.getFirst());
            }
            //Tell the callbacks we have new symbols to update
            listener.onFollowSetUpdated(thisSymbol.getSymbol(),followSymbols);
        }

        //At the end, tell the callbacks that we want to contain S within C, where S -> C
        //Might be getting a bug here. Follow(A) is contained within Follow(B) when A -> B.
        //I should WAIT to execute this code until the REST of the follows have been determined.

    }*/

    public Set<String> getFollow(){
        return followSet;
    }


    public void addToFollow(Set<String> follows){
        followSet.addAll(follows);
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

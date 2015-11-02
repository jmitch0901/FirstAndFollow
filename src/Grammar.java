import java.io.*;
import java.util.*;

/**
 * Singleton Class
 * Created by jmitch on 10/20/2015.
 */
public class Grammar{

    //Map a rule BY it's rule name, followed by a list of all rules.
    private Map<String,List<Rule>> grammar;

    public Grammar(String fileName) throws IOException{

        grammar = new HashMap<String,List<Rule>>();

        InputStream is = new FileInputStream(fileName);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        String line = null;
        while((line= reader.readLine()) != null){
            //System.out.println(line);
            Rule rule = new Rule(line);
            if(grammar.get(rule.getRuleName())==null){
                grammar.put(rule.getRuleName(),new ArrayList<Rule>());
            }
            grammar.get(rule.getRuleName()).add(rule);
        }

        is.close();
        reader.close();

        linkMyRules();

    }



    public String getFirstSetsAsString(){

        StringBuilder builder = new StringBuilder();

        for(Map.Entry<String,List<Rule>> entry : grammar.entrySet()){
            Set<String> firstSet = new HashSet<String>();

            String key = entry.getKey();
            List<Rule> rules = entry.getValue();

            for(Rule r : rules){
                firstSet.addAll(r.getFirst());
            }

            builder.append(key+" -> ");
            builder.append("{");
            for(String s : firstSet){
                builder.append(s+", ");
            }

            builder.replace(builder.toString().length()-2,builder.toString().length(),"");

            builder.append("}\n");

        }

        return builder.toString();

    }

    private void mergeFollows(String rule){
        List<Rule> rules =  grammar.get(rule);

        if(rule!=null){
            Set<String> strings = new HashSet<String>();
            for(Rule r : rules){
                strings.addAll(r.getFollow());
            }

            strings.remove(Rule.Symbol.LAMBA);

            for(Rule r : rules){
                r.setFollow(strings);
            }
        }
    }


    private void initFollowSets(){
        boolean itemsNotChanging = false;

        int k = 0;

        while(k < 100) {

            //System.out.println("fdsafdsf");
            itemsNotChanging = true;

            for (Map.Entry<String, List<Rule>> entry : grammar.entrySet()) {

                String key = entry.getKey();
                List<Rule> rules = entry.getValue();

                for (Rule r : rules) {

                    Rule.Symbol A_String = r.getStartSymbol();
                    List<Rule.Symbol> Other_Strings = r.getRuleResult();

                    for (int i = 0; i < Other_Strings.size() - 1; i++) {
                        Rule.Symbol first = Other_Strings.get(i);
                        if(first.isTerminal()) continue;
                        Rule.Symbol next = Other_Strings.get(i + 1);

                        if (!next.getFirst().contains(Rule.Symbol.LAMBA)) {
                            //System.out.println(first.getSymbol());
                            grammar.get(first.getSymbol())
                                    .get(0)
                                    .getFollow()
                                    .addAll(next.getFirst());

                            mergeFollows(first.getSymbol());
                        } else {

                            grammar.get(first.getSymbol())
                                    .get(0)
                                    .getFollow()
                                    .addAll(next.getFirst());

                            mergeFollows(first.getSymbol());


                            grammar.get(first.getSymbol())
                                    .get(0)
                                    .getFollow()
                                    .addAll(r.getFollow());

                            mergeFollows(first.getSymbol());
                        }
                    }

                    Rule.Symbol endSymbol = Other_Strings.get(Other_Strings.size() - 1);

                    if (!endSymbol.isTerminal()) {

                        try {
                            grammar.get(endSymbol.getSymbol())
                                    .get(0)
                                    .getFollow()
                                    .addAll(r.getFollow());
                        } catch (Exception e){
                            continue;
                        }

                        mergeFollows(endSymbol.getSymbol());
                    }
                }
            }
            k++;
        }
    }

    public String getFollowSetsAsString(){

        initFollowSets();

        StringBuilder builder = new StringBuilder();

        for(Map.Entry<String,List<Rule>> entry : grammar.entrySet()){
            Set<String> followSet = new HashSet<String>();

            String key = entry.getKey();
            List<Rule> rules = entry.getValue();

            for(Rule r : rules){
                followSet.addAll(r.getFollow());
            }

            builder.append(key+" -> ");
            builder.append("{");
            for(String s : followSet){
                builder.append(s+", ");
            }

            if(!followSet.isEmpty()) {
                builder.replace(builder.toString().length() - 2, builder.toString().length(), "");
            }

            builder.append("}\n");

        }

        return builder.toString();

    }



    /**
     * The purpose of this method is to internally link a symbol to it's rule so it's
     * easy to retrieve the first/follow sets.
     */
    private void linkMyRules(){
        //for every rule
        for(Map.Entry<String,List<Rule>> entry : grammar.entrySet()){
            String key = entry.getKey();
            List<Rule> rules = entry.getValue();

            //for ever expression of given rule
            for(Rule r : rules){

                List<Rule.Symbol> symbols = r.getRuleResult();

                //Take each symbol, and link the appropriate rule
                for(Rule.Symbol s : symbols){
                    s.linkRule(grammar.get(s.getSymbol()));
                }
            }
        }
    }


    /**
     * For debugging purposes.
     * @return The linkage of the internal symbols to it's corresponding rule.
     */
    public String reportRulesLinks(){
        StringBuilder builder = new StringBuilder();
        int i = 1;
        for(Map.Entry<String,List<Rule>> entry : grammar.entrySet()){
            String key = entry.getKey();
            List<Rule> rules = entry.getValue();

            for(Rule r : rules) {

                builder.append("RULE " + i + ": "+r.toString()+ "\n" +r.getRuleReport()+"\n");
                i++;
            }
        }

        return builder.toString();

    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for(Map.Entry<String,List<Rule>> entry : grammar.entrySet()){
            String key = entry.getKey();
            List<Rule> rules = entry.getValue();

            for(int i = 0; i < rules.size(); i++){

                builder.append(rules.get(i).toString());
                builder.append("\n");

            }

        }

        return builder.toString();
    }


}

import java.io.*;
import java.util.*;

/**
 * Singleton Class
 * Created by jmitch on 10/20/2015.
 */
public class Grammar {

    //Map a rule BY it's rule name, followed by a list of all rules.
    private Map<String,List<Rule>> grammar;

    private static Grammar instance;

    private Grammar(String fileName) throws IOException{

        grammar = new HashMap<>();

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

    public static Grammar instantiate(String fileName){

        try{
            instance = new Grammar(fileName);
        } catch (FileNotFoundException e){
            instance=null;
            System.out.println("File could not be found: "+fileName+". :"+e.toString());
        } catch (IOException e){
            instance=null;
            System.out.println("ERROR: There was an IO Exception building the singleton: "+e.toString());
        }

        return instance;
    }

    public static Grammar getInstance() {
        if(instance==null){
            System.out.println("ERROR, you need to instantiate the grammar from a fileName before getting the instance.");
        }
        return instance;
    }

    public String getFirstSetsAsString(){

        StringBuilder builder = new StringBuilder();




        for(Map.Entry<String,List<Rule>> entry : grammar.entrySet()){
            Set<String> firstSet = new HashSet<>();

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

    public String getFollowSetsAsString(){
        return "";
    }

    private void linkMyRules(){

        //for every rule
        for(Map.Entry<String,List<Rule>> entry : grammar.entrySet()){
            String key = entry.getKey();
            List<Rule> rules = entry.getValue();


            //for ever expression of given rule
            for(Rule r : rules){

                List<Rule.Symbol> symbols = r.getSymbolList();

                //Take each symbol, and link the appropriate rule
                for(Rule.Symbol s : symbols){
                    s.linkRule(grammar.get(s.getSymbol()));
                }

            }

        }
    }

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

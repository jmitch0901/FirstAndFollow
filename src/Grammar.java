import com.sun.istack.internal.Nullable;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jmitch on 10/20/2015.
 */
public class Grammar {

    //Map a rule BY it's rule name, followed by a list of all rules.
    private Map<String,List<Rule>> grammar;

    private static Grammar instance;

    private Grammar(String fileName) throws IOException{

        grammar = new HashMap<>();


        BufferedReader reader = new BufferedReader(new FileReader(fileName));




        String line = null;
        while((line= reader.readLine()) !=null){

            Rule rule = new Rule(line);
            if(grammar.get(rule.getRuleName())==null){
                grammar.put(rule.getRuleName(),new ArrayList<>());
            }
            grammar.get(rule.getRuleName()).add(rule);
        }

        reader.close();

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
        }  finally {
            return instance;
        }
    }

    @Nullable
    public static Grammar getInstance() {
        if(instance==null){
            System.out.println("ERROR, you need to instantiate the grammar from a fileName before getting the instance.");
        }
        return instance;
    }
}

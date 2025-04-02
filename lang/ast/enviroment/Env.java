package lang.ast.enviroment;

import java.util.HashMap;
import java.util.Map;
public class Env {

    private HashMap<String, Integer> m;

    public Env(){
        m = new HashMap<String,Integer>(100);
    }

    public void store(String vname, Integer value){
        m.put(vname,value);
    }

    public Integer read(String vname){
        Integer i = m.get(vname);
        if(i == null){
            throw new RuntimeException("Unknow variable " + vname);
        }
        return i;
    }
    private String repeatStr(int n, String c){
        String s= "";
        for(;n>0;n--){
            s += c;
        }
        return s;
    }

    public void dumpTable(){
        String title = "Variavel";

        System.out.println(repeatStr(6 - title.length()/2," ") +
                title +
                repeatStr(6 - title.length()/2," ") + "|  Valor");
        System.out.println(repeatStr(22,"-"));
        for(Map.Entry<String,Integer> e : m.entrySet()){
            System.out.println(e.getKey() + repeatStr(12- e.getKey().length()," ") + "| " + e.getValue());
            System.out.println(repeatStr(22,"-"));
        }

    }

}

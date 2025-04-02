package lang.ast;

import java.util.ArrayList;
import lang.ast.decl.Data;
import lang.ast.decl.FunDef;

public class Program extends Node{

   private ArrayList<FunDef> funcs;
   private ArrayList<Data> datas;


   public Program(int l, int c,ArrayList<Data> datas, ArrayList<FunDef> fs){
       super(l,c);
       this.datas = datas;
       this.funcs = fs;
   }

   public ArrayList<FunDef> getFuncs(){return funcs;}

   public void accept(NodeVisitor v){v.visit(this);}

}

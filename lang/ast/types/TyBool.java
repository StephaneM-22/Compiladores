package lang.ast.types;


import lang.ast.NodeVisitor;


public class TyBool extends LType {

      public TyBool(int line, int col){
          super(line,col);
      }

        public String getTypeName(){
            return "Bool";
        }
      
     public void accept(NodeVisitor v){v.visit(this);}

}

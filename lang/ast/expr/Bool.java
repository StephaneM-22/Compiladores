package lang.ast.expr;
import lang.ast.NodeVisitor;

public class Bool extends Exp{

      private boolean value;
      
      public Bool(int line, int col, boolean value){
           super(line,col);
           this.value = value;
      }

      public boolean getValue(){ return value;}

      @Override
      public void accept(NodeVisitor v){v.visit(this);}

      @Override
      public String toString() {
            return "Bool{" + "value=" + value + "}";
      }
}

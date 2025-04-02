package lang.ast.expr;

import lang.ast.NodeVisitor;

public class Div extends BinOp {

      public Div(int line, int col, Exp el, Exp er){
           super(line,col,el,er);

      }

      @Override
      public void accept(NodeVisitor v){v.visit(this);}
}



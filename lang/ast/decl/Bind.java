package lang.ast.decl;

import lang.ast.Node;
import lang.ast.expr.Var;
import lang.ast.types.TyChar;
import lang.ast.NodeVisitor;

public class Bind extends Node {

      private Var v;
      private TyChar t;

      public Bind(int line, int col, TyChar t, Var v){
          super(line,col);
          this.t = t;
          this.v = v;
      }

      public TyChar getType(){ return t;}
      public Var getVar(){ return v;}

      public void accept(NodeVisitor v){v.visit(this);}
}

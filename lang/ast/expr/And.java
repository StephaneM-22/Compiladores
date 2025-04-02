package lang.ast.expr;

import lang.ast.NodeVisitor;

public class And extends BinOp {

    public And(int line, int col, Exp el, Exp er){
        super(line,col,el,er);

    }

    @Override
    public void accept(NodeVisitor v){v.visit(this);}
}
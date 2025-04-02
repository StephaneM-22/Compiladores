package lang.ast.expr;

import lang.ast.NodeVisitor;

public class Different extends BinOp{
    public Different(int line, int col, Exp el, Exp er){
        super(line,col,el,er);
    }

    @Override
    public void accept(NodeVisitor v){v.visit(this);}
}

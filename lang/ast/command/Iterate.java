package lang.ast.command;

import lang.ast.NodeVisitor;
import lang.ast.decl.StmtBlock;
import lang.ast.expr.LoopCond;

public class Iterate extends Cmd {
    private final LoopCond loopCond;
    private final StmtBlock body;

    public Iterate(int line, int column, LoopCond loopCond, StmtBlock body) {
        super(line, column);
        this.loopCond = loopCond;
        this.body = body;
    }

    public LoopCond getLoopCond() {
        return loopCond;
    }

    public StmtBlock getBody() {
        return body;
    }

    @Override
    public void accept(NodeVisitor v) {
        v.visit(this);
    }

    @Override
    public String toString() {
        return "Iterate(" + loopCond + ", " + body + ")";
    }
}

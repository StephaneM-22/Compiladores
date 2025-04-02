package lang.ast.command;

import lang.ast.expr.Exp;
import lang.ast.NodeVisitor;
import lang.ast.decl.StmtBlock;

public class If extends Cmd {
    private Exp condition;
    private StmtBlock thenBlock;
    private StmtBlock elseBlock; // null se não houver 'else'

    public If(int line, int column, Exp condition, StmtBlock thenBlock, StmtBlock elseBlock) {
        super(line, column);
        this.condition = condition;
        this.thenBlock = thenBlock;
        this.elseBlock = elseBlock;
    }

    public Exp getCondition() {
        return condition;
    }

    public StmtBlock getThenBlock() {
        return thenBlock;
    }

    public StmtBlock getElseBlock() {
        return elseBlock;
    }

    @Override
    public void accept(NodeVisitor v) {
        v.visit(this);
    }

    @Override
    public String toString() {
        return "IfCmd(condition=" + condition +
               ", thenBlock=" + thenBlock +
               (elseBlock != null ? ", elseBlock=" + elseBlock : "") + ")";
    }
}
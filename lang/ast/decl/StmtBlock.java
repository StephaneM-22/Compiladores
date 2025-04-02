package lang.ast.decl;

import lang.ast.command.Cmd;
import lang.ast.NodeVisitor;

public class StmtBlock extends Cmd {
    private final Block block; // Pode ser um bloco
    private final Cmd singleCmd; // Ou um único comando

    // Construtor para um bloco
    public StmtBlock(int line, int column, Block block) {
        super(line, column);
        this.block = block;
        this.singleCmd = null;
    }

    // Construtor para um único comando
    public StmtBlock(int line, int column, Cmd singleCmd) {
        super(line, column);
        this.block = null;
        this.singleCmd = singleCmd;
    }

    public boolean isBlock() {
        return block != null;
    }

    public Block getBlock() {
        return block;
    }

    public Cmd getSingleCmd() {
        return singleCmd;
    }

    @Override
    public void accept(NodeVisitor v) {
        v.visit(this);
    }

    @Override
    public String toString() {
        return isBlock() ? "StmtBlock(" + block + ")" : "StmtBlock(" + singleCmd + ")";
    }
}

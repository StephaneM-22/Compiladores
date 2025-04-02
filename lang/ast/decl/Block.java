package lang.ast.decl;

import java.util.List;
import lang.ast.command.Cmd;
import lang.ast.Node;
import lang.ast.NodeVisitor;

public class Block extends Node { // Agora Block NÃO herda de StmtBlock
    private final List<Cmd> commands;

    public Block(int line, int col, List<Cmd> commands) {
        super(line, col);
        this.commands = commands;
    }

    public List<Cmd> getCommands() {
        return commands;
    }

    @Override
    public void accept(NodeVisitor v) {
        v.visit(this);
    }

    @Override
    public String toString() {
        return "Block{" + commands + "}";
    }
}

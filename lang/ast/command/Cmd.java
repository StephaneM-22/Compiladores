package lang.ast.command;

import lang.ast.Node;
import lang.ast.NodeVisitor;

public class Cmd extends Node {
    public Cmd(int line, int column) {
        super(line, column);
    }

    public void accept(NodeVisitor v) {
        v.visit(this);
    }
}
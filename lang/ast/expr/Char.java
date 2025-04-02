package lang.ast.expr;

import lang.ast.NodeVisitor;

public class Char extends Exp {
    public char value;

    public Char(int line, int col, char value) {
        super(line, col);
        this.value = value;
    }

    public char getValue() {
        return value;
    }

    @Override
    public void accept(NodeVisitor v) {
        v.visit(this);
    }

    @Override
    public String toString() {
        return "Char{" + "value=" + value + "}";
    }
}
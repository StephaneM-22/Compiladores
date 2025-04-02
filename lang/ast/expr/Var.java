package lang.ast.expr;

import lang.ast.NodeVisitor;

//Essa classe representa uma variável
public class Var extends Exp {

    private String name;

    public Var(int line, int col, String name) {
        super(line, col);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public void accept(NodeVisitor v) {
        v.visit(this);
    }

    @Override
    public String toString() {
        return "Var(" + name + ")";
    }
}

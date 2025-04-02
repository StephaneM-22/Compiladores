package lang.ast.expr;

// import lang.ast.types.TyId;
import lang.ast.NodeVisitor;

public class LoopCond {
    private final String varName; // Pode ser null se for apenas uma expressão
    private final Exp expression;

    // Construtor para "ID : exp"
    public LoopCond(String varName, Exp expression) {
        this.varName = varName;
        this.expression = expression;
    }

    // Construtor para apenas "exp"
    public LoopCond(Exp expression) {
        this(null, expression);
    }

    public boolean hasVar() {
        return varName != null;
    }

    public String getVarName() {
        return varName;
    }

    public Exp getExpression() {
        return expression;
    }

    public void accept(NodeVisitor v) {
        v.visit(this);
    }

    @Override
    public String toString() {
        return hasVar() ? varName + " : " + expression : expression.toString();
    }
}

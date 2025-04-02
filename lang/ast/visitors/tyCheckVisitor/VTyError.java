package lang.ast.visitors.tyCheckVisitor;

public class VTyError extends VType {

    private static VTyError instance = null;
    private VTyError() { super(CLTypes.ERR); }

    public static VTyError newError() {
        if (instance == null) {
            instance = new VTyError();
        }
        return instance;
    }

    @Override
    public boolean match(VType t) {
        return true;
    }
}

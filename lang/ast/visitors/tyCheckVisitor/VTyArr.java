package lang.ast.visitors.tyCheckVisitor;

public class VTyArr extends VType {
    private VType arg;

    public VTyArr(VType a){
        super(CLTypes.ARR);
        arg = a;
    }

    public VType getTyArg(){ return arg;}

    public boolean match(VType t){
        if (getTypeValue() == t.getTypeValue()){
            return arg.match( ((VTyArr)t).getTyArg());
        }
        return false;
    }
}

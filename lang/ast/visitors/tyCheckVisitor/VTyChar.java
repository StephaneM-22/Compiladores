package lang.ast.visitors.tyCheckVisitor;

public class VTyChar extends VType{
    private static VTyChar instance = null;
    private VTyChar(){
        super(CLTypes.CHAR);
    }

    public static VTyChar newChar(){
        if(instance == null){
            instance = new VTyChar();
        }
        return instance;
    }

    public boolean match(VType t){ return getTypeValue() == t.getTypeValue();}
}

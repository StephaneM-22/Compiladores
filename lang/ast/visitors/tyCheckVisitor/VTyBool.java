package lang.ast.visitors.tyCheckVisitor;

public class VTyBool extends VType {

     private static VTyBool instance = null;
     private VTyBool(){
        super(CLTypes.BOOL);
     }

     public static VTyBool newBool(){
         if(instance == null){
             instance = new VTyBool();
         }
         return instance;
     }

     public boolean match(VType t){ return getTypeValue() == t.getTypeValue();}
}

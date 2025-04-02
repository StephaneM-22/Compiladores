package lang.ast.visitors.tyCheckVisitor;

public class VTyFloat extends VType {

     private static VTyFloat instance = null;
     private VTyFloat(){
        super(CLTypes.FLOAT);
     }

     public static VTyFloat newFloat(){
         if(instance == null){
             instance = new VTyFloat();
         }
         return instance;
     }

     public boolean match(VType t){ return getTypeValue() == t.getTypeValue();}
}

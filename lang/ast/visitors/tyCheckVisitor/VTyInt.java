package lang.ast.visitors.tyCheckVisitor;

public class VTyInt extends VType {

     private static VTyInt instance = null;
     private VTyInt(){
        super(CLTypes.INT);
     }

     public static VTyInt newInt(){
         if(instance == null){
             instance = new VTyInt();
         }
         return instance;
     }

     public boolean match(VType t){ return getTypeValue() == t.getTypeValue();}
}

package behaviour;

import java.util.*;

import ast.Definicion;
import ast.ParComp;
//import behaviour.LeeFichero;
import ast.Sentencia;

public class CompilationContextIL {
	public final List<String> variables = new ArrayList<String>();
	public final List<ParComp> variablesTipo = new ArrayList<ParComp>();
	public final List<String> parametros = new ArrayList<String>();
	public final List<String> funciones = new ArrayList<String>();
	
	public final int maxStack;
	public final StringBuilder codeIL = new StringBuilder(); 
	
	public StringBuilder codeILFunciones = new StringBuilder(); 
	
	private int currentLabel = 0;
	
	public CompilationContextIL(Sentencia prog) {
		this(prog.freeVariables(new HashSet<String>()), prog.maxStackIL());
	}
	
	public CompilationContextIL(Definicion prog) {
		this(prog.freeVariables(new HashSet<String>()), prog.maxStackIL());
	}
	
	public CompilationContextIL(Collection<String> variables, int maxStack) {
		this.variables.addAll(variables);
		this.maxStack = maxStack;
	}
	
	/** Usar este método para obtener un número único de etiqueta a la hora de
	 *  compilar construcciones que usan saltos.
	 */
	public String newLabel() {
		return "IL_" + Integer.toString(currentLabel++, 16);
	}
	
	/** Este método se utiliza para generar el código IL y obtener como un
	 *  String.
	 */
	public static String compileIL(Sentencia prog, String funcionesIL) {
		CompilationContextIL ctx = new CompilationContextIL(prog);
		ctx.codeIL.append("// variables = "+ ctx.variables +"\n");
		ctx.codeIL.append("// maxStack =  "+ ctx.maxStack +"\n");
			
		ctx = prog.compileIL(ctx);//devuelve codeIL de funcion
		
	    String local = ".locals init (";
	    for (int i = 0; i < ctx.variablesTipo.size(); i++) {
	    	ParComp aux = ctx.variablesTipo.get(i);
	    	if (aux.getTipo().equals("entero")){
	    		local += "int32 V_" + i;	
	    	}else{
	    		if (aux.getTipo().equals("texto")){
		    		local += "string V_" + i;	
		    	}else{
		    		if (aux.getTipo().equals("boolean")){
			    		local += "bool V_" + i;	
			    	}else{
			        	if (aux.getTipo().equals("listaentero")){
				    		local += "int32[] V_" + i;	
				    	}else{
				    		if (aux.getTipo().equals("listatexto")){
					    		local += "string[] V_" + i;	
					    	}else{
					    		if (aux.getTipo().equals("listaboolean")){
						    		local += "bool[] V_" + i;	
						    	}else{
						    		
						    	}
					    	}
				    	}
			    	}
		    	}
	    	}
	    		
			if (i != ctx.variablesTipo.size()-1)
				local += ",";
		}
	    local += ")";
	   		
		String file = Archivo.generarTemplate(ctx.maxStack+"", local, ctx.codeIL.toString(),funcionesIL);
		Archivo.generarArchivo(file);
		
		return ctx.codeIL.toString();
		 
	}
	
	
	
	public static String compileIL(Definicion prog) {
		
		CompilationContextIL ctx = new CompilationContextIL(prog);
		ctx = prog.compileIL(ctx);
	
		return ctx.codeIL.toString(); 		
		 
	}
}

package behaviour;

import java.util.*;

import ast.Definicion;
//import behaviour.LeeFichero;
import ast.Sentencia;

public class CompilationContextIL {
	public final List<String> variables = new ArrayList<String>();
	public final List<String> parametros = new ArrayList<String>();
	public final List<String> funciones = new ArrayList<String>();
	
	public final int maxStack;
	public final StringBuilder codeIL = new StringBuilder(); 
	
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
	public static String compileIL(Sentencia prog) {
		CompilationContextIL ctx = new CompilationContextIL(prog);
		ctx.codeIL.append("// variables = "+ ctx.variables +"\n");
		ctx.codeIL.append("// maxStack =  "+ ctx.maxStack +"\n");
		

		prog.compileIL(ctx);
		ctx.codeIL.append("ret");
		return ctx.codeIL.toString(); 
		
		 
	}
	public static String compileIL(Definicion prog) {
		CompilationContextIL ctx = new CompilationContextIL(prog);
		ctx.codeIL.append("// variables = "+ ctx.variables +"\n");
		ctx.codeIL.append("// maxStack =  "+ ctx.maxStack +"\n");
		

		prog.compileIL(ctx);
		ctx.codeIL.append("ret");
		return ctx.codeIL.toString(); 
		
		 
	}
}

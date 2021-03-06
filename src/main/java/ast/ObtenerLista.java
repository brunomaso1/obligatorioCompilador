/**
 * Universidad Catolica - Compiladores - Obligatorio.
 */
package ast;

import java.util.Random;
import java.util.Set;

import behaviour.CompilationContextIL;

public class ObtenerLista extends Expresion {
	public final String id;
	public final Expresion posicion;

	public ObtenerLista(String id, Expresion posicion) {
		this.id = id;
		this.posicion = posicion;
	}

	@Override public String unparse() {
		return "obtener posicion "+posicion.unparse()+" "+id;
	}

	/*@Override public Object evaluate(Estado state) {
		return null;//state.get(id);
	}*/

	@Override public Set<String> freeVariables(Set<String> vars) {
		return vars;
	}

	@Override public int maxStackIL() {
		return 4;
	}

	@Override public CompilationContextIL compileIL(CompilationContextIL ctx) {
		//ldloc.0     // a
		//IL_0015:  ldc.i4.2    
		//IL_0016:  ldelem.i4   
		
		Integer index = ctx.variables.indexOf(id);
		ctx.codeIL.append("ldloc " +  index + " // "+id+"\n");
		
		ctx = posicion.compileIL(ctx);
		//ctx.codeIL.append("ldelem " +  index + "\n");
		ctx.codeIL.append("ldelem.i4" + " // "+id+"\n");
		
		
		return ctx;
	}
	
	@Override public Expresion optimization(Estado state){		
		Expresion exp = posicion.optimization(state);
		return new ObtenerLista(id, exp);
	}

	@Override public String toString() {
		return "Obtener Posicion "+posicion.unparse()+" "+id;
	}

	@Override public int hashCode() {
		int result = 1;
		result = result * 31 + (this.id == null ? 0 : this.id.hashCode());
		return result;
	}

	@Override public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		Variable other = (Variable)obj;
		return (this.id == null ? other.id == null : this.id.equals(other.id));
	}

	/*public static ObtenerLista generate(Random random, int min, int max) {
		//String id; 
		//id = ""+"abcdefghijklmnopqrstuvwxyz".charAt(random.nextInt(26));
		return null;//new Variable(id);
	}*/
	
	@Override public Object check(ChequearEstado checkstate){
		Par par = checkstate.devolverValor(id);
		if (par == null){
			Errores.exceptionList.add(new Errores("ObtenerLista lista \"" + id + "\" no definida"));
		}
		else{
			if (posicion.check(checkstate).equals("entero")){
				String aux = par.getTipo();
				return aux.replace("lista","");
			}else{
				Errores.exceptionList.add(new Errores("ObtenerLista Posicion \"" + posicion.toString() + "\" no numerica."));
			}
		}
		return checkstate;
	}
}

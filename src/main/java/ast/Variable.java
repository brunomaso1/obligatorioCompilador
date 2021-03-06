/**
 * Universidad Catolica - Compiladores - Obligatorio.
 */
package ast;

import java.util.*;

import behaviour.*;


/**
 * Representacion de las variables.
 *
 * @author Grupo_9
 * @version 0.0.1
 * @date 30 oct. 2016
 */
public class Variable extends Expresion {
	public final String id;
	
	public Variable(String id) {
		this.id = id;
	}

	@Override public String unparse() {
		return id;
	}

	/*@Override public Object evaluate(Estado state) {
		return state.get(id);
	}*/

	@Override public Set<String> freeVariables(Set<String> vars) {
		return vars;
	}

	@Override public int maxStackIL() {
		return 1;
	}

	@Override public CompilationContextIL compileIL(CompilationContextIL ctx) {
		if(ctx.parametros.contains(id)){
			Integer index = ctx.parametros.indexOf(id);
			ctx.codeIL.append("ldarg " +  index + " // "+id+"\n");
			return ctx;
		}	
		Integer index = ctx.variables.indexOf(id);
		ctx.codeIL.append("ldloc " +  index + " // "+id+"\n");
		return ctx;
	}
	
	@Override public Expresion optimization(Estado state){	
		Object obj = state.get(id);
		if(obj != null){
			if (obj instanceof Double){
				return new Numeral((Double)obj);
			}
			if (obj instanceof String){
				return new Texto((String)obj);
			}
			if (obj instanceof Boolean){
				return new ValorVerdad((Boolean)obj);
			}
		}return this;
	}

	@Override public String toString() {
		return "Variable("+ id +")";
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

	/*public static Variable generate(Random random, int min, int max) {
		String id; 
		id = ""+"abcdefghijklmnopqrstuvwxyz".charAt(random.nextInt(26));
		return new Variable(id);
	}*/
	
	@Override public Object check(ChequearEstado checkstate){
		return checkstate.devolverValor(id).getTipo();
	}
}

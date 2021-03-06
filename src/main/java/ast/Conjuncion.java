/**
 * Universidad Catolica - Compiladores - Obligatorio.
 */
package ast;

import java.util.*;

import behaviour.*;

/**
 * Representacion de conjunciones booleanas.
 * @author Grupo_9
 * @version 0.0.1
 * @date 30 oct. 2016
 */
public class Conjuncion extends Expresion {
	public final Expresion left;
	public final Expresion right;

	/**
	 * Constructor de la clase.
	 * @param left
	 * @param right
	 */
	public Conjuncion(Expresion left, Expresion right) {
		this.left = left;
		this.right = right;
	}

	@Override public String unparse() {
		return "("+ left.unparse() +" & "+ right.unparse() +")";
	}

	@Override public Set<String> freeVariables(Set<String> vars) {
		return right.freeVariables(left.freeVariables(vars));
	}

	@Override public int maxStackIL() {
		return Math.max(left.maxStackIL(), right.maxStackIL())+ 1;
	}

	@Override public CompilationContextIL compileIL(CompilationContextIL ctx) {
		ctx = left.compileIL(ctx);
		ctx = right.compileIL(ctx);
		ctx.codeIL.append("and \n");
		return ctx;
	}
	
	/**
	 * Optimizaciones:
	 * - Simplificaciones algebraicas.
	 * - Pliegue de constantes.
	 */
	@Override public Expresion optimization(Estado state){
		Expresion izq = left.optimization(state);
		Expresion der = right.optimization(state);
		if(izq instanceof Expresion && ((ValorVerdad)der).value == false){
			return new ValorVerdad (false);
		}
		
		if(((ValorVerdad)izq).value == false && der instanceof Expresion){
			return new ValorVerdad(false);
		}
	
		if(izq instanceof ValorVerdad && der instanceof ValorVerdad){
			if(((ValorVerdad)izq).value == true && ((ValorVerdad)der).value == true){
				return new ValorVerdad(true);
			}
		}
		Conjuncion b = new Conjuncion(izq, der);
		return b;	
	}
	@Override public String toString() {
		return "Conjuncion("+ left +", "+ right +")";
	}

	@Override public int hashCode() {
		int result = 1;
		result = result * 31 + (this.left == null ? 0 : this.left.hashCode());
		result = result * 31 + (this.right == null ? 0 : this.right.hashCode());
		return result;
	}

	@Override public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		Conjuncion other = (Conjuncion)obj;
		return (this.left == null ? other.left == null : this.left.equals(other.left))
			&& (this.right == null ? other.right == null : this.right.equals(other.right));
	}
	
	@Override
	public Object check(ChequearEstado checkstate) {
		if ((left.check(checkstate).equals("boolean")) & (right.check(checkstate).equals("boolean"))){
			return new String("boolean");
		}else {
			Errores.exceptionList.add(new Errores("Conjuncion \"" + this.toString() + "\" tipos no booleanos."));
		}
		return checkstate;
	}
}
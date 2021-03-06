/**
 * Universidad Catolica - Compiladores - Obligatorio.
 */
package ast;

import java.util.*;

import behaviour.*;

/**
 * Representacion de las divisiones.
 *
 * @author Grupo_9
 * @version 0.0.1
 * @date 30 oct. 2016
 */
public class Division extends Expresion {
	public final Expresion left;
	public final Expresion right;

	public Division(Expresion left, Expresion right) {
		this.left = left;
		this.right = right;
	}

	@Override public String unparse() {
		return "("+ left.unparse() +" / "+ right.unparse() +")";
	}

	/*@Override
	public Object evaluate(Estado state) {
		if ((left.evaluate(state) instanceof Double) & (right.evaluate(state) instanceof Double))
			if (!((Double)right.evaluate(state) == 0))
				return new Double((Double)left.evaluate(state) / (Double)right.evaluate(state));
			else {
				System.out.print("Estas dividiendo entre 0.");
				return null;
			}
		else {
			System.out.print("Estas comparando mal. Nuemro1 -> " + left.evaluate(state) + " Numero2 -> " + right.evaluate(state));
			return null;
		}
	}*/

	@Override public Set<String> freeVariables(Set<String> vars) {
		return right.freeVariables(left.freeVariables(vars));
	}

	@Override public int maxStackIL() {
		return Math.max(left.maxStackIL(), right.maxStackIL())+ 1;
	}

	@Override public CompilationContextIL compileIL(CompilationContextIL ctx) {
		ctx = left.compileIL(ctx);
		ctx = right.compileIL(ctx);
		ctx.codeIL.append("div \n");
		return ctx;
	}
	
	@Override public Expresion optimization(Estado state){
		Expresion opt1 = left.optimization(state);
		Expresion opt2 = right.optimization(state);
		
		if(opt1 instanceof Numeral && opt2 instanceof Numeral)
		{
			
			if (((Numeral)opt2).number != 0)
			{
				
				if(((Numeral)opt1).number == 0)
					return new Numeral(0.0);
				//a / a = 1
				if(((Numeral)opt1).number == ((Numeral)opt2).number)
					return new Numeral(1.0);
				//a / 1 = a
				if(((Numeral)opt2).number == 1)
					return opt1;
				
				//Si no entra en los otros casos, devolvemos un Numeral(opt1 / opt2)
				return new Numeral( ((Numeral)opt1).number / ((Numeral)opt2).number );
			}else{
				Errores.exceptionList.add(new Errores("Division \"" + this.toString() + "\" no valida."));
			}
		
		}	return new Division(opt1, opt2);
	}

	@Override public String toString() {
		return "Division("+ left +", "+ right +")";
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
		Division other = (Division)obj;
		return (this.left == null ? other.left == null : this.left.equals(other.left))
			&& (this.right == null ? other.right == null : this.right.equals(other.right));
	}

	/*public static Division generate(Random random, int min, int max) {
		Expresion left; Expresion right; 
		left = Expresion.generate(random, min-1, max-1);
		right = Expresion.generate(random, min-1, max-1);
		return new Division(left, right);
	}*/
	
	@Override public Object check(ChequearEstado checkstate){
		
		if ((left.check(checkstate).equals("entero")) & (right.check(checkstate).equals("entero")))
			return new String("entero");
		else {
			Errores.exceptionList.add(new Errores("Division \"" + this.toString() + "\" tipos no numericos."));
		}	
		return checkstate;
	}

}

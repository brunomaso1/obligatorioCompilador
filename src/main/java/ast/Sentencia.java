/**
 * Universidad Catolica - Compiladores - Obligatorio.
 */
package ast;

import java.util.*;

import behaviour.*;


/**
 * Representacion de las sentencias.
 *
 * @author Grupo_9
 * @version 0.0.1
 * @date 30 oct. 2016
 */
public abstract class Sentencia extends Programa{
	
	public static final Sentencia skip = new Secuencia(new Sentencia[0]);
	
	abstract public String unparse();

	//abstract public Estado evaluate(Estado state);

	abstract public Set<String> freeVariables(Set<String> vars);

	abstract public int maxStackIL();

	abstract public CompilationContextIL compileIL(CompilationContextIL ctx);

	abstract public Sentencia optimization(Estado state);
	
	@Override public abstract String toString();

	@Override public abstract int hashCode();
	
	abstract public ChequearEstado check(ChequearEstado checkstate);

	@Override public abstract boolean equals(Object obj);

	/*public static Sentencia generate(Random random, int min, int max) {
		final int TERMINAL_COUNT = 0;
		final int NONTERMINAL_COUNT = 4;
		int i = random.nextInt(TERMINAL_COUNT + NONTERMINAL_COUNT);
		switch (i) {
		//Terminales
		//No terminales
			case 0: return Asignacion.generate(random, min-1, max-1);
			case 1: return Secuencia.generate(random, min-1, max-1);
			case 2: return SiEntoncesSino.generate(random, min-1, max-1);
			case 3: return MientrasHacer.generate(random, min-1, max-1);
			default: throw new Error("Unexpected error at Stmt.generate()!");
		}
	}*/
}

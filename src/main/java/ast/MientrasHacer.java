package ast;

import java.util.*;
import behaviour.*;
import java.io.*;

/**
 * Representacion de los bucles.
 *
 * @author Grupo_9
 * @version 0.0.1
 * @date 30 oct. 2016
 */
public class MientrasHacer extends Stmt {
	public final BExp condition;
	public final Stmt body;

	public MientrasHacer(BExp condition, Stmt body) {
		this.condition = condition;
		this.body = body;
	}

	@Override public String unparse() {
		return "Mientras "+ condition.unparse() +" hacer { "+ body.unparse() +" }";
	}

	@Override public State evaluate(State state) {
		while (condition.evaluate(state)) state = body.evaluate(state);
					return state;
	}

	@Override public Set<String> freeVariables(Set<String> vars) {
		vars = condition.freeVariables(vars); return body.freeVariables(vars);
	}

	@Override public int maxStackIL() {
		return Math.max(condition.maxStackIL(), body.maxStackIL());
	}

	@Override public CompilationContextIL compileIL(CompilationContextIL ctx) {
		
		//CS[while b do cs] = br L2 L1: :cs L2: CB[[b]]:brtrue L1
		String etiqueta = ctx.newLabel();
		String etiqueta2 = ctx.newLabel();
		
		ctx.codeIL.append("br " +etiqueta2+ "\n");
		ctx.codeIL.append(etiqueta+ ": " +"\n");
		ctx = body.compileIL(ctx);
		ctx.codeIL.append(etiqueta2+ ": " +"\n");
		ctx = condition.compileIL(ctx);
		ctx.codeIL.append("brtrue "+ etiqueta + "\n");
		
		return ctx;
	}

	@Override public String toString() {
		return "MientrasHacer("+ condition +", "+ body +")";
	}

	@Override public int hashCode() {
		int result = 1;
		result = result * 31 + (this.condition == null ? 0 : this.condition.hashCode());
		result = result * 31 + (this.body == null ? 0 : this.body.hashCode());
		return result;
	}

	@Override public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		MientrasHacer other = (MientrasHacer)obj;
		return (this.condition == null ? other.condition == null : this.condition.equals(other.condition))
			&& (this.body == null ? other.body == null : this.body.equals(other.body));
	}

	public static MientrasHacer generate(Random random, int min, int max) {
		BExp condition; Stmt body; 
		condition = BExp.generate(random, min-1, max-1);
		body = Stmt.generate(random, min-1, max-1);
		return new MientrasHacer(condition, body);
	}
}
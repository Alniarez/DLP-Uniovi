package ast;

import java.util.*;

public abstract class AbstractTraceable implements Traceable {

	@Override
	public Position getStart() {
		return start;
	}

	@Override
	public Position getEnd() {
		return end;
	}

	public Traceable startPosition(Position start) {
		this.start = start;
		return this;
	}

	public Traceable endPosition(Position end) {
		this.end = end;
		return this;
	}

	// -----------------------------------------------
	// Métodos public para ser usados desde el Yacc

	public Traceable startPosition(Object object) {
		trySetStart(object);
		return this;
	}

	public Traceable endPosition(Object object) {
		trySetEnd(object);
		return this;
	}

	public Traceable setPositions(Object start, Object end) {
		trySetStart(start);
		trySetEnd(end);
		return this;
	}

	public Traceable setPositions(Object object) {
		setPositions(object, object);
		return this;
	}

	// -----------------------------------------------
	// Método protected para ser llamado desde los
	// constructores de las clases AST

	// searchForPositions: Establece:
	// 1. La posición inicial (start) a partir 
	//    del PRIMER argumento que tenga posición inicial.
	// 2. La posición final (end) a partir del ULTIMO 
	//    argumento que tenga posición final.
	protected void searchForPositions(Object... object) {
		List<Object> objects = new ArrayList<Object>(Arrays.asList(object));
		trySetStart(objects);
		trySetEnd(objects);
	}

	// -----------------------------------------------
	// Métodos privados para ser llamados solo desde los
	// métodos anteriores

	private boolean trySetStart(Traceable traceable) {
		if (traceable != null && traceable.getStart() != null) {
			startPosition(traceable.getStart());
			return true;
		}
		return false;
	}

	private boolean trySetEnd(Traceable traceable) {
		if (traceable != null && traceable.getEnd() != null) {
			endPosition(traceable.getEnd());
			return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	private boolean trySetStart(Object object) {
		if (object instanceof Traceable)
			return trySetStart((Traceable) object);
		if (object instanceof List)
			return trySetStart((List<Object>) object);
		return false;
	}

	@SuppressWarnings("unchecked")
	private boolean trySetEnd(Object object) {
		if (object instanceof Traceable)
			return trySetEnd((Traceable) object);
		if (object instanceof List)
			return trySetEnd((List<Object>) object);
		return false;
	}

	private boolean trySetStart(List<Object> objects) {
		boolean encontrado = false;
		for (int i = 0; i < objects.size() && !encontrado; i++)
			encontrado = trySetStart(objects.get(i));
		return encontrado;
	}

	private boolean trySetEnd(List<Object> objects) {
		boolean encontrado = false;
		for (int i = objects.size() - 1; i >= 0 && !encontrado; i--)
			encontrado = trySetEnd(objects.get(i));
		return encontrado;
	}

	private Position start, end;
}



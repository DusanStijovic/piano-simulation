package piano.formatter;

import java.io.IOException;

import piano.Composition;

public abstract class Formatter {
	protected Composition composition;
	
	public Formatter(Composition composition) {
		super();
		this.composition = composition;
	}

	public abstract void exportComposition(String fileName) throws IOException;
}

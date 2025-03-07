package nn;

import interfaces.Sample;
import math.Vector;

public class NNData implements Sample<Vector, Vector> {

	private Vector input;
	private Vector output;

	public NNData(Vector input, Vector output) {
		this.input = input;
		this.output = output;
	}

	public Vector getData() {
		return this.input;
	}

	public Vector getLabel() {
		return this.output;
	}
}

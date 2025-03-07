package nn.activationFunctions;
import interfaces.ActivationFunction;
import math.Vector;

public class ReLU implements ActivationFunction<Vector>{
	
	Vector input;

	public ReLU(){}

	@Override
	public Vector forward(Vector input){

		this.input = input;

		Vector result = new Vector(input.getLength());
		
		for(int i = 0; i < result.getLength(); i++){
			result.setValue(i, Math.max(0.0, input.getValue(i)));
		}
		
		return result;
	}

	@Override
	public Vector backward(Vector gradient){

		Vector result = new Vector(gradient.getLength());

		for(int i = 0; i < result.getLength(); i++){
			result.setValue(i, this.input.getValue(i) >= 0.0 ? gradient.getValue(i) : 0.0);
		}

		return result;
	}

	@Override
	public void zeroGrad(){};

	@Override
	public void update(double learningRate, int t, int batchSize){};


}

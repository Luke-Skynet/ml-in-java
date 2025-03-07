package nn;

import math.Matrix;
import math.Vector;
import interfaces.Layer;

public class Dense implements Layer<Vector, Vector>{
	
	// Parameters
	private Matrix weights;
	private Vector bias;

	// Gradients for Backprop
	private Matrix weightGradients;
	private Vector biasGradients;

	private Vector input;
	private Vector output;

	// Adam Stuff
	private double beta1 = 0.9;
	private double beta2 = 0.999;

	private Matrix Wm;
	private Matrix Wv;

	private Vector Bm;
	private Vector Bv;

	public Dense(int inDimemsion, int outDimension){

		this.weights = new Matrix(outDimension, inDimemsion);
		this.weights.setValuesRandom();

		this.bias = new Vector(outDimension);
		
		this.weightGradients = new Matrix(outDimension, inDimemsion);
		this.biasGradients = new Vector(outDimension);

		this.Wm = new Matrix(outDimension, inDimemsion);
		this.Wv= new Matrix(outDimension, inDimemsion);

		this.Bm = new Vector(outDimension);
		this.Bv = new Vector(outDimension);
	}

	@Override
	public Vector forward(Vector x){
		this.input = x;
		this.output = this.weights.dot(x).plus(bias);
		return this.output;
	}

	@Override
	public Vector backward(Vector gradient){

		Matrix weightGradients = gradient.outer(this.input);
		Vector biasGradients = gradient;

		this.weightGradients = this.weightGradients.plus(weightGradients);
		this.biasGradients = this.biasGradients.plus(biasGradients);

		return this.weights.transpose().dot(gradient);
	}

	@Override
	public void zeroGrad(){
		this.weightGradients.scale(0);
		this.biasGradients.scale(0);
	}

	@Override
	public void update(double learningRate, int t, int batchSize){

		this.weightGradients.scale(1.0 / batchSize);
		this.biasGradients.scale(1.0 / batchSize);

		for(int i = 0; i < this.weightGradients.getColumnSize(); i++){

			for(int j = 0; j < this.weightGradients.getRowSize(); j++){

				this.Wm.setValue(i, j, this.beta1*this.Wm.getValue(i, j) + (1.0 - beta1) *          this.weightGradients.getValue(i, j));
				this.Wv.setValue(i, j, this.beta2*this.Wv.getValue(i, j) + (1.0 - beta2) * Math.pow(this.weightGradients.getValue(i, j), 2.0));

				double Wmh = this.Wm.getValue(i, j) / (1.0 - Math.pow(beta1, t));
				double Wvh = this.Wv.getValue(i, j) / (1.0 - Math.pow(beta2, t));

				this.weights.setValue(i, j, this.weights.getValue(i, j) - ( (learningRate * Wmh) / (Math.pow(Wvh, .5) + .00000001) ) );
			}
			
			this.Bm.setValue(i, this.beta1 * this.Bm.getValue(i) + (1.0 - beta1) *          this.biasGradients.getValue(i));
			this.Bv.setValue(i, this.beta2 * this.Bv.getValue(i) + (1.0 - beta2) * Math.pow(this.biasGradients.getValue(i), 2.0));

			double Bmh = this.Bm.getValue(i) / (1.0 - Math.pow(beta1, t));
			double Bvh = this.Bv.getValue(i) / (1.0 - Math.pow(beta2, t));

			this.bias.setValue(i, this.bias.getValue(i) - ( (learningRate * Bmh) / (Math.pow(Bvh, .5) + .00000001) ) );

		}
		this.zeroGrad();
	}
}
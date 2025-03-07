package nn;

import math.Vector;

import interfaces.Model;
import interfaces.Layer;

import java.util.ArrayList;

public class NeuralNetwork implements Model<Vector, Vector, NNData>{

	private ArrayList<Layer<Vector, Vector>> layers;

	public NeuralNetwork(){
		this.layers = new ArrayList<Layer<Vector, Vector>>();
	}

	public void addLayer(Layer<Vector, Vector> layer){
		this.layers.add(layer);
	}

	@Override
	public Vector compute(Vector input){
		Vector x = input;
		for(int i = 0; i < layers.size(); i++){
			x = layers.get(i).forward(x);
		}
		return x;
	}

	/**
	 * This is an internal method for taking single training step based off of a batch of samples.
	 * @param training - array of training samples to calculate gradients
	 * @param learningRate - floating point scalar multiplier used to scale gradient before adding them to wieghts and bias
	 */
	private void forwardBackward(NNData[] training, double learningRate, int epoch) {

		for(int l = 0; l < layers.size(); l++){
			this.layers.get(l).zeroGrad();
		}
		for (int i = 0; i < training.length; i++) {

			Vector xi = training[i].getData();
			Vector yi = training[i].getLabel();

			Vector yhat = this.compute(xi);
			Vector gradient = yhat.minus(yi);
			
			for(int l = layers.size() - 1; l >= 0; l--){
				gradient = this.layers.get(l).backward(gradient);
			}
		}

		for(int l = 0; l < layers.size(); l++){
			this.layers.get(l).update(learningRate, epoch, training.length);
		}
	}

	@Override
	public void train(NNData[] training, NNData[] testing, int batchSize, double learningRate, int epochs, boolean verbose){

		int batchCount = training.length / batchSize;
		int spareCount = training.length % batchSize;
		
		int place = 0;
		
		NNData[][] batches = new NNData[batchCount][batchSize];
		for (int i = 0; i < batchCount; i++) {
			for (int j = 0; j < batchSize; j++) {
				batches[i][j] = new NNData(training[place].getData(), training[place].getLabel());
				place++;
			}
		}
		
		NNData[] spareBatch = new NNData[spareCount];
		if (spareCount > 0) {
			for (int i = 0; i < spareBatch.length; i++) {
				spareBatch[i] = new NNData(training[place].getData(), training[place].getLabel());
				place++;
			}
		}

		if (verbose){
			System.out.println("Starting Training:");
		}

		for(int e = 1; e <= epochs; e++){
			for (int i = 0; i < batchCount; i++) {
				this.forwardBackward(batches[i], learningRate, e);
			}
		
			if (spareCount > 0) {
				this.forwardBackward(spareBatch, learningRate, e);
			}
		
			if (verbose) {
				System.out.println("Epoch: " + e + " Loss: " + this.getLoss(testing) + " Accuracy: " + this.getAccuracy(testing));
			}
		}
	}

	@Override
	public double getLoss(NNData[] validation){

		double loss = 0;
		int n = validation.length;

		for(int i = 0; i < validation.length; i++){

			Vector yi = validation[i].getLabel();
			Vector xi = validation[i].getData();

			loss += -1 * yi.dot(this.compute(xi).plus(.00000001).log());
		}
		return loss / n;
	}

	public double getAccuracy(NNData[] validation){

		int incorrect = 0;
		int n = validation.length;

		for(int i = 0; i < validation.length; i++){

			Vector yi = validation[i].getLabel();
			Vector xi = validation[i].getData();

			Vector yhat = this.compute(xi);

			double similarity = yi.dot(yhat);

			if (similarity < .9) {
				incorrect += 1;
			}
		}
		return (double) (n - incorrect) / n;
	}
}
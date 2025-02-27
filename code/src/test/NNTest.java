package test;

import java.io.*;
import nn.*;
import math.*;

 

public class NNTest {

    public static void main(String[] args){
        
        // HyperParameters

        int batchSize = 64;
        double learningRate = .01;
        int epochs = 10;
        boolean verbose = true;
        double trainTestSplit = .8;

        try{
            // Adapted from https://github.com/turkdogan/mnist-data-reader

            String dataFilePath  = "/Users/luke/git/ml-in-java/code/src/test/data/t10k-images.idx3-ubyte";
            String labelFilePath = "/Users/luke/git/ml-in-java/code/src/test/data/t10k-labels.idx1-ubyte";

            DataInputStream dataInputStream = new DataInputStream(new BufferedInputStream(new FileInputStream(dataFilePath)));
            int magicNumber = dataInputStream.readInt();
            int numberOfItems = dataInputStream.readInt();
            int nRows = dataInputStream.readInt();
            int nCols = dataInputStream.readInt();

            DataInputStream labelInputStream = new DataInputStream(new BufferedInputStream(new FileInputStream(labelFilePath)));
            int labelMagicNumber = labelInputStream.readInt();
            int numberOfLabels = labelInputStream.readInt();

            Vector[] data   = new Vector[numberOfItems];
            Vector[] labels = new Vector[numberOfLabels];

            assert numberOfItems == numberOfLabels;

            for(int i = 0; i < numberOfItems; i++) {

                Matrix mnistMatrix = new Matrix(nRows, nCols);

                for (int r = 0; r < nRows; r++) {
                    for (int c = 0; c < nCols; c++) {
                        mnistMatrix.setValue(r, c, (double) dataInputStream.readUnsignedByte() / 255);
                    }
                }

                data[i] = mnistMatrix.flatten();

                labels[i] = new Vector(10);
                labels[i].setValue(labelInputStream.readUnsignedByte(), 1.0);
            }

            dataInputStream.close();
            labelInputStream.close();

            // format train and test datasets

            int trainSize = (int) (numberOfItems * trainTestSplit);
            int testSize = numberOfItems - trainSize;

            NNData[] training = new NNData[trainSize];
            NNData[] testing = new NNData[testSize];

            for (int i = 0; i < trainSize; i++){
                training[i] = new NNData(data[i], labels[i]);
            }

            for(int j = 0; j < testSize; j++){
                int i = trainSize + j;
                testing[j] = new NNData(data[i], labels[i]);
            }

            // model creation and training

            NeuralNetwork model = new NeuralNetwork();

            model.addLayer(new nn.Dense(784, 128));
            model.addLayer(new nn.activationFunctions.ReLU());
            model.addLayer(new nn.Dense(128, 10));
            model.addLayer(new nn.activationFunctions.Softmax());
            
            model.train(training, testing, batchSize, learningRate, epochs, verbose);

            System.out.println("\nConfusion Matrix:");
            Matrix confusionMatrix = confusionMatrix(testing, model);
            confusionMatrix.print();

        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    public static Matrix confusionMatrix(NNData[] data, NeuralNetwork model){

        int dimension = data[0].getLabel().getLength();
        Matrix confusionMatrix = new Matrix(dimension, dimension);

        for(int i = 0; i < data.length; i++) {

            Vector groundTruth = data[i].getLabel();
            Vector prediction  = model.compute(data[i].getData());
            
            int truthIndex = 0;
            double truthMax = 0.0;
            int predIndex = 0;
            double predMax = 0.0;

            for(int j = 0; j < dimension; j++){
                if (groundTruth.getValue(j) > truthMax){
                    truthMax = groundTruth.getValue(j);
                    truthIndex = j;
                }

                if (prediction.getValue(j) > predMax){
                    predMax = prediction.getValue(j);
                    predIndex = j;
                }
            }

            confusionMatrix.setValue(truthIndex, predIndex, confusionMatrix.getValue(truthIndex, predIndex) + 1);

        }
        return confusionMatrix;
    }
} 

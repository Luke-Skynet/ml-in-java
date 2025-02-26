package math;

import java.util.Random;

/**
 * This class provides a matrix data type (mat = double(m,n)). 
 */
public class Matrix {

	private final double[][] arr;

	//Constructors

	/**
	 * This constructs a matrix object by defensively copying the contents of a 2d rectangular double array.
	 * @param arr - a non jagged 2d double array
	 */
	public Matrix(double[][] arr) {
	
		for(int i = 0; i < arr.length; i++){
			if (arr[0].length != arr[i].length)
				throw new IllegalArgumentException("Cannot create Matrix - 2D array is jagged");
		}
		
		this.arr = new double[arr.length][arr[0].length];
		for(int i = 0; i < arr.length; i++) {
			for(int j = 0; j < arr[0].length; j++) {
				this.arr[i][j] = arr[i][j];
			}
		}
	}
	
	/**
	 * This constructs a matrix object full of default values (0) given the dimensions (m,n).
	 * @param columnSize - number of rows in the matrix (m)
	 * @param rowSize - number of colums in the matrix (n)
	 */
	public Matrix(int columnSize, int rowSize) {
		arr = new double[columnSize][rowSize];
	}
	
	//Accessors and Mutators

	/**
	 * @return int - row count / column size (m) of matrix
	 */
	public int getColumnSize(){
		return arr.length;
	}
	
	/**
	 * @return int - column count / row size (n) of matrix
	 */
	public int getRowSize() {
		return arr[0].length;
	}
	
	/**
	 * This modifies a single value in the Matrix, given its index (i,j) or (m,n).
	 * @param i - first index (m)
	 * @param j - second index (n)
	 * @param value - the new doubleing point value in Mat(i,j)
	 */
	public void setValue(int i, int j, double value) {
		arr[i][j] = value;
	}
	
	/**
	 * This sets all of the values in the matrix to random normally distributed numbers.
	 */
	public void setValuesRandom() {
		Random random = new Random();

		for (int i = 0; i < this.getColumnSize(); i++) {
			for (int j = 0; j < this.getRowSize(); j++) {
				this.setValue(i, j, random.nextGaussian());
			}
		}
	}
	
	/**
	 * This accesses a single value in the matrix, given its index (i,j) or (m,n).
	 * @param i - first index (m)
	 * @param j - second index (n)
	 * @return double - value at mat(i,j)
	 */
	public double getValue(int i, int j) {
		return arr[i][j];
	}
	
	/**
	 * This returns the (jth) column in the Matrix as a 1D Vector.
	 * @param column - the index of the column
	 * @return Vector - defensively copied Vector from the column array in the matrix
	 */
	public Vector getColumnVector(int column) {
		
		Vector result = new Vector(this.getColumnSize());
		
		for (int i = 0; i < result.getLength(); i++) {
			result.setValue(i, this.getValue(i, column));
		}
		
		return result;
	}
	
	/**
	 * This returns the (ith) row in the Matrix as a 1D Vector.
	 * @param row - the index of the row
	 * @return Vector - Defensively copied Vector from the row array in the matrix
	 */
	public Vector getRowVector(int row) {
		
		Vector result = new Vector(this.getRowSize());
		
		for (int i = 0; i < result.getLength(); i++) {
			result.setValue(i, this.getValue(row, i));
		}
		
		return result;
	}

	/**
	 * This returns a whole new deep copy of the matrix, seperate from the original.
	 * @return Matrix - a replica of the matrix with the same values
	 */
	public Matrix deepCopy(){
		return new Matrix(this.arr);
	}
	
	/**
	 * This prints out the array with each line as a row.
	 */
	public void print() {
		for (int i = 0; i < this.getColumnSize(); i++) {
			for (int j = 0; j < this.getRowSize(); j++) {
				System.out.print(this.getValue(i, j) + " ");
			}
			System.out.println();;
		}
	}
	
	//Math Functions
	
	/**
	 * Element wise matrix addition.
	 * @param that - another matrix of the same dimension
	 * @return matrix C = A + B
	 */
	public Matrix plus(Matrix that) {
		
		if(this.getColumnSize() != that.getColumnSize() || this.getRowSize() != that.getRowSize())
			throw new IllegalArgumentException("Matrix dimensions do not match.");
		
		int columnsize = this.getColumnSize();
		int rowsize = this.getRowSize();
		
		Matrix result = new Matrix(columnsize, rowsize);
		
		for (int i = 0; i < columnsize; i++) {
			for (int j = 0; j < rowsize; j++) {
				result.setValue(i, j, this.getValue(i, j) + that.getValue(i, j));
			}
		}
		
		return result;
	}


	/**
	 * Add a double offset to each element in matrix.
	 * @param scalar
	 * @return matrix C = A + [1]b
	 */
	public Matrix plus(double scalar) {

		int columnsize = this.getColumnSize();
		int rowsize = this.getRowSize();
		
		Matrix result = new Matrix(columnsize, rowsize);
		
		for (int i = 0; i < columnsize; i++) {
			for (int j = 0; j < rowsize; j++) {
				result.setValue(i, j, this.getValue(i, j) + scalar);
			}
		}
		
		return result;
	}
	
	/**
	 * Element wise matrix subtraction.
	 * @param that - another matrix of the same dimension
	 * @return Matrix C = A - B
	 */
	public Matrix minus(Matrix that) {
		
		if(this.getColumnSize() != that.getColumnSize() || this.getRowSize() != that.getRowSize())
			throw new IllegalArgumentException();
		
		int columnsize = this.getColumnSize();
		int rowsize = this.getRowSize();
		
		Matrix result = new Matrix(columnsize, rowsize);
		
		for (int i = 0; i < columnsize; i++) {
			for (int j = 0; j < rowsize; j++) {
				result.setValue(i, j, this.getValue(i, j) - that.getValue(i, j));
			}
		}
		
		return result;
	}
	
	/**
	 * Linear Algebra dot product/composition operation. (non-commutative)
	 * @param that - another matrix where ||this(j)|| = ||that(i)||
	 * @return matrix C = AB
	 */
	public Matrix dot(Matrix that){

		if (this.getRowSize() != that.getColumnSize())
			throw new IllegalArgumentException();
		
		int columnsize = this.getColumnSize();
		int rowsize = that.getRowSize();
		
		Matrix result = new Matrix(columnsize, rowsize);

		for (int i = 0; i < columnsize; i++) {
			for (int j = 0; j < rowsize; j++) {
				result.setValue(i,j, this.getRowVector(i).dot(that.getColumnVector(j)));
			}
		}
		
		return result;

	}
	
	/**
	 * Matrix transformation of a Vector
	 * @param that vector input where dim(vec) = dim(mat(n))
	 * @return vector B = Ax
	 */
	public Vector dot(Vector that) {
		
		int x = this.getRowSize();
		
		if(x != that.getLength())
			throw new IllegalArgumentException();
		
		Vector result = new Vector(this.getColumnSize());
		
		for (int i = 0; i < result.getLength(); i++) {
			double total = 0.0;
			for (int k = 0; k < x; k++) {
				total += this.getValue(i, k) * that.getValue(k);
			}
			result.setValue(i, total);
		}
		
		return result;
	}
	
	/**
	 * Element wise multiplication between two matrices.
	 * @param that - another matrix of the same dimension
	 * @return - matrix C = A ox B
	 */
	public Matrix eleMult(Matrix that) {
		
		if(this.getColumnSize() != that.getColumnSize() || this.getRowSize() != that.getRowSize())
			throw new IllegalArgumentException();
		
		int columnsize = this.getColumnSize();
		int rowsize = this.getRowSize();
		
		Matrix result = new Matrix(columnsize, rowsize);
		
		for (int i = 0; i < columnsize; i++) {
			for (int j = 0; j < rowsize; j++) {
				result.setValue(i, j, this.getValue(i, j) * that.getValue(i, j));
			}
		}
		
		return result;
	}
	
	/**
	 * Element wise division between two matrices.
	 * @param that - another matrix of the same dimension
	 * @return - matrix C = A / B
	 */
	public Matrix eleDiv(Matrix that) {
		
		if(this.getColumnSize() != that.getColumnSize() || this.getRowSize() != that.getRowSize())
			throw new IllegalArgumentException();
		
		int columnsize = this.getColumnSize();
		int rowsize = this.getRowSize();
		
		Matrix result = new Matrix(columnsize, rowsize);
		
		for (int i = 0; i < columnsize; i++) {
			for (int j = 0; j < rowsize; j++) {
				result.setValue(i, j, this.getValue(i, j) / that.getValue(i, j));
			}
		}
		
		return result;
	}

	/**
	 * Element wise multiplication between two matrices.
	 * @param power - another matrix of the same dimension
	 * @return - matrix C = A o^ b
	 */
	public Matrix elePow(double power) {
		
		int columnsize = this.getColumnSize();
		int rowsize = this.getRowSize();
		
		Matrix result = new Matrix(columnsize, rowsize);
		
		for (int i = 0; i < columnsize; i++) {
			for (int j = 0; j < rowsize; j++) {
				result.setValue(i, j, Math.pow(this.getValue(i, j), power));
			}
		}
		
		return result;
	}
	
	/**
	 * Inplace scaling of the matrix by a double scalar.
	 * @param scalar - double scalar k in A <- kA
	 */
	public void scale(double scalar) {
		
		for (int i = 0; i < this.getColumnSize(); i++) {
			for (int j = 0; j < this.getRowSize(); j++) {
				arr[i][j] *= scalar;
			}
		}
	}
	
	/**
	 * Non-inplace scaling of a matrix by a double scalar.
	 * @param scalar - double scalar k in kA
	 * @return Matrix B = kA
	 */
	public Matrix scaled(double scalar) {
		
		int columnsize = this.getColumnSize();
		int rowsize = this.getRowSize();
		
		Matrix result = new Matrix(columnsize, rowsize);
		
		for (int i = 0; i < columnsize; i++) {
			for (int j = 0; j < rowsize; j++) {
				result.setValue(i, j, arr[i][j] * scalar);
			}
		}
		
		return result;
	}
	
	/**
	 * This method returns a new matrix with the columns and rows flipped
	 * @return matrix B = A^T
	 */
	public Matrix transpose() {
		
		int columnsize = this.getRowSize();
		int rowsize = this.getColumnSize();
		
		Matrix result = new Matrix(columnsize, rowsize);
		
		for (int i = 0; i < columnsize; i++) {
			for (int j = 0; j < rowsize; j++) {
				result.setValue(i, j, this.getValue(j, i));
			}
		}
		
		return result;		
	}
	/**
	 * This method returns a vector of the rows of the matrix appended together
	 * @return
	 */
	public Vector flatten() {

		Vector result = new Vector(this.getColumnSize() * this.getRowSize());

		for (int i = 0; i < this.getColumnSize(); i++){
			for (int j = 0; j < this.getRowSize(); j++){
				result.setValue(i * this.getRowSize() + j, this.getValue(i, j));
			}
		}

		return result;
	}
	
	public static void main(String[] args) {
		
	}
}

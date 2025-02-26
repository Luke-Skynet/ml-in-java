package math;

/**
 * This class provides a vector datatype of doubles. V = f^n
 * NOTE: Although these vectors interact with matrices a lot, they do NOT have a definitive orientation.
 * Each member function specifies when needed whether the vector acts as a column or a row.
 */
public class Vector {

	private final double[] arr;
	
	//Constructors
	
	/**
	 * This constructor creates a vector object using the values from a double array.
	 * @param arr - the double array that is deeply copied.
	 */
	public Vector(double[] arr) {
		this.arr = new double[arr.length];
		for(int i = 0; i < arr.length; i++) {
			this.arr[i] = arr[i];
		}
	}
	
	/**
	 * This constructore creates a vector object with a specified length and all values set to default (0).
	 * @param length int - the length / dimension of the vector
	 */
	public Vector(int length) {
		arr = new double[length];
	}
	
	//Accessors
	
	/**
	 * This returns the dimension of the vector double^n -> n.
	 * @return int - natural number
	 */
	public int getLength() {
		return arr.length;
	}
	
	/**
	 * This returns the scalar value at the specified index.
	 * @param i int - the index
	 * @return double - value at index i
	 */
	public double getValue(int i) {
		return arr[i];
	}
	/**
	 * This returns a new Vector with the same values as the vector
	 * @return vector - replica of the copied vector
	 */
	public Vector deepCopy(){
		return new Vector(this.arr);
	}

	//Mutators
	
	/**
	 * This mutates specific element in the vector.
	 * @param i int - the index of the mutated value
	 * @param value double - the new value at the index
	 */
	public void setValue(int i, double value) {
		arr[i] = value;
	}
	
	/**
	 * This sets all the elements in the vector to a single value.
	 * @param value double - the value to set all elements to
	 */
	public void setValues(double value) {
		for(int i = 0; i < arr.length; i++) {
			this.arr[i] = value;
		}
	}

	/**
	 * This sets all the elements to random numbers between (-1,1)
	 */
	public void setValuesRandom() {
		for(int i = 0; i < arr.length; i++) {
			this.arr[i] = (double) ( 2 * ( Math.random() - .5 ) );
		}
	}
	
	//Printers
	/**
	 * This prints out all the values of the vector to console.
	 */
	public void print() {
		for(int i = 0; i < arr.length; i++) {
			System.out.print(this.getValue(i) + " ");
		}
	} 
	
	/**
	 * This returns all the elements of the vector with spaces in between them.
	 */
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < this.getLength(); i++)
			sb.append(arr[i] + " ");
	
		sb.deleteCharAt(sb.length() - 1);
		
		return sb.toString();
	}
	
	//Math Functions
	/**
	 * Element-wise vector addition.
	 * @param that - another vector of the same dimension
	 * @return vector C = A + B
	 */
	public Vector plus(Vector that) {
		
		if (this.getLength() != that.getLength())
			throw new IllegalArgumentException();
		
		Vector result = new Vector(this.getLength());
		
		for (int i = 0; i < result.getLength(); i++) {
			result.setValue(i, this.getValue(i) + that.getValue(i));
		}
		
		return result;
	}

	/**
	 * Element-wise vector value offset.
	 * @param scalar - double to add to each vector element
	 * @return vector C = A[i] + scalar for i in A
	 */	
	public Vector plus(double scalar) {
		
		Vector result = new Vector(this.getLength());
		
		for (int i = 0; i < result.getLength(); i++) {
			result.setValue(i, this.getValue(i) + scalar);
		}
		
		return result;
	}
	
	/**
	 * Element-wise vector subtraction.
	 * @param that - another vector of the same dimension
	 * @return vector C = A - B
	 */
	public Vector minus(Vector that) {
		
		if (this.getLength() != that.getLength())
			throw new IllegalArgumentException();
		
		Vector result = new Vector(this.getLength());
		
		for (int i = 0; i < result.getLength(); i++) {
			result.setValue(i, this.getValue(i) - that.getValue(i));
		}
		
		return result;
	}
	
	/**
	 * Element-wise vector multiplication.
	 * @param that - another vector of the same dimension
	 * @return Ci = Ai * Bi for i in vector
	 */
	public Vector times(Vector that) {
		
		if (this.getLength() != that.getLength())
			throw new IllegalArgumentException();
		
		Vector result = new Vector(this.getLength());
		
		for (int i = 0; i < result.getLength(); i++) {
			result.setValue(i, this.getValue(i) * that.getValue(i));
		}
		
		return result;
	}
	/**
	 * Element-wise vector division.
	 * @param that - another vector of the same dimension
	 * @return Ci = Ai / Bi for i in vector
	 */
	public Vector divide(Vector that) {
		
		if (this.getLength() != that.getLength())
			throw new IllegalArgumentException();
		
		Vector result = new Vector(this.getLength());
		
		for (int i = 0; i < result.getLength(); i++) {
			result.setValue(i, this.getValue(i) / that.getValue(i));
		}
		
		return result;
	}
	
	/**
	 * Typical Vector dot product.
	 * @param that - aother vector of the same dimension
	 * @return double y = sum(Ai * bi) for i in vectors A,B
	 */
	public double dot(Vector that) {
		
		if (this.getLength() != that.getLength())
			throw new IllegalArgumentException();
		
		double result = 0;
		
		for (int i = 0; i < this.getLength(); i++) {
			result += this.getValue(i) * that.getValue(i);
		}
		
		return result;
	}

	/**
	 * Vector outer product
	 * @param that - aother vector
	 * @return matrix - M[i][j] = A[i] * B[j]
	 */
	public Matrix outer(Vector that) {
		
		Matrix result = new Matrix(this.getLength(), that.getLength());

		for(int i = 0; i < this.getLength(); i++){
			for(int j = 0; j < that.getLength(); j++) {
				result.setValue(i, j, this.getValue(i) * that.getValue(j));
			}
		}

		return result;
	}

	/**
	 * Element-wise vector logarithm.
	 * @return B[i] = ln(A[i]) for i in A
	 */
	public Vector log() {
		Vector result = new Vector(this.getLength());
		for(int i = 0; i < result.getLength(); i++){
			result.setValue(i, Math.log(this.getValue(i) + .001f));
		}
		return result;
	}
	
	/**
	 * Element-wise vector power.
	 * @return B[i] = A[i]^n for i in A
	 */
	public Vector pow(double power) {
		Vector result = new Vector(this.getLength());
		for(int i = 0; i < result.getLength(); i++){
			result.setValue(i, Math.pow(this.getValue(i), power));
		}
		return result;
	}


	/**
	 * Inplace scaling of each element by a scalar.
	 * @param scalar - double scalar k in V -> kV
	 */
	public void scale(double scalar) {
		
		for (int i = 0; i < arr.length; i++) {
			arr[i] = arr[i] * scalar;
		}
	}
	
	/**
	 * Non-inplace scaling of each element by a double scalar.
	 * @param scalar - double scalar k in kV
	 * @return vector W = kV
	 */
	public Vector scaled(double scalar) {
		
		Vector result = new Vector(arr.length);
		
		for (int i = 0; i < arr.length; i++) {
			result.setValue(i, arr[i] * scalar);
		}
		
		return result;
	}
	
	public static void main(String[] args) {
		
	}
}

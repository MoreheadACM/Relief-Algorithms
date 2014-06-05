public class ReliefArrayMethods {
	/**
	 * Doubles the size of an array.
	 * scaleUpArray(the array to be scaled).
	 *
	 * @param  _array  the array to be doubled
	 * @return      a new array with the old data and a doubled length
	 */
	public String[] scaleUpArray(String[] _array) {
		String[] newArray = new String[_array.length*2];
		System.arraycopy(_array, 0, newArray, 0, _array.length);
		return newArray;
	}

	/**
	 * Trims the null values from the end of an array.
	 * trimArray(the array to be trimmed).
	 * If no null values exist at the end of the array, the original array is returned.
	 *
	 * @param  _array  the array to be trimmed
	 * @return      a new array with the trailing null values removed, or the original array
	 */
	public String[] trimArray(String[] _array) {

		int newLength = 0;
		for(int i = _array.length-1; (_array[i] == null) && (i >= 0); i--) {
			newLength = i;
		}
		if(newLength != 0) {
			String[] newArray = new String[newLength];
			System.arraycopy(_array, 0, newArray, 0, newLength);
			return newArray;
		}
		else {
			return _array;
		}
	}

	/**
	 * Returns the data held in the given 2D array as a string.
	 * The data is returned as a string, separated in the first dimension by commas, and by newlines in the second.
	 *
	 * @return      a string, separated in the first dimension by commas, and by newlines in the second
	 */
	public String display2DArray(int[][] _array) {
		String data = "";

		for(int i = 0; i < _array.length; i++) {
			for(int j = 0; j < _array[0].length; j++) {
				data += _array[i][j]+" ";
			}
			data += "\n";
		}

		return data;
	}
}
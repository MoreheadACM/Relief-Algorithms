import java.util.Scanner;
import java.io.*;

public class Relief extends ReliefArrayMethods {
	static boolean debug;
	Scanner fileScanner;
	String fileContents[];
	private int[][] samples;
	private double[] weights;

	public static void main(String args[]) {
		debug = args.length >= 1 && args[0].contains("-dbg");
		if(debug) {System.out.println("\nConsole Debugging Enabled\n");}
		Relief algorithm = new Relief();
	}

	/**
	 * Class Constructor
	 * Makes a call to ReliefInit() to initialize data.
	 *
	 * @see ReliefInit()
	 */
	public Relief()	{
		ReliefInit();
		
	}

	/**
	 * Reads from the data file/uses sample data, and populates samples[][]. 
	 *
	 * @return      null
	 */
	private void ReliefInit() {
		fileContents = new String[4];
		try {
			fileScanner = new Scanner(new File("data"));
			int i = 0;
			while(fileScanner.hasNextLine()) {
				if(i >= fileContents.length) {
					fileContents = scaleUpArray(fileContents);
				}
				fileContents[i] = fileScanner.nextLine();
				i++;
			}
		}
		catch(FileNotFoundException err) {
			if(debug) {System.out.println("Error: "+err.getMessage());}

			System.out.println("Populating Sample Data...");
			samples = populateWithSampleData();
		}

		fileScanner.close();

		fileContents = trimArray(fileContents);

		if(debug) {System.out.println("fileContents.length after trim == "+fileContents.length+"\n");}

		if(fileContents.length == 0) {
			populateWithSampleData();
		}
		else {
			String[] currentRow = fileContents[0].split(",");
			samples = new int[fileContents.length][currentRow.length];
			for(int i = 0; i < samples.length; i++) {
				currentRow = fileContents[i].split(",");
				for(int j = 0; j < currentRow.length; j++) {
					samples[i][j] = Integer.parseInt(currentRow[j]);
				}
			}
		}

		if(debug) {System.out.println(display2DArray(samples));}

		//System.out.println(nearestMiss(0,0));

		setFeatureWeights();

		if(debug) {System.out.println("");}

		for(int i = 0; i < weights.length; i++) {
			if(debug) {System.out.println("W("+(i+1)+"): "+weights[i]);}
		}

	}

	/**
	 * Returns an array of features from 1 sample.
	 * getSampleFeature(the index of the selected sample).
	 * If the sample index is out of bounds, the method will return null.
	 *
	 * @param  sampleIndex  the index of the selected sample
	 * @return      an array of features from the selected sample, or null
	 */
	private int[] getSampleFeatures(int sampleIndex) {
		if(sampleIndex >= samples.length || sampleIndex < 0) {
			if(debug) {System.out.println("getSampleFeatures() index out of range");}
			return null;
		}
		else {
			int[] featuresToReturn = new int[samples[sampleIndex].length-1];
			System.arraycopy(samples[sampleIndex], 0, featuresToReturn, 0, featuresToReturn.length);
			return featuresToReturn;
		}
	}

	/**
	 * Returns one feature from a sample.
	 * getSampleFeature(the index of the selected sample, the index of the selected feature).
	 * If the feature index is out of bounds, the program will exit with a status of 1.
	 *
	 * @param  sampleIndex  the index of the selected sample
	 * @param  featureIndex the index of the selected feauture
	 * @return      the selected feature, or will exit
	 */
	private int getSampleFeature(int sampleIndex, int featureIndex) {
		if(sampleIndex >= samples.length || sampleIndex < 0) {
			if(debug) {System.out.println("getSampleFeature() index out of range");}
			System.exit(1);
			return -1; // Doesn't get reached
		}
		else {
			return getSampleFeatures(sampleIndex)[featureIndex];
		}
	}

	/**
	 * Returns the class of the selected sample.
	 * getSampleClass(the index of the selected sample).
	 * If the sample index is out of bounds, the program will exit with a status of 1.
	 *
	 * @param  sampleIndex  the index of the selected sample
	 * @return      the selected sample's class, or will exit
	 */
	private int getSampleClass(int sampleIndex) {
		if(sampleIndex >= samples.length || sampleIndex < 0) {
			if(debug) {System.out.println("getSampleFeature() index out of range");}
			System.exit(1);
			return -1; // Doesn't get reached
		}
		else {
			return samples[sampleIndex][samples[sampleIndex].length-1];
		}
	}

	/**
	 * Populates left operand (2D array) with dummy data.
	 *
	 * @return      a 2D array of sample data (int[4][3])
	 */
	private int[][] populateWithSampleData() {
		return new int[][] {{3,4,1},{2,5,1},{6,7,2},{5,6,2}};
	}

	/**
	 * Returns the distance between two n-dimensional points as a double.
	 * nDistance(first array, second array).
	 * Uses the Euclidean distance formula, or d(p,q) = sqrt((p1 - q1)^2 + ... + (pn - qn)^2).
	 *
	 * @param  _arrayFrom  the first array of features
	 * @param  _arrayTo  the second array of features
	 * @return      the distance between the given arrays as a double
	 */
	public double nDistance(int[] _arrayFrom, int[] _arrayTo) {
		double distance = 0.0;
		for(int i = 0; i < _arrayFrom.length; i++) {
			distance += Math.pow((_arrayFrom[i] - _arrayTo[i]), 2);
		}
		return Math.sqrt(distance);
	}

	/**
	 * Returns the samples index of the nearest miss.
	 *
	 * @param		sampleIndex the index of the selected sample
	 * @param		featureIndex the index of the selected feature
	 * @return      the samples index of the nearest miss as an int
	 */
	private int nearestMiss(int sampleIndex, int featureIndex) {
		int shortestDistance = -1, currentDistance = 0;
		for(int i = 0; i < samples.length; i++) {
			if(getSampleClass(sampleIndex) != getSampleClass(i)) {
				currentDistance = getSampleFeature(sampleIndex, featureIndex) - getSampleFeature(i, featureIndex);
				if(Math.pow(currentDistance,2) < Math.pow(shortestDistance,2) || shortestDistance == -1) {
					shortestDistance = currentDistance;
				}
			}
		} // for
		return shortestDistance;
	} // nearestMiss

	/**
	 * Returns the samples index of the nearest hit.
	 *
	 * @param		sampleIndex the index of the selected sample
	 * @param		featureIndex the index of the selected feature
	 * @return      the samples index of the nearest hit as an int
	 */
	private double nearestHit(int sampleIndex, int featureIndex) {
		double shortestDistance = -1, currentDistance = 0;
		for(int i = 0; i < samples.length; i++) {
			if(getSampleClass(sampleIndex) == getSampleClass(i) && sampleIndex != i) {
				currentDistance = getSampleFeature(sampleIndex, featureIndex) - getSampleFeature(i, featureIndex);
				if(Math.pow(currentDistance,2) < Math.pow(shortestDistance,2) || shortestDistance == -1) {
					shortestDistance = currentDistance;
				}
			}
		} // for
		return shortestDistance;
	} // nearestMiss


	/**
	 * Puts weights for each feature in the global weights array
	 *
	 * @return      null
	 */
	private void setFeatureWeights() {
		weights = new double[getSampleFeatures(0).length];
		weights[0] = 0.0;
		for(int i = 0; i < samples.length; i++) {
			for(int j = 0; j < weights.length; j++) {

				weights[j] = weights[j] - Math.pow(nearestHit(i,j),2) + Math.pow(nearestMiss(i,j),2);
			}
		}
		for(int i = 0; i < weights.length; i++) {
			weights[i] = weights[i] / samples.length;
		}
	}
}

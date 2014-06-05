import java.util.Scanner;
import java.io.*;

public class Relief
{
	static boolean debug;
	Scanner fileScanner;
	String fileContents[];
	private int[][] samples;

	public static void main(String args[])
	{
		debug = args.length == 1 && args[0].contains("dbg");
		if(debug) {System.out.println("\nConsole Debugging Enabled\n");}
		Relief algorithm = new Relief();
		
		
	}

	public Relief()
	{
		ReliefInit();
		
	}

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
			populateWithSampleData();
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

		if(debug) {System.out.println(displayData());}
	}

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

	private String[] scaleUpArray(String[] _array) {
		String[] newArray = new String[_array.length*2];
		System.arraycopy(_array, 0, newArray, 0, _array.length);
		return newArray;
	}

	private String[] trimArray(String[] _array) {

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

	private String displayData() {
		String data = "";

		for(int i = 0; i < samples.length; i++) {
			for(int j = 0; j < samples[0].length; j++) {
				data += samples[i][j]+" ";
			}
			data += "\n";
		}

		return data;
	}

	private int setFeaturesArraySize(int[] _array) {
		return 0;
	}

	private void populateWithSampleData() {
		samples = new int[][] {{3,4,1},{2,5,1},{6,7,2},{5,6,2}};
	}
}

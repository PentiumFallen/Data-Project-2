package dataClasses;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

import lineClasses.ArrivalQueue;
import peopleClasses.Customer;

/**
 * The main data reader of the system
 * 
 * @author Raúl Vargas
 * @ID 801-14-8312
 * @Section 030
 *
 */
public class DataReader {

	private String parentDirectory; 
	private String outDirectory;
	private ArrayList<String> files = new ArrayList<>();
	private ArrayList<ArrivalQueue> arrival;	

	/**
	 * Constructor
	 * @throws FileNotFoundException
	 */
	public DataReader() throws FileNotFoundException {
		parentDirectory = "inputFiles"; 
		outDirectory = "outputFiles";
		Scanner parameters = new Scanner(new File(parentDirectory, "dataFiles.txt")); 
		while (parameters.hasNext())
			files.add(parameters.nextLine());
		parameters.close();
	}

	/**
	 * 
	 * @return a list of all the ArrivalQueues for each day/file
	 * @throws FileNotFoundException 
	 */
	public ArrayList<ArrivalQueue> readDataFiles() throws FileNotFoundException {
		arrival = new ArrayList<>();
		for (int i = 0; i < files.size(); i++) {
			String fileName = files.get(i);
			Scanner inputFile;
			try {
				inputFile = new Scanner(new File(parentDirectory, fileName));
			} catch (FileNotFoundException e) {
				inputFile = null;
			}
			if (inputFile==null) {
				arrival.add(i, new ArrivalQueue());
				invalidWriter(i+1, "Input file not found.");
			}
			else {
				ArrivalQueue temp = new ArrivalQueue();
				String thisLine;
				while (inputFile.hasNextLine()) {
					thisLine=inputFile.nextLine();
					StringTokenizer token = new StringTokenizer(thisLine);
					if (token.countTokens()!=2) {
						invalidWriter(i+1, "Input file does not meet the expected format or it is empty.");
						arrival.add(i, new ArrivalQueue());
						break;
					}
					else {
						int token1, token2;
						try {
							token1=Integer.parseInt(token.nextToken());
						} catch (NumberFormatException e) {
							invalidWriter(i+1, "Input file does not meet the expected format or it is empty.");
							arrival.add(i, new ArrivalQueue());
							break;
						}
						try {
							token2=Integer.parseInt(token.nextToken());
						} catch (NumberFormatException e) {
							invalidWriter(i+1, "Input file does not meet the expected format or it is empty.");
							arrival.add(i, new ArrivalQueue());
							break;
						}
						if (token1<0 || token2<1) {
							invalidWriter(i+1, "Input file does not meet the expected format or it is empty.");
							arrival.add(i, new ArrivalQueue());
							break;
						}
						temp.enqueue(new Customer(token1, token2));
						if (!inputFile.hasNextLine())
							arrival.add(i, temp);			
					}
				}
				inputFile.close();
			}
		}
		return arrival;
	}

	/**
	 * Writes the invalid/missing files' output
	 * 
	 * @param i file ID
	 * @param output to be printed
	 * @throws FileNotFoundException
	 */
	private void invalidWriter(int i, String output) throws FileNotFoundException {
		String outFile = "data_"+i+"_OUT.txt";
		PrintWriter dataFile = new PrintWriter(new File(outDirectory, outFile)); 
		dataFile.println(output);
		dataFile.close();
	}
	
	/**
	 * Writes the data files' output
	 * 
	 * @param i file ID
	 * @param output to be printed
	 * @throws FileNotFoundException
	 */
	public void validWriter(int i, String output) throws FileNotFoundException {
		String outFile = "data_"+i+"_OUT.txt";
		PrintWriter dataFile = new PrintWriter(new File(outDirectory, outFile)); 
		dataFile.println(output);
		dataFile.close();		
	}
}

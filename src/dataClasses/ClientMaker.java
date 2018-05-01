package dataClasses;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import peopleClasses.Customer;

/**
 * The main data generator of the system
 * 
 * @author Raúl Vargas
 * @ID 801-14-8312
 * @Section 030
 *
 */
public class ClientMaker {

	private int days;					//total amount of data files (valid and invalid)
	private int clientCap;				//max amount of clients per day
	private Integer[] sizes;			//size of each day file
	private Customer[][] clients;		//array of files of customers
	private Random rnd;					//random number generator for various uses 
	private final int serviceCap = 19;	//one less than max amount of service time per client
	private int dayTime;	//max amount of arrival time per day

	/**
	 * ClientMaker constructor; see above for variables
	 * 
	 * @param days
	 * @param clientCap
	 */
	public ClientMaker(int days, int clientCap) {
		this.days = days;
		this.clientCap = clientCap;
		dayTime = clientCap*4;
		rnd = new Random();
	}

	/**
	 * Generates the Customer data
	 */
	public void generateData() {
		clients = new Customer[days][];
		generateSizes();
		for (int i = 0; i < days; i++) {
			if (sizes[i]<=0 && sizes[i]>=-2 ) 
				clients[i]=null;
			else { 
				if (sizes[i]<-2) 
					clients[i] = new Customer[0];
				else 
					if (sizes[i]>0){
						ArrayList<Customer> set = new ArrayList<>();
						while (set.size() != sizes[i]) 
							set.add(new Customer(rnd.nextInt(dayTime), rnd.nextInt(serviceCap)+1));
						set.sort(null);
						clients[i] = set.toArray(new Customer[sizes[i]]);
					}
			}
		}
	}

	/**
	 * Generates the amount of Customers per day and invalid/missing data
	 */
	private void generateSizes() {
		sizes = new Integer[days];
		for (int i=0; i<days; i++) {
			sizes[i] = rnd.nextInt(clientCap+(clientCap/5))-(clientCap/5);
		}
	}

	/**
	 * Generates the input data files
	 * @throws FileNotFoundException
	 */
	public void generateFiles() throws FileNotFoundException {
		String parentDirectory = "inputFiles";
		PrintWriter dataFile = new PrintWriter(new File(parentDirectory, "dataFiles.txt")); 
		for (int i = 0; i < clients.length; i++) {
			String fileName = "data_" + (i+1) + ".txt"; 
			if (clients[i]==null) {}
			else {
				if (clients[i].length==0) {
					PrintWriter out = new PrintWriter(new File(parentDirectory, fileName)); 
					out.println("invalid");
					out.close();
				}
				else 
					if (clients[i].length>0) {
						PrintWriter out = new PrintWriter(new File(parentDirectory, fileName)); 
						for (int j = 0; j < clients[i].length; j++) 
							out.println(clients[i][j].getArrivalTime()+" "+clients[i][j].getServiceTime());
						out.close();
					}
			}
			dataFile.println(fileName);
		}
		dataFile.close();
	}

	/**
	 * Used for testing
	 * @return the "sizes" of each day
	 */
	public Integer[] getSizes() {
		return sizes;
	}
}

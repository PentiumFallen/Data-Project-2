package dataClasses;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import lineClasses.ArrivalQueue;
import managerClasses.MLMSBLL_Manager;
import managerClasses.MLMSBWT_Manager;
import managerClasses.MLMS_Manager;
import managerClasses.SLMS_Manager;

/**
 * The main class of the system
 * 
 * @author Raúl Vargas
 * @ID 801-14-8312
 * @Section 030
 *
 */
public class P2_MAIN {

	public static void main(String[] args) throws FileNotFoundException {
		int days, maxClients;
		Scanner in = new Scanner(System.in);
		System.out.print("Enter amount of days: ");
		days = in.nextInt();
		System.out.print("Enter max amount of clients per day: ");
		maxClients = in.nextInt();
		in.close();
		ClientMaker gen = new ClientMaker(days, maxClients);
		gen.generateData();
		gen.generateFiles();

		//Tester for Data reading
		DataReader read = new DataReader();
		ArrayList<ArrivalQueue> allDays = read.readDataFiles();
		for (ArrivalQueue arrivalQueue : allDays) {
			if (arrivalQueue.isEmpty()) {
				System.out.println("data_"+(allDays.indexOf(arrivalQueue)+1)+".txt is invalid or missing");
				continue;
			}
			else {
				String output = letTheWorldBurn(arrivalQueue);
				read.validWriter(allDays.indexOf(arrivalQueue)+1, output);
				System.out.println("data_"+(allDays.indexOf(arrivalQueue)+1)+".txt has been processed");
			}
		}
		System.out.println("Check inputFiles folder for dataFiles list and input files");
		System.out.println("Check outputFiles folder for final output files");
		System.out.println("System is done");
	}

	/**
	 * Executes each of the approaches with all three server sizes
	 * @param arrivalQueue Day to be processed
	 * @return Output to be written
	 */
	public static String letTheWorldBurn(ArrivalQueue arrivalQueue) {
		String output = "Number of customers is: "+arrivalQueue.size();
		ArrivalQueue slms1 = arrivalQueue.copy();
		SLMS_Manager slmsManage1 = new SLMS_Manager(1, slms1);
		output+=slmsManage1.execute();
		ArrivalQueue slms3 = arrivalQueue.copy();
		SLMS_Manager slmsManage3 = new SLMS_Manager(3, slms3);
		output+=slmsManage3.execute();
		ArrivalQueue slms5 = arrivalQueue.copy();
		SLMS_Manager slmsManage5 = new SLMS_Manager(5, slms5);
		output+=slmsManage5.execute();
		ArrivalQueue mlms1 = arrivalQueue.copy();
		MLMS_Manager mlmsManage1 = new MLMS_Manager(1, mlms1);
		output+=mlmsManage1.execute();
		ArrivalQueue mlms3 = arrivalQueue.copy();
		MLMS_Manager mlmsManage3 = new MLMS_Manager(3, mlms3);
		output+=mlmsManage3.execute();
		ArrivalQueue mlms5 = arrivalQueue.copy();
		MLMS_Manager mlmsManage5 = new MLMS_Manager(5, mlms5);
		output+=mlmsManage5.execute();
		ArrivalQueue bll1 = arrivalQueue.copy();
		MLMSBLL_Manager bllManage1 = new MLMSBLL_Manager(1, bll1);
		output+=bllManage1.execute();
		ArrivalQueue bll3 = arrivalQueue.copy();
		MLMSBLL_Manager bllManage3 = new MLMSBLL_Manager(3, bll3);
		output+=bllManage3.execute();
		ArrivalQueue bll5 = arrivalQueue.copy();
		MLMSBLL_Manager bllManage5 = new MLMSBLL_Manager(5, bll5);
		output+=bllManage5.execute();
		ArrivalQueue bwt1 = arrivalQueue.copy();
		MLMSBWT_Manager bwtManage1 = new MLMSBWT_Manager(1, bwt1);
		output+=bwtManage1.execute();
		ArrivalQueue bwt3 = arrivalQueue.copy();
		MLMSBWT_Manager bwtManage3 = new MLMSBWT_Manager(3, bwt3);
		output+=bwtManage3.execute();
		ArrivalQueue bwt5 = arrivalQueue.copy();
		MLMSBWT_Manager bwtManage5 = new MLMSBWT_Manager(5, bwt5);
		output+=bwtManage5.execute();
		return output;
	}
}

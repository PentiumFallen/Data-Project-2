package dataClasses;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import lineClasses.ArrivalQueue;
import managerClasses.MLMSBLL_Manager;
import managerClasses.MLMSBWT_Manager;
import managerClasses.MLMS_Manager;
import managerClasses.SLMS_Manager;
import peopleClasses.Customer;

@SuppressWarnings("unused")
public class Testing {
	
	public static void main(String[] args) throws FileNotFoundException {
		
		//Tester for data genning
		ClientMaker gen = new ClientMaker(20, 20);
		gen.generateData();
		gen.generateFiles();
		Integer[] sizes = gen.getSizes();
		System.out.print("[ ");
		for (int i = 0; i < sizes.length; i++)
			System.out.print(sizes[i]+" ");
		System.out.println("]");
		
		//Tester for Data reading
		DataReader read = new DataReader();
		ArrayList<ArrivalQueue> allDays = read.readDataFiles();
		
		//Tester for checking file contents
//		for (ArrivalQueue arrivalQueue : allDays) {
//			System.out.println("\nFile data_"+(allDays.indexOf(arrivalQueue)+1)+" has:");
//			while (!arrivalQueue.isEmpty()) {
//				Customer thisGuy = arrivalQueue.dequeue();
//				System.out.println("Customer arrives at "+thisGuy.getArrivalTime()+
//						" and stays for "+thisGuy.getServiceTime());
//			}
//			System.out.println("Day "+(allDays.indexOf(arrivalQueue)+1)+" done!");
//		}
		
		//Tester for approaches
		ArrayList<String> outputs = new ArrayList<>();
		for (ArrivalQueue arrivalQueue : allDays) {
//		ArrivalQueue arrivalQueue =  new ArrivalQueue();
//		for (int i = 1; i <= 10; i++) {
//			arrivalQueue.enqueue(new Customer(i*2, i));
//			if (i==5)
//				arrivalQueue.enqueue(new Customer((i*2)+1, 20));
//			System.out.println("Customer arrives at "+(i*2)+" and stays for "+i);
//		}
			if (arrivalQueue.isEmpty())
				continue;
			String output = "Number of customers is: \t"+arrivalQueue.size();
//			System.out.print("Number of customers is: "+arrivalQueue.size());
			ArrivalQueue slms1 = arrivalQueue.copy();
			SLMS_Manager slmsManage1 = new SLMS_Manager(1, slms1);
			output+=slmsManage1.execute();
//			System.out.print(slmsManage1.execute());
			ArrivalQueue slms3 = arrivalQueue.copy();
			SLMS_Manager slmsManage3 = new SLMS_Manager(3, slms3);
			output+=slmsManage3.execute();
//			System.out.print(slmsManage3.execute());
			ArrivalQueue slms5 = arrivalQueue.copy();
			SLMS_Manager slmsManage5 = new SLMS_Manager(5, slms5);
			output+=slmsManage5.execute();
//			System.out.print(slmsManage5.execute());
			ArrivalQueue mlms1 = arrivalQueue.copy();
			MLMS_Manager mlmsManage1 = new MLMS_Manager(1, mlms1);
			output+=mlmsManage1.execute();
//			System.out.print(mlmsManage1.execute());
			ArrivalQueue mlms3 = arrivalQueue.copy();
			MLMS_Manager mlmsManage3 = new MLMS_Manager(3, mlms3);
			output+=mlmsManage3.execute();
//			System.out.print(mlmsManage3.execute());
			ArrivalQueue mlms5 = arrivalQueue.copy();
			MLMS_Manager mlmsManage5 = new MLMS_Manager(5, mlms5);
			output+=mlmsManage5.execute();
//			System.out.print(mlmsManage5.execute());
			ArrivalQueue bll1 = arrivalQueue.copy();
			MLMSBLL_Manager bllManage1 = new MLMSBLL_Manager(1, bll1);
			output+=bllManage1.execute();
//			System.out.print(bllManage1.execute());
			ArrivalQueue bll3 = arrivalQueue.copy();
			MLMSBLL_Manager bllManage3 = new MLMSBLL_Manager(3, bll3);
			output+=bllManage3.execute();
//			System.out.print(bllManage3.execute());
			ArrivalQueue bll5 = arrivalQueue.copy();
			MLMSBLL_Manager bllManage5 = new MLMSBLL_Manager(5, bll5);
			output+=bllManage5.execute();
//			System.out.print(bllManage5.execute());
			ArrivalQueue bwt1 = arrivalQueue.copy();
			MLMSBWT_Manager bwtManage1 = new MLMSBWT_Manager(1, bwt1);
			output+=bwtManage1.execute();
//			System.out.print(bwtManage1.execute());
			ArrivalQueue bwt3 = arrivalQueue.copy();
			MLMSBWT_Manager bwtManage3 = new MLMSBWT_Manager(3, bwt3);
			output+=bwtManage3.execute();
//			System.out.print(bwtManage3.execute());
			ArrivalQueue bwt5 = arrivalQueue.copy();
			MLMSBWT_Manager bwtManage5 = new MLMSBWT_Manager(5, bwt5);
			output+=bwtManage5.execute();
//			System.out.print(bwtManage5.execute());
			read.validWriter(allDays.indexOf(arrivalQueue)+1, output);
		}
	}
}

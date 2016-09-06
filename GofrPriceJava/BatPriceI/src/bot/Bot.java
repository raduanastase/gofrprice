package bot;

import java.io.IOException;
//import java.util.Objects;
import java.util.Observable;
import java.util.Observer;

import bot.communicators.DBCommunicator;
import bot.communicators.JSONCommunicator;
import bot.parsers.ParsersManager;
import bot.item.CreatorModule;
import bot.item.ItemData;


public class Bot implements Observer {

	private JSONCommunicator xmlComm;
	private CreatorModule creator;
	private ParsersManager parsersManager;
	private DBCommunicator dbCommunicator;
	private ItemData[] itemDataArr;


	public Bot() throws IOException {

		xmlComm = new JSONCommunicator();

		parsersManager = new ParsersManager();

		dbCommunicator = new DBCommunicator();

		creator = new CreatorModule(xmlComm.getItemsToCreate(), parsersManager.crawlers(), dbCommunicator.getItemsDict());

		//creator listens to the "complete" event and writes the data back to db
		creator.addObserver(this);




		//Hibernate Test - working
		/*ManageEmployee ME = new ManageEmployee();

      *//* Add few employee records in database *//*
		Integer empID1 = ME.addEmployee("Zara", "Ali", 1000);
		Integer empID2 = ME.addEmployee("Daisy", "Das", 5000);
		Integer empID3 = ME.addEmployee("John", "Paul", 10000);

      *//* List down all the employees *//*
		ME.listEmployees();

      *//* Update employee's records *//*
		ME.updateEmployee(empID1, 5000);

      *//* Delete an employee from the database *//*
		ME.deleteEmployee(empID2);

      *//* List down new list of the employees *//*
		ME.listEmployees();*/
	}

	public void update(Observable obs, Object obj) {
		if(obs == creator) {
			System.out.println("STATE CHANGED " + creator.getState());
			if(creator.getState().equals(CreatorModule.COMPLETE)) {
				dbCommunicator.writeDataToDB();
			}
		}
	}

	//todo make it work or make a Debugger Class OR BETTER YET, USE LOG4J
	public static void trace(String value) {
		System.out.println("[LOG]"+value);
	}
}

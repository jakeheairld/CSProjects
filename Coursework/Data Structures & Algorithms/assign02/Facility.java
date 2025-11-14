package assign02;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Scanner;

/**
 * This class represents a record of patients that have visited a UHealth
 * facility.
 *
 * @author Eric Heisler and Jake Heairld and Anton Smolyanyy
 * @version Jan 25, 2024
 */
public class Facility {

	private ArrayList<CurrentPatient> patientList;

	/**
	 * Creates an empty facility record.
	 */
	public Facility() {
		this.patientList = new ArrayList<CurrentPatient>();
	}

	/**
	 * Adds the given patient to the list of patients, avoiding duplicates.
	 *
	 * @param patient - patient to be added to this record
	 * @return true if the patient was added,
	 *         false if the patient was not added because they already exist in the record
	 */
	public boolean addPatient(CurrentPatient patient) {
		if (patientList.contains(patient)) {
			return false;
		}
		patientList.add(patient);
		return true;
	}

	/**
	 * Retrieves the patient with the given UHealthID.
	 *
	 * @param UHealthID of patient to be retrieved
	 * @return the patient with the given ID, or null if no such patient
	 * 			exists in the record
	 */
	public CurrentPatient lookupByUHID(UHealthID patientID) {
		for (int i = 0; i < patientList.size(); i++) {
			if (patientList.get(i).getUHealthID().equals(patientID)) {
				return patientList.get(i);
			}		
		}
		return null;
	}

	/**
	 * Retrieves the patient(s) with the given physician.
	 *
	 * @param physician - physician of patient(s) to be retrieved
	 * @return a list of patient(s) with the given physician (in any order),
	 * 	     or an empty list if no such patients exist in the record
	 */
	public ArrayList<CurrentPatient> lookupByPhysician(int physician) {
		
		ArrayList<CurrentPatient> physiciansPatients = new ArrayList<CurrentPatient>();
		
		for (int i = 0; i < patientList.size(); i++) {
			if (patientList.get(i).getPhysician() == physician) {
				physiciansPatients.add(patientList.get(i));
			}		
		}
		return physiciansPatients;
	}

	/**
	 * Retrieves the patient(s) with last visits older than a given date.
	 *
	 * NOTE: If the last visit date equals this date, do not add the patient.
	 *
	 * @param date - cutoff date later than visit date of all returned patients.
	 * @return a list of patient(s) with last visit date before cutoff (in any order),
	 * 	     or an empty list if no such patients exist in the record
	 */
	public ArrayList<CurrentPatient> getInactivePatients(GregorianCalendar date) {

		ArrayList<CurrentPatient> inactivePatients = new ArrayList<CurrentPatient>();

		for (int i = 0; i < patientList.size(); i++) {
			if (patientList.get(i).getLastVisit().compareTo(date) < 0) {
				inactivePatients.add(patientList.get(i));
			}		
		}
		return inactivePatients;
	}

	/**
	 * Retrieves a list of physicians assigned to patients at this facility.
	 *
	 * * NOTE: Do not put duplicates in the list. Make sure each physician
	 *       is only added once.
	 *
	 * @return a list of physician(s) assigned to current patients,
	 * 	     or an empty list if no patients exist in the record
	 */
	public ArrayList<Integer> getPhysicianList() {

		ArrayList<Integer> physicians = new ArrayList<Integer>();

		for (int i = 0; i < patientList.size(); i++) {
			if (!(physicians.contains(patientList.get(i).getPhysician()))) {
				physicians.add(patientList.get(i).getPhysician());
			}		
		}
		return physicians;
	}

	/**
	 * Sets the physician of a patient with the given UHealthID.
	 *
	 * NOTE: If no patient with the ID exists in the collection, then this
	 * 		method has no effect.
	 *
	 * @param patientID - UHealthID of patient to modify
	 * @param physician - identifier of patient's new physician
	 */
	public void setPhysician(UHealthID patientID, int physician) {
		for(int i = 0; i<patientList.size(); i++) {
			if(patientList.get(i).getUHealthID().equals(patientID)) {
				patientList.get(i).updatePhysician(physician);
				break;
			}
		}
	}

	/**
	 * Sets the last visit date of a patient with the given UHealthID.
	 *
	 * NOTE: If no patient with the ID exists in the collection, then this
	 * 		method has no effect.
	 *
	 * @param patientID - UHealthID of patient to modify
	 * @param date - new date of last visit
	 */
	public void setLastVisit(UHealthID patientID, GregorianCalendar date) {
		for(int i = 0; i<patientList.size(); i++) {
			if(patientList.get(i).getUHealthID().equals(patientID)) {
				patientList.get(i).updateLastVisit(date);
				break;
			}
		}
	}

	// The methods below should not be changed -----------------------------------

	/**
	 * Adds the patients specified by the input file to the record of patients.
	 *
	 * Assumes a very strict file format:
	 * (each line): FirstName LastName ABCD-0123 u0123456 2023 05 16
	 *
	 * Also assumes there are no duplicate patients in the file.
	 *
	 * @param filename - full or relative path to file containing patient data
	 */
	public void addAll(String filename) {
		try {
			Scanner fileIn = new Scanner(new File(filename));
			int lineNumber = 0;

			while (fileIn.hasNextLine()) {
				String line = fileIn.nextLine();
				lineNumber++;
				CurrentPatient patient = parsePatient(line, lineNumber);

				addPatient(patient);
			}  // repeat for more patients

			fileIn.close();
		}
		catch (FileNotFoundException e) {
			System.err.println(e.getMessage() + " No patients added to facility record.");
		}
		catch (ParseException e) {
			System.err.println(e.getLocalizedMessage() + " formatted incorrectly at line " + e.getErrorOffset()
					+ ". No patients added to facility record.");
		}
	}

	/**
	 * Helper method for parsing the information about a patient from file.
	 *
	 * @param line - string to be parsed
	 * @param lineNumber - line number in file, used for error reporting (see above)
	 * @return the Patient constructed from the information
	 * @throws ParseException if file containing line is not properly formatted (see above)
	 */
	private CurrentPatient parsePatient(String line, int lineNumber) throws ParseException {
		Scanner lineIn = new Scanner(line);
		lineIn.useDelimiter(" ");

		if (!lineIn.hasNext()) {
			lineIn.close();
			throw new ParseException("First name", lineNumber);
		}
		String firstName = lineIn.next();

		if (!lineIn.hasNext()) {
			lineIn.close();
			throw new ParseException("Last name", lineNumber);
		}
		String lastName = lineIn.next();

		if (!lineIn.hasNext()) {
			lineIn.close();
			throw new ParseException("UHealth ID", lineNumber);
		}
		String patientIDString = lineIn.next();

		if (!lineIn.hasNext()) {
			lineIn.close();
			throw new ParseException("physician", lineNumber);
		}
		String physicianString = lineIn.next();
		int physician = Integer.parseInt(physicianString.substring(1, 8));

		if (!lineIn.hasNext()) {
			lineIn.close();
			throw new ParseException("year of last visit", lineNumber);
		}
		String yearString = lineIn.next();
		int year = Integer.parseInt(yearString);

		if (!lineIn.hasNext()) {
			lineIn.close();
			throw new ParseException("month of last visit", lineNumber);
		}
		String monthString = lineIn.next();
		int month = Integer.parseInt(monthString);

		if (!lineIn.hasNext()) {
			lineIn.close();
			throw new ParseException("day of last visit", lineNumber);
		}
		String dayString = lineIn.next();
		int day = Integer.parseInt(dayString);

		GregorianCalendar lastVisit = new GregorianCalendar(year, month, day);

		lineIn.close();

		return new CurrentPatient(firstName, lastName, new UHealthID(patientIDString),
								physician, lastVisit);
	}
}

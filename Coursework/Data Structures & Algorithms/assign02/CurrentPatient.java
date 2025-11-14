package assign02;

import java.util.*;

/**
 * This class represents a current patient, with an assigned physician represented by the physician's UNID, a unique
 * UHealthID Identifier, and a date of last visit.
 * 
 * @author Jake Heairld and Anton Smolyanyy
 * @version Jan 25, 2024
 */
public class CurrentPatient extends Patient {
	
	private int physician;
	private GregorianCalendar lastVisit;

	/**
	 * Creates a patient and associates them with provided information.
	 * 
	 * @param firstName - First name of the patient.
	 * @param lastName - Last name of the patient.
	 * @param uHealthID - Unique health identifier associated with the patient.
	 * @param physician - The UNID of the physician assigned to the patient.
	 * @param lastVisit - The date of the patient's last visit.
	 */
	public CurrentPatient(String firstName, String lastName, UHealthID uHealthID, int physician, GregorianCalendar lastVisit) {
		super(firstName, lastName, uHealthID);
		this.physician = physician;
		this.lastVisit = lastVisit;
	}
	
	/**
	 * Retrieves the physician assigned to this patient.
	 * 
	 * @return The physician assigned to this patient.
	 */
	public int getPhysician() {
		return this.physician;
	}
	
	/**
	 * Retrieves the date of last visit for this patient.
	 * 
	 * @return The date of the patients last visit.
	 */
	public GregorianCalendar getLastVisit( ) {
		return this.lastVisit;
	}
	
	/**
	 * Updates the patient's physician to the new physician.
	 * 
	 * @param newPhysician - The new physician assigned to the patient.
	 */
	public void updatePhysician(int newPhysician) {
		this.physician = newPhysician;
	}
	
	/**
	 * Updates the patient's date of last visit.
	 * 
	 * @param date - New date of last visit.
	 */
	public void updateLastVisit(GregorianCalendar date) {
		this.lastVisit = date;
	}
	
}
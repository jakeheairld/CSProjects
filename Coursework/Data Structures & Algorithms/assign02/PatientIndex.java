package assign02;
import java.util.*;

/**
 * This class represents a patient index storing and managing patients
 * each with a unique identifier
 *
 * @author Jake Heairld and Anton Smolyanyy
 * @version Jan 25, 2024
 */
public class PatientIndex{
	
	private TreeMap<UHealthID, String> patientMap;
	
	/**
	 * Creates an empty patient index.
	 */
	public PatientIndex() {
		patientMap = new TreeMap<UHealthID, String>((UHealthID1, UHealthID2) -> (UHealthID1.toString().compareTo(UHealthID2.toString())));
	}
	
	/**
	 * Adds a patient to the patient index, if a patient entry that already exists has the same unique identifier as the
	 * patient being added, update the name of the patient associated with the unique identifier.
	 * 
	 * @param p - The patient to add to the index or the patient that will have their name updated.
	 */
	public void addPatient(Patient p) {
		patientMap.put(p.getUHealthID(), p.getFirstName() + " " + p.getLastName());
	}
	
	/**
	 * Removes the patient from the patient index.
	 * 
	 * @param p - The patient to remove.
	 */
	public void removePatient(Patient p) {
		if (patientMap.containsKey(p.getUHealthID())) {
			patientMap.remove(p.getUHealthID());
		}
	}

	/**
	 * Retrieves the name of the patient associated with the unique identifier.
	 * 
	 * @param id - The UHealthID of the patient who's name is retrieved.
	 * @return The name of the patient associated with the id.
	 */
	public String getName(UHealthID id) {
		if (!(patientMap.containsKey(id))) {
			return null;
		}
		return patientMap.get(id);
	}
	
}
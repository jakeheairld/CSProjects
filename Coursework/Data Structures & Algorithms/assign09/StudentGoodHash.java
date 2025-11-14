package assign09;

import java.text.DecimalFormat;

/**
 * This class provides a simple representation for a University of Utah student.
 * Object's hashCode method is overridden with a correct hash function for this
 * object, but one that does a good job of distributing students in a hash
 * table.
 * 
 * @author Erin Parker and Jake Heairld and Anton Smolyanyy
 * @version April 03, 2024
 */
public class StudentGoodHash {

	private int uid;
	private String firstName;
	private String lastName;

	/**
	 * Creates a new student with the specified uid, firstName, and lastName.
	 * 
	 * @param uid
	 * @param firstName
	 * @param lastName
	 */
	public StudentGoodHash(int uid, String firstName, String lastName) {
		this.uid = uid;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	/**
	 * @return the UID for this student object
	 */
	public int getUid() {
		return this.uid;
	}

	/**
	 * @return the first name for this student object
	 */
	
	public String getFirstName() {
		return this.firstName;
	}
 
	/**
	 * @return the last name for this student object
	 */
	public String getLastName() {
		return this.lastName;
	}

	/**
	 * @return true if this student and 'other' have the same UID, first name, and last name; false otherwise
	 */
	public boolean equals(Object other) {
		// change to StudentMediumHash and StudentGoodHash for two new classes
		if(!(other instanceof StudentGoodHash))
			return false;

		StudentGoodHash rhs = (StudentGoodHash) other;

		return this.uid == rhs.uid && this.firstName.equals(rhs.firstName) && this.lastName.equals(rhs.lastName);
	}
	
	/**
	 * @return a textual representation of this student
	 */
	public String toString() {
		DecimalFormat formatter = new DecimalFormat("0000000");
		return firstName + " " + lastName + " (u" + formatter.format(uid) + ")";
	}

	/**
	 * Generates a hash code for this student.
	 * 
	 * @return - The hash code for this student.
	 */
	public int hashCode() {
 		int hashVal = 17;
 		for(int i = 0; i < this.uid%10 + 5; i++) {
 			hashVal = 19 * hashVal + this.uid%7 + lastName.length() * 7;
 		} 		
 		return hashVal; 	
	}
}

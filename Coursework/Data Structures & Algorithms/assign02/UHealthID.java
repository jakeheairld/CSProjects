package assign02;

/**
 * This class represents a unique UHealthID, which has a four letter
 * prefix and four number suffix, such as ABCD-0123
 *
 * @author Eric Heisler
 * @version May 5, 2023
 */
public class UHealthID {

	private String prefix;

	private String suffix;

	/**
	 * Parses the given string to create a UHealthID.
	 *
	 * @param uHID - the given string, representing a UHealthID
	 */
	public UHealthID(String uHID) {
		uHID = uHID.replaceAll("-|\\s|\\.|\\(|\\)", "");

		boolean isValid = true;
		if (uHID.length() != 8) {
			isValid = false;
		}
		for (int i = 0; isValid && i < 4; i++) {
			if (!Character.isLetter(uHID.charAt(i))) {
				isValid = false;
			}
		}
		for (int i = 4; isValid && i < 8; i++) {
			if (!Character.isDigit(uHID.charAt(i))) {
				isValid = false;
			}
		}

		if (isValid) {
			this.prefix = uHID.substring(0, 4).toUpperCase();
			this.suffix = uHID.substring(4, 8);
		}
		else {
			this.prefix = "XXXX";
			this.suffix = "0000";
			System.err.println("Patient ID \"" + uHID +
								"\" is not formatted correctly, initializing as " +
					            toString() + ".");
		}
	}

	/**
	 * Two patient IDs are considered equal if they have the same prefix and
	 * suffix numbers.
	 *
	 * @param other - the object being compared with this UHealth ID
	 * @return true if the other object is a UHealthID type and is equal to
	 * 			this ID, false otherwise
	 */
	public boolean equals(Object other) {
		if (!(other instanceof UHealthID)) {
			return false;
		}
		UHealthID otherID = (UHealthID) other;

		return otherID.prefix.equals(this.prefix) && otherID.suffix.equals(this.suffix);
	}

	/**
	 * Returns a textual representation of this UHealth ID.
	 */
	public String toString() {
		return this.prefix + "-" + this.suffix;
	}
}

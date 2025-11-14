package assign09;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * This is a tester class for custom hashCode methods.
 * 
 * @author Jake Heairld and Anton Smolyanyy
 * @version April 04, 2024
 */  
class HashCodeTest {
	
	@Test
	void testStudentBadHash() {
		StudentBadHash joe = new StudentBadHash(1024513, "Joe", "Johnson");
		assertEquals(3, joe.hashCode());
	}
	
	@Test
	void testStudentMediumHash() {
		StudentMediumHash bill = new StudentMediumHash(1058758, "Bill", "Ward");
		assertEquals(387, bill.hashCode());
	}
	
	@Test
	void testStudentGoodHash() {
		StudentGoodHash samantha = new StudentGoodHash(1066947, "Samantha", "Victoria");
		assertEquals(1544228993, samantha.hashCode());
	}
	
}
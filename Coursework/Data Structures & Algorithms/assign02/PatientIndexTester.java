package assign02;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * This class contains tests for PatientIndex.
 *
 * @author Jake Heairld and Anton Smolyanyy
 * @version Jan 25, 2024
 */
class PatientIndexTester {

	private PatientIndex emptyIndex, testerIndex, filledIndex;
	private UHealthID id1, id2, id3, id4;
	private Patient p1, p2, p3, p4, p5;
	
	@BeforeEach
	void setUp() {
		emptyIndex = new PatientIndex();
		testerIndex = new PatientIndex();
		filledIndex = new PatientIndex();
		id1 = new UHealthID("XXXX-1111");
 		id2 = new UHealthID("BBBB-1111");
 		id3 = new UHealthID("FFFF-1111");
 		id4 = new UHealthID("BBBB-2222");
 		p1 = new Patient("John", "Mark", id1);
 		p2 = new Patient("John", "Mark", id2);
 		p3 = new Patient("Melanie", "Hoover", id3);
 		p4 = new Patient("Bryan", "Ward", id4);
 		p5 = new Patient("Susan", "Stealer", id1);
 		filledIndex.addPatient(p1);
 		filledIndex.addPatient(p2);
 		filledIndex.addPatient(p3);
 		filledIndex.addPatient(p4);
	}
	
	@Test
	void testGetNameEmpty() {
		assertNull(emptyIndex.getName(id1));
	}
	
	@Test
	void testGetNameNoPatient() {
		assertNull(filledIndex.getName(new UHealthID("WXYZ-3251")));
	}
	
	@Test
	void testGetSameNamePatients() {
		assertTrue(filledIndex.getName(id1).equals("John Mark") &&
				filledIndex.getName(id2).equals("John Mark"));
	}
	
	@Test
	void testGetNamePatients() {
		assertTrue(filledIndex.getName(id1).equals("John Mark") &&
				filledIndex.getName(id2).equals("John Mark") &&
				filledIndex.getName(id3).equals("Melanie Hoover") &&
				filledIndex.getName(id4).equals("Bryan Ward"));
	}
	
	@Test
	void testAddPatientSingle() {
		assertNull(testerIndex.getName(id1));
		testerIndex.addPatient(p1);
		assertEquals(testerIndex.getName(id1), "John Mark");
	}
	
	@Test
	void testAddSamePatientTwice() {
		testerIndex.addPatient(p1);
		testerIndex.addPatient(p1);
		assertEquals(testerIndex.getName(id1), "John Mark");
	}
	
	@Test
	void testAddPatientUpdateName() {
		testerIndex.addPatient(p1);
		testerIndex.addPatient(p5);
		assertEquals(testerIndex.getName(id1), "Susan Stealer");
	}
	
	@Test
	void testAddPatientUpdateNameTwice() {
		testerIndex.addPatient(p1);
		testerIndex.addPatient(p5);
		assertEquals(testerIndex.getName(id1), "Susan Stealer");
		testerIndex.addPatient(p1);
		assertEquals(testerIndex.getName(id1), "John Mark");
	}
	
	@Test
	void testAddTwoPatient() {
		testerIndex.addPatient(p1);
		testerIndex.addPatient(p3);
		assertTrue(testerIndex.getName(id3).equals("Melanie Hoover") &&
				testerIndex.getName(id1).equals("John Mark"));
		
	}
	
	@Test
	void testUpdateSingleNameIndex() {
		filledIndex.addPatient(new Patient("Updated", "Name", id3));
		assertEquals(filledIndex.getName(id3), "Updated Name");
	}
	
	@Test
	void testRemovePatientEmpty() {
		emptyIndex.removePatient(p1);
		assertNull(emptyIndex.getName(id1));
	}

	@Test
	void testRemovePatientNotIncluded() {
		filledIndex.removePatient(new Patient("Don't", "Exist", new UHealthID("WWWW-1234")));
		assertTrue(filledIndex.getName(id1).equals("John Mark") &&
				filledIndex.getName(id2).equals("John Mark") &&
				filledIndex.getName(id3).equals("Melanie Hoover") &&
				filledIndex.getName(id4).equals("Bryan Ward"));
	}
	
	@Test
	void testRemovePatientSameUHID() {
		filledIndex.removePatient(new Patient("Don't", "Exist", id1));
		assertNull(filledIndex.getName(id1));
	}
	
	@Test
	void testRemovePatientSingle() {
		testerIndex.addPatient(p1);
		testerIndex.removePatient(p1);
		assertNull(testerIndex.getName(id1));
	}
	
	@Test
	void testRemovePatientDouble() {
		filledIndex.removePatient(p2);
		filledIndex.removePatient(p3);
		assertNull(filledIndex.getName(id2));
		assertNull(filledIndex.getName(id3));
		assertEquals(filledIndex.getName(id1), "John Mark");
	}

}
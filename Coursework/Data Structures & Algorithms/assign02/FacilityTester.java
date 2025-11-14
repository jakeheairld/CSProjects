package assign02;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashSet;

/**
 * This class contains tests for Facility.
 *
 * @author Eric Heisler and Anton Smolyanyy and Jake Heairld
 * @version Jan 25, 2024
 */
public class FacilityTester {

	private Facility emptyFacility, verySmallFacility, smallFacility, largeFacility;
	private UHealthID uHID1, uHID2, uHID3;
	private GregorianCalendar date1, date2, date3;
	
	// IDs, dates, names
	private UHealthID[] uHIDs;
	private GregorianCalendar[] dates;
	private String[] firstNames, lastNames;
	
	int nPatients = 1000;
	int nPhysicians = 10;
	
	// A private helper to generate UHIDs
	private UHealthID[] generateUHIDs(String prefix, int howMany) {
		UHealthID[] ids = new UHealthID[howMany];
		for (int i = 0; i < howMany; i++)
			ids[i] = new UHealthID(prefix + "-" + String.format("%04d", i));
		return ids;
	}

	// A private helper to generate dates
	private GregorianCalendar[] generateDates(int howMany) {
		GregorianCalendar[] dates = new GregorianCalendar[howMany];
		for (int i = 0; i < howMany; i++)
			dates[i] = new GregorianCalendar(2000 + i%22, i%12, i%28);
		return dates;
	}

	// A private helper to generate names
	private String[] generateNames(int howMany, int a, int b) {
		String[] names = new String[howMany];
		for (int i = 0; i < howMany; i++)
			names[i] = (char)('A' + (i+a) % 26) + "" + (char)('a' + (b*i) % 26);
		return names;
	}

	@BeforeEach
	void setUp() throws Exception {

		uHID1 = new UHealthID("AAAA-1111");
		uHID2 = new UHealthID("BCBC-2323");
		uHID3 = new UHealthID("HRHR-7654");

		date1 = new GregorianCalendar(2023, 0, 1);
		date2 = new GregorianCalendar(2023, 3, 17);
		date3 = new GregorianCalendar(2022, 8, 21);

		emptyFacility = new Facility();

		verySmallFacility = new Facility();
		verySmallFacility.addPatient(new CurrentPatient("Jane", "Doe", uHID1, 1010101, date1));
		verySmallFacility.addPatient(new CurrentPatient("Drew", "Hall", uHID2, 3232323, date2));
		verySmallFacility.addPatient(new CurrentPatient("Riley", "Nguyen", uHID3, 9879876, date3));

		smallFacility = new Facility();
		smallFacility.addAll("src/assign02/small_patient_list.txt");

		largeFacility = new Facility();
		
		uHIDs = generateUHIDs("PATS", nPatients); 

		dates = generateDates(nPatients);

		firstNames = generateNames(nPatients, 1, 2);
		lastNames = generateNames(nPatients, 5, 3);

		for (int i = 0; i < nPatients; i++) {
			largeFacility.addPatient(new CurrentPatient(firstNames[i], lastNames[i], uHIDs[i], 1234567 + i%nPhysicians, dates[i]));
		}
	}

	// Empty Facility tests --------------------------------------------------------

	@Test
	public void testEmptyLookupUHID() {
		assertNull(emptyFacility.lookupByUHID(uHID1));
	}

	@Test
	public void testEmptyLookupPhysician() {
		ArrayList<CurrentPatient> patients = emptyFacility.lookupByPhysician(1010101);
		assertEquals(0, patients.size());
	}

	@Test
	public void testEmptySetVisit() {
		emptyFacility.setLastVisit(uHID2, date3);
	}

	@Test
	public void testEmptySetPhysician() {
		emptyFacility.setPhysician(uHID2, 1010101);
	}

	@Test
	public void testEmptyGetInactivePatients() {
		ArrayList<CurrentPatient> patients = emptyFacility.getInactivePatients(date3);
		assertEquals(0, patients.size());
	}
	
	@Test
	public void testEmptyGetPhysicianList() {
		ArrayList<Integer> actual = emptyFacility.getPhysicianList();
		assertEquals(0, actual.size());
	}

	// Very small facility tests ---------------------------------------------------

	@Test
	public void testVerySmallAddNewPatient() {
		assertTrue(verySmallFacility.addPatient(new CurrentPatient("Jane", "Doe", new UHealthID("BBBB-2222"), 1010101, date1)));
	}
	
	@Test
	public void testVerySmallAddPatientFalse() {
		assertFalse(verySmallFacility.addPatient(new CurrentPatient("Jane", "Doe", new UHealthID("AAAA-1111"), 1010101, date1)));
	}
	
	@Test
	public void testVerySmallLookupUHID() {
		Patient expected = new Patient("Drew", "Hall", new UHealthID("BCBC-2323"));
		CurrentPatient actual = verySmallFacility.lookupByUHID(new UHealthID("BCBC-2323"));
		assertEquals(expected, actual);
	}
	
	@Test
	public void testVerySmallLookupByUHIDNull() {
		assertNull(verySmallFacility.lookupByUHID(new UHealthID("DHJG-6511")));
	}


	@Test
	public void testVerySmallLookupPhysicianCount() {
		ArrayList<CurrentPatient> actualPatients = verySmallFacility.lookupByPhysician(9879876);
		assertEquals(1, actualPatients.size());
	}

	@Test
	public void testVerySmallLookupPhysicianPatient() {
		Patient expectedPatient = new Patient("Riley", "Nguyen", new UHealthID("HRHR-7654"));
		ArrayList<CurrentPatient> actualPatients = verySmallFacility.lookupByPhysician(9879876);
		assertEquals(expectedPatient, actualPatients.get(0));
	}
	
	@Test
	public void testVerySmallLookupByPhysicianReturnsEmptyList() {
		
		ArrayList<CurrentPatient> actualPatients = verySmallFacility.lookupByPhysician(2222222);
		assertEquals(0, actualPatients.size());
	}
	
	@Test
	public void testVerySmallGetInactivePatients() {
		ArrayList<CurrentPatient> actual = verySmallFacility.getInactivePatients(new GregorianCalendar(2023, 0, 0));
		assertEquals(1, actual.size());
	}
	
	@Test
	public void testVerySmallGetInactivePatientsReturnsEmptyList() {
		ArrayList<CurrentPatient> actual = verySmallFacility.getInactivePatients(new GregorianCalendar(1920, 0, 0));
		assertEquals(0, actual.size());
	}

	@Test
	public void testVerySmallGetPhysicianList() {
		ArrayList<Integer> actual = verySmallFacility.getPhysicianList();
		assertEquals(3, actual.size());
	}
	
	@Test
	public void testVerySmallGetPhysicianListUniqueEntries() {
		HashSet<Integer> physicians = new HashSet<Integer>();
		ArrayList<Integer> actual = verySmallFacility.getPhysicianList();
		physicians.addAll(actual);
		assertEquals(physicians.size(), actual.size());
	}
	
	@Test
	public void testVerySmallUpdatePhysician() {
		verySmallFacility.lookupByUHID(uHID1).updatePhysician(9090909);
		CurrentPatient patient = verySmallFacility.lookupByUHID(uHID1);
		assertEquals(9090909, patient.getPhysician());
	}
	
	@Test
	public void testVerySmallSetLastVisit() {
		
		verySmallFacility.lookupByUHID(uHID3).updateLastVisit(new GregorianCalendar(2023, 4, 3));
		CurrentPatient patient = verySmallFacility.lookupByUHID(uHID3);
		assertEquals(new GregorianCalendar(2023, 4, 3), patient.getLastVisit());
	}

	// Small facility tests -------------------------------------------------------------------------

	@Test
	public void testSmallAddPatientTrue() {
		assertTrue(smallFacility.addPatient(new CurrentPatient("Jane", "Doe", new UHealthID("BBBB-2222"), 1010101, new GregorianCalendar(2006, 7, 3))));
	}
	
	@Test
	public void testSmallAddPatientFalse() {
		assertFalse(smallFacility.addPatient(new CurrentPatient("Blake", "Bird", new UHealthID("JHSD-7483"), 0000000, new GregorianCalendar(2000, 2, 3))));
	}
	
	@Test
	public void testSmallLookupByUHID() {
		Patient expectedPatient = new Patient("Kennedy", "Miller", new UHealthID("QRST-3456"));
		assertEquals(expectedPatient, smallFacility.lookupByUHID(new UHealthID("QRST-3456")));
	}
	
	@Test
	public void testSmallLookupByUHIDNull() {
		assertNull(smallFacility.lookupByUHID(new UHealthID("ABHD-3456")));
	}
	
	@Test
	public void testSmallLookupPhysicianCount() {
		ArrayList<CurrentPatient> actualPatients = smallFacility.lookupByPhysician(8888888);
		assertEquals(2, actualPatients.size());
	}
	
	@Test
	public void testSmallLookupPhysicianPatient() {
		Patient expectedPatient1 = new Patient("Kennedy", "Miller", new UHealthID("QRST-3456"));
		Patient expectedPatient2 = new Patient("Taylor", "Miller", new UHealthID("UVWX-7890"));

		ArrayList<CurrentPatient> actualPatients = smallFacility.lookupByPhysician(8888888);
		assertTrue(actualPatients.contains(expectedPatient1) && actualPatients.contains(expectedPatient2));
	}
	
	@Test
	public void testSmallLookupByPhysicianEmptyList() {
		
		ArrayList<CurrentPatient> actualPatients = smallFacility.lookupByPhysician(2222222);
		assertEquals(0, actualPatients.size());
	}
	
	@Test
	public void testSmallGetInactivePatients() {
		ArrayList<CurrentPatient> actual = smallFacility.getInactivePatients(new GregorianCalendar(2020, 0, 0));
		assertEquals(9, actual.size());
	}
	
	@Test
	public void testSmallGetInactivePatientsEmptyList() {
		ArrayList<CurrentPatient> actual = smallFacility.getInactivePatients(new GregorianCalendar(1920, 0, 0));
		assertEquals(0, actual.size());
	}
	
	@Test
	public void testSmallGetPhysicianList() {
		ArrayList<Integer> actual = smallFacility.getPhysicianList();
		assertEquals(7, actual.size());
	}
	
	@Test
	public void testSmallGetPhysicianListUniqueEntries() {
		HashSet<Integer> physicians = new HashSet<Integer>();
		ArrayList<Integer> actual = smallFacility.getPhysicianList();
		physicians.addAll(actual);
		assertEquals(physicians.size(), actual.size());
	}
	
	@Test
	public void testSetPhysician() {
		
		Patient expectedPatient = new Patient("Samantha", "Schooner", new UHealthID("OUDC-6143"));
		smallFacility.setPhysician(new UHealthID("OUDC-6143"), 9999999);
		ArrayList<CurrentPatient> actual = smallFacility.lookupByPhysician(9999999);

		assertTrue(actual.contains(expectedPatient));
	}
	
	@Test
	public void testSetLastVisit() {
		
		smallFacility.lookupByUHID(new UHealthID("OUDC-6143")).updateLastVisit(new GregorianCalendar(2021, 4, 3));
		CurrentPatient patient = smallFacility.lookupByUHID(new UHealthID("OUDC-6143"));
		assertEquals(new GregorianCalendar(2021, 4, 3), patient.getLastVisit());

	}
	
	// Large facility tests -------------------------------------------------------------------------

	@Test
	public void testLargeLookupPhysicianCount() {
		ArrayList<CurrentPatient> actualPatients = largeFacility.lookupByPhysician(1234567);
		assertEquals(100, actualPatients.size());
	}
	
	@Test
	public void testLargeLookupByPhysicianReturnsEmptyList() {
		
		ArrayList<CurrentPatient> actualPatients = largeFacility.lookupByPhysician(2222222);
		assertEquals(0, actualPatients.size());
	}

	@Test
	public void testLargeGetInactivePatientsReturnsEmptyList() {
		ArrayList<CurrentPatient> actual = largeFacility.getInactivePatients(new GregorianCalendar(1920, 0, 0));
		assertEquals(0, actual.size());
	}
	
	@Test
	public void testLargeGetPhysicianList() {
		ArrayList<Integer> actual = largeFacility.getPhysicianList();
		assertEquals(10, actual.size());
	}
	
	@Test
	public void testLargeGetPhysicianListUniqueEntries() {
		HashSet<Integer> physicians = new HashSet<Integer>();
		ArrayList<Integer> actual = largeFacility.getPhysicianList();
		physicians.addAll(actual);
		assertEquals(physicians.size(), actual.size());
	}
	
}
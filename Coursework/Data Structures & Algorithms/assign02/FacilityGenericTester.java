package assign02;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.GregorianCalendar;

/**
 * This class contains tests for FacilityGeneric.
 *
 * @author Eric Heisler and Jake Heairld and Anton Smolyanyy
 * @version Jan 25, 2024
 */
public class FacilityGenericTester {
	// Generic Facility
	private FacilityGeneric<Integer> uNIDFacility, emptyFacility, phase3Facility;
	private FacilityGeneric<UHealthID> uHIDFacility;
	private FacilityGeneric<String> nameFacility;
	// IDs, dates, names
	private UHealthID[] uHIDs1, uHIDs2;
	private GregorianCalendar[] dates;
	private String[] firstNames, lastNames, physicianNames;
	// For phase 3
	private UHealthID p3id1, p3id2, p3id3, p3id4;
	private GregorianCalendar p3date1, p3date2, p3date3, p3date4;

	// Don't change these numbers. It will affect some tests.
	int nPatients = 20;
	int nPhysicians = 8;

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

		uHIDs1 = generateUHIDs("PATS", nPatients); // for patients
		uHIDs2 = generateUHIDs("DOCS", nPhysicians); // for physicians

		dates = generateDates(nPatients);

		firstNames = generateNames(nPatients, 1, 2);
		lastNames = generateNames(nPatients, 5, 3);
		physicianNames = generateNames(nPhysicians, 10, 4);

		uNIDFacility = new FacilityGeneric<Integer>();
		uHIDFacility = new FacilityGeneric<UHealthID>();
		nameFacility = new FacilityGeneric<String>();
		emptyFacility = new FacilityGeneric<Integer>();
		phase3Facility = new FacilityGeneric<Integer>();

		for (int i = 0; i < nPatients; i++) {
			uNIDFacility.addPatient(new CurrentPatientGeneric<Integer>(
										firstNames[i], lastNames[i],
										uHIDs1[i], 1234567 + i%nPhysicians, dates[i]));
			uHIDFacility.addPatient(new CurrentPatientGeneric<UHealthID>(
										firstNames[i], lastNames[i],
										uHIDs1[i], uHIDs2[i%nPhysicians], dates[i]));
			nameFacility.addPatient(new CurrentPatientGeneric<String>(
										firstNames[i], lastNames[i],
										uHIDs1[i], physicianNames[i%nPhysicians], dates[i]));
		}

		p3id1 = new UHealthID("XXXX-1111");
 		p3id2 = new UHealthID("BBBB-1111");
 		p3id3 = new UHealthID("FFFF-1111");
 		p3id4 = new UHealthID("BBBB-2222");
 		p3date1 = new GregorianCalendar(2019, 1, 5);
 		p3date2 = new GregorianCalendar(2019, 1, 4);
 		p3date3 = new GregorianCalendar(2019, 1, 3);
 		p3date4 = new GregorianCalendar(2019, 1, 2);

		phase3Facility.addPatient(new CurrentPatientGeneric<Integer>("A", "B", new UHealthID("XXXX-1111"), 7, new GregorianCalendar(2019, 1, 5)));
		phase3Facility.addPatient(new CurrentPatientGeneric<Integer>("A", "B", new UHealthID("BBBB-1111"), 7, new GregorianCalendar(2019, 1, 4)));
		phase3Facility.addPatient(new CurrentPatientGeneric<Integer>("A", "C", new UHealthID("FFFF-1111"), 7, new GregorianCalendar(2019, 1, 3)));
		phase3Facility.addPatient(new CurrentPatientGeneric<Integer>("R", "T", new UHealthID("BBBB-2222"), 7, new GregorianCalendar(2019, 1, 2)));
	}

	// empty Facility tests --------------------------------------------------------

	@Test
	public void testEmptySetVisit() {
		// ensure no exceptions thrown
		emptyFacility.setLastVisit(uHIDs1[0], dates[3]);
	}

	@Test
	public void testEmptySetPhysician() {
		// ensure no exceptions thrown
		emptyFacility.setPhysician(uHIDs1[0], 1010101);
	}

	@Test
	public void testEmptyGetInactivePatients() {
		ArrayList<CurrentPatientGeneric<Integer>> patients = emptyFacility.getInactivePatients(dates[4]);
		assertEquals(0, patients.size());
	}
	
	@Test
	public void testEmptyGetPhysicianList() {
		ArrayList<Integer> actual = emptyFacility.getPhysicianList();
		assertEquals(0, actual.size());
	}
	
	@Test
	public void testEmptyAddPatientFacilityPhysicianSize() {
		FacilityGeneric<String> twoPatientFacility = new FacilityGeneric<String>();
		twoPatientFacility.addPatient(new CurrentPatientGeneric<String>("John", "Doe", new UHealthID("YYYY-2222"), "Doctor Joe", new GregorianCalendar(2020, 5, 1)));
		twoPatientFacility.addPatient(new CurrentPatientGeneric<String>("Anton", "Vargas", new UHealthID("XXXX-1111"), "Doctor Joe", new GregorianCalendar(2019, 1, 5)));
		ArrayList<String> actual = twoPatientFacility.getPhysicianList();
		assertEquals(1, actual.size());
	}
	
	@Test
	public void testEmptyAddPatientFacilityPatientDize() {
		FacilityGeneric<String> twoPatientFacility = new FacilityGeneric<String>();
		twoPatientFacility.addPatient(new CurrentPatientGeneric<String>("John", "Doe", new UHealthID("YYYY-2222"), "Doctor Joe", new GregorianCalendar(2020, 5, 1)));
		twoPatientFacility.addPatient(new CurrentPatientGeneric<String>("Anton", "Vargas", new UHealthID("XXXX-1111"), "Doctor Joe", new GregorianCalendar(2019, 1, 5)));
		ArrayList<CurrentPatientGeneric<String>> actual = twoPatientFacility.lookupByPhysician("Doctor Joe");
		assertEquals(2, actual.size());
	}
	
	@Test
	public void testEmptyLookupUHID() {
		assertNull(emptyFacility.lookupByUHID(uHIDs1[0]));
	}
	
	@Test
	public void testEmptyLookupPhysician() {
		ArrayList<CurrentPatientGeneric<Integer>> patients = emptyFacility.lookupByPhysician(1010101);
		assertEquals(0, patients.size());
	}
	
	// uNID Facility tests --------------------------------------------------------
	
	@Test
	public void testUNIDAddDuplicatePatient() {
		assertFalse(uNIDFacility.addPatient(new CurrentPatientGeneric<Integer>(firstNames[1], lastNames[1], new UHealthID(uHIDs1[1].toString()), 1234568, dates[1])));
	}
	
	@Test
	public void testUNIDAddNewPatient() {
		assertTrue(uNIDFacility.addPatient(new CurrentPatientGeneric<Integer>(firstNames[1], lastNames[1], new UHealthID("ZZZZ-9999"), 1234568, dates[1])));
	}
	
	public void testUNIDLookupUHID() {
		Patient expectedPatient1 = new Patient(firstNames[1], lastNames[1], new UHealthID(uHIDs1[1].toString()));
		assertEquals(expectedPatient1, uNIDFacility.lookupByUHID(uHIDs1[1]));
	}
	
	@Test
	public void testUNIDLookupUHIDReturnsNull() {
		assertNull(uNIDFacility.lookupByUHID(new UHealthID("ZZZZ-9999")));
	}
	
	@Test
	public void testUNIDLookupPhysician() {
		Patient expectedPatient1 = new Patient(firstNames[1], lastNames[1], new UHealthID(uHIDs1[1].toString()));
		Patient expectedPatient2 = new Patient(firstNames[9], lastNames[9], new UHealthID(uHIDs1[9].toString()));
		ArrayList<CurrentPatientGeneric<Integer>> actualPatients = uNIDFacility.lookupByPhysician(1234568);
		assertTrue(actualPatients.contains(expectedPatient1) && actualPatients.contains(expectedPatient2));
	}
	
	@Test
	public void testUNIDLookupPhysicianReturnsEmptyList() {
		ArrayList<CurrentPatientGeneric<Integer>> actualPatients = uNIDFacility.lookupByPhysician(0000000);
		assertEquals(0, actualPatients.size());
	}


	@Test
	public void testUNIDLookupPhysicianCount() {
		ArrayList<CurrentPatientGeneric<Integer>> actualPatients = uNIDFacility.lookupByPhysician(1234568);
		assertEquals(3, actualPatients.size());
	}

	@Test
	public void testUNIDLookupPhysicianPatient() {
		Patient expectedPatient = new Patient(firstNames[1], lastNames[1], new UHealthID(uHIDs1[1].toString()));
		ArrayList<CurrentPatientGeneric<Integer>> actualPatients = uNIDFacility.lookupByPhysician(1234568);
		assertEquals(expectedPatient, actualPatients.get(0));
	}
	
	@Test
	public void testUNIDGetInactivePatients() {
		ArrayList<CurrentPatientGeneric<Integer>> actual = uNIDFacility.getInactivePatients(new GregorianCalendar(2010, 0, 0));
		assertEquals(10, actual.size());
	}
	
	@Test
	public void testUNIDGetInactivePatientsReturnsEmptyList() {
		ArrayList<CurrentPatientGeneric<Integer>> actual = uNIDFacility.getInactivePatients(new GregorianCalendar(1999, 0, 0));
		assertEquals(0, actual.size());
	}

	@Test
	public void testUNIDGetPhysicianList() {
		ArrayList<Integer> actual = uNIDFacility.getPhysicianList();
		assertEquals(8, actual.size());
	}
	
	@Test
	public void testUNIDSetLastVisitPhysician() {
		uNIDFacility.setLastVisit(uHIDs1[1], p3date2);
		assertEquals(p3date2, uNIDFacility.lookupByUHID(uHIDs1[1]).getLastVisit());
	}

	// UHealthID facility tests ---------------------------------------------------
	
	@Test
	public void testUHIDAddDuplicatePatient() {
		assertFalse(uHIDFacility.addPatient(new CurrentPatientGeneric<UHealthID>(firstNames[1], lastNames[1], new UHealthID(uHIDs1[1].toString()), uHIDs2[1], dates[1])));
	}
	
	@Test
	public void testUHIDAddNewPatient() {
		assertTrue(uHIDFacility.addPatient(new CurrentPatientGeneric<UHealthID>(firstNames[1], lastNames[1], new UHealthID("ZZZZ-9999"), uHIDs2[1], dates[1])));
	}

	@Test
	public void testUHIDLookupUHID() {
		Patient expected = new Patient(firstNames[0], lastNames[0], new UHealthID(uHIDs1[0].toString()));
		CurrentPatientGeneric<UHealthID> actual = uHIDFacility.lookupByUHID(new UHealthID(uHIDs1[0].toString()));
		assertEquals(expected, actual);
	}
	
	@Test
	public void testUHIDLookupUHIDReturnsNull() {
		assertNull(uHIDFacility.lookupByUHID(new UHealthID("ZZZZ-9999")));
	}

	@Test
	public void testUHIDLookupPhysician() {
		Patient expectedPatient1 = new Patient(firstNames[1], lastNames[1], new UHealthID(uHIDs1[1].toString()));
		Patient expectedPatient2 = new Patient(firstNames[9], lastNames[9], new UHealthID(uHIDs1[9].toString()));
		ArrayList<CurrentPatientGeneric<UHealthID>> actualPatients = uHIDFacility.lookupByPhysician(uHIDs2[1]);
		assertTrue(actualPatients.contains(expectedPatient1) && actualPatients.contains(expectedPatient2));
	}
	
	@Test
	public void testUHIDLookupPhysicianCount() {
		ArrayList<CurrentPatientGeneric<UHealthID>> actualPatients = uHIDFacility.lookupByPhysician(uHIDs2[1]);
		assertEquals(3, actualPatients.size());
	}
	
	@Test
	public void testUHIDLookupPhysicianReturnsEmptyList() {
		ArrayList<CurrentPatientGeneric<UHealthID>> actualPatients = uHIDFacility.lookupByPhysician(p3id1);
		assertEquals(0, actualPatients.size());
	}

	@Test
	public void testUHIDUpdatePhysician() {
		uHIDFacility.lookupByUHID(uHIDs1[2]).updatePhysician(uHIDs2[0]);
		CurrentPatientGeneric<UHealthID> patient = uHIDFacility.lookupByUHID(uHIDs1[2]);
		assertEquals(uHIDs2[0], patient.getPhysician());
	}

	@Test
	public void testUHIDGetInactivePatients() {
		ArrayList<CurrentPatientGeneric<UHealthID>> actual = uHIDFacility.getInactivePatients(new GregorianCalendar(2010, 0, 0));
		assertEquals(10, actual.size());
	}
	
	@Test
	public void testUHIDGetInactivePatientsReturnsEmptyList() {
		ArrayList<CurrentPatientGeneric<UHealthID>> actual = uHIDFacility.getInactivePatients(new GregorianCalendar(1999, 0, 0));
		assertEquals(0, actual.size());
	}
	
	@Test
	public void testUHIDGetPhysicianList() {
		ArrayList<UHealthID> actual = uHIDFacility.getPhysicianList();
		assertEquals(8, actual.size());
	}
	
	@Test
	public void testUHIDSetPhysician() {
		uHIDFacility.setPhysician(uHIDs1[1], uHIDs2[4]);
		assertEquals(uHIDs2[4], uHIDFacility.lookupByUHID(uHIDs1[1]).getPhysician());
	}
	
	@Test
	public void testUHIDSetLastVisitPhysician() {
		uHIDFacility.setLastVisit(uHIDs1[1], p3date2);
		assertEquals(p3date2, uHIDFacility.lookupByUHID(uHIDs1[1]).getLastVisit());
	}

	// name facility tests -------------------------------------------------------------------------

    @Test
	public void testNameAddDuplicatePatient() {
		assertFalse(nameFacility.addPatient(new CurrentPatientGeneric<String>(firstNames[1], lastNames[1], new UHealthID(uHIDs1[1].toString()), physicianNames[1], dates[1])));
	}
	
	@Test
	public void testNameAddNewPatient() {
		assertTrue(nameFacility.addPatient(new CurrentPatientGeneric<String>(firstNames[1], lastNames[1], new UHealthID("ZZZZ-9999"), physicianNames[4], dates[1])));
	}
	
	@Test
	public void testNameLookupUHID() {
		Patient expectedPatient1 = new Patient(firstNames[1], lastNames[1], new UHealthID(uHIDs1[1].toString()));
		assertEquals(expectedPatient1, nameFacility.lookupByUHID(uHIDs1[1]));
	}
	
	@Test
	public void testNameLookupUHIDReturnsNull() {
		assertNull(nameFacility.lookupByUHID(new UHealthID("ZZZZ-9999")));
	}
	
	@Test
	public void testNameLookupPhysician() {
		Patient expectedPatient1 = new Patient(firstNames[1], lastNames[1], new UHealthID(uHIDs1[1].toString()));
		Patient expectedPatient2 = new Patient(firstNames[9], lastNames[9], new UHealthID(uHIDs1[9].toString()));

		ArrayList<CurrentPatientGeneric<String>> actualPatients = nameFacility.lookupByPhysician(physicianNames[1]);
		assertTrue(actualPatients.contains(expectedPatient1) && actualPatients.contains(expectedPatient2));
	}
	
	@Test
	public void testNameLookupPhysicianReturnsEmptyList() {
		ArrayList<CurrentPatientGeneric<String>> actualPatients = nameFacility.lookupByPhysician("False Doctor");
		assertEquals(0, actualPatients.size());
	}
	
	@Test
	public void testNameLookupPhysicianCount() {
		ArrayList<CurrentPatientGeneric<String>> actualPatients = nameFacility.lookupByPhysician(physicianNames[1]);
		assertEquals(3, actualPatients.size());
	}

	@Test
	public void testNameGetInactivePatients() {
		ArrayList<CurrentPatientGeneric<String>> actual = nameFacility.getInactivePatients(new GregorianCalendar(2010, 0, 0));
		assertEquals(10, actual.size());
	}
	
	@Test
	public void testNameGetInactivePatientsReturnsEmptyList() {
		ArrayList<CurrentPatientGeneric<String>> actual = nameFacility.getInactivePatients(new GregorianCalendar(1999, 0, 0));
		assertEquals(0, actual.size());
	}

	@Test
	public void testNameGetPhysicianList() {
		ArrayList<String> actual = nameFacility.getPhysicianList();
		assertEquals(8, actual.size());
	}
	
	@Test
	public void testNameSetPhysician() {
		nameFacility.setPhysician(uHIDs1[1], physicianNames[6]);
		assertEquals(physicianNames[6], nameFacility.lookupByUHID(uHIDs1[1]).getPhysician());
	}
	
	@Test
	public void testNameSetLastVisitPhysician() {
		nameFacility.setLastVisit(uHIDs1[1], p3date2);
		assertEquals(p3date2, nameFacility.lookupByUHID(uHIDs1[1]).getLastVisit());
	}
	
	// phase 3 tests ---------------------------------------------------------------------------

	@Test
 	public void testOrderedByUHIDCount() {
 		ArrayList<CurrentPatientGeneric<Integer>> actual = phase3Facility.getOrderedByUHealthID();
 		assertEquals(4, actual.size());
 	}

	@Test  
 	public void testOrderedByUHIDCountEmpty() {
 		ArrayList<CurrentPatientGeneric<Integer>> actual = emptyFacility.getOrderedByUHealthID();
 		assertEquals(0, actual.size());
 	}
	
	@Test
	public void testOrderedByUHIDOrder() {
		ArrayList<CurrentPatientGeneric<Integer>> actual = phase3Facility.getOrderedByUHealthID();
 		assertTrue(actual.get(3).equals(new CurrentPatientGeneric<Integer>("A", "B", p3id1, 7, p3date1)) &&
 				actual.get(0).equals(new CurrentPatientGeneric<Integer>("A", "B", p3id2, 7, p3date2)) &&
 				actual.get(2).equals(new CurrentPatientGeneric<Integer>("A", "C", p3id3, 7, p3date3)) &&
 				actual.get(1).equals(new CurrentPatientGeneric<Integer>("R", "T", p3id4, 7, p3date4)));
	}
 	
	@Test
 	public void testGetInactivePatientsEmptyFacility() {
 		GregorianCalendar cutoff = new GregorianCalendar(2000, 1, 1);
 		ArrayList<CurrentPatientGeneric<Integer>> actual = emptyFacility.getInactivePatients(cutoff);
 		assertEquals(0, actual.size());
 	}
 	
 	@Test
 	public void testGetInactivePateientsAllInactive() {
 		GregorianCalendar cutoff = new GregorianCalendar(2020, 1, 1);
 		ArrayList<CurrentPatientGeneric<Integer>> actual = phase3Facility.getInactivePatients(cutoff);
 		assertEquals(4, actual.size());
 	}
 	
 	@Test
 	public void testGetInactivePatientsAllActive() {
 		GregorianCalendar cutoff = new GregorianCalendar(2019, 1, 1);
 		ArrayList<CurrentPatientGeneric<Integer>> actual = phase3Facility.getInactivePatients(cutoff);
 		assertEquals(0, actual.size());
 	}
 	
 	@Test
 	public void testGetInactivePatientsSingleEdgeDate() {
 		GregorianCalendar cutoff = new GregorianCalendar(2019, 1, 5);
 		ArrayList<CurrentPatientGeneric<Integer>> actual = phase3Facility.getInactivePatients(cutoff);
 		assertEquals(3, actual.size());
 	}
 	
 	@Test
 	public void testGetInactivePatientsFirstEdgeDate() {
 		GregorianCalendar cutoff = new GregorianCalendar(2019, 1, 1);
 		ArrayList<CurrentPatientGeneric<Integer>> actual = phase3Facility.getInactivePatients(cutoff);
 		assertEquals(0, actual.size());
 	}
 	
 	@Test
 	public void testGetInactivePatientsSplitDate() {
 		GregorianCalendar cutoff = new GregorianCalendar(2019, 1, 4);
 		ArrayList<CurrentPatientGeneric<Integer>> actual = phase3Facility.getInactivePatients(cutoff);
 		assertEquals(2, actual.size());
 	}
 	
 	@Test
 	public void testGetInactivePatientsSingleEntry() {
 		GregorianCalendar cutoff = new GregorianCalendar(2019, 1, 3);
 		ArrayList<CurrentPatientGeneric<Integer>> actual = phase3Facility.getInactivePatients(cutoff);
 		assertTrue(actual.get(0).equals(new CurrentPatientGeneric<Integer>("R", "T", p3id4, 7, p3date4)));
 	}
 	
 	@Test
 	public void testGetRecentPatientsEmptyFacility() {
 		GregorianCalendar cutoff = new GregorianCalendar(2000, 1, 1);
 		ArrayList<CurrentPatientGeneric<Integer>> actual = emptyFacility.getRecentPatients(cutoff);
 		assertEquals(0, actual.size());
 	}
 	
 	@Test
 	public void testGetRecentPateientsAllInactive() {
 		GregorianCalendar cutoff = new GregorianCalendar(2020, 1, 1);
 		ArrayList<CurrentPatientGeneric<Integer>> actual = phase3Facility.getRecentPatients(cutoff);
 		assertEquals(0, actual.size());
 	}
 	
 	@Test
 	public void testGetRecentPatientsAllActive() {
 		GregorianCalendar cutoff = new GregorianCalendar(2019, 1, 1);
 		ArrayList<CurrentPatientGeneric<Integer>> actual = phase3Facility.getRecentPatients(cutoff);
 		assertEquals(4, actual.size());
 	}
 	
 	@Test
 	public void testGetRecentPatientsSingleEdgeDate() {
 		GregorianCalendar cutoff = new GregorianCalendar(2019, 1, 4);
 		ArrayList<CurrentPatientGeneric<Integer>> actual = phase3Facility.getRecentPatients(cutoff);
 		assertEquals(1, actual.size());
 	}
 	
 	@Test
 	public void testGetRecentPatientsFirstEdgeDate() {
 		GregorianCalendar cutoff = new GregorianCalendar(2019, 1, 2);
 		ArrayList<CurrentPatientGeneric<Integer>> actual = phase3Facility.getRecentPatients(cutoff);
 		assertEquals(3, actual.size());
 	}
 	
 	@Test
 	public void testGetRecentPatientsSplitDate() {
 		GregorianCalendar cutoff = new GregorianCalendar(2019, 1, 3);
 		ArrayList<CurrentPatientGeneric<Integer>> actual = phase3Facility.getRecentPatients(cutoff);
 		assertEquals(2, actual.size());
 	}
 	
 	@Test
 	public void testGetRecentPatientsAllActiveOrder() {
 		GregorianCalendar cutoff = new GregorianCalendar(2018, 1, 1);
 		ArrayList<CurrentPatientGeneric<Integer>> actual = phase3Facility.getRecentPatients(cutoff);
 		assertTrue(actual.get(1).equals(new CurrentPatientGeneric<Integer>("A", "B", p3id1, 7, p3date1)) &&
 				actual.get(0).equals(new CurrentPatientGeneric<Integer>("A", "B", p3id2, 7, p3date2)) &&
 				actual.get(2).equals(new CurrentPatientGeneric<Integer>("A", "C", p3id3, 7, p3date3)) &&
 				actual.get(3).equals(new CurrentPatientGeneric<Integer>("R", "T", p3id4, 7, p3date4)));
 	}
 	
 	@Test
 	public void testGetRecentPatientsHalfActiveOrder() {
 		GregorianCalendar cutoff = new GregorianCalendar(2019, 1, 3);
 		ArrayList<CurrentPatientGeneric<Integer>> actual = phase3Facility.getRecentPatients(cutoff);
 		assertTrue(actual.get(1).equals(new CurrentPatientGeneric<Integer>("A", "B", p3id1, 7, p3date1)) &&
 				actual.get(0).equals(new CurrentPatientGeneric<Integer>("A", "B", p3id2, 7, p3date2)));
 	}
 		
 	@Test
 	public void testSetPatientLastVisitSize() {
 		GregorianCalendar cutoff = new GregorianCalendar(2019, 1, 3);
 		phase3Facility.setLastVisit(p3id4, new GregorianCalendar(2019, 1, 7));
 		ArrayList<CurrentPatientGeneric<Integer>> actual = phase3Facility.getRecentPatients(cutoff);
 		assertEquals(3, actual.size());
 	}
 	
}
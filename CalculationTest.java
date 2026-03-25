import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
/**
 * The test class CalculationTest.
 *
 * @author  Mohamed Salem
 * @version 1.0
 */
public class CalculationTest
{
    private Calculation calc;
    private List<DataPoint> data;
    
    /**
     * Test fixture.
     * Called automatically before every test case method.
     */
    @BeforeEach
    public void setUp() {
        calc = new Calculation();
        data = new ArrayList<>();
    }   

    /**
     * Positive test: Checks if the average calculation works for a standard list.
     * Expected: (10 + 20 + 30) / 3 = 20.0
     */
    @Test
    public void testFindAverageFromData_NormalList() {
        data.add(new DataPoint(1,100, 100, 10.0));
        data.add(new DataPoint(2,200, 200, 20.0));
        data.add(new DataPoint(3,300, 300, 30.0));
        
        double actualAverage = calc.findAverageFromData(data);
        
        assertEquals(20.0, actualAverage, 0.001, "The average of 10, 20, and 30 should be 20.0");
    }
    
    /**
     * Boundary test: Checks behavior with exactly one element.
     * Expected: Returns the value of that single element.
     */
    @Test
    public void testFindAverageFromData_SingleElementBoundary() {
        data.add(new DataPoint(1,150, 150, 15.5));

        double average = calc.findAverageFromData(data);
        assertEquals(15.5, average, 0.001, "Average of one element should be its own value");
    }
    
    /**
     * Negative test: Empty list.
     * Expected: Returns NaN due to division by zero, rather than crashing.
     */
    @Test
    public void testFindAverageFromData_EmptyListNegative() {
        double average = calc.findAverageFromData(data);
        assertTrue(Double.isNaN(average), "Empty list should result in NaN");
    }
    
    /**
     * Positive test: Point is well within the acceptable coordinates.
     * Expected: The list returned should contain this 1 point.
     */
    @Test
    public void testFindRelevantData_InsideBoundary() {
        // Safely inside (Easting: 530000, Northing: 180000)
        data.add(new DataPoint(1,530000, 180000, 10.0));

        List<DataPoint> result = calc.findRelevantData(data);
        assertEquals(1, result.size(), "Point inside boundaries should be kept");
    }
    
    /**
     * Boundary test: Point is exactly on the Min Easting and Max Northing lines.
     * Expected: it should be kept.
     */
    @Test
    public void testFindRelevantData_ExactlyOnBoundary() {
        // Exactly on Min_Easting (510394) and Max_Northing (193305)
        data.add(new DataPoint(1,510394, 193305, 10.0));

        List<DataPoint> result = calc.findRelevantData(data);
        assertEquals(1, result.size(), "Point exactly on the boundary line should be kept");
    }
    
    /**
     * Negative test: Point is just outside the boundaries (off by 1).
     * Expected: The point is filtered out; returns the list being empty.
     */
    @Test
    public void testFindRelevantData_JustOutsideBoundary() {
        // Easting is 510393 (1 unit below Min_Easting)
        data.add(new DataPoint(1,510393, 180000, 10.0));

        List<DataPoint> result = calc.findRelevantData(data);
        assertEquals(0, result.size(), "Point outside boundaries should be removed");
    }
    
    /**
     * Positive test: Finds the highest pollution value in a standard list.
     * Expected: Returns 45.5.
     */
    @Test
    public void testReturnMaxLevelOfPollution_NormalList() {
        data.add(new DataPoint(1,100, 100, 10.0));
        data.add(new DataPoint(2,200, 200, 45.5)); // Max value
        data.add(new DataPoint(3,300, 300, 5.0));

        double max = calc.returnMaxLevelOfPollution(data);
        assertEquals(45.5, max, 0.001, "Should identify 45.5 as the maximum value");
    }
    
    /**
     * Negative/Boundary test: Empty list.
     * Expected: returns 0 
     */
    @Test
    public void testReturnMaxLevelOfPollution_EmptyList() {
        double max = calc.returnMaxLevelOfPollution(data);
        assertEquals(0.0, max, 0.001, "An empty list should return the default max of 0.0");
    }
    
    /**
     * Positive test: Finds the exact DataPoint object with the highest value.
     * Expected: Returns the exact DataPoint object that has 45.5.
     */
    @Test
    public void testGetLocationOfMaxValue_NormalList() {
        DataPoint expectedMaxPoint = new DataPoint(1,200, 200, 45.5);
        
        data.add(new DataPoint(2,100, 100, 10.0));
        data.add(expectedMaxPoint);
        data.add(new DataPoint(3,300, 300, 5.0));

        DataPoint actualMaxPoint = calc.getLocationOfMaxValue(data);
        assertSame(expectedMaxPoint, actualMaxPoint, "Should return the DataPoint object with the highest value");
    }
    
    /**
     * Negative/Boundary test: Empty list.
     * Expected: returns null.
     */
    @Test
    public void testGetLocationOfMaxValue_EmptyList(){
        DataPoint actualMaxPoint = calc.getLocationOfMaxValue(data);
        assertNull(actualMaxPoint, "An empty list should return a null DataPoint");
    }
    
    /**
     * Positive test: Verifies that the full pipeline works.
     * It should load the file, filter the relevant data, and calculate the average.
     */
    @Test
    public void testFindAverageFromSpecificYear_RealData() {        
        String testPollutant = "PM10"; 
        String testYear = "2018";
        
        double actualAverage = calc.findAverageFromSpecificYear(testPollutant, testYear);
        
        double expectedAverage = 18.18229615; // Excel Sheet average
        
        assertEquals(expectedAverage, actualAverage, 0.001, "Pipeline should calculate correct average for PM10 in 2018");
    }
    
    /**
     * Positive test: Finds a specific value based on exact Easting and Northing coordinates.
     * Expected: Returns the value of the matching DataPoint.
     */
    @Test
    public void testFindSpecificVal_PointExists() {
        data.add(new DataPoint(1, 520000, 180000, 25.5));
        data.add(new DataPoint(2, 530000, 190000, 30.0));

        double value = calc.findSpecificVal(data, 530000, 190000);
        
        assertEquals(30.0, value, 0.001, "Should return the value for the exact matching coordinates");
    }

    /**
     * Negative test: Looks for coordinates that don't exist in the list.
     * Expected: Returns 0.0.
     */
    @Test
    public void testFindSpecificVal_PointDoesNotExist() {        
        data.add(new DataPoint(1, 520000, 180000, 25.5));

        double value = calc.findSpecificVal(data, 999999, 999999);
        assertEquals(0.0, value, 0.001, "Should return 0.0 if the coordinates are not found");
    }
    
    /**
     * Positive test: Adds a sum for a gridcode that isn't in the averages map yet.
     * Expected: Creates a new entry with [value, 1.0].
     */
    @Test
    public void testAddSum_NewGridcode() {
        Map<Integer, Double[]> averages = new HashMap<>();
        Map<Integer, DataPoint> lookupMap = new HashMap<>();
        
        int gridcode = 123;
        lookupMap.put(gridcode, new DataPoint(gridcode, 100, 100, 15.0));

        calc.addSum(averages, lookupMap, gridcode);
        
        assertNotNull(averages.get(gridcode), "Gridcode should be added to the averages map");
        assertEquals(15.0, averages.get(gridcode)[0], 0.001, "Sum should be 15.0");
        assertEquals(1.0, averages.get(gridcode)[1], 0.001, "Count should be 1.0");
    }

    /**
     * Positive test: Adds a sum for a gridcode that already exists in the map.
     * Expected: Updates the existing sum and increments the count.
     */
    @Test
    public void testAddSum_ExistingGridcode() {
        Map<Integer, Double[]> averages = new HashMap<>();
        Map<Integer, DataPoint> lookupMap = new HashMap<>();
        
        int gridcode = 123;
        averages.put(gridcode, new Double[]{20.0, 2.0}); 
        lookupMap.put(gridcode, new DataPoint(gridcode, 100, 100, 10.0));

        calc.addSum(averages, lookupMap, gridcode);
        
        assertEquals(30.0, averages.get(gridcode)[0], 0.001, "New sum should be 20.0 + 10.0 = 30.0");
        assertEquals(3.0, averages.get(gridcode)[1], 0.001, "Count should increment from 2.0 to 3.0");
    }
    
    /**
     * Positive test: Merges maps and calculates the correct average.
     * Expected: Computes sum / count and maps it to the DataPoint.
     */
    @Test
    public void testMergeMaps_ValidData() {
        Map<Integer, DataPoint> gridToDataPoint = new HashMap<>();
        Map<Integer, Double[]> gridcodeAverage = new HashMap<>();
        
        int gridcode = 1;
        DataPoint dp = new DataPoint(gridcode, 100, 100, 0.0);
        
        gridToDataPoint.put(gridcode, dp);
        gridcodeAverage.put(gridcode, new Double[]{50.0, 2.0}); 

        Map<DataPoint, Double> result = calc.mergeMaps(gridToDataPoint, gridcodeAverage);
        
        assertTrue(result.containsKey(dp), "Result map should contain the original DataPoint");
        assertEquals(25.0, result.get(dp), 0.001, "Average should be correctly calculated as 50.0 / 2.0");
    }
    
    /**
     * Positive test: Converts a map of DataPoints and their new averages into a list of new DataPoints.
     * Expected: Returns a list with the updated values.
     */
    @Test
    public void testReturnNewDataPoints_ValidMap() {
        Map<DataPoint, Double> map = new HashMap<>();
        
        DataPoint originalDp = new DataPoint(1, 100, 200, 5.0);
        map.put(originalDp, 42.5); 

        List<DataPoint> result = calc.returnNewDataPoints(map);
        
        assertEquals(1, result.size(), "Should return a list with exactly one element");
        assertEquals(42.5, result.get(0).value(), 0.001, "The new DataPoint should have the updated value of 42.5");
        assertEquals(1, result.get(0).gridCode(), "Gridcode should remain unchanged");
    }
    
    /**
     * Integration test: Checks that the pipeline runs for all years for a specific pollutant.
     * Expected: Returns a map containing keys for 2018 through 2023.
     */
    @Test
    public void testFindAverageFromAllYears_RealData() {
        String testPollutant = "PM10";
        
        HashMap<String, Double> results = calc.findAverageFromAllYears(testPollutant);
        
        assertEquals(6, results.size(), "Should contain exactly 6 years of data");
        assertTrue(results.containsKey("2018"), "Map should contain data for 2018");
        assertTrue(results.containsKey("2023"), "Map should contain data for 2023");
        
        // Basic  check to ensure the values aren't 0.0 or NaN
        assertFalse(Double.isNaN(results.get("2018")), "Average for 2018 should be a valid number");
        assertTrue(results.get("2018") > 0, "Average for 2018 should be greater than 0");
    }
}
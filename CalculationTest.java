import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

/**
 * The test class CalculationTest.
 *
 
 */
public class CalculationTest
{
    /**
     * Positive test: Checks if the average calculation works for a standard list.
     * Expected: (10 + 20 + 30) / 3 = 20.0
     */
    @Test
    public void testFindAverageFromData_NormalList() {
        
        Calculation calculation = new Calculation();
        List<DataPoint> dummyData = new ArrayList<>();
        
        dummyData.add(new DataPoint(1,100, 100, 10.0));
        dummyData.add(new DataPoint(2,200, 200, 20.0));
        dummyData.add(new DataPoint(3,300, 300, 30.0));
        
        double actualAverage = calculation.findAverageFromData(dummyData);
        
        assertEquals(20.0, actualAverage, 0.001, "The average of 10, 20, and 30 should be 20.0");
        
    }
    
    /**
     * Boundary test: Checks behavior with exactly one element.
     * Expected: Returns the value of that single element.
     */
    @Test
    public void testFindAverageFromData_SingleElementBoundary() {
        Calculation calc = new Calculation();
        List<DataPoint> data = new ArrayList<>();
        
        data.add(new DataPoint(1,150, 150, 15.5));

        double average = calc.findAverageFromData(data);
        assertEquals(15.5, average, 0.001, "Average of one element should be its own value");
    }
    
    /**
     * Negative test: Empty list.
     * Expected: Returns NaN (Not a Number) due to division by zero, rather than crashing.
     */
    @Test
    public void testFindAverageFromData_EmptyListNegative() {
        Calculation calc = new Calculation();
        List<DataPoint> emptyData = new ArrayList<>();

        double average = calc.findAverageFromData(emptyData);
        assertTrue(Double.isNaN(average), "Empty list should result in NaN");
    }
    
    /**
     * Positive test: Point is well within the acceptable coordinates.
     * Expected: The list returned should contain this 1 point.
     */
    @Test
    public void testFindRelevantData_InsideBoundary() {
        Calculation calc = new Calculation();
        List<DataPoint> data = new ArrayList<>();
        
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
        Calculation calc = new Calculation();
        List<DataPoint> data = new ArrayList<>();
        
        // Exactly on Min_Easting (510394) and Max_Northing (193305)
        data.add(new DataPoint(1,510394, 193305, 10.0));

        List<DataPoint> result = calc.findRelevantData(data);
        assertEquals(1, result.size(), "Point exactly on the boundary line should be kept");
    }
    
    /**
     * Negative test: Point is just outside the boundaries (off by 1).
     * Expected: The point is filtered out; returned list is empty.
     */
    @Test
    public void testFindRelevantData_JustOutsideBoundary() {
        Calculation calc = new Calculation();
        List<DataPoint> data = new ArrayList<>();
        
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
        Calculation calc = new Calculation();
        List<DataPoint> data = new ArrayList<>();
        
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
        Calculation calc = new Calculation();
        List<DataPoint> data = new ArrayList<>();

        double max = calc.returnMaxLevelOfPollution(data);
        assertEquals(0.0, max, 0.001, "An empty list should return the default max of 0.0");
    }
    
    /**
     * Positive test: Finds the exact DataPoint object with the highest value.
     * Expected: Returns the exact DataPoint object that has 45.5.
     */
    @Test
    public void testGetLocationOfMaxValue_NormalList() {
        Calculation calc = new Calculation();
        List<DataPoint> data = new ArrayList<>();
        
        DataPoint expectedMaxPoint = new DataPoint(1,200, 200, 45.5);
        
        data.add(new DataPoint(2,100, 100, 10.0));
        data.add(expectedMaxPoint);
        data.add(new DataPoint(3,300, 300, 5.0));

        DataPoint actualMaxPoint = calc.getLocationOfMaxValue(data);
        // assertSame checks if they are the exact same object in memory
        assertSame(expectedMaxPoint, actualMaxPoint, "Should return the DataPoint object with the highest value");
    
    }
    
    /**
     * Negative/Boundary test: Empty list.
     * Expected: returns null.
     */
    @Test
    public void testGetLocationOfMaxValue_EmptyList() {
        Calculation calc = new Calculation();
        List<DataPoint> data = new ArrayList<>();

        DataPoint actualMaxPoint = calc.getLocationOfMaxValue(data);
        assertNull(actualMaxPoint, "An empty list should return a null DataPoint");
    }
    
    /**
     * Positive test: Verifies that the full pipeline works.
     * It should load the file, filter the relevant data, and calculate the average.
     */
    @Test
    public void testFindAverageFromSpecificYear_RealData() {
        Calculation calc = new Calculation();
        
        String testPollutant = "PM10"; 
        String testYear = "2018";
        
        double actualAverage = calc.findAverageFromSpecificYear(testPollutant, testYear);
        
        
        double expectedAverage = 18.18229615; // Excel Sheet average
        
        assertEquals(expectedAverage, actualAverage, 0.001, "Pipeline should calculate correct average for PM10 in 2018");
    }
}
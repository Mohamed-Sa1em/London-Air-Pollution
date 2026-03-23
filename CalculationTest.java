import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * The test class CalculationTest.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class CalculationTest
{
    private Calculation calculation;
    
    /**
     * Default constructor for test class CalculationTest
     */
    public CalculationTest()
    {
    }

    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @BeforeEach
    public void setUp()
    {
        calculation = new Calculation();
    }

    /**
     * Tears down the test fixture.
     *
     * Called after every test case method.
     */
    @AfterEach
    public void tearDown()
    {
    }
    
    @Test
    public void testFindSpecificValExists() {
        DataLoader loader = new DataLoader();
        DataSet dataSet = loader.loadDataFile("UKAirPollutionData/NO2/mapno22018.csv");
        
        
    }
}
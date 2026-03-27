import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
/**
 * The LiveData class holds the key functionality for the API,
 * especially the functionality that is common between different
 * API calls such as the weather and pollution API.
 *
 * @author Zubair Abdul Matin
 * @version 1.0
 */
public abstract class LiveData
{

    /**
     * Constructor for objects of class LiveData
     */
    public LiveData()
    {
    }
    
    /**
     * Gets the parsed data from a specific URL
     * 
     * @param url - the url used for the API
     * @return array of all the parsed values
     */
    protected double[] getParsedData(URL url) throws Exception, java.io.IOException {

        BufferedReader reader =
        new BufferedReader(new InputStreamReader(url.openStream()));

        String line;
        StringBuilder result = new StringBuilder();

        while((line = reader.readLine()) != null){
            result.append(line);
        }

        reader.close();
        
        String jsonString = result.toString();
        
        double[] parsedValues = parseData(jsonString);
        
        return parsedValues;
    }
    
    /**
     * Returns the data specific to weather or pollution
     * 
     * @return an array with the parsed values
     */
    abstract public double[] parseData(String jsonString) throws Exception;
    
    /**
     * Finds the data at a given point.
     * 
     * @param latitude, longitude
     * @return data in string format
     */
    abstract public String getDataAtPoint(double latitude, double longitude) throws Exception;
}
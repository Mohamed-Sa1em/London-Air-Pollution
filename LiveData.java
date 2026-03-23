import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
/**
 * Write a description of class LiveData here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public abstract class LiveData
{

    /**
     * Constructor for objects of class LiveData
     */
    public LiveData()
    {
    }
    
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
        
        System.out.println(jsonString);
        
        double[] parsedValues = parseData(jsonString);
        
        return parsedValues;
    }
    
    abstract public double[] parseData(String jsonString) throws Exception;
    
    abstract public String getDataAtPoint(double latitude, double longitude) throws Exception;
}
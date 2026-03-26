import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * A class to call the API and return weather data.
 * The class also parses the data and returns it in 
 * String format.
 *
 * @author Zubair Abdul Matin
 * @version 1.0
 */
public class WeatherAPI extends LiveData
{
    private static final String API_KEY = "8ba6990fa2a1c29023afcbc72b7e9969";
    private String urlString;
    
    /**
     * Constructor for objects of class WeatherAPI
     */
    public WeatherAPI(){
        
    }
    
    private double[] getAPIResponse(double lat, double lon) throws Exception {
        String urlString =
        "https://api.openweathermap.org/data/2.5/weather"
        + "?lat=" + lat
        + "&lon=" + lon
        + "&appid=" + API_KEY
        + "&units=metric";

        URL url = new URL(urlString);
        
        double[] parsedValues = getParsedData(url);
        
        return parsedValues;
    }
    
    /**
    * Returns the data parsed into an array format
    * @param jsonString - the string received from the API.
    * @return an array with the weather values
    */ 
    public double[] parseData(String jsonString) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
    
        JsonNode root = mapper.readTree(jsonString);
    
        // main
        JsonNode main = root.get("main");
        double temperature = main.get("temp").asDouble();
        double humidity = main.get("humidity").asDouble();
    
        // wind
        JsonNode wind = root.get("wind");
        double windSpeed = wind.get("speed").asDouble();
        
        return new double[]{temperature, humidity, windSpeed};
    }
    
    /**
     * Gets the data in string format at a given point
     * @param latitude - latitude of point on the map
     * @param longitude - longitude of point on the map
     * @return string format of data
     */
    public String getDataAtPoint(double latitude, double longitude) throws Exception {
        double[] weatherValues = getAPIResponse(latitude, longitude);
        return ("Temp: " + weatherValues[0] + "<br>" + "Humidity: " + weatherValues[1] + "<br>" + "Wind: " + weatherValues[2]);
    }
}
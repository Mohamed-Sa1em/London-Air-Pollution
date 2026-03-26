import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
/**
 * A class to call the API and return pollution data.
 * The class also parses the data and returns it in 
 * String format.
 *
 * @author Zubair Abdul Matin
 * @version 1.0
 */
public class PollutionAPI extends LiveData
{
    private static final String API_KEY = "8ba6990fa2a1c29023afcbc72b7e9969";
    
    public PollutionAPI() {}

    private double[] getAPIResponse(double lat, double lon) throws Exception {

        String urlString =
        "https://api.openweathermap.org/data/2.5/air_pollution"
        + "?lat=" + lat
        + "&lon=" + lon
        + "&appid=" + API_KEY;

        URL url = new URL(urlString);

        double[] parsedValues = getParsedData(url);
        
        return parsedValues;
    }
    
    /**
    * Returns the data parsed into an array format
    * @param jsonString - the string received from the API.
    * @return an array with the pollution values
    */ 
    public double[] parseData(String jsonString) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
    
        JsonNode root = mapper.readTree(jsonString);
    
        JsonNode pollution = root.get("list").get(0);
    
        int aqi = pollution.get("main").get("aqi").asInt();
    
        JsonNode components = pollution.get("components");
    
        double pm25 = components.get("pm2_5").asDouble();
        double pm10 = components.get("pm10").asDouble();
        double no2 = components.get("no2").asDouble();
        
        
        return new double[]{pm25, pm10, no2};
    }
    
    /**
     * Gets the data in string format at a given point
     * @param latitude - latitude of point on the map
     * @param longitude - longitude of point on the map
     * @return string format of data
     */
    public String getDataAtPoint(double latitude, double longitude) throws Exception {
        double[] pollutionValues = getAPIResponse(latitude, longitude);
        return ("PM2.5: " + pollutionValues[0] + "<br>" + "PM10: " + pollutionValues[1] + "<br>" + "NO2: " + pollutionValues[2]);
    }
}

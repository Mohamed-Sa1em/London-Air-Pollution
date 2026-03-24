import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Write a description of class WeatherAPI here.
 *
 * @author (your name)
 * @version (a version number or a date)
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
        
        //System.out.println(pm25 + " " + pm10 + " " + no2);
        
        return new double[]{temperature, humidity, windSpeed};
    }
    
    public String getDataAtPoint(double latitude, double longitude) throws Exception {
        double[] weatherValues = getAPIResponse(latitude, longitude);
        return ("Temp: " + weatherValues[0] + "<br>" + "Humidity: " + weatherValues[1] + "<br>" + "Wind: " + weatherValues[2]);
    }
}
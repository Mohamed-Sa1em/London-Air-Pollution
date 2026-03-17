import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
/**
 * Write a description of class PollutionApi here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class PollutionApi
{
    private static final String API_KEY = "8ba6990fa2a1c29023afcbc72b7e9969";
    
    public void PollutionApi() {}

    public double[] getAPIResponse(double lat, double lon) throws Exception {

        String urlString =
        "https://api.openweathermap.org/data/2.5/air_pollution"
        + "?lat=" + lat
        + "&lon=" + lon
        + "&appid=" + API_KEY;
        
        System.out.println(urlString);

        URL url = new URL(urlString);

        BufferedReader reader =
        new BufferedReader(new InputStreamReader(url.openStream()));

        String line;
        StringBuilder result = new StringBuilder();

        while((line = reader.readLine()) != null){
            result.append(line);
        }

        reader.close();
        
        String jsonString = result.toString();
        
        //System.out.println(jsonString);
        
        double[] parsedValues = parsePollution(jsonString);
        
        for (double value: parsedValues) { 
            System.out.println(value); 
        }
        //System.out.println(parsedValues);
        
        return parsedValues;
    }
    
    private double[] parsePollution(String json) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
    
        JsonNode root = mapper.readTree(json);
    
        JsonNode pollution = root.get("list").get(0);
    
        int aqi = pollution.get("main").get("aqi").asInt();
    
        JsonNode components = pollution.get("components");
    
        double pm25 = components.get("pm2_5").asDouble();
        double pm10 = components.get("pm10").asDouble();
        double no2 = components.get("no2").asDouble();
        
        //System.out.println(pm25 + " " + pm10 + " " + no2);
        
        return new double[]{pm25, pm10, no2};
    }
    
}
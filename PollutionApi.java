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

    public static String getAPIResponse(double lat, double lon) throws Exception {

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

        return result.toString();
    }
    
    public static void parsePollution(String json) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
    
        JsonNode root = mapper.readTree(json);
    
        JsonNode pollution = root.get("list").get(0);
    
        int aqi = pollution.get("main").get("aqi").asInt();
    
        JsonNode components = pollution.get("components");
    
        double pm25 = components.get("pm2_5").asDouble();
        double pm10 = components.get("pm10").asDouble();
        double no2 = components.get("no2").asDouble();
        double o3 = components.get("o3").asDouble();
    
        System.out.println("AQI: " + aqi);
        System.out.println("PM2.5: " + pm25);
        System.out.println("PM10: " + pm10);
        System.out.println("NO2: " + no2);
        System.out.println("O3: " + o3);
    }
    
    public static void main(String[] args) {

        try {
    
            double lat = 51.5074;
            double lon = -0.1278;
    
            String json = getAPIResponse(lat, lon);
    
            parsePollution(json);
    
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }
    
}
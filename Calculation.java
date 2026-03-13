import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;


/**
 * Write a description of class Calculation here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Calculation
{

    private ArrayList<String> years = new ArrayList<>(List.of("2018", "2019", "2020", "2021", "2022", "2023"));

    /**
     * Constructor for objects of class Calcutation
     */
    public Calculation()
    {
    }

    public HashMap<String, Double> findAverageFromAllYears(String pollutant)
    {
        HashMap<String, Double> map = new HashMap<>();
        	
        for (String year: this.years) {
            double average = findAverageFromSpecificYear(pollutant, year);
            map.put(year, average);
        }
        return map;
    }
    
    public HashMap<String, Double> findAverageFromRangeOfYears(String pollutant, String start, String end) {
        HashMap<String, Double> map = new HashMap<>();

        List<String> timePeriod = findMiddleYears(start, end);
        
        for (String year: timePeriod) {
            double average = findAverageFromSpecificYear(pollutant, year);
            map.put(year, average);
        }
        return map;
    }
    
    public double findAverageFromSpecificYear(String pollutant, String year) {
        String folderName = pollutant.toUpperCase();
        String filePrefix = pollutant.replace(".", "").toLowerCase();
        
        String suffix = "";
        if (filePrefix.contains("pm")) {
            suffix = "g";
        }
        String filePath = "UKAirPollutionData/" + folderName + "/map" + filePrefix + year + suffix + ".csv";
        
        DataLoader myLoader = new DataLoader();	
        DataSet data = myLoader.loadDataFile(filePath);
        List<DataPoint> dataPoints = data.getData();
        
        List<DataPoint> relevantData = findRelevantData(dataPoints);
        
        double average = findAverageFromData(relevantData);
        return average;
    }
    
    public List<DataPoint> findRelevantData(List<DataPoint> data) {
        double Max_Easting = 553297;
        double Min_Easting = 510394;
        double Max_Northing = 193305;
        double Min_Northing = 168504;
        
        ArrayList<DataPoint> relevant = new ArrayList<DataPoint>();
        for (DataPoint dataPoint: data) {
            double This_Easting = dataPoint.x();
            double This_Northing = dataPoint.y();
            if (This_Easting >= Min_Easting && This_Easting <= Max_Easting &&
                This_Northing >= Min_Northing && This_Northing <= Max_Northing
            ) {
                relevant.add(dataPoint);
            }
        }
        
        return relevant;
    }
    
    public double findAverageFromData(List<DataPoint> data) {
        double sum = 0;
        for (DataPoint dataPoint: data) {
            sum += dataPoint.value();
        }
        
        return sum / data.size();
    }
    
    private List<String> findMiddleYears(String start, String end) {
        int startIndex = years.indexOf(start);
        int endIndex = years.indexOf(end);
        return years.subList(startIndex, endIndex+1);
    }
}

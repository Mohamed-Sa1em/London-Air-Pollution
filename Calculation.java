import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

/**
 * Calculation class is used perform calculations for other classes to reduce redundant methods.
 *
 * @author Salaah Shameem
 * @version 1.0
 */
public class Calculation
{
    // an ArrayList with all the years that the data spans for
    private ArrayList<String> years = new ArrayList<>(List.of("2018", "2019", "2020", "2021", "2022", "2023"));

    /**
     * Constructor for objects of class Calcutation
     */
    public Calculation()
    {
    }

    /**
     * Finds an average for a pollutant from all years
     * @return a hashmap with year as key and the average as the value.
     */
    public HashMap<String, Double> findAverageFromAllYears(String pollutant)
    {
        HashMap<String, Double> map = new HashMap<>();
            
        for (String year: this.years) {
            double average = findAverageFromSpecificYear(pollutant, year);
            map.put(year, average);
        }
        return map;
    }
    
    /**
     * Finds an average for a pollutant from a range years
     * @return a hashmap with year as key and the average as the value.
     */
    public HashMap<String, Double> findAverageFromRangeOfYears(String pollutant, String start, String end) {
        HashMap<String, Double> map = new HashMap<>();

        List<String> timePeriod = findMiddleYears(start, end);
        
        for (String year: timePeriod) {
            double average = findAverageFromSpecificYear(pollutant, year);
            map.put(year, average);
        }
        return map;
    }
    
    /**
     * Finds an average for a pollutant from a specific year.
     * @return average
     */
    public double findAverageFromSpecificYear(String pollutant, String year) {
        List<DataPoint> dataPoints = returnDataSet(pollutant, year);
        
        List<DataPoint> relevantData = findRelevantData(dataPoints);
        
        double average = findAverageFromData(relevantData);
        return average;
    }
    
    /**
     * Finds the value for a pollutant from a specific location from all years.
     * @return a hashmap with year as key and the pollutant value as the value.
     */
    public HashMap<String, Double> findAverageFromLocation(String pollutant,String eastingStr, String northingStr) {
        HashMap<String, Double> map = new HashMap<>();
        
        double easting = Integer.valueOf(eastingStr);
        double northing = Integer.valueOf(northingStr);
        
        double sum = 0;
        
        for (String year: this.years) {
            List<DataPoint> dataPoints = returnDataSet(pollutant, year);
            List<DataPoint> data = findRelevantData(dataPoints);
            double specificVal = findSpecificVal(data, easting, northing);
            map.put(year, specificVal);
        }
        return map;
    }
    
    /**
     * Finds the relevant data points from a larger list of data
     * @return list of relevant points
     */
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
    
    /**
     * Finds an average from a list of datapoints
     * @return the average
     */
    public double findAverageFromData(List<DataPoint> data) {
        double sum = 0;
        for (DataPoint dataPoint: data) {
            sum += dataPoint.value();
        }
        
        return sum / data.size();
    }
    
    /**
     * Loads in the required file to retrieve the data points from it
     * @return a list of datapoints from the file
     */
    private List<DataPoint> returnDataSet(String pollutant, String year){
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
        return dataPoints;
    }
    
    /**
     * @return maximum pollution value
     */
    public double returnMaxLevelOfPollution(List<DataPoint> dataPoints) {
        double max = 0;
        for (DataPoint dataPoint: dataPoints) {
            if (max < dataPoint.value()) {
                max = dataPoint.value();
            }
        }
        
        return max;
    }
    
    /**
     * @return DataPoint of the max pollutant value
     */
    public DataPoint getLocationOfMaxValue(List<DataPoint> dataPoints) {
        double max = 0;
        DataPoint loc = null;
        for (DataPoint dataPoint: dataPoints) {
            if (max < dataPoint.value()) {
                max = dataPoint.value();
                loc = dataPoint;
            }
        }
        
        return loc;
    }
    
    /**
     * Breaks the year range to return a sublist
     * @return a sublist of years
     */
    private List<String> findMiddleYears(String start, String end) {
        int startIndex = years.indexOf(start);
        int endIndex = years.indexOf(end);
        return years.subList(startIndex, endIndex+1);
    }
    
    /**
     * Finds a specific value from a data point
     * @return the value.System.out.print
     */
    private double findSpecificVal(List<DataPoint> dataPoints, double easting, double northing) {
        for (DataPoint dp: dataPoints) {
            if (easting == dp.x() && northing == dp.y()) {
                return dp.value();
            }
        }
        return 0;
    }
}

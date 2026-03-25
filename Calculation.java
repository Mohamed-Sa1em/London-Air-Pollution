import java.util.*;

/**
 * Calculation class is used perform calculations for other classes to reduce redundant methods.
 *
 * @author Salaah Shameem
 * @version 1.1
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
     * Finds an average for a pollutant from a specific year.
     * @return average
     */
    public double findAverageFromSpecificYear(String pollutant, String year) {
        DataSet data = returnDataSet(pollutant, year);
        
        List<DataPoint> relevantData = findRelevantData(data.getData());
        
        double average = findAverageFromData(relevantData);
        return average;
    }
    
    /**
     * Finds an average from a list of datapoints
     * @return average
     */
    public double findAverageFromData(List<DataPoint> data) {
        double sum = 0;
        for (DataPoint dataPoint: data) {
            sum += dataPoint.value();
        }
        
        return sum / data.size();
    }
    
    /**
     * Find the relevant datapoints from a list of existing datapoints
     * @param datapoints the list of datapoints
     * @return a list containing the relevant datapoints
     */
    public List<DataPoint> findRelevantData(List<DataPoint> dataPoints) {
        int maxEasting = 553297;
        int minEasting = 510394;
        int maxNorthing = 193305;
        int minNorthing = 168504;
        
        List<DataPoint> newDataPoints = new ArrayList<DataPoint>();
        
        for (DataPoint dataPoint: dataPoints) {
            if (dataPoint.x() >= minEasting && dataPoint.x() <= maxEasting && dataPoint.y() >= minNorthing 
                && dataPoint.y() <= maxNorthing) {
                newDataPoints.add(dataPoint);
            }
        }
        
        return newDataPoints;
    }
    
    /**
     * Turns a map of <Datapoints, Double> into a list of new Datapoints.
     * @param map a map of <DataPoint, Double>
     * @return list containing new DataPoints
     */
    public List<DataPoint> returnNewDataPoints(Map<DataPoint, Double> map) {
        List<DataPoint> result = new ArrayList<>();
        
        Set<DataPoint> datapoints = map.keySet();
        for (DataPoint dp: datapoints) {
            result.add(new DataPoint(dp.gridCode(), dp.x(), dp.y(), map.get(dp)));
        }
        
        return result;
    }
    
    /**
     * Loads in the required file to retrieve the data points from it
     * @return the dataset
     */
    private DataSet returnDataSet(String pollutant, String year){
        String folderName = pollutant.toUpperCase();
        String filePrefix = pollutant.replace(".", "").toLowerCase();
        
        String suffix = "";
        if (filePrefix.contains("pm")) {
            suffix = "g";
        }
        String filePath = "UKAirPollutionData/" + folderName + "/map" + filePrefix + year + suffix + ".csv";
        
        DataLoader myLoader = new DataLoader();    
        DataSet data = myLoader.loadDataFile(filePath);
        return data;
    }
    
    /**
     * Returns the value of the maximum pollution level from a list of datapoints
     * @param dataPoints list of datapoints
     * @return maximum value
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
     * Returns the datapoint of the location with the maximum value.
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
     * Adds the total sum of a specific location in order to calculate average later on.
     */
    public void addSum(Map<Integer, Double[]> averages, Map<Integer, DataPoint> lookupMap, Integer gridcode){
        if (averages.get(gridcode) != null){
            Double[] array = averages.get(gridcode);
            double currentSum = array[0];
            double newSum = currentSum + lookupMap.get(gridcode).value();
            array[0] = newSum;
            array[1] += 1;
            
        } else {
            averages.put(gridcode, new Double[]{lookupMap.get(gridcode).value(), 1.0});
        }
    }
    
    /**
     * Merges two maps based on their key's.
     * @return a map that that has been merged from the two parameters maps
     */
    public Map<DataPoint, Double> mergeMaps(Map<Integer, DataPoint> gridToDataPoint, Map<Integer, Double[]> gridcodeAverage) {
        Map<DataPoint, Double> result = new HashMap<>();
        
        for (Integer gridcode: gridcodeAverage.keySet()) {
            DataPoint dp = gridToDataPoint.get(gridcode);
            Double[] stats = gridcodeAverage.get(gridcode);
            
            if (dp != null && stats != null && stats[1] != 0) {
                double calculatedAverage = stats[0] / stats[1];
                result.put(dp, calculatedAverage);
            }
        }
        
        return result;
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
     * @return value
     */
    public double findSpecificVal(List<DataPoint> dataPoints, double easting, double northing) {
        for (DataPoint dp: dataPoints) {
            if (easting == dp.x() && northing == dp.y()) {
                return dp.value();
            }
        }
        return 0;
    }
}

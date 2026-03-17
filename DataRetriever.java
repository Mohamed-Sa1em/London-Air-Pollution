import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

/**
 * Write a description of class DataRetriever here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class DataRetriever
{
    private final PollutionApi pollutionAPI = new PollutionApi();
    private final CoordinateConverter coordinateConverter = new CoordinateConverter();

    /**
     * Constructor for objects of class DataRetriever
     */
    public DataRetriever()
    {
        
    }
    
    public DataSet getAllDataFromAPI(DataSet dataSet, String pollutant) throws Exception {
        
        DataSet newData = new DataSet(pollutant, "live", dataSet.getMetric(), dataSet.getUnits());
        
        for (DataPoint dataPoint: dataSet.returnRelevantData()){
            System.out.println(getDataAtPoint(dataPoint, pollutant));
            double liveValue = getDataAtPoint(dataPoint, pollutant);
            System.out.println(liveValue);
            newData.addData(
                new String[]{
                    String.valueOf(dataPoint.gridCode()), 
                    String.valueOf(dataPoint.x()), 
                    String.valueOf(dataPoint.y()), 
                    String.valueOf(liveValue)
                }
            );
            
        }
    
        
        return newData;
    }
    
    private double getDataAtPoint(DataPoint dataPoint, String pollutant) throws Exception {
        int easting = dataPoint.x();
        int northing = dataPoint.y();
        
        double[] lonLatValues = coordinateConverter.returnLonLatValues(easting, northing);
        
        double longitude = lonLatValues[0];
        double latitude = lonLatValues[1];
        
        double[] pollutionValues = pollutionAPI.getAPIResponse(latitude, longitude);
        
        HashMap<String, Double> pollutionData = new HashMap<>();
        pollutionData.put("PM2.5", pollutionValues[0]);
        pollutionData.put("PM10", pollutionValues[1]);
        pollutionData.put("NO2", pollutionValues[2]);
        
        System.out.println(pollutionData.get(pollutant));
        
        if (pollutionData.get(pollutant) != null) return pollutionData.get(pollutant);
        return 1;
        
    }
}
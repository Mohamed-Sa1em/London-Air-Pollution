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
    
    public List<DataPoint> getAllDataFromAPI(DataSet dataSet) throws Exception {
        
        List<DataPoint> liveRelevantDataSet = new ArrayList<DataPoint>();
        
        for (DataPoint dataPoint: dataSet.getData()){
            double liveValue = getDataAtPoint(dataPoint, dataSet.getPollutant());
            liveRelevantDataSet.add(new DataPoint(dataPoint.gridCode(), dataPoint.x(), dataPoint.y(), liveValue));
        }
        return liveRelevantDataSet;
    }
    
    public double getDataAtPoint(DataPoint dataPoint, String pollutant) throws Exception {
        int easting = dataPoint.x();
        int northing = dataPoint.y();
        
        double[] lonLatValues = coordinateConverter.returnLonLatValues(easting, northing);
        
        double longitude = lonLatValues[0];
        double latitude = lonLatValues[1];
        
        double[] pollutionValues = pollutionAPI.getAPIResponse(latitude, longitude);
        
        HashMap<String, Double> pollutionData = new HashMap<>();
        pollutionData.put("pm2.5", pollutionValues[0]);
        pollutionData.put("pm10", pollutionValues[1]);
        pollutionData.put("no2", pollutionValues[2]);
        
        return pollutionData.get(pollutant);
        
    }
}
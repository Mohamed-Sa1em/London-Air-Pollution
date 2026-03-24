import java.util.*;

/**
 * A DataSet object holds all the data from a pollution data file.
 * 
 * It is assumed that the data is derived from a DEFRA air pollution file (see
 * https://uk-air.defra.gov.uk/data/pcm-data). 
 * 
 * The data consists of a few bits of information about the nature of the data, and a list
 * of data points.
 * 
 * @author Michael Kölling
 * @version 1.0
 */
public class DataSet
{
    private String pollutant;
    private String year;
    private String metric;
    private String units;
    
    private List<DataPoint> data;
    
    private final double Max_Easting = 553297;
    private final double Min_Easting = 510394;
    private final double Max_Northing = 193305;
    private final double Min_Northing = 168504;

    /**
     * Constructor for objects of class DataSet
     */
    public DataSet(String pollutant, String year, String metric, String units)
    {
        this.pollutant = pollutant;
        this.year = year;
        this.metric = metric;
        this.units = units;
        
        data = new ArrayList<DataPoint>();
    }

    /**
     * Return the pollutant information for this dataset.
     */
    public String getPollutant()
    {
        return pollutant;
    }
    
    /**
     * Return the year information for this dataset.
     */
    public String getYear()
    {
        return year;
    }
    
    /**
     * Return the metric information for this dataset.
     */
    public String getMetric()
    {
        return metric;
    }
    
    /**
     * Return the units information for this dataset.
     */
    public String getUnits()
    {
        return units;
    }
    
    /**
     * Return the data points of this dataset.
     */
    public List<DataPoint> getData()
    {
        return data;
    }
    
    /**
     * Add a data point to this dataset. 
     * A data point consists of 4 pieces od data:
     * 
     *     gridcode, x, y, value
     *     
     * The data is provided in a String array of length 4. If the value is invalid or
     * missing, it will be stored as -1.
     *
     * @param  values  An array with the four data values (as Strings)
     */
    public void addData(String[] values)
    {
        data.add(new DataPoint(toInt(values[0]), 
                               toInt(values[1]), 
                               toInt(values[2]), 
                               toDouble(values[3]))); 
    }
    
    public List<DataPoint> returnRelevantData() {
        ArrayList<DataPoint> relevantDataPoints = new ArrayList<DataPoint>();
        for (DataPoint dataPoint: data) {
            double This_Easting = dataPoint.x();
            double This_Northing = dataPoint.y();
            if (This_Easting >= Min_Easting && This_Easting <= Max_Easting &&
                This_Northing >= Min_Northing && This_Northing <= Max_Northing
            ) {
                relevantDataPoints.add(dataPoint);
            }
        }
        
        return relevantDataPoints;
    }
    
    /**
     * Convert a string to int. 
     * @param intString  The String holding the int value
     * @return  The int value, or -1 if the string is not a readable number
     */
    private int toInt(String intString)
    {
        try {
            return Integer.parseInt(intString);
        }
        catch (NumberFormatException exc) {
            return -1;
        }
    }

    /**
     * Convert a string to double. 
     * @param doubleString  The String holding the double value
     * @return  The double value, or -1.0 if the string is not a readable number
     */
    private double toDouble(String doubleString)
    {
        try {
            return Double.parseDouble(doubleString);
        }
        catch (NumberFormatException exc) {
            return -1.0;
        }
    }

    /**
     * Return a string representation of this dataset info.
     */
    public String toString()
    {
        return String.format("Dataset: Pollutant: %s, Year: %s, Metric: %s, Units: %s (%d data points)",
                             pollutant, year, metric, units, data.size());
    }
}

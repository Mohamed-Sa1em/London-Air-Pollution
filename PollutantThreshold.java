
/**
 * Enumeration class PollutantThreshold - write a description of the enum class here
 *
 * @author (your name here)
 * @version (version number or date here)
 */
public enum PollutantThreshold
{
    //inital values up for change
    //pm25 values is not right  as they are all green rn  for later years 
    NO2(15.0, 19.5, 25.0),
    PM10(15.0, 18.0, 22.0),   
    PM25(11.0, 12.0, 13.0);
    
    private final double greenMax;
    private final double yellowMax;
    private final double orangeMax;

    // Constructor for the Enum
    PollutantThreshold(double greenMax, double yellowMax, double orangeMax) {
        this.greenMax = greenMax;
        this.yellowMax = yellowMax;
        this.orangeMax = orangeMax;
    }

    public double getGreenMax() { return greenMax; }
    public double getYellowMax() { return yellowMax; }
    public double getOrangeMax() { return orangeMax; }
    
    /**
     * Helper method to easily convert the String from the CSV file into this Enum
     */
    public static PollutantThreshold fromString(String pollutantName) {
        if (pollutantName.toLowerCase().contains("pm10")) return PM10;
        if (pollutantName.toLowerCase().contains("pm2.5")) return PM25;
        return NO2; // Default
    }
}
    


/**
 * Enumeration class PollutantThreshold - write a description of the enum class here
 *
 * @author (your name here)
 * @version (version number or date here)
 */
public enum PollutantThreshold
{
    NO2(0, 0, 0),
    PM10(0, 0, 0),   
    PM25(0, 0, 0);
    
    private double greenMax;
    private double yellowMax;
    private double orangeMax;

    // Constructor for the Enum
    PollutantThreshold(double greenMax, double yellowMax, double orangeMax) {
        this.greenMax = greenMax;
        this.yellowMax = yellowMax;
        this.orangeMax = orangeMax;
    }

    public double getGreenMax() { return greenMax; }
    public void setGreenMax(double newGreenMax) { this.greenMax = newGreenMax;}
    
    public double getYellowMax() { return yellowMax; }
    public void setYellowMax(double newYellowMax) { this.yellowMax = newYellowMax;}
    
    public double getOrangeMax() { return orangeMax; }
    public void setOrangeMax(double newOrangeMax) { this.orangeMax = newOrangeMax;}
    
    /**
     * Helper method to easily convert the String from the CSV file into this Enum
     */
    public static PollutantThreshold fromString(String pollutantName) {
        if (pollutantName.toLowerCase().contains("pm10")) return PM10;
        if (pollutantName.toLowerCase().contains("pm2.5")) return PM25;
        return NO2; // Default
    }
}
    

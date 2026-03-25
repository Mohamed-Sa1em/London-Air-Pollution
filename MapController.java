import javafx.scene.layout.Pane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.*;
import javafx.scene.paint.Color;
import javafx.scene.control.*;
import javafx.util.Duration;
import java.io.*;
import javafx.application.Platform;
import java.util.*;

/**
 * MapController class is soley responsibly for rendering the map of London so far.
 * Eventually it will act as a canvas where we "allocate" and display the pollution data points 
 *
 *
 * @author Mohamed Salem
 * @version 1.2
 */
public class MapController extends Pane
{
    // Variable that holds and display the london.png image
    private ImageView mapBackground;
    private Calculation calculation;
    
    private final double Max_Easting = 553297;
    private final double Min_Easting = 510394;
    private final double Max_Northing = 193305;
    private final double Min_Northing = 168504;
    
    private final double RangeX = Max_Easting - Min_Easting;
    private final double RangeY = Max_Northing - Min_Northing;

    /**
     * Constructor for objects of class MapController#
     * makes a map pane and loads the image
     */
    public MapController()
    {
        //loading the image
        Image mapImage = new Image("file:assets/London.png");
        mapBackground = new ImageView(mapImage);
        calculation = new Calculation();

        //scaling the image to set the window 
        mapBackground.setFitWidth(1088);

        //preserve the image from distortion
        mapBackground.setPreserveRatio(true); 
        mapBackground.setSmooth(true); //"crisper image "

        this.getChildren().add(mapBackground); //adds the image to the scrren 
    }

    /**
     * Plots the data from a given dataset
     * @param dataSet the dataset 
     * @param filterLevel this considers which category of pollutant level should be displayed
     */
    public void plotData(DataSet dataSet, String filterLevel)
    {
        final double RangeX = Max_Easting - Min_Easting;
        final double RangeY = Max_Northing - Min_Northing;
    
        String currentPollutant = dataSet.getPollutant();
        String year = dataSet.getYear();

        List<DataPoint> relevantDataPoints = calculation.findRelevantData(dataSet.getData());
        relevantDataCalc(relevantDataPoints, currentPollutant, year, filterLevel);
    
    }
    
    /**
     * Calculates relevant information about the datapoints and proceeds to display them on the map
     */
    private void relevantDataCalc(List<DataPoint> relevantDataPoints, String currentPollutant, String year, String filterLevel) {
        double maxPollutionForThisData = calculation.returnMaxLevelOfPollution(relevantDataPoints);
        double meanPollutionForThisData = calculation.findAverageFromData(relevantDataPoints);
        
        PollutantThreshold thresholds = PollutantThreshold.fromString(currentPollutant);
        setThresholdValues(meanPollutionForThisData, thresholds);
        
        this.getChildren().removeIf(node -> node instanceof Rectangle);
        for(DataPoint dataPoint : relevantDataPoints){
            showOneDataPoint(dataPoint, thresholds, currentPollutant, year, filterLevel);
        }
    }
    
    /**
     * Plots a single data point on the map
     */
    private void showOneDataPoint(DataPoint dataPoint, PollutantThreshold thresholds, String currentPollutant, String year, String filterLevel) {
        double This_Easting = dataPoint.x();
        double This_Northing = dataPoint.y();
        double This_Value= dataPoint.value();
        
        double baseOpacity = 0.15;
        
        double translated_X = (((This_Easting - Min_Easting)/RangeX)* mapBackground.getBoundsInLocal().getWidth());
        double translated_Y = (((Max_Northing - This_Northing)/RangeY)* mapBackground.getBoundsInLocal().getHeight());

        Rectangle marker = new Rectangle(translated_X,translated_Y, 26.5, 26.5);
        String pointCategory = setMarkerColours(thresholds, marker, This_Value);
        
        marker.setOpacity(baseOpacity);
            
        // adds hovering functionality
        setupHoveringTool(dataPoint, currentPollutant, This_Value, year, marker, baseOpacity);
        
        if (filterLevel.equals("All") || filterLevel.equals(pointCategory)) {
            this.getChildren().add(marker);
        }
    }
    
    /**
     * Determines the thresholdvalues given the mean of the data and the actual thresholds
     */
    private void setThresholdValues(double meanPollutionForThisData, PollutantThreshold thresholds) {
        thresholds.setGreenMax(meanPollutionForThisData * 0.9);
        thresholds.setYellowMax(meanPollutionForThisData);
        thresholds.setOrangeMax(meanPollutionForThisData * 1.1);
    }
    
    /**
     * Sets the colors of the datapoint markers and returns a relevant category for the pollutant level
     */
    private String setMarkerColours(PollutantThreshold thresholds, Rectangle marker, double This_Value){
        if (This_Value < thresholds.getGreenMax()) {
            marker.setFill(Color.GREEN.darker());
            return "Low";
        } else if (This_Value < thresholds.getYellowMax()) {
            marker.setFill(Color.YELLOW);
            return "Medium";
        } else if (This_Value < thresholds.getOrangeMax()) {
            marker.setFill(Color.DARKORANGE);
            return "Medium";
        } else {
            marker.setFill(Color.CRIMSON);
            return "High";
        }
    }
    
    /**
     * Helper methood that get both the pollutant and the year
     * Then updates the map 
     */
    public void updateMapData(String pollutant, String year, String filterLevel) {

        String folderName = pollutant.toUpperCase();
        String filePrefix = pollutant.replace(".", "").toLowerCase();
        
        String suffix = "";
        if (filePrefix.contains("pm")) {
            suffix = "g";
        }
        
        String filePath = "UKAirPollutionData/" + folderName + "/map" + filePrefix + year + suffix + ".csv";
        
        DataLoader myLoader = new DataLoader();
        DataSet newData = myLoader.loadDataFile(filePath);

        if (newData != null) {  
            this.plotData(newData, filterLevel);
        }   else {

            System.out.println("Error: Could not find file " + filePath);
        }
    }
    
    /**
     * Used to show the highest level for a selected pollutant on the map
     */
    public void showHighestPollutant(String year, String pollutant) {
        DataSet data = getDataForYearAndPollutant(pollutant, year);
        
        List<DataPoint> relevantDataPoints = calculation.findRelevantData(data.getData());
        double maxValue = calculation.returnMaxLevelOfPollution(relevantDataPoints);
        DataPoint location = calculation.getLocationOfMaxValue(relevantDataPoints);
        
        showMaxOnMap(maxValue, location, pollutant, year);
    }
    
    /**
     * Shows the average pollutant levels for relevant datapoints for the area 
     * within a specific time period
     */
    public void showAverageForTimePeriod(String pollutant, String start, String end) {
        List<String> years = new ArrayList<>(List.of("2018", "2019", "2020", "2021", "2022", "2023"));
        List<String> middleYears = years.subList(years.indexOf(start), years.indexOf(end)+1);
        
        Map<Integer, Double[]> gridcodeAverage = new HashMap<>();
        Map<Integer, DataPoint> gridToDataPoint = new HashMap<>();
        
        for(String year: middleYears) {
            DataSet data = getDataForYearAndPollutant(pollutant, year);
            List<DataPoint> dataPoints = calculation.findRelevantData(data.getData());
            for (DataPoint dp: dataPoints) {
                int key = dp.gridCode();
                gridToDataPoint.put(key, dp);
                calculation.addSum(gridcodeAverage, gridToDataPoint, key);
            }
        }
        
        Map<DataPoint, Double> averageMap = calculation.mergeMaps(gridToDataPoint, gridcodeAverage);
        
        List<DataPoint> newDataPoints = calculation.returnNewDataPoints(averageMap);
        
        // plots markers with specialised hovering tool text
        relevantDataCalc(newDataPoints, pollutant,  middleYears.get(0) + " to " + middleYears.get(middleYears.size()-1) , "All");
        
    }
    
    /**
     * Adds the marker for the highest pollutant level on the map
     */
    private void showMaxOnMap(double maxValue, DataPoint dataPoint, String currentPollutant, String year) {
        this.getChildren().removeIf(node -> node instanceof Rectangle);
        
        double This_Easting = dataPoint.x();
        double This_Northing = dataPoint.y();
        double This_Value= dataPoint.value();
        
        double baseOpacity = 0.4;
        
        double translated_X = (((This_Easting - Min_Easting)/RangeX)* mapBackground.getBoundsInLocal().getWidth());

        double translated_Y = (((Max_Northing - This_Northing)/RangeY)* mapBackground.getBoundsInLocal().getHeight());

        Rectangle marker = new Rectangle(translated_X,translated_Y, 26.5, 26.5);
        marker.setFill(Color.CRIMSON);
        marker.setOpacity(baseOpacity);
        
        setupHoveringTool(dataPoint, currentPollutant, This_Value, year, marker, baseOpacity);

        this.getChildren().add(marker);
    }
    
    /**
     * Adds the hovering tool to each marker
     */
    private void setupHoveringTool(DataPoint dataPoint, String currentPollutant, double This_Value, String year, Rectangle marker, double baseOpacity){
        //Hovering and value formatting
        String labelText = String.format("Grid Code: " + dataPoint.gridCode() + "\nx: " + dataPoint.x() + "\ny: " + dataPoint.y() + "\n" + year + "\n" + currentPollutant + " Level: %.2f", This_Value); 
        Tooltip infoPopup = new Tooltip(labelText);
        infoPopup.setShowDelay(Duration.ZERO);
            
        Tooltip.install(marker, infoPopup);

        marker.setOnMouseEntered(event -> {
                marker.setOpacity(0.6);    
                marker.toFront();          
        });

        marker.setOnMouseExited(event -> {
                marker.setOpacity(baseOpacity);    
        });
    
    }
    
    /**
     * Returns the dataset of a selected year and pollutant
     */
    private DataSet getDataForYearAndPollutant(String pollutant, String year) {
        String folderName = pollutant.toUpperCase();
        String filePrefix = pollutant.replace(".", "").toLowerCase();
        
        String suffix = "";
        if (filePrefix.contains("pm")) {
            suffix = "g";
        }
        String filePath = "UKAirPollutionData/" + folderName + "/map" + filePrefix + year + suffix + ".csv";

        DataLoader myLoader = new DataLoader();
        DataSet newData = myLoader.loadDataFile(filePath);
        
        if (newData != null) {
            return newData;         
        } else {
             System.out.println("File not found");
             return null;
        }
    }
}
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
 * MapController class is soley responsibly for rendering the map of london so far.
 * Eventually it will act as a canvas where we "allocate" and display the pollution data points 
 *
 *
 * @author The 4 Mohameds
 * @version 1.1
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


    public void plotData(DataSet dataSet, String filterLevel)
    {
        final double RangeX = Max_Easting - Min_Easting;
        final double RangeY = Max_Northing - Min_Northing;
        
        List<DataPoint> relevantDataPoints = dataSet.returnRelevantData();
        double maxPollutionForThisData = calculation.returnMaxLevelOfPollution(relevantDataPoints);
        double meanPollutionForThisData = calculation.findAverageFromData(relevantDataPoints);
        
        String currentPollutant = dataSet.getPollutant();
        String year = dataSet.getYear();

        PollutantThreshold thresholds = PollutantThreshold.fromString(currentPollutant);
        setThresholdValues(meanPollutionForThisData, thresholds);
        
        this.getChildren().removeIf(node -> node instanceof Rectangle);
        for(DataPoint dataPoint : relevantDataPoints){
            showOneDataPoint(dataPoint, thresholds, currentPollutant, year, filterLevel);
        }
    
    }
    
    
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
            
        setupHoveringTool(dataPoint, currentPollutant, This_Value, year, marker, baseOpacity);
        
        if (filterLevel.equals("All") || filterLevel.equals(pointCategory)) {
            this.getChildren().add(marker);
        }
    }
    
    
    private void setThresholdValues(double meanPollutionForThisData, PollutantThreshold thresholds) {
        thresholds.setGreenMax(meanPollutionForThisData * 0.9);
        thresholds.setYellowMax(meanPollutionForThisData);
        thresholds.setOrangeMax(meanPollutionForThisData * 1.1);
    }
    
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
     * Used to show the highest level for a selected pollutant
     */
    public void showHighestPollutant(String year, String pollutant) {
        DataSet data = getDataForYearAndPollutant(pollutant, year);
        
        List<DataPoint> relevantDataPoints = data.returnRelevantData();
        double maxValue = calculation.returnMaxLevelOfPollution(relevantDataPoints);
        DataPoint location = calculation.getLocationOfMaxValue(relevantDataPoints);
        
        showMaxOnMap(maxValue, location, pollutant, year);
    }
    
    public void showAverageForTimePeriod(String pollutant, String start, String end) {
        List<String> years = new ArrayList<>(List.of("2018", "2019", "2020", "2021", "2022", "2023"));
        List<String> middleYears = years.subList(years.indexOf(start), years.indexOf(end)+1);
        
        Map<Integer, Double[]> gridcodeAverage = new HashMap<>();
        Map<Integer, DataPoint> gridToDataPoint = new HashMap<>();
        
        for(String year: middleYears) {
            List<DataPoint> dataPoints = getDataForYearAndPollutant(pollutant, year).returnRelevantData();
            for (DataPoint dp: dataPoints) {
                int key = dp.gridCode();
                gridToDataPoint.put(key, dp);
                calculation.addSum(gridcodeAverage, gridToDataPoint, key);
            }
        }
        
        Map<DataPoint, Double> averageMap = calculation.mergeMaps(gridToDataPoint, gridcodeAverage);
        
        plotAverageData(pollutant, averageMap, middleYears);
    }
    
    private void plotAverageData(String pollutant, Map<DataPoint, Double> averageMap, List<String> years) {
        Set<DataPoint> set = averageMap.keySet();
        
        for(DataPoint dp: set) {            
            double This_Easting = dp.x();
            double This_Northing = dp.y();
            double This_Average= averageMap.get(dp);
            
            double baseOpacity = 0.15;
            double translated_X = (((This_Easting - Min_Easting)/RangeX)* mapBackground.getBoundsInLocal().getWidth());
            double translated_Y = (((Max_Northing - This_Northing)/RangeY)* mapBackground.getBoundsInLocal().getHeight());
    
            Rectangle marker = new Rectangle(translated_X,translated_Y, 26.5, 26.5);
            
            marker.setOpacity(baseOpacity);
            marker.setFill(Color.GREEN);

            setupHoveringTool(dp, pollutant, This_Average, years.get(0) + " to " + years.get(years.size()-1), marker, baseOpacity);
            this.getChildren().add(marker);
        }
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
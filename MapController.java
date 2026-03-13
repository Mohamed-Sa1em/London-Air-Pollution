import javafx.scene.layout.Pane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.*;
import javafx.scene.paint.Color;
import javafx.scene.control.*;
import javafx.util.Duration;
import java.util.List;
import java.util.ArrayList;

/**
 * MapController class is soley responsibly for rendering the map of london so far.
 * Eventually it will act as a canvas where we "allocate" and display the pollution data points 
 *
 *
 * @author The 4 Mohameds
 * @version 1
 */
public class MapController extends Pane
{
    // Variable that holds and display the london.png image
    private ImageView mapBackground;

    /**
     * Constructor for objects of class MapController#
     * makes a map pane and loads the image
     */
    public MapController()
    {
        //loading the image
        Image mapImage = new Image("file:assets/London.png");
        mapBackground = new ImageView(mapImage);

        //scaling the image to set the window 
        mapBackground.setFitWidth(1088);

        //preserve the image from distortion
        mapBackground.setPreserveRatio(true); 
        mapBackground.setSmooth(true); //"crisper image "

        this.getChildren().add(mapBackground); //adds the image to the scrren 
    }

    /**
     * 
     */
    public void plotData(DataSet data)
    {
        final double Max_Easting = 553297;
        final double Min_Easting = 510394;
        final double Max_Northing = 193305;
        final double Min_Northing = 168504;

        final double RangeX = Max_Easting - Min_Easting;
        final double RangeY = Max_Northing - Min_Northing;
        
        List<DataPoint> dataPoints = data.getData();
        List<DataPoint> relevantDataPoints = returnRelevantData(dataPoints, Min_Easting, Max_Easting, Min_Northing, Max_Northing);
        double maxPollutionForThisData = returnMaxLevelOfPollution(relevantDataPoints);
        double meanPollutionForThisData = getMeanPollutionValueForData(relevantDataPoints);
        
        String currentPollutant = data.getPollutant();
        
        PollutantThreshold thresholds = PollutantThreshold.fromString(currentPollutant);
        thresholds.setGreenMax(meanPollutionForThisData * 0.9);
        thresholds.setYellowMax(meanPollutionForThisData);
        thresholds.setOrangeMax(meanPollutionForThisData * 1.1);
        
        this.getChildren().removeIf(node -> node instanceof Rectangle);
        for(DataPoint dp : relevantDataPoints){

            double This_Easting = dp.x();
            double This_Northing = dp.y();
            double This_Value= dp.value();
            
            double baseOpacity = 0.15;
            
            double translated_X = (((This_Easting - Min_Easting)/RangeX)* mapBackground.getBoundsInLocal().getWidth());

            double translated_Y = (((Max_Northing - This_Northing)/RangeY)* mapBackground.getBoundsInLocal().getHeight());

            Rectangle marker = new Rectangle(translated_X,translated_Y, 26.5, 26.5);
            if (This_Value < thresholds.getGreenMax()) {
                marker.setFill(Color.GREEN.darker());
            } else if (This_Value < thresholds.getYellowMax()) {
                marker.setFill(Color.YELLOW);
            } else if (This_Value < thresholds.getOrangeMax()) {
                marker.setFill(Color.DARKORANGE);
            } else {
                marker.setFill(Color.CRIMSON);
            }
            marker.setOpacity(baseOpacity);
                
            //Hovering and value formatting
            String labelText = String.format("Grid Code: " + dp.gridCode() + "\nx: " + dp.x() + "\ny: " + dp.y() + "\n" + currentPollutant + " Level: %.2f", This_Value); 
            Tooltip infoPopup = new Tooltip(labelText);
            infoPopup.setShowDelay(Duration.ZERO);
                
            Tooltip.install(marker, infoPopup);

            marker.setOnMouseEntered(event -> {
                    //marker.setRadius(7);     
                    marker.setOpacity(0.5);    
                    marker.toFront();          
            });

            marker.setOnMouseExited(event -> {
                    //marker.setRadius(3);      
                    marker.setOpacity(baseOpacity);    
            });

            this.getChildren().add(marker);
        

        }
    
    }
    
    /**
     * Helper methood that get both the pollutant and the year
     * Then updates the map 
     */
    public void updateMapData(String pollutant, String year) {
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
            this.plotData(newData);
        } else {
            System.out.println("Error: Could not find file " + filePath);
        }
    }
    
    private double returnMaxLevelOfPollution(List<DataPoint> dataPoints) {
        double max = 0;
        for (DataPoint dataPoint: dataPoints) {
            if (max < dataPoint.value()) {
                max = dataPoint.value();
            }
        }
        
        return max;
    }
    
    private double getMeanPollutionValueForData(List<DataPoint> dataPoints) {
        double sum = 0;
        for (DataPoint dataPoint: dataPoints) {
            sum += dataPoint.value();
        }
        
        return sum / dataPoints.size();
    }
    
    private List<DataPoint> returnRelevantData(
        List<DataPoint> dataPoints, double MinEasting, double MaxEasting, double MinNorthing, double MaxNorthing
        ) {
        ArrayList<DataPoint> relevantDataPoints = new ArrayList<DataPoint>();
        for (DataPoint dataPoint: dataPoints) {
            double This_Easting = dataPoint.x();
            double This_Northing = dataPoint.y();
            if (This_Easting >= MinEasting && This_Easting <= MaxEasting &&
                This_Northing >= MinNorthing && This_Northing <= MaxNorthing
            ) {
                relevantDataPoints.add(dataPoint);
            }
        }
        
        return relevantDataPoints;
    }

}
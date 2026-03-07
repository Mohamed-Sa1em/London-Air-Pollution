import javafx.scene.layout.Pane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import javafx.scene.control.*;
import javafx.util.Duration;
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
        Image mapImage = new Image("file:London.png");
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

        final double RangeX = Max_Easting - Min_Easting ;
        final double RangeY = Max_Northing - Min_Northing ;
        this.getChildren().removeIf(node -> node instanceof Circle);
        for(DataPoint dp : data.getData()){

            double This_Easting = dp.x();
            double This_Northing = dp.y();
            double This_Value= dp.value();

            String currentPollutant = data.getPollutant(); 
            PollutantThreshold thresholds = PollutantThreshold.fromString(currentPollutant);
            
            if (This_Easting >= Min_Easting && This_Easting <= Max_Easting &&
            This_Northing >= Min_Northing && This_Northing <= Max_Northing){
                double translated_X = (((This_Easting - Min_Easting)/RangeX)* mapBackground.getBoundsInLocal().getWidth());

                double translated_Y = (((Max_Northing - This_Northing)/RangeY)* mapBackground.getBoundsInLocal().getHeight());

                Circle marker = new Circle(translated_X,translated_Y,3);
                if (This_Value < thresholds.getGreenMax()) {
                    marker.setFill(Color.GREEN);
                } else if (This_Value < thresholds.getYellowMax()) {
                    marker.setFill(Color.YELLOW);
                } else if (This_Value < thresholds.getOrangeMax()) {
                    marker.setFill(Color.ORANGE);
                } else {
                    marker.setFill(Color.RED);
                }
                marker.setOpacity(0.6);
                
                //Hovering and value formatting
                String labelText = String.format(currentPollutant +" Level: %.2f", This_Value); 
                Tooltip infoPopup = new Tooltip(labelText);
                infoPopup.setShowDelay(Duration.ZERO);
                
                Tooltip.install(marker, infoPopup);

                marker.setOnMouseEntered(event -> {
                            marker.setRadius(7);     
                            marker.setOpacity(1.0);    
                            marker.toFront();          
                    });

                marker.setOnMouseExited(event -> {
                            marker.setRadius(3);      
                            marker.setOpacity(0.6);    
                    });

                this.getChildren().add(marker);
            }

        }

    
    }

}
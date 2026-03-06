import javafx.scene.layout.Pane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
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

    
}
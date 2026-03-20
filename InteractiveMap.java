import com.sothawo.mapjfx.*;
import javafx.scene.layout.StackPane;
/**
 * Write a description of class InteractiveMap here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class InteractiveMap extends StackPane {
    private MapView mapView;
    
    public InteractiveMap() { 
        mapView = new MapView();
        
        Coordinate londonCenter = new Coordinate(51.5074, -0.1278);
        mapView.setCenter(londonCenter);
        mapView.setZoom(12);
        mapView.setMapType(MapType.OSM);
        
        this.getChildren().add(mapView);
        mapView.initialize(Configuration.builder().showZoomControls(true).build());
    }
}
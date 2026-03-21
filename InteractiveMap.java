import com.sothawo.mapjfx.*;
import com.sothawo.mapjfx.event.MapViewEvent;
import javafx.scene.layout.StackPane;
/**
 * Write a description of class InteractiveMap here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class InteractiveMap extends StackPane {
    private MapView mapView;
    private Marker clickedMark;
    private MapLabel clickedLabel;
    private PollutionApi liveData;
    private String info;
    
    
    public InteractiveMap() { 
        mapView = new MapView();
        liveData = new PollutionApi();
        
        Coordinate londonCenter = new Coordinate(51.5074, -0.1278);
        mapView.setCenter(londonCenter);
        mapView.setZoom(12);
        mapView.setMapType(MapType.OSM);
        
        
        this.getChildren().add(mapView);  
        mapView.initializedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) { // newValue is true when the map is officially ready
            // Now it is safe to add the marker
                clickedMark = Marker.createProvided(Marker.Provided.RED);
                clickedMark.setPosition(londonCenter);
                clickedMark.setVisible(true);
                mapView.addMarker(clickedMark);
                clickedLabel = new MapLabel("Click this map to get the pollution for that point", 10, -30);
                clickedLabel.setPosition(londonCenter);
                clickedLabel.setVisible(true);
                mapView.addLabel(clickedLabel);
                
                System.out.println("Map is ready and marker added.");
            }
        });
        
        mapView.addEventHandler(MapViewEvent.MAP_CLICKED, event -> {
            Coordinate clickedCoord = event.getCoordinate();
            clickedMark.setPosition(clickedCoord);
            System.out.println("Map clicked at: " + clickedCoord.getLatitude());
            
            try
            {
                info = liveData.getDataAtPoint(clickedCoord.getLatitude(), clickedCoord.getLongitude());
            }
            catch (Exception e)
            {
                info = "N/A";
            }
                                
            if (clickedLabel != null) {
                mapView.removeLabel(clickedLabel);
            }
            
            clickedLabel = new MapLabel(info, 10, -30);
            clickedLabel.setPosition(clickedCoord);
            clickedLabel.setVisible(true);
            mapView.addLabel(clickedLabel);
        });
    
        mapView.initialize(Configuration.builder().showZoomControls(true).build());
    }
}
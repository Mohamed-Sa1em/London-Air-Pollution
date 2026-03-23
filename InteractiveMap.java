import com.sothawo.mapjfx.*;
import com.sothawo.mapjfx.event.MapViewEvent;
import javafx.scene.layout.StackPane;
import java.awt.event.ActionEvent;

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
    private LiveData liveData;
    private String info;
    
    
    public InteractiveMap(String mapType) { 
        mapView = new MapView();
        chooseMap(mapType);
        
        Coordinate londonCenter = new Coordinate(51.5074, -0.1278);
        mapView.setCenter(londonCenter);
        mapView.setZoom(12);
        mapView.setMapType(MapType.OSM);
        
        this.getChildren().add(mapView);  
        mapView.initializedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) { // newValue is true when the map is officially ready
                initialiseMap(londonCenter);
            }
        });
        
        mapView.addEventHandler(MapViewEvent.MAP_CLICKED, event -> mapClickedEvent(event));
        mapView.initialize(Configuration.builder().showZoomControls(true).build());
    }
    
    private void chooseMap(String mapType) {
        if (mapType.equals("Weather")) {
            liveData = new WeatherAPI();
        }else if (mapType.equals("Pollution")) {
            liveData = new PollutionAPI();
        }
    }
    
    private void initialiseMap(Coordinate londonCenter) {
        // Now it is safe to add the marker
        clickedMark = Marker.createProvided(Marker.Provided.RED);
        clickedMark.setPosition(londonCenter);
        clickedMark.setVisible(true);
        mapView.addMarker(clickedMark);
        clickedLabel = new MapLabel("Click this map to get the data for that point", 10, -30);
        clickedLabel.setPosition(londonCenter);
        clickedLabel.setVisible(true);
        mapView.addLabel(clickedLabel);
    }
    
    private void mapClickedEvent (MapViewEvent event) {
        Coordinate clickedCoord = event.getCoordinate();
        clickedMark.setPosition(clickedCoord);
        double lat = clickedCoord.getLatitude();
        double lon = normalizeLongitude(clickedCoord.getLongitude());
        
        try{ info = liveData.getDataAtPoint(lat, lon); }
        catch (Exception e){ info = "N/A"; }
                            
        if (clickedLabel != null) {
            mapView.removeLabel(clickedLabel);
        }
                
        clickedLabel = new MapLabel(info, 10, -30);
        clickedLabel.setPosition(clickedCoord);
        clickedLabel.setVisible(true);
        mapView.addLabel(clickedLabel);
    }
    
    private double normalizeLongitude(double lon) {
        // This formula wraps the number so it always stays between -180 and 180
        double n = (lon + 180.0) % 360.0;
        if (n < 0) {
            n += 360.0;
        }
        return n - 180.0;
    }
}
import org.locationtech.proj4j.CRSFactory;
import org.locationtech.proj4j.CoordinateReferenceSystem;
import org.locationtech.proj4j.CoordinateTransform;
import org.locationtech.proj4j.CoordinateTransformFactory;
import org.locationtech.proj4j.ProjCoordinate;
/**
 * Write a description of class CoordinateConversion here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class CoordinateConverter
{
    
    public CoordinateConverter() {}
    
    public int[] returnEastingNorthingValues(double lat, double lon) {

        CRSFactory crsFactory = new CRSFactory();

        // WGS84 (GPS lat/lon)
        CoordinateReferenceSystem WGS84 =
            crsFactory.createFromParameters(
                "WGS84",
                "+proj=longlat +datum=WGS84 +no_defs"
            );

        // British National Grid
        CoordinateReferenceSystem BNG =
            crsFactory.createFromParameters(
                "BNG",
                "+proj=tmerc +lat_0=49 +lon_0=-2 +k=0.9996012717 " +
                "+x_0=400000 +y_0=-100000 +ellps=airy " +
                "+towgs84=446.448,-125.157,542.060,0.1502,0.2470,0.8421,-20.4894 " +
                "+units=m +no_defs"
            );

        CoordinateTransformFactory ctFactory =
            new CoordinateTransformFactory();

        CoordinateTransform transform =
            ctFactory.createTransform(WGS84, BNG);

        ProjCoordinate src = new ProjCoordinate(lon, lat);
        ProjCoordinate dest = new ProjCoordinate();

        transform.transform(src, dest);
        
        double easting = dest.x;
        double northing = dest.y;
        
        int[] eastingNorthing = roundEastingNorthing(easting, northing);

        return eastingNorthing;
    }
    
    
    public double[] returnLonLatValues(int easting, int northing) {

        CRSFactory crsFactory = new CRSFactory();

        // British National Grid
        CoordinateReferenceSystem BNG =
            crsFactory.createFromParameters(
                "BNG",
                "+proj=tmerc +lat_0=49 +lon_0=-2 +k=0.9996012717 " +
                "+x_0=400000 +y_0=-100000 +ellps=airy " +
                "+towgs84=446.448,-125.157,542.060,0.1502,0.2470,0.8421,-20.4894 " +
                "+units=m +no_defs"
            );

        // WGS84 (GPS coordinates)
        CoordinateReferenceSystem WGS84 =
            crsFactory.createFromParameters(
                "WGS84",
                "+proj=longlat +datum=WGS84 +no_defs"
            );

        CoordinateTransformFactory ctFactory =
            new CoordinateTransformFactory();

        CoordinateTransform transform =
            ctFactory.createTransform(BNG, WGS84);

        ProjCoordinate src = new ProjCoordinate(easting, northing);
        ProjCoordinate dest = new ProjCoordinate();

        transform.transform(src, dest);

        double lon = dest.x;
        double lat = dest.y;

        return new double[]{lon, lat};
    }
    
    private int[] roundEastingNorthing(double easting, double northing) {
        easting = (int) Math.floor(easting) + 500;
        northing = (int) Math.floor(northing) + 500;
        
        return new int[]{(int) easting, (int) northing};
    }
    
    
}
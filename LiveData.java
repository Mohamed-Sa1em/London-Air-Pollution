
/**
 * Write a description of class LiveData here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public abstract class LiveData
{

    /**
     * Constructor for objects of class LiveData
     */
    public LiveData()
    {
    }
    
    abstract public String getDataAtPoint(double latitude, double longitude) throws Exception;
}
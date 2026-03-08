import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.layout.*;
import javafx.scene.text.*;

/**
 * Write a description of JavaFX class StatsWindow here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class StatsWindow extends VBox
{
    public StatsWindow()
    {
        //super(root,width,height);
        Label titleLabel = new Label("Stats");
        titleLabel.setFont(Font.font("Arial",FontWeight.BOLD,26));
        
        Label instructionLabel = new Label(
        "This is where the stats panel will be.");
        
        instructionLabel.setFont(Font.font("Arial",40));
        
        this.getChildren().addAll(titleLabel,instructionLabel);

    
    }
    
    public double highestRecordedPollution(DataSet Data) {
        return 0;
    }
}
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.geometry.Insets;
import javafx.scene.control.*;
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
public class StatsWindow extends BorderPane
{
    
    public StatsWindow()
    {
        //super(root,width,height);
    
        
        this.setLeft(createSelectionBar());
        this.setCenter(createEmpty());

    
    }
    
    private Label createEmpty() {
        Label label = new Label("Hello world");
        return label;
    }
    
    
    private VBox createSelectionBar() {
        VBox vbox = new VBox(20);
        vbox.getStyleClass().add("filters");
        
        Label titleLabel = new Label("Stats");
        titleLabel.setFont(Font.font("Arial",FontWeight.BOLD, 20));

        Button averagePollutantLevel = new Button("Average Pollutant Level");
        averagePollutantLevel.setOnAction(this::avgPollutantScreen);
        
        Button highestPollutantLevel = new Button("Highest Pollutant Levels");
        highestPollutantLevel.setOnAction(this::highPollutantScreen);
        
        Button trendsOverTime = new Button("Trends over Time");
        trendsOverTime.setOnAction(this::trendOverTimeScreen);
        
        vbox.getChildren().addAll(titleLabel, averagePollutantLevel, highestPollutantLevel, trendsOverTime);
        
        //vbox.setMaxWidth(110);  return to this afterwards
        return vbox;
    }
    
    private void avgPollutantScreen(ActionEvent event) {
        return;
    }
    
    private void highPollutantScreen(ActionEvent event) {
        return;
    }
    
    private void trendOverTimeScreen(ActionEvent event) {
        return;
    }
    
}

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.geometry.Insets;

/**
 * Write a description of JavaFX class MainWindow here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class MainWindow extends Application
{

    /**
     * The start method is the main entry point for every JavaFX application. 
     * It is called after the init() method has returned and after 
     * the system is ready for the application to begin running.
     *
     * @param  stage the primary stage for this application.
     */
    @Override
    public void start(Stage stage)
    {
        HBox topBar = new HBox(10);
        topBar.setStyle("-fx-border-color: black; -fx-border-width: 0 0 2 0;");
        
        Button welcomeWindowButton = new Button("Welcome");
        welcomeWindowButton.setFont(new Font(20));
        
        Button dataMapButton = new Button("Data Map");
        dataMapButton.setFont(new Font(20));
        
        Button pollutionStatsButton = new Button("Pollution Stats");
        pollutionStatsButton.setFont(new Font(20));
        
        topBar.getChildren().addAll(welcomeWindowButton, dataMapButton, pollutionStatsButton);
        
        
        
        VBox filtersBar = new VBox(20);
        filtersBar.setStyle("-fx-border-color: black; -fx-border-width: 0 2 0 0;");
        
        Label filtersAndControlsLabel = new Label ("Filters and Controls");
        filtersBar.getChildren().addAll(filtersAndControlsLabel);
        
        
        BorderPane root = new BorderPane();
        root.setBorder(new Border(
        new BorderStroke(
                Color.BLACK,               // border color
                BorderStrokeStyle.SOLID,   // line style
                null,
                new BorderWidths(3)        // thickness
            )
        ));
        root.setTop(topBar);
        root.setLeft(filtersBar);

        // JavaFX must have a Scene (window content) inside a Stage (window)
        Scene scene = new Scene(root, 1200, 800);
        stage.setTitle("Air Pollution Monitor");
        stage.setScene(scene);

        // Show the Stage (window)
        stage.show();
    }
}
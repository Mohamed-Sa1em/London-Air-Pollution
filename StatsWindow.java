import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.layout.*;
import javafx.scene.text.*;

/**
 * Write a description of JavaFX class StatsWindow here.
 *
 * @author (your name)
 * @version 1.0
 */
public class StatsWindow extends BorderPane
{
    // the map used in the stats window
    private MapController map;
    
    public StatsWindow()
    {
        //super(root,width,height);
        map = new MapController();
        
        this.setLeft(createSelectionBar());
        this.setCenter(createEmpty());

    
    }
    
    /**
     * Place holder text
     */
    private Label createEmpty() {
        Label label = new Label("Hello world");
        return label;
    }
    
    /**
     * Creates the button for the user to select the wanted functionality
     * @return VBox
     */
    private VBox createSelectionBar() {
        VBox vbox = new VBox(50);
        vbox.getStyleClass().add("filters"); // adding stylesheet to the node
        
        Label titleLabel = new Label("Stats");
        titleLabel.setFont(Font.font("Arial",FontWeight.BOLD, 20));

        Button averagePollutantLevel = new Button("Average Pollutant Level");
        averagePollutantLevel.setOnAction(this::avgPollutantScreen);
        
        Button highestPollutantLevel = new Button("Highest Pollutant Levels");
        highestPollutantLevel.setOnAction(this::highPollutantScreen);
        
        Button trendsOverTime = new Button("Trends over Time");
        trendsOverTime.setOnAction(this::trendOverTimeScreen);
        
        vbox.getChildren().addAll(titleLabel, averagePollutantLevel, highestPollutantLevel, trendsOverTime);
        
        vbox.setMaxWidth(110);
        return vbox;
    }
    
    private void avgPollutantScreen(ActionEvent event) {
        return;
    }
    
    private void highPollutantScreen(ActionEvent event) {
        VBox root = new VBox(100);
        root.setAlignment(Pos.CENTER);

        
        Label titleLabel = new Label("Select the Pollutant and the Year");
        titleLabel.setFont(Font.font("Arial",FontWeight.BOLD, 20));
        
        HBox pollutantAndYearBar = new HBox(75);
        pollutantAndYearBar.setAlignment(Pos.CENTER);
        
        
        ComboBox<String> yearSelectorComboBox = new ComboBox<>();
        yearSelectorComboBox.getItems().addAll(
            "2018",
            "2019",
            "2020",
            "2021",
            "2022",
            "2023"
        );

        yearSelectorComboBox.getSelectionModel().selectFirst();


        ComboBox<String>  pollutantSelectorComboBox = new ComboBox<>();

        pollutantSelectorComboBox.getItems().addAll(
            "NO2",
            "PM2.5",
            "PM10"

        );
        pollutantSelectorComboBox.getSelectionModel().selectFirst();
        
        Button selectButton = new Button("Select");
        selectButton.setOnAction(selectionEvent -> {displayMap(pollutantSelectorComboBox, yearSelectorComboBox);});
        
        pollutantAndYearBar.getChildren().addAll(pollutantSelectorComboBox, yearSelectorComboBox, selectButton);
        
        root.getChildren().addAll(titleLabel, pollutantAndYearBar);
        this.setCenter(root);
    }
    
    private void trendOverTimeScreen(ActionEvent event) {
        VBox root = new VBox(100);
        root.setAlignment(Pos.CENTER);
        
        Label titleLabel = new Label("Select the Pollutant");
        titleLabel.setFont(Font.font("Arial",FontWeight.BOLD, 20));
        
        HBox pollutantBar = new HBox(75);
        pollutantBar.setAlignment(Pos.CENTER);
        
        Button no2 = new Button("NO2");
        no2.setOnAction(this::drawNO2Graph);
        
        Button pm25 = new Button("PM2.5");
        pm25.setOnAction(this::drawPM25Graph);
        
        Button pm10 = new Button("PM10");
        pm10.setOnAction(this::drawPM10Graph);
        
        Button allThree = new Button("All 3 together");
        allThree.setOnAction(this::drawAllThreeGraph);
        
        pollutantBar.getChildren().addAll(no2, pm25, pm10, allThree);
        
        root.getChildren().addAll(titleLabel, pollutantBar);
        this.setCenter(root);
    }
    
    private void displayMap(ComboBox<String> pollutant, ComboBox<String> year) {
        String selectedYear = year.getValue();
        String selectedPollutant = pollutant.getValue();
        
        this.setCenter(map);
        map.showHighestPollutant(selectedYear, selectedPollutant);
    }
    
    private void drawNO2Graph(ActionEvent event) {
        Graph graph = new Graph("NO2");
        this.setCenter(graph.returnLineChart());
    }
    
    private void drawPM25Graph(ActionEvent event) {
        Graph graph = new Graph("PM2.5");
        this.setCenter(graph.returnLineChart());
    }

    private void drawPM10Graph(ActionEvent event) {
        Graph graph = new Graph("PM10");
        this.setCenter(graph.returnLineChart());
    }
    
    private void drawAllThreeGraph(ActionEvent event) {
        Graph graph = new Graph("NO2", "PM2.5", "PM10");
        this.setCenter(graph.returnLineChart());
    }
}
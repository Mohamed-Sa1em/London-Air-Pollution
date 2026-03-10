import javafx.application.Application;
import java.io.FileInputStream;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.image.*;
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
    private MapController map = new MapController();
    private WelcomeWindow welcome = new WelcomeWindow();
    private StatsWindow stats = new StatsWindow();
    
    /**
     * The start method is the main entry point for every JavaFX application. 
     * It is called after the init() method has returned and after 
     * the system is ready for the application to begin running.
     *
     * @param  stage the primary stage for this application.
     */
    @Override
    public void start(Stage stage) throws java.io.FileNotFoundException
    {
        BorderPane root = new BorderPane();
        root.setBorder(new Border(
                new BorderStroke(
                    Color.BLACK,               // border color
                    BorderStrokeStyle.SOLID,   // line style
                    null,
                    new BorderWidths(3)        // thickness
                )
            ));
        root.setTop(createMenuBar(root));

        // JavaFX must have a Scene (window content) inside a Stage (window)
        Scene scene = new Scene(root, 1200, 728);
        scene.getStylesheets().add("styles.css");
        stage.setResizable(false); // makes the window non resizeable by the user
        stage.setTitle("Air Pollution Monitor");
        stage.setScene(scene);

        root.setCenter(new WelcomeWindow());
        //MapController myPollutionMap = new MapController();
        //root.setCenter(myPollutionMap);

        // Show the Stage (window)
        stage.show();
    }

    private HBox createMenuBar(BorderPane root) throws java.io.FileNotFoundException {
        HBox topBar = new HBox(10);
        topBar.getStyleClass().add("nav-bar");
        
        Image image = new Image(new FileInputStream("Logo2.png"));
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(100); 
        imageView.getStyleClass().add("logo");
        
        Region spacing = new Region();
        topBar.setHgrow(spacing, Priority.ALWAYS);
        
        Button welcomeWindowButton = new Button("Welcome");
        welcomeWindowButton.setOnAction(event -> {welcomeButtonPressed(event, root);});
        welcomeWindowButton.getStyleClass().add("nav-bar-button");

        Button dataMapButton = new Button("Data Map");
        dataMapButton.setOnAction(event -> {dataMapButtonPressed(event, root);});
        dataMapButton.getStyleClass().add("nav-bar-button");

        Button pollutionStatsButton = new Button("Pollution Stats");
        pollutionStatsButton.setOnAction(event -> {statsButtonPressed(event, root);});
        pollutionStatsButton.getStyleClass().add("nav-bar-button");

        topBar.getChildren().addAll(imageView, spacing, welcomeWindowButton, dataMapButton, pollutionStatsButton);

        return topBar;
    }

    private VBox createFiltersAndControlsBar() {
        VBox filtersBar = new VBox(20);
        filtersBar.getStyleClass().add("filters");

        Label filtersAndControlsLabel = new Label ("Filters and Controls");
        Label selectYearLabel = new Label("Select Year");

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


        Label pollutantSelectionLabel = new Label("Select Pollutant");

        ComboBox<String>  pollutantSelectorComboBox = new ComboBox<>();

        pollutantSelectorComboBox.getItems().addAll(
            "NO2",
            "PM2.5",
            "PM10"

        );


        pollutantSelectorComboBox.getSelectionModel().selectFirst();

        //Handles the change of the year and the pollutant
        //Loading the new dataset

        yearSelectorComboBox.setOnAction(event -> {comboBoxSelection(yearSelectorComboBox, pollutantSelectorComboBox);});

        pollutantSelectorComboBox.setOnAction(event -> {comboBoxSelection(yearSelectorComboBox, pollutantSelectorComboBox);});

        filtersBar.getChildren().addAll(filtersAndControlsLabel, selectYearLabel, yearSelectorComboBox, pollutantSelectionLabel, pollutantSelectorComboBox);

        return filtersBar;
    }
    
    
    private void welcomeButtonPressed(ActionEvent event, BorderPane root) {
        root.setCenter(welcome);
        root.setLeft(null);
    }
    
    private void dataMapButtonPressed(ActionEvent event, BorderPane root) {
        root.setCenter(map);
        root.setLeft(createFiltersAndControlsBar());
        DataLoader myLoader = new DataLoader();
        DataSet no2Data = myLoader.loadDataFile("UKAirPollutionData/NO2/mapno22023.csv");
        map.plotData(no2Data);
    }
    
    private void statsButtonPressed(ActionEvent event, BorderPane root) {
        root.setCenter(stats);
        root.setLeft(null);
    }
    
    private void comboBoxSelection(ComboBox<String> yearSelectorComboBox, ComboBox<String> pollutantSelectorComboBox) {
        String selectedpollutant = pollutantSelectorComboBox.getValue();
        String selectedYear = yearSelectorComboBox.getValue();
        map.updateMapData(selectedpollutant, selectedYear);
    
    }
}
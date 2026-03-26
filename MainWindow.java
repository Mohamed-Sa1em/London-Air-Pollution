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
import java.awt.event.ActionListener;
import javafx.concurrent.Task;


/**
 * The MainWindow class hosts the functionality of the main window that is presented
 * to the user. 
 *
 * @author Zubair Abdual Matin
 * @version 1.0
 */
public class MainWindow extends Application
{
    // instance variables
    private MapController map = new MapController();
    private WelcomeWindow welcome = new WelcomeWindow();
    private StatsWindow stats = new StatsWindow();
    private InteractiveMap liveMap;
    private boolean staticMap = true;
    private BorderPane root = new BorderPane();
    
    /**
     * The start method is the main entry point for every JavaFX application. 
     * It is called after the init() method has returned and after 
     * the system is ready for the application to begin running.
     *
     * @param  stage the primary stage for this application.
     */
    @Override

    public void start(Stage stage) throws Exception,java.io.FileNotFoundException

    {
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
        Scene scene = new Scene(root, 1220, 760);
        scene.getStylesheets().add("styles.css");
        Font.loadFont(getClass().getResourceAsStream("/assets/Roboto-Condensed.ttf"), 10);
        Font.loadFont(getClass().getResourceAsStream("/assets/Roboto-BoldCondensed.ttf"), 10);
        
        stage.setResizable(false); // makes the window non resizeable by the user
        stage.setTitle("Air Pollution Monitor");
        stage.setScene(scene);

        root.setCenter(new WelcomeWindow());

        // Show the Stage (window)
        stage.show();
    }

    /**
     * Creates the menu bar
     */
    private HBox createMenuBar(BorderPane root) throws Exception, java.io.FileNotFoundException {
        HBox topBar = new HBox(10);
        topBar.getStyleClass().add("nav-bar");
        
        // logo
        Image image = new Image("file:assets/Logo2.png");
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(100); 
        imageView.getStyleClass().add("logo");
        
        Region spacing = new Region();
        topBar.setHgrow(spacing, Priority.ALWAYS);
        
        // the welcome button
        Button welcomeWindowButton = new Button("Welcome");
        welcomeWindowButton.setOnAction(event -> {welcomeButtonPressed(event, root);});
        welcomeWindowButton.getStyleClass().add("nav-bar-button");

        // the data map button
        Button dataMapButton = new Button("Data Map");
        
        dataMapButton.setOnAction(event -> {
            try{ dataMapButtonPressed(event, root); }
            catch (Exception e){ e.printStackTrace(); }
        });
        dataMapButton.setMinWidth(dataMapButton.getPrefWidth());
        dataMapButton.getStyleClass().add("nav-bar-button");

        // the pollution stats button
        Button pollutionStatsButton = new Button("Pollution Stats");
        pollutionStatsButton.setOnAction(event -> {statsButtonPressed(event, root);});
        pollutionStatsButton.getStyleClass().add("nav-bar-button");

        topBar.getChildren().addAll(imageView, spacing, welcomeWindowButton, dataMapButton, pollutionStatsButton);

        return topBar;
    }

    /**
     * Creates the filter and control bar, visible when in Data Map screen
     */
    private VBox createFiltersAndControlsBar() throws Exception {
        VBox filtersBar = new VBox(20);
        filtersBar.getStyleClass().add("filters");
        
        Label filtersAndControlsLabel = new Label ("Filters and Controls");
        filtersAndControlsLabel.getStyleClass().add("bar-title");
        Label selectYearLabel = new Label("Select Year");

        ComboBox<String> yearSelectorComboBox = new ComboBox<>();

        yearSelectorComboBox.getItems().addAll(
            "2018",
            "2019",
            "2020",
            "2021",
            "2022",
            "2023");

        yearSelectorComboBox.getSelectionModel().selectFirst();


        Label pollutantSelectionLabel = new Label("Select Pollutant");

        ComboBox<String>  pollutantSelectorComboBox = new ComboBox<>();

        pollutantSelectorComboBox.getItems().addAll(
            "NO2",
            "PM2.5",
            "PM10"

        );
        pollutantSelectorComboBox.getSelectionModel().selectFirst();
        
        
        Label pollutionLevelSelectionLabel = new Label("Select Level");

        ComboBox<String>  pollutionLevelSelectorComboBox = new ComboBox<>();

        pollutionLevelSelectorComboBox.getItems().addAll(
            "All",
            "High",
            "Medium",
            "Low"
        );
        
        pollutionLevelSelectorComboBox.getSelectionModel().selectFirst();
        
        // labels for the API integration
        Label liveMapSelectionLabel = new Label("Live Maps:");
        
        Button pollutionLiveButton = new Button("Pollution");
        pollutionLiveButton.setOnAction(event -> {
            String text = pollutionLiveButton.getText();
            swapMapButton(event, root, text);
        });
        pollutionLiveButton.getStyleClass().add("filters-button");
        
        Button weatherLiveButton = new Button("Weather");
        weatherLiveButton.setOnAction(event -> {
            String text = weatherLiveButton.getText();
            swapMapButton(event, root, text);
        });
        weatherLiveButton.getStyleClass().add("filters-button");

        //Handles the change of the year and the pollutant
        //Loading the new dataset

        setupComboBoxEvent(yearSelectorComboBox, pollutantSelectorComboBox, pollutionLevelSelectorComboBox);

        yearSelectorComboBox.setOnAction(event -> {
            try
            {
                comboBoxSelection(yearSelectorComboBox, pollutantSelectorComboBox, pollutionLevelSelectorComboBox);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        });

        pollutantSelectorComboBox.setOnAction(event -> {
            try
            {
                comboBoxSelection(yearSelectorComboBox, pollutantSelectorComboBox, pollutionLevelSelectorComboBox);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        });
        
        pollutionLevelSelectorComboBox.setOnAction(event -> {
            try
            {
                comboBoxSelection(yearSelectorComboBox, pollutantSelectorComboBox, pollutionLevelSelectorComboBox);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        });
        
        filtersBar.getChildren().addAll(
            filtersAndControlsLabel, 
            selectYearLabel,
            yearSelectorComboBox,
            pollutantSelectionLabel, 
            pollutantSelectorComboBox,
            pollutionLevelSelectionLabel,
            pollutionLevelSelectorComboBox,
            liveMapSelectionLabel,
            pollutionLiveButton,
            weatherLiveButton
        );
        
        return filtersBar;
    }
    
    /**
     * Handles the actions that occur when users select values from the combo box 
     */
    private void setupComboBoxEvent(ComboBox yearSelectorComboBox, ComboBox pollutantSelectorComboBox, ComboBox pollutionLevelSelectorComboBox) {
        yearSelectorComboBox.setOnAction(event -> {
            try{ comboBoxSelection(yearSelectorComboBox, pollutantSelectorComboBox, pollutionLevelSelectorComboBox); }
            catch (Exception e){ e.printStackTrace(); }
        });

        pollutantSelectorComboBox.setOnAction(event -> {
            try{ comboBoxSelection(yearSelectorComboBox, pollutantSelectorComboBox, pollutionLevelSelectorComboBox); }
            catch (Exception e){ e.printStackTrace(); }
        });
        
        pollutionLevelSelectorComboBox.setOnAction(event -> {
            try{ comboBoxSelection(yearSelectorComboBox, pollutantSelectorComboBox, pollutionLevelSelectorComboBox); }
            catch (Exception e){ e.printStackTrace(); }
        });
    }
    
    /**
     * Triggers the welcome screen
     */
    private void welcomeButtonPressed(ActionEvent event, BorderPane root) {
        root.setCenter(welcome);
        root.setLeft(null);
    }
    
    /**
     * Triggers the data map screen
     */
    private void dataMapButtonPressed(ActionEvent event, BorderPane root) throws Exception {
        root.setCenter(map);
        root.setLeft(createFiltersAndControlsBar());
        DataLoader myLoader = new DataLoader();
        DataSet no2Data = myLoader.loadDataFile("UKAirPollutionData/NO2/mapno22018.csv");
        map.plotData(no2Data, "All");
    }
    
    /**
     * Swtiches between the different interactive maps
     */
    private void swapMapButton(ActionEvent event, BorderPane root, String text){
        if (text.equals("Weather")) {
            liveMap = new InteractiveMap("Weather");
        }else {
            liveMap = new InteractiveMap("Pollution");
        }
        root.setCenter(liveMap);
    }
    
    /**
     * Triggers the statistics screen
     */
    private void statsButtonPressed(ActionEvent event, BorderPane root) {
        root.setCenter(stats);
        root.setLeft(null);
    }
    
    /**
     * Updates the map according to the values in the combobox
     */
    private void comboBoxSelection(
        ComboBox<String> yearSelectorComboBox, ComboBox<String> pollutantSelectorComboBox, 
        ComboBox<String> pollutionLevelComboBox
        ) throws Exception {
        String selectedPollutant = pollutantSelectorComboBox.getValue();
        String selectedYear = yearSelectorComboBox.getValue();
        String selectedPollutionLevel = pollutionLevelComboBox.getValue();
        root.setCenter(map);
        map.updateMapData(selectedPollutant, selectedYear, selectedPollutionLevel);
    }
}
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
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.LinearGradient;
import javafx.geometry.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

/**
 * WelcomeWindow is a ScrollPane that displays all the information for the user to navigate
 * the application.
 *
 * @author Mohammed Aziz
 * @version 1.5
 */
public class WelcomeWindow extends ScrollPane
{
    /**
     * Constructor for WelcomeWindow
     */
    public WelcomeWindow()
    {
        VBox content = new VBox();
        this.setFitToWidth(true);
        
        VBox welcomeInfo = new VBox();
        welcomeInfo.getStyleClass().add("welcome-contents");
        
        Label instructionLabel = new Label(
        "This is a quick guide to show you how to use the app:");
        instructionLabel.getStyleClass().add("welcome-body");
        
        Image navBar = new Image("file:assets/navButtons.png");
        ImageView navBarView = new ImageView(navBar);
        navBarView.setPreserveRatio(true);
        
        Label navInfo = new Label(
        "In the top right right of the screen, you will see these buttons. They allow you to navigate to the different parts of this app.");
        navInfo.getStyleClass().add("welcome-body");
        
        welcomeInfo.getChildren().addAll(instructionLabel,navBarView,navInfo);
                                    
        content.getChildren().addAll(heroHeader(),welcomeInfo,new Separator(),dataMap(),new Separator(),statsPage());
        this.setContent(content);
    }
    
    /**
     * The title image and text at the top of the welcome page.
     * @return StackPane
     */
    private StackPane heroHeader(){
        StackPane heroSection = new StackPane();
        heroSection.setPrefHeight(600);
        heroSection.setMaxWidth(Double.MAX_VALUE);
        
        // Image Background
        ImageView background = new ImageView(new Image("file:assets/London_bg.jpeg"));
        background.setPreserveRatio(false);
        background.fitWidthProperty().bind(heroSection.widthProperty());
        background.fitHeightProperty().bind(heroSection.heightProperty());
        
        VBox heroTextContainer = new VBox(10);
        heroTextContainer.setAlignment(Pos.BOTTOM_LEFT);
        heroTextContainer.setPadding(new Insets(0, 0, 30, 60));
        
        // Overlay
        Rectangle overlay = new Rectangle();
        overlay.widthProperty().bind(heroSection.widthProperty());
        overlay.heightProperty().bind(heroSection.heightProperty());
        overlay.setFill(Color.rgb(0, 0, 0, 0.25));
        
        // Title
        Label titleLabel = new Label("London Pollution Tracker");
        titleLabel.getStyleClass().add("welcome-title");
        
        heroTextContainer.getChildren().add(titleLabel);
        
        heroSection.setAlignment(heroTextContainer, Pos.BOTTOM_LEFT);
        heroSection.getChildren().addAll(background, overlay, heroTextContainer);
        
        return heroSection;
    }
    
    /**
     * The description of the data map section on the welcome page.
     * @return VBox
     */
    private VBox dataMap(){
        VBox dataInfo = new VBox(25);
        dataInfo.getStyleClass().add("welcome-contents");
        
        // Title
        Label dataMapTitle = new Label("Data Map");
        dataMapTitle.getStyleClass().add("welcome-subtitle");
        
        // Large Static Map Bubble
        HBox staticMapInfo = new HBox(20);
        staticMapInfo.getStyleClass().add("content-bubble");
        
        Image dataMap = new Image("file:assets/dataMap.png");
        ImageView dataMapView = new ImageView(dataMap);
        dataMapView.setFitWidth(800);
        dataMapView.setPreserveRatio(true);
        
        Label dataMapInfo = new Label("This app's map is broken down into 1km by 1km squares, colour coded by how polluted it is. Red for heavily polluted down to green for lightly polluted.");
        dataMapInfo.getStyleClass().add("image-descriptor");
        
        staticMapInfo.getChildren().addAll(dataMapView,dataMapInfo);
        
        HBox dataMapExtras = new HBox(25);
        
        // Tooltip Bubble
        VBox tooltipBubble = new VBox(15);
        tooltipBubble.getStyleClass().add("content-bubble");
        
        Image mapDetails = new Image("file:assets/hoverDetails.png");
        ImageView mapDetailsView = new ImageView(mapDetails);
        mapDetailsView.setFitWidth(400);
        mapDetailsView.setPreserveRatio(true);
        
        Label mapDetailsInfo = new Label("When hovering over a grid square, a tooltip containing details about the pollution in that area will appear, alongside its unique area code.");
        mapDetailsInfo.getStyleClass().add("welcome-body");
        
        tooltipBubble.getChildren().addAll(mapDetailsView, mapDetailsInfo);
        
        // Filters bubble
        VBox filtersBubble = new VBox(15);
        filtersBubble.getStyleClass().add("content-bubble");
        
        Image filters = new Image("file:assets/filters.png");
        ImageView filtersView = new ImageView(filters);
        filtersView.setFitWidth(180);
        filtersView.setPreserveRatio(true);
        
        Label filtersInfo = new Label("On the left panel, the year and pollution type for the data shown can be selected.");
        filtersInfo.getStyleClass().add("welcome-body");
        
        filtersBubble.getChildren().addAll(filtersView,filtersInfo);
        
        // Live Map bubble
        VBox liveMapBubble = new VBox(15);
        liveMapBubble.getStyleClass().add("content-bubble");
        
        Image liveMaps = new Image("file:assets/liveMaps.png");
        ImageView liveMapsView = new ImageView(liveMaps);
        liveMapsView.setFitWidth(300);
        liveMapsView.setPreserveRatio(true);
        
        Label liveMapInfo = new Label("These buttons switch to the live maps for pollution and weather. Click on the map to get live data for that location.");
        liveMapInfo.getStyleClass().add("welcome-body");
        
        liveMapBubble.getChildren().addAll(liveMapsView,liveMapInfo);
        
        HBox.setHgrow(tooltipBubble, Priority.ALWAYS);
        HBox.setHgrow(filtersBubble, Priority.ALWAYS);
        HBox.setHgrow(liveMapBubble, Priority.ALWAYS);
        
        tooltipBubble.setMaxWidth(Double.MAX_VALUE);
        filtersBubble.setMaxWidth(Double.MAX_VALUE);
        liveMapBubble.setMaxWidth(Double.MAX_VALUE);
        
        dataMapExtras.getChildren().addAll(tooltipBubble,filtersBubble,liveMapBubble);
        
        dataInfo.getChildren().addAll(dataMapTitle,staticMapInfo,dataMapExtras);
        return dataInfo;
    }
    
    /**
     * The description of the Statistics Page on the welcome page.
     * @return VBox
     */
    private VBox statsPage() {
        VBox statsInfo = new VBox();
        statsInfo.getStyleClass().add("welcome-contents");
        
        Label statsTitle = new Label("Statistics Page");
        statsTitle.getStyleClass().add("welcome-subtitle");
        
        Label statsDetails = new Label(
        "The stats page aggregates data from the static map and retrieves useful trends/graphs.");
        statsDetails.getStyleClass().add("welcome-body");
        
        Image statsPage = new Image("file:assets/statsPage.png");
        ImageView statsPageView = new ImageView(statsPage);
        statsPageView.setFitWidth(800);
        statsPageView.setPreserveRatio(true);
        
        Label statsPageDetails = new Label(
        "The stats page can be navigated using the left hand side, for different metrics.");
        statsPageDetails.getStyleClass().add("welcome-body");
        
        statsInfo.getChildren().addAll(statsTitle,statsDetails,statsPageView,statsPageDetails);
        return statsInfo;
    }
}
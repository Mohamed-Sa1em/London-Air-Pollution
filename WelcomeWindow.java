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
 * Write a description of JavaFX class WelcomeWindow here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class WelcomeWindow extends VBox
{
    public WelcomeWindow()
    {
        //super(root,width,height);
        Label titleLabel = new Label("Welcome to the London Air ppp");
        titleLabel.setFont(Font.font("Arial",FontWeight.BOLD,26));
        
        Label instructionLabel = new Label(
        "This is a quick guide to show you how to use the app");
        
        instructionLabel.setFont(Font.font("Arial",40));
        
        this.getChildren().addAll(titleLabel,instructionLabel);

    
    }
    

     
}
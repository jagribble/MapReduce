package com.gribble;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.HashMap;


public class UserInterface extends Application {

    private Group root = new Group();
    private String helpString = "The file path should point to the folder which contains BOTH " +
            "'Top30_airports_LatLong.csv' & 'AComp_Passenger_data.csv'";
    @Override
    public void start(Stage primaryStage) throws Exception {
      //  primaryStage = new Stage();
        try {
            Class<?> macFontFinderClass = Class.forName("com.sun.t2k.MacFontFinder");
            Field psNameToPathMap = macFontFinderClass.getDeclaredField("psNameToPathMap");
            psNameToPathMap.setAccessible(true);
            psNameToPathMap.set(null, new HashMap<String, String>());
        } catch (Exception e) {

        }
        primaryStage.setTitle("Map Reduce Coursework - 23004709");


        // ------ Objective boxes ---------

        VBox objectiveBoxes = new VBox();
        Label help = new Label(helpString);
        help.setWrapText(true);

        Label obj1Label = new Label("Objective 1");
        final TextArea obj1 = new TextArea();

        Label obj2Label = new Label("Objective 2");
        final TextArea obj2 = new TextArea();

        Label obj3Label = new Label("Objective 3");
        final TextArea obj3 = new TextArea();

        HBox fileButtons = new HBox();
        Button makeTxt = new Button("Make Text files");
        makeTxt.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                StaticClass.makeTxtFile("Objective1.txt",obj1.getText());
                StaticClass.makeTxtFile("Objective2.txt",obj2.getText());
                StaticClass.makeTxtFile("Objective3.txt",obj3.getText());
            }
        });
        Button makeCSV = new Button("Make CSV files");
        makeCSV.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                StaticClass.createCSV("Objective1.csv",StaticClass.objective1CSVString);
                StaticClass.createCSV("Objective2.csv",StaticClass.objective2CSVString);
                StaticClass.createCSV("Objective3.csv",StaticClass.objective3CSVString);
            }
        });
        fileButtons.getChildren().addAll(makeTxt,makeCSV);
        objectiveBoxes.getChildren().addAll(help,obj1Label,obj1,obj2Label,obj2,obj3Label,obj3,fileButtons);
        //-------------------------------
        HBox hBox = new HBox();
        hBox.setPadding(new Insets(15, 12, 15, 12));
        hBox.setSpacing(10);
        // hbox.setStyle("-fx-background-color: #336699;");

        Label label = new Label("Please Enter File path for Files");
        final TextField path = new TextField ();
        Button run = new Button("Run Map Reduce");
        run.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // window.hide();
                String dirName = path.getText();
                if(!dirName.isEmpty() && dirName.charAt(dirName.length()-1) == '/'){
                    Main.getPassangerData(dirName);
                    Main.getAirportData(dirName);
                    StaticClass.getAirportHashMap(Main.airportLines);
                    try {
                        String reduced1 = Main.runObjective1();
                        String reduced2 = Main.runObjective2();
                        String reduced3 = Main.runObjective3();
                        obj1.setText(reduced1);
                        obj2.setText(reduced2);
                        obj3.setText(reduced3);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        hBox.getChildren().addAll(label, path,run);


        HBox root = new HBox(10d);
        BorderPane border = new BorderPane();
        VBox output = new VBox(5);
        border.setTop(hBox);
        border.setCenter(objectiveBoxes);
        root.getChildren().add(border);
        primaryStage.setScene(new Scene(root,800,600));
        primaryStage.show();

    }





    public static void main(String[] args){
        launch();
    }
}

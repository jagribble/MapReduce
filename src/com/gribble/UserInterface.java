package com.gribble;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.HashMap;


public class UserInterface extends Application {

    private Group root = new Group();
    private String helpString = "The file path should point to the folder which contains BOTH " +
            "'Top30_airports_LatLong.csv' & 'AComp_Passenger_data.csv'";
    @Override
    public void start(final Stage primaryStage) throws Exception {
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

        final Label outputPathLabel = new Label("Please enter the path you would like the output files to be saved to");
        Label outputDesc = new Label("Enter FULL file path of where you expect the output OR leave empty to save in the same folder as the .jar");
        final TextField outputPath = new TextField();
        HBox fileButtons = new HBox();
        Button makeTxt = new Button("Make Text files");
        makeTxt.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String dirName = outputPath.getText();
                StaticClass.makeTxtFile(dirName+"Objective1.txt",StaticClass.objective1TextString);
                StaticClass.makeTxtFile(dirName+"Objective2.txt",StaticClass.objective2TextString);
                StaticClass.makeTxtFile(dirName+"Objective3.txt",StaticClass.objective3TextString);
            }
        });
        Button makeCSV = new Button("Make CSV files");
        makeCSV.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String dirName = outputPath.getText();
                StaticClass.createCSV(dirName+"Objective1.csv",StaticClass.objective1CSVString);
                StaticClass.createCSV(dirName+"Objective2.csv",StaticClass.objective2CSVString);
                StaticClass.createCSV(dirName+"Objective3.csv",StaticClass.objective3CSVString);
            }
        });
        final DirectoryChooser outputFolder = new DirectoryChooser();
        outputFolder.setTitle("Choose output directory");
        Button fileOutputButton = new Button("Open Folder");
        fileOutputButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                File file = outputFolder.showDialog(primaryStage);
                if (file != null) {
                    outputPath.setText(file.getAbsolutePath()+"/");
                }
            }
        });
        fileButtons.getChildren().addAll(makeTxt,makeCSV);
        objectiveBoxes.getChildren().addAll(help,obj1Label,obj1,obj2Label,obj2,obj3Label,obj3,outputPathLabel,outputDesc,fileOutputButton,outputPath,fileButtons);
        //-------------------------------
        HBox hBox = new HBox();

        hBox.setSpacing(4);
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

        final DirectoryChooser inputFolder = new DirectoryChooser();
        inputFolder.setTitle("Choose input directory");
        Button fileInputButton = new Button("Open Folder");
        fileInputButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                File file = inputFolder.showDialog(primaryStage);
                if (file != null) {
                    path.setText(file.getAbsolutePath()+"/");
                    System.out.println(file.getAbsolutePath());
                    //   openFile(file);
                }
            }
        });
        hBox.getChildren().addAll(label, path,fileInputButton,run);


        HBox root = new HBox(10d);
        BorderPane border = new BorderPane();
        VBox output = new VBox(5);
        border.setTop(hBox);
        border.setCenter(objectiveBoxes);
        root.getChildren().add(border);
        primaryStage.setScene(new Scene(root,800,700));
        primaryStage.show();

    }





    public static void main(String[] args){
        launch();
    }
}

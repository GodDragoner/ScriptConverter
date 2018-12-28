import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

//The driver class of the app. Currently, you need to change the file path of the input folder
//to whatever file you want to read in. Output isn't getting written to the output file right now
//You can just copy paste it from the console window for now
public class Driver extends Application
{
    private Stage primaryStage;
    private Button TAIButton;
    private Button TAJButton;
    private TextField TAIText;
    private TextField TAJText;
    private Button ConvertButton;
    
    private String path1;
    private String path2;

    public static void main(String[] args)
    {
        launch(args);
        /*RegexTesting testingRegex = new RegexTesting();
        testingRegex.testSendMessage();
        testingRegex.testCommands();
        
        ScriptAnalyzer analyzer = new ScriptAnalyzer();
        File input = new File("C:\\Users\\tyler\\Desktop\\Coding\\Testing\\test\\TeaseAIJavaGithub\\Personalities\\HouseOfTease\\"
                + "Original\\House_of_Tease_6\\Modules\\AV_ModFetish2.txt"); /*"C:\\Users\\tyler\\Desktop\\Coding\\Testing\\test\\TeaseAIJavaGithub\\Personalities\\HouseOfTease\\"s + "Original\\House_of_Tease_6\\Stroke\\End\\Extended_End.txt"
        File output = new File("C:\\Users\\tyler\\Desktop\\Coding\\Testing\\test\\TeaseAIJavaGithub\\Personalities\\HouseOfTease\\"
                + "Structure\\Start\\Extended_Start.js");
        StringBuffer outputBuffer;
        try
        {
            outputBuffer = analyzer.analyze(input, output);
            if (!output.exists())
            {
                output.createNewFile();
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(output));
            //writer.write(outputBuffer.toString());
            writer.flush();
            writer.close();
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/
    }
    
    public void Convert()
    {
        path1 = TAIText.getText();
        path2 = TAJText.getText();
        if (path1 != null && path2 != null)
        {   
            ScriptAnalyzer analyzer = new ScriptAnalyzer();
            File input = new File(path1); /*"C:\\Users\\tyler\\Desktop\\Coding\\Testing\\test\\TeaseAIJavaGithub\\Personalities\\HouseOfTease\\"s + "Original\\House_of_Tease_6\\Stroke\\End\\Extended_End.txt"*/
            File output = new File(path2 + File.separator + (input.getName()).replaceAll(".txt", ".js"));
            StringBuffer outputBuffer;
            try
            {
                outputBuffer = analyzer.analyze(input, output);
                if (!output.exists())
                {
                    output.createNewFile();
                }
                BufferedWriter writer = new BufferedWriter(new FileWriter(output));
                writer.write(outputBuffer.toString());
                writer.flush();
                writer.close();
            }
            catch (Exception e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    
    @Override
    public void start(Stage arg0) throws Exception
    {
        this.primaryStage = arg0;
        this.primaryStage.setTitle("Script Converter TAI->TAJ");
        // TODO Auto-generated method stub
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Driver.class.getResource("main.fxml"));
        AnchorPane rootLayout = (AnchorPane) loader.load();
        Scene scene = new Scene(rootLayout);
        primaryStage.setScene(scene);
        primaryStage.show();
        TAIButton = (Button) scene.lookup("#TAIButton");
        TAJButton = (Button) scene.lookup("#TAJButton");
        ConvertButton = (Button) scene.lookup("#convertButton");
        TAIText = (TextField) scene.lookup("#TAIText");
        TAJText = (TextField) scene.lookup("#TAJText");
        TAJButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                DirectoryChooser chooser = new DirectoryChooser();
                if (TAJText.getText() != null)
                {
                    chooser.setInitialDirectory(new File(TAJText.getText()));
                }
                chooser.setTitle("Select Tagging Folder");

                String dir;
                if (TAJText.getText() != null && new File(TAJText.getText()).exists()) {
                    dir = TAJText.getText();
                } else {
                    dir = System.getProperty("user.dir");
                }

                File defaultDirectory = new File(dir);
                chooser.setInitialDirectory(defaultDirectory);
                File selectedDirectory = chooser.showDialog(primaryStage);

                if (selectedDirectory != null) {
                    TAJText.setText(selectedDirectory.getPath());
                    //Create new stage window
                    path2 = selectedDirectory.getPath();
                    System.out.println(path2);
                }
            }
        });
        
        TAIButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                if (path1 != null)
                {
                    fileChooser.setInitialDirectory((new File(path1)).getParentFile());
                }
                // Set extension filter
                FileChooser.ExtensionFilter extFilter = 
                        new FileChooser.ExtensionFilter("TEXT files (*.txt)", "*.txt");
                fileChooser.getExtensionFilters().add(extFilter);

                // Show open file dialog
                File file = fileChooser.showOpenDialog(primaryStage);
                if (file != null) {
                    TAIText.setText(file.getPath());
                    path1 = file.getPath();
                    System.out.println(path1);
                }
            }
        });
        
        ConvertButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                Convert();
            }
        });
    }

}

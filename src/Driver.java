import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;

public class Driver
{

    public static void main(String[] args)
    {
        RegexTesting testingRegex = new RegexTesting();
        testingRegex.testSendMessage();
        testingRegex.testCommands();
        
        ScriptAnalyzer analyzer = new ScriptAnalyzer();
        File input = new File("C:\\Users\\tyler\\Desktop\\Coding\\Testing\\test\\TeaseAIJavaGithub\\Personalities\\HouseOfTease\\"
                + "Original\\House_of_Tease_6\\Stroke\\Start\\Extended_Start.txt"); /*"C:\\Users\\tyler\\Desktop\\Coding\\Testing\\test\\TeaseAIJavaGithub\\Personalities\\HouseOfTease\\"s + "Original\\House_of_Tease_6\\Stroke\\End\\Extended_End.txt"*/
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

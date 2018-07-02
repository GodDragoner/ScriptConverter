import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;

public class Driver
{

    public static void main(String[] args)
    {
        ScriptAnalyzer analyzer = new ScriptAnalyzer();
        File input = new File("C:\\Users\\tyler\\Desktop\\ScriptAnalyzer\\Input\\MissBlue1.txt");
        File output = new File("C:\\Users\\tyler\\Desktop\\ScriptAnalyzer\\Output\\MissBlue1.js");
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

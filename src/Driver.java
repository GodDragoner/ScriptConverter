import java.io.File;
import java.io.FileNotFoundException;

public class Driver
{

    public static void main(String[] args)
    {
        ScriptAnalyzer analyzer = new ScriptAnalyzer();
        File input = new File("C:\\Users\\tyler\\Desktop\\ScriptAnalyzer\\Input\\I Was Just Thinking About You.txt");
        File output = new File("C:\\Users\\tyler\\Desktop\\ScriptAnalyzer\\Output\\I Was Just Thinking About You.js");
        try
        {
            analyzer.analyze(input, output);
        }
        catch (FileNotFoundException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}

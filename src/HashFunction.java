import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//A hash function is a type of function in TAI that uses a # to
//start the function instead of an @. These are also typically
//allowed inside of messages Ex: #random(1, 7)
public class HashFunction extends LineComponent
{
    public ArrayList<String> arguments;
    public String commandName;
    public HashFunction(String content)
    {
        super(content);
        // TODO Auto-generated constructor stub
        
        arguments = new ArrayList<String>();
        String argumentRegex = "(?<=(\\(|\\[)|\\s?)(\\s?\\w+\\s?)(?=(\\)|\\])|\\s?,)";
        Matcher argumentMatcher = Pattern.compile(argumentRegex).matcher(content);
        Matcher commandNameMatcher = Pattern.compile("#\\w+").matcher(content);
        if (commandNameMatcher.find())
        {
            commandName = commandNameMatcher.group().trim().replaceFirst("#", "");
        }
        while (argumentMatcher.find())
        {
            arguments.add(argumentMatcher.group().trim());
        }
    }
    
    public String getOutput()
    {
        String toOutput = "";
        switch (commandName.toLowerCase())
        {
        case "random":
            if (arguments.size() != 2)
            {
                throw new IllegalStateException("arguments size needs to be 2");
            }
            toOutput = "randomInt(" + arguments.get(0) + ", " + arguments.get(1) + ")";
            break;
            
            default:
                toOutput = "--HashFunction " + commandName + arguments.toString();
        }
        return toOutput;
    }
    
    @Override
    public String toString()
    {
        return getOutput();
    }
}

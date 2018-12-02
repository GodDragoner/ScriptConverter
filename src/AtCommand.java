import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


//An AtCommand is a command in the original TeaseAI
//An example AtCommand could be @RapidTextOn or @Goto(AV_DecOrg)
//In both examples, the command name would be RapidTextOn and Goto respectively
//However, the second command will have a parameters list of size 1 with the parameter
//"AV_DecOrg"
public class AtCommand extends LineComponent
{
    public String commandName;
    public ArrayList<LineComponent> parameters;
    public AtCommand(String content)
    {
        super(content);
        parameters = new ArrayList<LineComponent>();

        String argumentRegex = "(?<=(\\(|\\[)|\\s?)(\\s?" + RegexHelper.hashFunction + "|" + RegexHelper.argument + "\\s?)(?=(\\)|\\])|\\s?,)";
        //String argumentRegex = RegexHelper.hashFunction;
        
        Matcher commandNameMatcher = Pattern.compile(RegexHelper.atCommandSimple).matcher(content);
        Matcher argumentMatcher = Pattern.compile(argumentRegex).matcher(content);
        
        if (commandNameMatcher.find())
        {
            String commandNameTemp = commandNameMatcher.group();
            this.commandName = StringHelper.removeChars(commandNameTemp, "@");
        }
        while (argumentMatcher.find())
        {
            String found = argumentMatcher.group().trim();
            if (found.matches(RegexHelper.hashFunction))
            {
                parameters.add(new HashFunction(found));
            }
            else if (found.matches(RegexHelper.simplePhrase))
            {
                parameters.add(new Phrase(found));
            }
            else if (found.matches(RegexHelper.randomText))
            {
                parameters.add(new RandomText(found));
            }
            else if (found.matches(RegexHelper.formatter))
            {
                parameters.add(new Formatter(found));
            }
            else if (found.matches(RegexHelper.variable))
            {
                parameters.add(new Variable(found));
            }
            else if (found.matches(RegexHelper.path))
            {
                parameters.add(new Path(found));
            }
        }
    }
    @Override
    public String toString()
    {
        String toReturn = commandName;
        if (parameters.size() > 0)
        {
            toReturn += "(";
            for (LineComponent param: parameters)
            {
                toReturn += param.toString() + ",";
            }
            toReturn = toReturn.substring(0, toReturn.length() - 1);
            toReturn += ")";
        }
        return "Command:" + toReturn;
    }

}

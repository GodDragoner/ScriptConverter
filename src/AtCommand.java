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
    public ArrayList<String> parameters;
    public AtCommand(String content)
    {
        super(content);
        parameters = new ArrayList<String>();

        String argumentRegex = "((\\(|\\[)|\\s?)(\\s?" + RegexHelper.argument + "\\s?)((\\)|\\])|\\s?,)";
        
        Matcher commandNameMatcher = Pattern.compile(RegexHelper.atCommandSimple).matcher(content);
        Matcher argumentMatcher = Pattern.compile(argumentRegex).matcher(content);
        
        if (commandNameMatcher.find())
        {
            String commandNameTemp = commandNameMatcher.group();
            this.commandName = StringHelper.removeChars(commandNameTemp, "@");
        }
        while (argumentMatcher.find())
        {
            //System.out.println("Argument:" + argumentMatcher.group());
            parameters.add(StringHelper.removeChars(argumentMatcher.group(), "[", "]", "(",")",","));
        }
        /*System.out.println("CommandName:" + this.commandName);
        for (String thisParam: parameters)
        {
            System.out.println("Global param:" + thisParam);
        }*/
    }
    @Override
    public String toString()
    {
        String toReturn = commandName;
        if (parameters.size() > 0)
        {
            toReturn += "(";
            for (String param: parameters)
            {
                toReturn += param + ",";
            }
            toReturn = toReturn.substring(0, toReturn.length() - 1);
            toReturn += ")";
        }
        return "Command:" + toReturn;
    }

}

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ModifyCommand extends LineComponent
{
    public String commandName;
    public String toChange;
    public ArrayList<String> argumentsList;
    public ArrayList<String> operatorsList;
    public ModifyCommand(String content)
    {
        super(content);
        Matcher commandNameMatcher = Pattern.compile(RegexHelper.atCommandSimple).matcher(content);
        if (commandNameMatcher.find())
        {
            String commandNameTemp = commandNameMatcher.group();
            this.commandName = StringHelper.removeChars(commandNameTemp, "@");
        }
        else
        {
            throw new IllegalArgumentException("Can't find command name");
        }
        argumentsList = new ArrayList<String>();
        operatorsList = new ArrayList<String>();
        String arguments = "(\\(|\\[)" + RegexHelper.argument + "(\\)|\\])";
        Matcher argumentsMatcher = Pattern.compile(arguments).matcher(content);
        int counter = 0;
        while (argumentsMatcher.find())
        {
            if (counter == 0)
            {
                toChange = StringHelper.removeChars(argumentsMatcher.group().trim(), "(",")","[","]");
            }
            else
            {
                argumentsList.add(StringHelper.removeChars(argumentsMatcher.group().trim(), "(",")","[","]"));
            }
            counter++;
        }
        Matcher operator = Pattern.compile(RegexHelper.operator).matcher(content);
        while (operator.find())
        {
            operatorsList.add(operator.group().trim());
        }
    }
    
    @Override
    public String toString()
    {
        String toReturn = commandName + " ";
        toReturn += toChange + "=";
        for(int i = 0; i < argumentsList.size(); i++)
        {
            toReturn += argumentsList.get(i);
            if (i < operatorsList.size())
            {
                toReturn+=operatorsList.get(i);
            }
        }
        return "ModifyCommand:" + toReturn;
    }

}

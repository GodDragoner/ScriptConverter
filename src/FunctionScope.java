import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FunctionScope extends CodeScope
{
    
    public FunctionScope(String name)
    {
        super();
        output.append("function " + name + "()");
        output.append("\n");
        output.append("{");
        output.append("\n");
        tabbing = "    ";
    }
    @Override
    public void addEnd()
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void addMethodCall(String methodName)
    {
        //System.out.println("method " + methodName);
        output.append(tabbing + methodName + "();");
        output.append("\n");
    }
    @Override
    public void addComplexMessage(String message, String methodCall)
    {
        // TODO Auto-generated method stub
        
    }
    @Override
    public void endScope()
    {
        output.append("}");
        output.append("\n");
        isOpen = false;
    }
    @Override
    public void addAtCommand(String atCommand)
    {
        Pattern myPattern = Pattern.compile("\\s*((@((\\w|\\d)+)))\\s*((\\w|\\.|,|<|>|#|'|\\/|\\s|\\?)+)\\s*");
        Matcher thisMatcher = myPattern.matcher(atCommand);
        ArrayList<String> commands = new ArrayList<String>();
        ArrayList<String> commandArgs = new ArrayList<String>();
        while (thisMatcher.find())
        {
            for (int i = 0; i < thisMatcher.groupCount(); i++)
            {
                if (i == 3)
                {
                    commands.add(thisMatcher.group(i));
                }
                else if (i == 5)
                {
                    commandArgs.add(thisMatcher.group(i));
                }
            }
        }
        for (int i = 0; i < commands.size(); i++)
        {
            if (commands.get(i).equalsIgnoreCase("end"))
            {
                System.out.println("debug a");
                endScope();
                if (i + 1 < commands.size())
                {
                    System.out.println("Error: Can not have commands after @End");
                }
            }
            else if (commands.get(i).equalsIgnoreCase("info"))
            {
                System.out.println("debug b");
                output.append(tabbing + "//Info: " + commandArgs.get(i));
                output.append("\n");
            }
        }
    }

}

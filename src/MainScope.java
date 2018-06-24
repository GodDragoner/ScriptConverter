import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainScope extends CodeScope
{
    public MainScope()
    {
        super();
    }
    
    @Override
    public void addEnd()
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void addMethodCall(String methodName)
    {
        output.append(methodName + "();");
        output.append("\n");
    }

    @Override
    public void addComplexMessage(String message, String methodCall)
    {
        addSimpleMessage(message);
        Matcher thisMatcher = Pattern.compile("@(\\w+\\d*)\\(((\\w|\\s|')+)\\)").matcher(methodCall);
        if (!thisMatcher.matches())
        {
            System.out.println("bad error");
        }
        String groupa = thisMatcher.group(1);
        String groupb = thisMatcher.group(2);
        //TODO add actual method calls
        addUninterpreted(methodCall);
    }

    @Override
    public void endScope()
    {
        // TODO Auto-generated method stub
        isOpen = false;
    }

    @Override
    public void addAtCommand(String atCommand)
    {
        Matcher thisMatcher = Pattern.compile("\\s*(@((\\w|\\s|')+))+((\\w|\\.|,|<|>|#|'|\\/|\\s|\\?)+)\\s*").matcher(atCommand);
        if (!thisMatcher.matches())
        {
            System.out.println("bad error");
        }
        for (int i = 0; i < thisMatcher.groupCount(); i++)
        {
            System.out.println("Group " + i + ":" + thisMatcher.group(i));
        }
        
        
    }

}

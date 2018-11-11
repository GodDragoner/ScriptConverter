import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainScope extends CodeScope
{
    private boolean inIfScope = false;
    private boolean addIfAfterScope = false;
    
    public MainScope()
    {
        super();
        addOutput(tabbing + "var exit = false;", "var exit = false;");
    }
    
    @Override
    public void addEnd()
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void addSimpleMessage(String simpleMessage)
    {
        if (addIfAfterScope && !inIfScope)
        {
            startIf();
        }
        addOutput(tabbing + "CMessage(\"" + replaceVocabs(simpleMessage) + "\");", simpleMessage);
    }
    
    @Override
    public void addComplexMessage(String message, String methodCall, boolean returnOut)
    {
        if (addIfAfterScope && !inIfScope)
        {
            startIf();
        }
        super.addComplexMessage(message, methodCall, returnOut);
    }
    
    @Override
    public void addMethodCall(String methodName, boolean endScope)
    {
        methodName = methodName.trim();
        methodName = methodName.replaceAll(" ", "");
        if (addIfAfterScope && !inIfScope)
        {
            startIf();
        }
        addOutput(tabbing + methodName + "();", methodName);
        addOutput(tabbing + "exit = true;", "exit = true;");
        addIfAfterScope = true;
        
        if (endScope)
            endScope();
    }

    @Override
    public void endScope()
    {
        isOpen = false;
        for (int i = 0; i < startedBraces; i++)
        {
            if (tabbing.length() < 4)
            {
                System.out.println("already under 4");
                break;
            }
            tabbing = tabbing.substring(0, tabbing.length() - 4);
            addOutput(tabbing + "}", "}");
        }
    }
    
    public void startIf()
    {
        addOutput(tabbing + "if (!exit) {", "if (!exit) {");
        tabbing += "    ";
        startedBraces++;
        addIfAfterScope = false;
    }

    @Override
    public void addAtCommand(String atCommand)
    {
        if (addIfAfterScope && !inIfScope)
        {
            startIf();
        }
        super.addAtCommand(atCommand);
    }
    
    @Override
    public void addCallReturn(String methodName)
    {
        if (addIfAfterScope && !inIfScope)
        {
            startIf();
        }
        super.addCallReturn(methodName);
    }
    
    @Override
    public void addCall(String methodName, boolean endScope)
    {
        if (addIfAfterScope && !inIfScope)
        {
            startIf();
        }
        addOutput(tabbing + "run(\"" + methodName + "\");", methodName);
        addOutput(tabbing + "exit = true;", "exit = true;");
        addIfAfterScope = true;
        
        if (endScope)
            endScope();
        
    }

}

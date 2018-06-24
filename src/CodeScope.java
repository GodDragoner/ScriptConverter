import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class CodeScope
{
    protected StringBuffer output;
    protected String tabbing = "";
    protected boolean isOpen = true;
    
    public CodeScope()
    {
        output = new StringBuffer();
    }
    
    public void addSimpleMessage(String simpleMessage)
    {
        output.append(tabbing + "CMessage(\"" + replaceVocabs(simpleMessage) + "\");");
        output.append("\n");
    }
    
    public abstract void addComplexMessage(String message, String methodCall);
    
    public abstract void addEnd();
    
    public abstract void addMethodCall(String methodName);
    
    public abstract void addAtCommand(String atCommand);
    
    public abstract void endScope();

    protected String replaceVocabs(String toChange)
    {
        Pattern pattern = Pattern.compile("(#)([a-zA-Z0-9]+)");
        Matcher matcher = pattern.matcher(toChange);
        while (matcher.find())
        {
            toChange = matcher.replaceFirst("%" + matcher.group(2) + "%");
            matcher = pattern.matcher(toChange);
        }
        toChange = toChange.replaceAll("\\/i", "");
        return toChange;
    }
    
    public void addUninterpreted(String uninterpreted)
    {
        output.append(tabbing + uninterpreted);
        output.append("\n");
    }
    
    public StringBuffer getOutput()
    {
        return output;
    }
    
    public boolean isOpen()
    {
        return isOpen;
    }

}

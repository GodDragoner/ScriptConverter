import java.util.ArrayList;
import java.util.Stack;

public class OutputGenerator
{
    private ArrayList<ParsedLine> parsedInput;
    
    private ArrayList<String> outputJavascript;
    private ParsedLine thisParsedLine;
    
    private Stack<String> scoping;
    
    public OutputGenerator(ArrayList<ParsedLine> input)
    {
        this.parsedInput = input;
        this.outputJavascript = new ArrayList<String>();
        scoping = new Stack<String>();
    }
    
    //loop through all parsed lines in the parsed input and then loop through
    //all line components for each parsed line. Take every line component and
    //send it off to get its output generated and pushed to the arraylist
    public String generateOutput()
    {
        outputJavascript.add("main();");
        pushFunction("main");
        
        for (ParsedLine parsedLine: parsedInput)
        {
            thisParsedLine = parsedLine;
            if (parsedLine.thisRegex.equals(ParsedLine.lineRegex.UNINTERPRETED))
            {
                pushLine("--UNINTERPRETED LINE:" + parsedLine.line);
            }
            else if (parsedLine.thisRegex.equals(ParsedLine.lineRegex.BLANK))
            {
                pushLine("");
            }
            else {
                for (LineComponent lineComponent: parsedLine.lineComponents)
                {
                    pushOutput(lineComponent);
                }
            }
        }
        while (!scoping.isEmpty())
        {
            popScoping();
        }
        System.out.println("Printing output");
        for (String outputString: outputJavascript)
        {
            System.out.println(outputString);
        }
        return null;
    }
    
    //Takes in any line component and pushes the output in javascript to
    //the arraylist
    private void pushOutput(LineComponent component)
    {
        if (component instanceof AtMethod)
        {
            pushFunction(((AtMethod)component).methodName);
        }
        else if (component instanceof AtCommand)
        {
            pushCommand((AtCommand) component);
        }
        else if (component instanceof Message)
        {
            pushLine("Message:" + component.content);
        }
        else if (component instanceof Response)
        {
            pushLine("Response:" + ((Response) component).responses.toString());
        }
        else if (component instanceof ModifyCommand)
        {
            pushLine("Modify:" + ((ModifyCommand) component).toString());
        }
        else if (component instanceof IfStatement)
        {
            pushLine("IfStatement:" + ((IfStatement) component).toString());
        }
    }
    
    //This is where most commands need to be converted
    //This functions takes in the AtCommand object and will push the
    //javascript/TAJ version of the command
    private void pushCommand(AtCommand command)
    {
        //JUST HERE TEMPORARILY FOR TESTING
        pushLine(command.toString());
    }
    
    
    //Start a new function
    private void pushFunction(String functionName)
    {
        while (!scoping.isEmpty())
        {
            popScoping();
        }
        pushLine("function " + functionName + "()");
        pushScoping("function:" + functionName);
    }
    
    //Start a new scope
    private void pushScoping(String scopeName)
    {
        pushLine("{");
        scoping.push(scopeName);
    }
    
    //Close a scope
    private String popScoping()
    {
        String toReturn = "";
        toReturn = scoping.pop();
        pushLine("}");
        return toReturn;
    }
    
    //Push a string onto the output arraylist and padding will be added
    private void pushLine(String lineToPush)
    {
        outputJavascript.add(getPadding() + lineToPush);
    }
    
    //Gets the proper indenting for the line
    private String getPadding()
    {
        String toReturn = "";
        for (int i = 0; i < scoping.size(); i++)
        {
            toReturn += "    ";
        }
        return toReturn;
    }
}

import java.util.ArrayList;
import java.util.Stack;

public class OutputGenerator
{
    private ArrayList<ParsedLine> parsedInput;
    
    private ArrayList<String> outputJavascript;
    private ParsedLine thisParsedLine;
    private int currentLineIndex;
    
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
        pushFunction("main");
        
        for (ParsedLine parsedLine: parsedInput)
        {
            thisParsedLine = parsedLine;
            currentLineIndex = 0;
            if (parsedLine.thisRegex.equals(ParsedLine.lineRegex.UNINTERPRETED))
            {
                //pushLine("--UNINTERPRETED LINE:" + parsedLine.line);
            }
            else if (parsedLine.thisRegex.equals(ParsedLine.lineRegex.BLANK))
            {
                pushLine("");
            }
            else {
                for (LineComponent lineComponent: parsedLine.lineComponents)
                {
                    pushOutput(lineComponent);
                    currentLineIndex++;
                }
            }
        }
        while (!scoping.isEmpty())
        {
            popScoping();
        }
        System.out.println("Printing output:");
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
            //pushCommand((AtCommand) component);
        }
        else if (component instanceof Message)
        {
            //pushLine("Message:" + ((Message) component).content);
            pushMessage((Message) component);
        }
        else if (component instanceof Response)
        {
            //pushLine("Response:" + ((Response) component).responses.toString());
        }
        else if (component instanceof ModifyCommand)
        {
            //pushLine("Modify:" + ((ModifyCommand) component).toString());
        }
        else if (component instanceof IfStatement)
        {
            //pushLine("IfStatement:" + ((IfStatement) component).toString());
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
    
    //This is where messages will get pushed and output generated for messages
    private void pushMessage(Message message)
    {
        //Check the command before this message and if its SystemMessage make this a SMessage instead of a CMessage
        String outputMessage = "";
        if (currentLineIndex >= 1)
        {
            if (thisParsedLine.lineComponents.get(currentLineIndex - 1) instanceof AtCommand && ((AtCommand) thisParsedLine.lineComponents.get(currentLineIndex - 1)).commandName.equalsIgnoreCase("systemmessage"))
            {
                outputMessage += "SMessage(";
            }
            else
            {
                outputMessage += "CMessage(";
            }
        }
        else
        {
            outputMessage += "CMessage(";
        }
        int counter = 0;
        for (LineComponent comp: message.messageComponents)
        {
            //Concatenate the different parts
            if (counter != 0)
            {
                outputMessage += " + ";
            }
            if (comp instanceof Phrase)
            {
                //All of this checking with the counter is just to make sure that there is proper spacing when @RT or other similar commands are used
                outputMessage += "\"" + ((Phrase) comp).message.trim();
                if (message.messageComponents.size() > (counter + 1))
                {
                    outputMessage += " \"";
                }
                else
                {
                    outputMessage += "\"";
                }
            }
            else if (comp instanceof RandomText)
            {
                RandomText randomText = (RandomText) comp;
                outputMessage += "RandomText(";
                boolean moreComponents = message.messageComponents.size() > (counter + 1);
                int randomTextCounter = 0;
                for (Phrase phrase: randomText.phrases)
                {
                    outputMessage += "\"" + phrase.message.trim();
                    if (moreComponents)
                    {
                        outputMessage += " \"";
                    }
                    else
                    {
                        outputMessage += "\"";
                    }
                    if (randomText.phrases.size() > randomTextCounter + 1)
                    {
                        outputMessage += ", ";
                    }
                    randomTextCounter++;
                }
                outputMessage += ")";
            }
            counter++;
        }
        outputMessage += ");";
        pushLine(outputMessage);
    }
    
    
    //Start a new function
    private void pushFunction(String functionName)
    {
        pushLine(functionName + "();");
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

import java.util.ArrayList;
import java.util.Stack;

//This class is the single class that generates all of the output javascript. It uses
//an arraylist of parsedlines which this class uses to be able to easily convert code
//without having to do any parsing or using regexes
public class OutputGenerator
{
    private ArrayList<ParsedLine> parsedInput;
    
    private ArrayList<String> outputJavascript;
    private ParsedLine thisParsedLine;
    private int indexInCurrentLine;
    private int currentLineIndex;
    
    private Stack<String> scoping;
    private int answerCounter = 0;
    
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
        currentLineIndex = 0;
        for (ParsedLine parsedLine: parsedInput)
        {
            if (!parsedLine.thisRegex.equals(ParsedLine.lineRegex.RESPONSE) && scoping.peek().contains("response"))
            {
                popScoping();
            }
            if (parsedLine.lineComponents.size() > 1 && parsedLine.lineComponents.get(0) instanceof AtCommand && ((AtCommand)parsedLine.lineComponents.get(0)).commandName.equalsIgnoreCase("differentanswer"))
            {
                currentLineIndex++;
                continue;
            }
            thisParsedLine = parsedLine;
            indexInCurrentLine = 0;
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
                    indexInCurrentLine++;
                }
            }
            currentLineIndex++;
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
            pushCommand((AtCommand) component);
        }
        else if (component instanceof Message)
        {
            //pushLine("Message:" + ((Message) component).content);
            pushMessage((Message) component);
        }
        else if (component instanceof Response)
        {
            //pushLine("Response:" + ((Response) component).responses.toString());
            pushResponse((Response) component);
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
        String commandName = command.commandName;
        ArrayList<String> parameters = command.parameters;
        if (parameters.size() == 0)
        {
            //Commands without parameters
            //Put any commands with no parameters in this switch and
            //if they have a output version in javascript
            //push the line with the converted javascript version
            switch (commandName.toLowerCase())
            {
                case "rapidcodeon":
                    break;
                case "nullresponse":
                    break;
                    
                default:
                    pushLine(command.toString());
                    break;
            }
        }
        else
        {
            //Put any commands with parameters in this switch and
            //if they have a output version in javascript
            //push the line with the converted javascript version
            //switching on the command name and then using the
            //parameters array list to access the command's parameters
            switch (commandName.toLowerCase())
            {
                
                default:
                    pushLine(command.toString());
                    break;
            }
        }
    }
    
    //Adds a response
    private void pushResponse(Response response)
    {
        String outputResponse = "";
        if (scoping.peek().equalsIgnoreCase("response:answer" + (answerCounter - 1)))
        {
            popScoping();
            outputResponse += "else if (answer" + (answerCounter-1) + ".isLike(";
        }
        else
        {
            outputResponse += "if (answer" + (answerCounter-1) + ".isLike(";
        }
        for (int i = 0; i < response.responses.size(); i++)
        {
           outputResponse += "\"" + response.responses.get(i) + "\"";
           if ((i+1) < response.responses.size())
           {
               outputResponse += ", ";
           }
        }
        outputResponse += "))";
        pushLine(outputResponse);
        pushScoping("response:answer" + (answerCounter - 1));
    }
    
    //This is where messages will get pushed and output generated for messages
    //Also includes the code to do a getresponse
    private void pushMessage(Message message)
    {
        if (message.messageComponents.get(0).content.equals("Are you horny already?"))
        {
            System.out.println("test");
        }
        boolean gettingInput = false;
        //Check the command before this message and if its SystemMessage make this a SMessage instead of a CMessage
        String outputMessage = "";
        if (indexInCurrentLine >= 1)
        {
            if (thisParsedLine.lineComponents.get(indexInCurrentLine - 1) instanceof AtCommand && ((AtCommand) thisParsedLine.lineComponents.get(indexInCurrentLine - 1)).commandName.equalsIgnoreCase("systemmessage"))
            {
                
                if (scoping.peek().equalsIgnoreCase("differentanswer"))
                {
                    outputMessage += "answer" + (answerCounter - 1) + " = getInput(";
                }
                //Here, we check if the next line is a response. If so this needs to be getting input instead of sending a message
                else if (parsedInput.size() > currentLineIndex + 1)
                {
                    ParsedLine tempLine = parsedInput.get(currentLineIndex + 1);
                    if (tempLine.lineComponents.size() >= 2)
                    {
                        if (tempLine.thisRegex.equals(ParsedLine.lineRegex.RESPONSE) && !thisParsedLine.thisRegex.equals(ParsedLine.lineRegex.RESPONSE))
                        {
                            outputMessage += "let answer" + answerCounter + " = getInput(";
                            gettingInput = true;
                            answerCounter++;
                        }
                        else
                        {
                            outputMessage += "SMessage(";
                        }
                    }
                    else
                    {
                        outputMessage += "SMessage(";
                    }
                }
                else
                {
                    outputMessage += "SMessage(";
                }
            }
            else
            {
                if (scoping.peek().equalsIgnoreCase("differentanswer"))
                {
                    outputMessage += "answer" + (answerCounter - 1) + " = getInput(";
                }
                else
                {
                    outputMessage += "CMessage(";
                }
            }
        }
        else
        {
            //Here, we check if the next line is a response. If so this needs to be getting input instead of sending a message
            if (parsedInput.size() > currentLineIndex + 1)
            {
                ParsedLine tempLine = parsedInput.get(currentLineIndex + 1);
                if (tempLine.lineComponents.size() >= 1)
                {
                    if (tempLine.thisRegex.equals(ParsedLine.lineRegex.RESPONSE))
                    {
                        outputMessage += "let answer" + answerCounter + " = getInput(";
                        gettingInput = true;
                        answerCounter++;
                    }
                    else
                    {
                        outputMessage += "CMessage(";
                    }
                    //if (tempLine.lineComponents.get(0) instanceof AtCommand && ((AtCommand)tempLine.lineComponents.get(0)).commandName.equalsIgnoreCase("differentanswer"))
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
        if (gettingInput)
        {
            int tempIndex = currentLineIndex + 1;
            ArrayList<String> allResponses = new ArrayList<String>();
            while (parsedInput.get(tempIndex).thisRegex.equals(ParsedLine.lineRegex.RESPONSE))
            {
                allResponses.addAll(((Response)parsedInput.get(tempIndex).lineComponents.get(0)).responses);
                tempIndex++;
                if (tempIndex >= parsedInput.size())
                {
                    System.out.println("returning because hit end of file");
                    //This is the case where we hit the end of the file and still haven't found a line that isnt a response
                    return;
                }
            }
            if (parsedInput.get(tempIndex).lineComponents.size() > 1 && parsedInput.get(tempIndex).lineComponents.get(0) instanceof AtCommand)
            {
                if (((AtCommand)parsedInput.get(tempIndex).lineComponents.get(0)).commandName.equalsIgnoreCase("differentanswer"))
                {
                    ParsedLine differentAnswerLine = parsedInput.get(tempIndex);
                    String differentAnswerOutput = "while (!(";
                    for (int i = 0; i < allResponses.size(); i++)
                    {
                        differentAnswerOutput += "answer" + (answerCounter-1) + ".isLike(\"" + allResponses.get(i) + "\")";
                        if (i + 1 < allResponses.size())
                        {
                            differentAnswerOutput += " || ";
                        }
                    }
                    differentAnswerOutput += "))";
                    pushLine(differentAnswerOutput);
                    pushScoping("differentAnswer");
                    
                    indexInCurrentLine = 1;
                    thisParsedLine = differentAnswerLine;
                    for (int i = 1; i < differentAnswerLine.lineComponents.size(); i++)
                    {
                        pushOutput(differentAnswerLine.lineComponents.get(i));
                        indexInCurrentLine++;
                    }
                    popScoping();
                    
                }
            }
        }
    }
    
    
    //Starts a new function with the given name
    private void pushFunction(String functionName)
    {
        pushLine(functionName + "();");
        while (!scoping.isEmpty())
        {
            popScoping();
        }
        pushLine("function " + functionName + "()");
        pushScoping("function:" + functionName);
        answerCounter = 0;
    }
    
    //Start a new scope. Use this every time an if or something similar is started
    private void pushScoping(String scopeName)
    {
        pushLine("{");
        scoping.push(scopeName);
    }
    
    //Close a scope
    //Use to close a bracing and end a scoping block
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

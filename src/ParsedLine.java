import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.scene.shape.Line;

public class ParsedLine
{
    public enum lineRegex{
        METHOD,
        SENDMESSAGE,
        COMMANDS,
        MESSAGEAFTERCOMMAND,
        RESPONSE,
        BLANK,
        UNINTERPRETED
    }
    
    public lineRegex thisRegex;
    public String line;
    public ArrayList<LineComponent> lineComponents;
    
    public ParsedLine(String line, lineRegex lineRegex)
    {
        //System.out.println("Line:" + line);
        this.line = line;
        this.thisRegex = lineRegex;
        lineComponents = new ArrayList<LineComponent>();
        if (thisRegex == ParsedLine.lineRegex.SENDMESSAGE)
        {
            addSendMessage(line);
        }
        else if (thisRegex == ParsedLine.lineRegex.COMMANDS)
        {
            Matcher commandsMatcher = Pattern.compile(RegexHelper.commandsLine).matcher(line);
            if (!commandsMatcher.matches())
            {
                throw new IllegalArgumentException("String does not match commandsLine regex");
            }
            else
            {
                addCommandsLine(line);
            }
        }
        else if (thisRegex == ParsedLine.lineRegex.MESSAGEAFTERCOMMAND)
        {
            addMessageAfterCommands(line);
        }
        else if (thisRegex == ParsedLine.lineRegex.METHOD)
        {
            Matcher methodMatcher = Pattern.compile(RegexHelper.methodStart).matcher(line);
            if (!methodMatcher.matches())
            {
                throw new IllegalArgumentException("String does not match methodStart regex");
            }
            else
            {
                lineComponents.add(new AtMethod(methodMatcher.group(2)));
                addCommandsLine(methodMatcher.group());
            }
        }
        else if (thisRegex == ParsedLine.lineRegex.RESPONSE)
        {
            Matcher responseMatcher = Pattern.compile(RegexHelper.response).matcher(line);
            if (!responseMatcher.matches())
            {
                throw new IllegalArgumentException("String does not match response regex");
            }
            else
            {
                lineComponents.add(new Response(responseMatcher.group(1)));
                String restOfString = line.substring(responseMatcher.group(1).length()).trim();
                //STILL NEEDS TESTING
                if (!restOfString.equals(""))
                {
                    if (restOfString.matches(RegexHelper.commandsLine))
                    {
                        addCommandsLine(restOfString);
                    }
                    else if (restOfString.matches(RegexHelper.messageAfterCommand))
                    {
                        addMessageAfterCommands(restOfString);
                    }
                    else if (restOfString.matches(RegexHelper.sendMessage))
                    {
                        addSendMessage(restOfString);
                    }
                }
            }
        }
        /*for (LineComponent thisComponent: lineComponents)
        {
            System.out.println(thisComponent.toString());
        }*/
    }
    
    public void addMessageAfterCommands(String messageAfterCommands)
    {
        Matcher messageAfterCommandMatcher = Pattern.compile(RegexHelper.messageAfterCommand).matcher(messageAfterCommands);
        if (!messageAfterCommandMatcher.matches())
        {
            throw new IllegalArgumentException("String does not match messageAfterCommand regex");
        }
        else
        {
            int endIndex = addCommandsLine(messageAfterCommandMatcher.group(2));

            String restOfString = messageAfterCommands.substring(endIndex);
            addSendMessage(restOfString.trim());
        }
    }
    
    //return index after last found
    public int addCommandsLine(String commandsLineString)
    {
        Matcher anyCommand = Pattern.compile(RegexHelper.anyAtCommand).matcher(commandsLineString);   
        int endIndex = 0;
        while (anyCommand.find())
        {
            if (Pattern.compile(RegexHelper.ifFunction).matcher(anyCommand.group().trim()).matches())
            {
                lineComponents.add(new IfStatement(anyCommand.group().trim()));
            }
            else if (Pattern.compile(RegexHelper.atCommandModify).matcher(anyCommand.group()).matches())
            {
                lineComponents.add(new ModifyCommand(anyCommand.group().trim()));
            }
            else 
            {
                lineComponents.add(new AtCommand(anyCommand.group().trim()));
            }
            endIndex = anyCommand.end();
        }
        return endIndex;
    }
    public void addSendMessage(String sendMessage)
    {
        Matcher sendMessageMatcher = Pattern.compile(RegexHelper.sendMessage).matcher(sendMessage);
        if (!sendMessageMatcher.matches())
        {
            throw new IllegalArgumentException("String does not match send message regex");
        }
        else
        {
            String firstMessage = sendMessageMatcher.group(2); //Get the first phrase
            lineComponents.add(new Message(firstMessage.trim()));
            String restOfLine = sendMessage.substring(firstMessage.length()); //Rest of line will be MessageAfterCommand
            //System.out.println("Rest of String:" + restOfLine);

            int endIndex = addCommandsLine(restOfLine);
            //System.out.println("After Commands:" + restOfLine.substring(endIndex));
            String phraseAfterCommands = restOfLine.substring(endIndex);
            if (phraseAfterCommands != null && !phraseAfterCommands.trim().equals(""))
            {
                lineComponents.add(new Message(phraseAfterCommands.trim()));
            }
        }
    }
}

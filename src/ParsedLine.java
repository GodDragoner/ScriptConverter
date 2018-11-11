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
        System.out.println("Line:" + line);
        this.line = line;
        this.thisRegex = lineRegex;
        lineComponents = new ArrayList<LineComponent>();
        if (thisRegex == ParsedLine.lineRegex.SENDMESSAGE)
        {
            Matcher sendMessageMatcher = Pattern.compile(RegexHelper.sendMessage).matcher(line);
            if (!sendMessageMatcher.matches())
            {
                throw new IllegalArgumentException("String does not match send message regex");
            }
            else
            {
                String firstMessage = sendMessageMatcher.group(2); //Get the first phrase
                lineComponents.add(new Message(firstMessage.trim()));
                String restOfLine = line.substring(firstMessage.length()); //Rest of line will be MessageAfterCommand
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
        else if (thisRegex == ParsedLine.lineRegex.COMMANDS)
        {
            Matcher methodMatcher = Pattern.compile(RegexHelper.commandsLine).matcher(line);
            if (!methodMatcher.matches())
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
            Matcher messageAfterCommandMatcher = Pattern.compile(RegexHelper.messageAfterCommand).matcher(line);
            if (!messageAfterCommandMatcher.matches())
            {
                throw new IllegalArgumentException("String does not match commandsLine regex");
            }
            else
            {
                System.out.println("Group0:" + messageAfterCommandMatcher.group());
                System.out.println("Group2:" + messageAfterCommandMatcher.group(2));
                int endIndex = addCommandsLine(messageAfterCommandMatcher.group(2));
                String phraseAfterCommands = line.substring(endIndex);
                if (phraseAfterCommands != null && !phraseAfterCommands.trim().equals(""))
                {
                    lineComponents.add(new Message(phraseAfterCommands.trim()));
                }
                else
                {
                    throw new IllegalArgumentException("MessageAfterCommand did not have a phrase after the commands");
                }
                String restOfString = line.substring(endIndex);
            }
        }
        for (LineComponent thisComponent: lineComponents)
        {
            System.out.println(thisComponent.toString());
        }
    }
    
    //return index after last found
    public int addCommandsLine(String commandsLineString)
    {
        Matcher anyCommand = Pattern.compile(RegexHelper.anyAtCommand).matcher(commandsLineString);   
        int endIndex = 0;
        while (anyCommand.find())
        {
            String s = anyCommand.group();
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
                lineComponents.add(new AtMethod(anyCommand.group().trim()));
            }
            endIndex = anyCommand.end();
        }
        return endIndex;
    }
}

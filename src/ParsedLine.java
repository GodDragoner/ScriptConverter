import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


//Parsed lines are the keystone of the interpeter step. A parsed line consists of an arraylist
//of linecomponents. It also has an enum that represents which regex this parsed line matched
//from the parsing step.
public class ParsedLine {
    public enum lineRegex {
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

    public ParsedLine(String line, lineRegex lineRegex) {
        //System.out.println("Line:" + line);
        this.line = line;
        this.thisRegex = lineRegex;
        lineComponents = new ArrayList<>();
        if (thisRegex == ParsedLine.lineRegex.SENDMESSAGE) {
            addSendMessage(line);
        } else if (thisRegex == ParsedLine.lineRegex.COMMANDS) {
            Matcher commandsMatcher = RegexHelper.COMMAND_LINE_PATTERN.matcher(line);
            if (!commandsMatcher.matches()) {
                throw new IllegalArgumentException("String does not match commandsLine regex");
            } else {
                addCommandsLine(line);
            }
        } else if (thisRegex == ParsedLine.lineRegex.MESSAGEAFTERCOMMAND) {
            addMessageAfterCommands(line);
        } else if (thisRegex == ParsedLine.lineRegex.METHOD) {
            Matcher methodMatcher = RegexHelper.METHOD_START_PATTERN.matcher(line);
            if (!methodMatcher.matches()) {
                throw new IllegalArgumentException("String does not match methodStart regex");
            } else {
                lineComponents.add(new AtMethod(methodMatcher.group(2)));
                addCommandsLine(methodMatcher.group());
            }
        } else if (thisRegex == ParsedLine.lineRegex.RESPONSE) {
            Matcher responseMatcher = RegexHelper.RESPONSE_PATTERN.matcher(line);

            if (!responseMatcher.matches()) {
                throw new IllegalArgumentException("String does not match response regex");
            } else {
                lineComponents.add(new Response(responseMatcher.group(1)));
                String restOfString = line.substring(responseMatcher.group(1).length()).trim();

                //STILL NEEDS TESTING
                if (!restOfString.equals("")) {
                    if (RegexHelper.COMMAND_LINE_PATTERN.matcher(restOfString).matches()) {
                        addCommandsLine(restOfString);
                    } else if (RegexHelper.MESSAGE_AFTER_COMMAND_PATTERN.matcher(restOfString).matches()) {
                        addMessageAfterCommands(restOfString);
                    } else if (RegexHelper.SEND_MESSAGE_PATTERN.matcher(restOfString).matches()) {
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

    //Adds linecomponents if the line is in the format of RegexHelper.messageAfterCommand
    public void addMessageAfterCommands(String messageAfterCommands) {
        Matcher messageAfterCommandMatcher = RegexHelper.MESSAGE_AFTER_COMMAND_PATTERN.matcher(messageAfterCommands);
        if (!messageAfterCommandMatcher.matches()) {
            throw new IllegalArgumentException("String does not match messageAfterCommand regex");
        } else {
            int endIndex = addCommandsLine(messageAfterCommandMatcher.group(2));

            String restOfString = messageAfterCommands.substring(endIndex);
            addSendMessage(restOfString.trim());
        }
    }

    //Adds linecomponents if the line is in the format of RegexHelper.commandsLine
    //Returns the index at the end of the last match
    public int addCommandsLine(String commandsLineString) {
        Matcher anyCommand = Pattern.compile(RegexHelper.anyAtCommand).matcher(commandsLineString);
        int endIndex = 0;
        while (anyCommand.find()) {
            if (Pattern.compile(RegexHelper.ifFunction).matcher(anyCommand.group().trim()).matches()) {
                lineComponents.add(new IfStatement(anyCommand.group().trim()));
            } else if (Pattern.compile(RegexHelper.atCommandModify).matcher(anyCommand.group()).matches()) {
                lineComponents.add(new ModifyCommand(anyCommand.group().trim()));
            } else {
                lineComponents.add(new AtCommand(anyCommand.group().trim()));
            }
            endIndex = anyCommand.end();
        }
        return endIndex;
    }

    //Adds line components if the line is in the format of RegexHelper.sendMessage
    public void addSendMessage(String sendMessage) {
        Matcher sendMessageMatcher = Pattern.compile(RegexHelper.sendMessage).matcher(sendMessage);
        if (!sendMessageMatcher.matches()) {
            throw new IllegalArgumentException("String does not match send message regex");
        } else {
            String firstMessage = sendMessageMatcher.group(2); //Get the first phrase
            lineComponents.add(new Message(firstMessage.trim()));
            String restOfLine = sendMessage.substring(firstMessage.length()); //Rest of line will be MessageAfterCommand
            //System.out.println("Rest of String:" + restOfLine);

            int endIndex = addCommandsLine(restOfLine);
            //System.out.println("After Commands:" + restOfLine.substring(endIndex));
            String phraseAfterCommands = restOfLine.substring(endIndex);
            if (phraseAfterCommands != null && !phraseAfterCommands.trim().equals("")) {
                lineComponents.add(new Message(phraseAfterCommands.trim()));
            }
        }
    }
}

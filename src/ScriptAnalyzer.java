import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScriptAnalyzer
{
    private Pattern sendMessageFormat;
    
    private Pattern funcStartFormat;
    
    private Pattern atIncludedFormat;
    
    private Pattern atCommandFormat;
    
    private Pattern responseFormat;
    
    private StringBuffer outPutString;
    
    private CodeScope currentScope;
    
    public ScriptAnalyzer()
    {
        sendMessageFormat = Pattern.compile("(\\s*(" + RegexHelper.messageChar + ")+\\s*)");
        funcStartFormat = Pattern.compile("\\s*(\\((\\w|\\s|'|_)+\\))\\s*");
        atCommandFormat = Pattern.compile(RegexHelper.allAtCommands);
        atIncludedFormat = Pattern.compile("\\s*((" + RegexHelper.messageChar + ")+)(" + atCommandFormat + ")");
        responseFormat = Pattern.compile("\\s*(\\[((" + RegexHelper.responseChar + "+)|\\w+\\d*\\s*\\w+\\d*|,\\s*)+])\\s*((" + RegexHelper.messageChar + ")+)\\s*((" + RegexHelper.atGotoFunction + ")|"
                + "(" + RegexHelper.atFunction +"))*");
        outPutString = new StringBuffer();
    }
    
    public StringBuffer analyze(File input, File output) throws FileNotFoundException
    {
        ArrayList<ParsedLine> parsedInput = new ArrayList<ParsedLine>();
        if (!input.exists())
        {
            throw new FileNotFoundException();
        }
        
        try
        {
            FileReader fileReader = new FileReader(input);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            
            currentScope = new MainScope();
            
            while ((line = bufferedReader.readLine()) != null) 
            {
                Matcher sendMessageMatcher = Pattern.compile(RegexHelper.sendMessage).matcher(line);
                Matcher commandsMatcher = Pattern.compile(RegexHelper.commandsLine).matcher(line);
                Matcher methodMatcher = Pattern.compile(RegexHelper.methodStart).matcher(line);
                Matcher messageAfterCommandMatcher = Pattern.compile(RegexHelper.messageAfterCommand).matcher(line);
                Matcher responseMatcher = Pattern.compile(RegexHelper.response).matcher(line);
                if (methodMatcher.matches())
                {  
                    parsedInput.add(new ParsedLine(line, ParsedLine.lineRegex.METHOD));
                    //System.out.println("Method Start in analyzer: " + line);
                }
                else if (sendMessageMatcher.matches())
                {
                    parsedInput.add(new ParsedLine(line, ParsedLine.lineRegex.SENDMESSAGE));
                    //System.out.println("Send Message in analyzer: " + line);
                }
                else if (!line.contains("@RT") && commandsMatcher.matches())
                {
                    parsedInput.add(new ParsedLine(line, ParsedLine.lineRegex.COMMANDS));
                    //System.out.println("Commands line line in analyzer: " + line);
                }
                else if (messageAfterCommandMatcher.matches())
                {
                    parsedInput.add(new ParsedLine(line, ParsedLine.lineRegex.MESSAGEAFTERCOMMAND));
                    //System.out.println("messageAfterCommandMatcher in analyzer: " + line);
                }
                else if (responseMatcher.matches())
                {
                    parsedInput.add(new ParsedLine(line, ParsedLine.lineRegex.RESPONSE));
                    //System.out.println("Response line in analyzer: " + line);
                }
                else 
                {
                    if (!line.trim().equals(""))
                    {
                        parsedInput.add(new ParsedLine(line, ParsedLine.lineRegex.UNINTERPRETED));
                        System.out.println("Uninterpreted line in analyzer: " + line);
                    }
                    else
                    {
                        parsedInput.add(new ParsedLine(line, ParsedLine.lineRegex.BLANK));
                    }
                }
                
            }
            fileReader.close();
            OutputGenerator generator = new OutputGenerator(parsedInput);
            generator.generateOutput();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return outPutString;
    }
}

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
        if (!input.exists())
        {
            throw new FileNotFoundException();
        }
        //System.out.println("file exists");
        
        try
        {
            FileReader fileReader = new FileReader(input);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            /*Matcher sendMessageMatcher;
            Matcher atMessageMatcher;
            Matcher atCommandMatcher;
            Matcher funcStartMatcher;
            Matcher responseMatcher;*/
            
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
                    ParsedLine parsedLine = new ParsedLine(line, ParsedLine.lineRegex.METHOD);
                    //System.out.println("Method Start in analyzer: " + line);
                }
                else if (sendMessageMatcher.matches())
                {
                    ParsedLine parsedLine = new ParsedLine(line, ParsedLine.lineRegex.SENDMESSAGE);
                    //System.out.println("Send Message in analyzer: " + line);
                }
                else if (!line.contains("@RT") && commandsMatcher.matches())
                {
                    ParsedLine parsedLine = new ParsedLine(line, ParsedLine.lineRegex.COMMANDS);
                    //System.out.println("Commands line line in analyzer: " + line);
                }
                else if (messageAfterCommandMatcher.matches())
                {
                    ParsedLine parsedLine = new ParsedLine(line, ParsedLine.lineRegex.MESSAGEAFTERCOMMAND);
                    //System.out.println("messageAfterCommandMatcher in analyzer: " + line);
                }
                else if (responseMatcher.matches())
                {
                    ParsedLine parsedLine = new ParsedLine(line, ParsedLine.lineRegex.RESPONSE);
                    //System.out.println("Response line in analyzer: " + line);
                }
                else 
                {
                    if (!line.trim().equals(""))
                    {
                        System.out.println("Uninterpreted line in analyzer: " + line);
                    }
                }
                //before changes
                /*sendMessageMatcher = sendMessageFormat.matcher(line);
                funcStartMatcher = funcStartFormat.matcher(line);
                atMessageMatcher = atIncludedFormat.matcher(line);
                atCommandMatcher = atCommandFormat.matcher(line);
                responseMatcher = responseFormat.matcher(line);
                if (line.equals("Are you completely naked?"))
                {
                    System.out.println("debug");
                }
                if (sendMessageMatcher.matches())
                {
                    if (!currentScope.isOpen)
                    {
                        outPutString.append(currentScope.getOutput());
                        currentScope = new MainScope();
                    }
                    currentScope.addSimpleMessage(line);
                }
                else if (funcStartMatcher.matches())
                {
                    String methodName = funcStartMatcher.group(1);
                    methodName = methodName.replaceAll("\\(", "");
                    methodName = methodName.replaceAll("\\)", "");
                    methodName = methodName.replaceAll(" ", "");
                    methodName = methodName.replaceAll("'", "");
                    if (currentScope != null && currentScope.isOpen())
                    {
                        currentScope.addMethodCall(methodName, true);
                    }
                    outPutString.append(currentScope.getOutput());
                    currentScope = new FunctionScope(methodName);
                }
                else if (atMessageMatcher.matches())
                {
                    if (!currentScope.isOpen)
                    {
                        outPutString.append(currentScope.getOutput());
                        currentScope = new MainScope();
                    }
                    String group1 = atMessageMatcher.group(1);
                    String group5 = atMessageMatcher.group(5);
                    if (group1.charAt(group1.length() - 1) == ' ')
                    {
                        group1 = group1.substring(0, group1.length() - 1);
                    }
                    currentScope.addComplexMessage(group1, group5, true);
                }
                else if (responseMatcher.matches())
                {
                    currentScope.addResponse(line);
                }
                else if (atCommandMatcher.matches())
                {
                    if (!currentScope.isOpen)
                    {
                        outPutString.append(currentScope.getOutput());
                        currentScope = new MainScope();
                    }
                    currentScope.addAtCommand(line);
                }
                else
                {
                    System.out.println("uninterpreted analyzer:" + line);
                    currentScope.addUninterpreted(line);
                }
                System.out.println("line = " + line);*/
                
            }
            fileReader.close();
            if (currentScope != null && currentScope.isOpen)
            {
                currentScope.endScope();
            }
            outPutString.append(currentScope.getOutput());
            System.out.println("Contents of file:");
            System.out.println(outPutString.toString());
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return outPutString;
    }
}

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class CodeScope
{
    protected StringBuffer output;
    protected String tabbing = "";
    protected String tempOutput;
    protected String tempRawOutput;
    protected int startedBraces = 0;
    
    protected boolean isOpen = true;
    
    public CodeScope()
    {
        output = new StringBuffer();
    }
    
    public void addSimpleMessage(String simpleMessage)
    {
        addOutput(tabbing + "CMessage(\"" + replaceVocabs(simpleMessage).trim() + "\");", simpleMessage);
    }

    public void addComplexMessage(String message, String methodCall, boolean returnOut)
    {
        addSimpleMessage(message);
        Matcher thisMatcher = Pattern.compile("((" + RegexHelper.atGotoFunction + ")|(" + RegexHelper.atFunction + "))").matcher(methodCall);
        /*if (!thisMatcher.matches())
        {
            System.out.println("bad error");
        }*/
        /*for (int i = 0; i < thisMatcher.groupCount(); i++)
        {
            System.out.println("Group " + i + ":" + thisMatcher.group(i) + ";");
        }*/
        ArrayList<String> methods = new ArrayList<String>();
        while (thisMatcher.find())
        {
            addAtCommand(thisMatcher.group(0).trim());
            //methods.add(thisMatcher.group(0).trim());
        }
    }
    
    public void addResponse(String response)
    {
        Matcher thisMatcher = Pattern.compile("\\s*(\\[(\\s*((" + RegexHelper.responseChar + "+\\s*)+),*)+\\])\\s*((" + RegexHelper.messageChar + ")+)\\s*((("+ RegexHelper.atGotoFunction +")|"
                + RegexHelper.atFunction + ")*)").matcher(response);
        if (!thisMatcher.matches())
        {
            System.out.println("Regex doesnt match when it should response");
        }
        for (int i = 0; i < thisMatcher.groupCount(); i++)
        {
            //System.out.println("Response " + i + ":" + thisMatcher.group(i));
        }
        String responses = thisMatcher.group(1);
        String message = thisMatcher.group(5);
        String commands = thisMatcher.group(7);
        ArrayList<String> responseList = new ArrayList<String>();
        if (!tempRawOutput.contains("[") | tempRawOutput.contains("}") | tempOutput.contains("var answer"))
        {
            String tempraw = tempRawOutput;
            tempOutput = null;
            tempRawOutput = null;
            addOutput(tabbing + "var answer = getInput(\"" + replaceVocabs(tempraw) + "\");", tempraw);
            Matcher matchParam = Pattern.compile("((\\s*" + RegexHelper.responseChar + "+\\s*)+)[,|\\]]").matcher(responses);
            String output = "if (";
            while (matchParam.find())
            {
                responseList.add(matchParam.group(1).trim());
            }
            for (String thisparam: responseList)
            {
                if (output.equalsIgnoreCase("if ("))
                {
                    output += "answer.isLike(\"" + thisparam + "\")";
                }
                else
                {
                    output += " || answer.isLike(\"" + thisparam + "\")";
                }
            }
            output += ")";
            addOutput(tabbing + output, response);
            addOutput(tabbing + "{", response);
            String beforeTabbing = tabbing;
            tabbing += "    ";
            if (commands.length() > 1)
            {
                addComplexMessage(message, commands, false);
            }
            else
            {
                addSimpleMessage(message);
            }
            tabbing = beforeTabbing;
            addOutput(tabbing + "}", response);
        }
        else
        {
            Matcher matchParam = Pattern.compile("((\\s*" + RegexHelper.responseChar + "+\\s*)+)[,|\\]]").matcher(responses);
            String output = "else if (";
            while (matchParam.find())
            {
                responseList.add(matchParam.group(1).trim());
            }
            for (String thisparam: responseList)
            {
                if (output.equalsIgnoreCase("else if ("))
                {
                    output += "answer.isLike(\"" + thisparam + "\")";
                }
                else
                {
                    output += " || answer.isLike(\"" + thisparam + "\")";
                }
            }
            output += ")";
            addOutput(tabbing + output, response);
            addOutput(tabbing + "{", response);
            String beforeTabbing = tabbing;
            tabbing += "    ";
            if (commands.length() > 1)
            {
                addComplexMessage(message, commands, false);
            }
            else
            {
                addSimpleMessage(message);
            }
            tabbing = beforeTabbing;
            addOutput(tabbing + "}", response);
        }
        
    }
    
    public abstract void addEnd();
    
    public abstract void addMethodCall(String methodName, boolean endScope);
    
    public void addAtCommand(String atCommand)
    {
        /*Pattern myPattern = Pattern.compile("\\s*((@((\\w|\\d)+)))\\s*((\\w|\\.|,|<|>|#|'|\\/|\\s|\\?)+)\\s*");
        Matcher thisMatcher = myPattern.matcher(atCommand);
        ArrayList<String> commands = new ArrayList<String>();
        ArrayList<String> commandArgs = new ArrayList<String>();
        while (thisMatcher.find())
        {
            for (int i = 0; i < thisMatcher.groupCount(); i++)
            {
                if (i == 3)
                {
                    commands.add(thisMatcher.group(i));
                }
                else if (i == 5)
                {
                    commandArgs.add(thisMatcher.group(i));
                }
            }
        }
        for (int i = 0; i < commands.size(); i++)
        {
            if (commands.get(i).equalsIgnoreCase("end"))
            {
                //System.out.println("debug a");
                endScope();
                if (i + 1 < commands.size())
                {
                    System.out.println("Error: Can not have commands after @End");
                }
            }
            else if (commands.get(i).equalsIgnoreCase("info"))
            {
                //System.out.println("debug b");
                output.append(tabbing + "//Info: " + commandArgs.get(i));
                output.append("\n");
            }
        }*/
        Matcher thisMatcher = Pattern.compile("\\s*(" + RegexHelper.atGotoFunction + "|" + RegexHelper.atFunctionWithParams + "|" + RegexHelper.atFunction +")\\s*").matcher(atCommand);

        while (thisMatcher.find())
        {
            String thisMatch = thisMatcher.group(0);
            Matcher atMatcher = Pattern.compile("\\s*(" + RegexHelper.atFunction + ")\\s*").matcher(thisMatch.trim());
            Matcher atGotoMatcher = Pattern.compile("\\s*(" + RegexHelper.atGotoFunction + ")\\s*").matcher(thisMatch.trim());
            Matcher atParamsMatcher = Pattern.compile("\\s*(" + RegexHelper.atFunctionWithParams + ")\\s*").matcher(thisMatch.trim());
            if (atGotoMatcher.matches())
            {
                String command = atGotoMatcher.group(2);
                String methodName = atGotoMatcher.group(3);
                methodName = methodName.replaceAll("\\)", "");
                methodName = methodName.replaceAll("\\(", "");
                methodName = methodName.trim();
                System.out.println(methodName);
                if (command.equalsIgnoreCase("goto"))
                {
                    addMethodCall(methodName, false);
                }
                else if (command.toLowerCase().contains("chance"))
                {
                    //System.out.println("in chance");
                    Matcher chanceMatcher = Pattern.compile("\\w+(\\d\\d)").matcher(command);
                    if (!chanceMatcher.matches())
                    {
                        System.out.println("regex doesnt match when it should");
                    }
                    int chance = Integer.parseInt(chanceMatcher.group(1));
                    String originalTabbing = tabbing;
                    addOutput(tabbing + "if ((Math.floor(Math.random() * (100 - 1 + 1)) + 1) <= " + chance + "){", methodName);
                    tabbing += "    ";
                    addMethodCall(methodName, false);
                    tabbing = originalTabbing;
                    addOutput(tabbing + "}", "}");
                    addOutput(tabbing + "else {", "else {");
                    tabbing += "    ";
                    startedBraces++;
                }
                else if (command.equalsIgnoreCase("checkflag"))
                {
                    String originalTabbing = tabbing;
                    addOutput(tabbing + "if (getVar(" + methodName + ", false)){", methodName);
                    tabbing += "    ";
                    addMethodCall(methodName, false);
                    tabbing = originalTabbing;
                    addOutput(tabbing + "}", "}");
                    addOutput(tabbing + "else {", "else {");
                    tabbing += "    ";
                    startedBraces++;
                }
                else 
                {
                    addUninterpreted(thisMatch);
                }
            }
            else if (atMatcher.matches())
            {
                String command = atMatcher.group(2);
                command = command.trim();
                if (command.equalsIgnoreCase("end"))
                {
                    System.out.println("ending scope");
                    endScope();
                }
                else if (command.equalsIgnoreCase("lockimages"))
                {
                    addOutput(tabbing + "lockImages();", "lockImages();");
                }
                else if (command.equalsIgnoreCase("unlockimages"))
                {
                    addOutput(tabbing + "unlockImages();", "unlockImages();");
                }
                else if (command.equalsIgnoreCase("edge"))
                {
                    addOutput(tabbing + "startEdging();", "startEdging();");
                }
                else if (command.equalsIgnoreCase("nullresponse") | command.equalsIgnoreCase("rapidcodeon"))
                {
                    //addOutput(tabbing + "startEdging();", "startEdging();");
                }
                else
                {
                    addUninterpreted(thisMatch);
                }
                //System.out.println(atParamsMatcher.group(2));
            }
            else if (atParamsMatcher.matches())
            {
                String command = atParamsMatcher.group(2);
                command = command.trim();
                String commandArgs = atParamsMatcher.group(3);
                commandArgs = commandArgs.trim();
                if (command.equalsIgnoreCase("info"))
                {
                    addOutput(tabbing + "//Info: " + commandArgs, "//Info: " + commandArgs);
                    
                }
                else
                {
                    addUninterpreted(command + ":" + commandArgs);
                }
                //System.out.println(atMatcher.group(2));
            }
            else 
            {
                addUninterpreted(atCommand);
                System.out.println("Bad error atCommand");
            }
            
        }
    }
    
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
        addOutput(tabbing + uninterpreted, uninterpreted);
    }
    
    public StringBuffer getOutput()
    {
        addOutput(null, null);
        return output;
    }
    
    public boolean isOpen()
    {
        return isOpen;
    }
    protected void addOutput(String toAdd, String raw)
    {
        if (tempOutput != null)
        {
            output.append(tempOutput);
            output.append("\n");
        }
        tempOutput = toAdd;
        tempRawOutput = raw;
    }

}

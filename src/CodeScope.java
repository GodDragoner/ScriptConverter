import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//THIS CLASS ISN'T BEING USED ANYMORE. IT WAS IN THE OLD CODE
//HOWEVER, IT MIGHT BE USEFUL FOR REFERENCE ON HOW TO CONVERT
//SOME OF THE COMMANDS
public abstract class CodeScope
{
    protected StringBuffer output;
    protected String tabbing = "";
    protected String tempOutput;
    protected String tempRawOutput;
    protected int startedBraces = 0;
    protected int randomcounter = 0;
    
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
        addAtCommand(methodCall);
    }
    
    public void addResponse(String response)
    {
        Matcher thisMatcher = Pattern.compile("\\s*(\\[(\\s*((" + RegexHelper.responseChar + "+\\s*)+),*)+\\])\\s*((" + RegexHelper.messageChar + ")+)\\s*(("+ RegexHelper.allAtCommands + ")*)").matcher(response);
        if (!thisMatcher.matches())
        {
            System.out.println("Regex doesnt match when it should response");
        }
        String responses = thisMatcher.group(1);
        String message = thisMatcher.group(5);
        String commands = thisMatcher.group(9);
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
            tabbing += "    ";
            startedBraces++;
            if (commands != null && commands.length() > 1)
            {
                addComplexMessage(message, commands, false);
            }
            else
            {
                addSimpleMessage(message);
            }
            startedBraces--;
            tabbing = tabbing.substring(0, tabbing.length() - 4);
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
            tabbing += "    ";
            startedBraces++;
            if (commands != null && commands.length() > 1)
            {
                addComplexMessage(message, commands, false);
            }
            else
            {
                addSimpleMessage(message);
            }
            startedBraces--;
            tabbing = tabbing.substring(0, tabbing.length() - 4);
            addOutput(tabbing + "}", response);
        }
        
    }
    
    public abstract void addEnd();
    
    public abstract void addMethodCall(String methodName, boolean endScope);
    
    public abstract void addCall(String methodName, boolean endScope);
    
    public void addCallReturn(String methodName)
    {
        addOutput(tabbing + "run(\"" + methodName + "\");", methodName);
    }
    
    public void addAtCommand(String atCommand)
    {
        Matcher thisMatcher = Pattern.compile("\\s*(" + RegexHelper.atGotoFunction + "|" + RegexHelper.atIfFunction + "|" + RegexHelper.atChangeVarFunction + "|" + RegexHelper.atSetFunction + "|" + RegexHelper.atBracesFunction + "|" + RegexHelper.atFunctionWithParams + "|" + RegexHelper.atFunction + ")\\s*").matcher(atCommand);
        
        boolean insideIf = false;
        while (thisMatcher.find())
        {
            String thisMatch = thisMatcher.group(0);
            Matcher atMatcher = Pattern.compile("\\s*(" + RegexHelper.atFunction + ")\\s*").matcher(thisMatch.trim());
            Matcher atGotoMatcher = Pattern.compile("\\s*(" + RegexHelper.atGotoFunction + ")\\s*").matcher(thisMatch.trim());
            Matcher atParamsMatcher = Pattern.compile("\\s*(" + RegexHelper.atFunctionWithParams + ")\\s*").matcher(thisMatch.trim());
            Matcher atIfMatcher = Pattern.compile("\\s*(" + RegexHelper.atIfFunction + ")\\s*").matcher(thisMatch.trim());
            Matcher atSetVarMatcher = Pattern.compile("\\s*(" + RegexHelper.atSetFunction + ")\\s*").matcher(thisMatch.trim());
            Matcher atChangeVarMatcher = Pattern.compile("\\s*" + RegexHelper.atChangeVarFunction + "\\s*").matcher(thisMatch.trim());
            Matcher atBracesMatcher = Pattern.compile("\\s*" + RegexHelper.atBracesFunction + "\\s*").matcher(thisMatch.trim());
            if (atGotoMatcher.matches())
            {
                String command = atGotoMatcher.group(2);
                String methodName = atGotoMatcher.group(4).trim();
                if (command.equalsIgnoreCase("goto"))
                {
                    Matcher gotoMethodsMatcher = Pattern.compile("(\\w|\\s|'|_|\\d)+").matcher(atGotoMatcher.group(3).trim());
                    ArrayList<String> methods = new ArrayList<String>();
                    while (gotoMethodsMatcher.find())
                    {
                        methods.add(gotoMethodsMatcher.group(0));
                    }
                    if (methods.size() == 1)
                    {
                        if (startedBraces == 0)
                        {
                            addMethodCall(methodName, true);
                        }
                        else
                        {
                            addMethodCall(methodName, false);
                        }
                    }
                    else
                    {
                        addOutput(tabbing + "var random" + randomcounter + " = Math.floor(Math.random() * (" + (methods.size() - 1) + " + 1));", "var random = Math.floor(Math.random() * (" + (methods.size() - 1) + " + 1));");
                        addOutput(tabbing + "if (random" + randomcounter + " == 0)", "if (random" + randomcounter + " == 0)");
                        addOutput(tabbing + "{", "{");
                        tabbing += "    ";
                        startedBraces++;
                        addMethodCall(methods.get(0), false);
                        startedBraces--;
                        tabbing = tabbing.substring(0, tabbing.length() - 4);
                        addOutput(tabbing + "}", "}");
                        for (int i = 1; i < methods.size(); i++)
                        {
                            addOutput(tabbing + "else if (random" + randomcounter + " == " + i + ")", "else if (random" + randomcounter + " == " + i + ")");
                            addOutput(tabbing + "{", "{");
                            tabbing += "    ";
                            startedBraces++;
                            addMethodCall(methods.get(i), false);
                            startedBraces--;
                            tabbing = tabbing.substring(0, tabbing.length() - 4);
                            addOutput(tabbing + "}", "}");
                        }
                        randomcounter++;
                    }
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
                    addOutput(tabbing + "if ((Math.floor(Math.random() * (100 - 1 + 1)) + 1) <= " + chance + "){", methodName);
                    tabbing += "    ";
                    int beforeBraces = startedBraces;
                    startedBraces++;
                    addMethodCall(methodName, false);
                    for (int i = 0; i < startedBraces - beforeBraces; i++)
                    {
                        tabbing = tabbing.substring(0, tabbing.length() - 4);
                        addOutput(tabbing + "}", "}");
                    }
                    startedBraces = beforeBraces;
                    /*if (this instanceof MainScope)
                    {
                        addOutput(tabbing + "else {", "else {");
                        tabbing += "    ";
                        startedBraces++;
                    }*/
                }
                else if (command.equalsIgnoreCase("checkflag"))
                {
                    addOutput(tabbing + "if (getVar(\"" + methodName + "\", false)){", methodName);
                    tabbing += "    ";
                    int beforeBraces = startedBraces;
                    startedBraces++;
                    addMethodCall(methodName, false);
                    for (int i = 0; i < startedBraces - beforeBraces; i++)
                    {
                        tabbing = tabbing.substring(0, tabbing.length() - 4);
                        addOutput(tabbing + "}", "}");
                    }
                    startedBraces = beforeBraces;
                    /*addOutput(tabbing + "else {", "else {");
                    tabbing += "    ";
                    startedBraces++;*/
                }
                else if (command.equalsIgnoreCase("setflag"))
                {
                    addOutput(tabbing + "setVar(\"" + methodName + "\", true);", methodName);
                }
                else if (command.equalsIgnoreCase("deleteflag"))
                {
                    addOutput(tabbing + "deleteVar(\"" + methodName + "\");", methodName);
                }
                else if (command.equalsIgnoreCase("tempflag"))
                {
                    addOutput(tabbing + "setTempVar(\"" + methodName + "\", true);", methodName);
                }
                else if (command.equalsIgnoreCase("wait"))
                {
                    addOutput(tabbing + "sleep(" + methodName + ");", "sleep(" + methodName + ");");
                }
                else if (command.equalsIgnoreCase("wait"))
                {
                    addOutput(tabbing + "sleep(" + methodName + ");", "sleep(" + methodName + ");");
                }
                else if (command.equalsIgnoreCase("callreturn"))
                {
                    addCallReturn(methodName);
                }
                else if (command.equalsIgnoreCase("call"))
                {
                    if (startedBraces == 0)
                    {
                        addCall(methodName, true);
                    }
                    else
                    {
                        addCall(methodName, false);
                    }
                }
                else if (command.equalsIgnoreCase("notflag"))
                {
                    addOutput(tabbing + "if (!getVar(\"" + methodName + "\", false)){", methodName);
                    tabbing += "    ";
                    insideIf = true;
                }
                else if (command.equalsIgnoreCase("flag"))
                {
                    addOutput(tabbing + "if (getVar(\"" + methodName + "\", false)){", methodName);
                    tabbing += "    ";
                    insideIf = true;
                }
                else if (command.equalsIgnoreCase("setdate"))
                {
                    Matcher localMatcher = Pattern.compile("((\\w|\\d|'|_|\\s)+)").matcher(methodName);
                    if (!localMatcher.find())
                    {
                        System.out.println("didnt find when should setdate 1");
                    }
                    String dateName = localMatcher.group(1);
                    if (!localMatcher.find())
                    {
                        System.out.println("didnt find when should setdate 2");
                    }
                    Matcher dateMatcher = Pattern.compile("(\\d+)\\s+((\\w|\\d|'|_|\\s)+)").matcher(methodName);
                    if (!dateMatcher.find())
                    {
                        System.out.println("didnt find when should setdate 3");
                    }
                    String dateNumber = dateMatcher.group(1);
                    String timeMeasurement = dateMatcher.group(2);
                    if (dateNumber.trim().equals("0"))
                    {
                        addOutput(tabbing + "setDate(\"" + dateName + "\");", "setDate(\"" + dateName + "\");");
                    }
                    else
                    {
                        System.out.println("weird arg for setdate " + dateNumber + timeMeasurement);
                    }
                }
                else if (command.equalsIgnoreCase("rt"))
                {
                    Matcher gotoMethodsMatcher = Pattern.compile("(\\w|\\s|'|_|\\d)+").matcher(atGotoMatcher.group(3).trim());
                    ArrayList<String> methods = new ArrayList<String>();
                    while (gotoMethodsMatcher.find())
                    {
                        methods.add(gotoMethodsMatcher.group(0));
                    }
                    addOutput(tabbing + "var random" + randomcounter + " = Math.floor(Math.random() * (" + (methods.size() - 1) + " + 1));", "var random = Math.floor(Math.random() * (" + (methods.size() - 1) + " + 1));");
                    addOutput(tabbing + "if (random" + randomcounter + " == 0)", "if (random" + randomcounter + " == 0)");
                    addOutput(tabbing + "{", "{");
                    tabbing += "    ";
                    startedBraces++;
                    addSimpleMessage(methods.get(0));
                    tabbing = tabbing.substring(0, tabbing.length() - 4);
                    addOutput(tabbing + "}", "}");
                    for (int i = 1; i < methods.size(); i++)
                    {
                        addOutput(tabbing + "else if (random" + randomcounter + " == " + i + ")", "else if (random" + randomcounter + " == " + i + ")");
                        addOutput(tabbing + "{", "{");
                        tabbing += "    ";
                        startedBraces++;
                        addSimpleMessage(methods.get(i));
                        startedBraces--;
                        tabbing = tabbing.substring(0, tabbing.length() - 4);
                        addOutput(tabbing + "}", "}");
                    }
                    randomcounter++;
                }
                else if (command.toLowerCase().contains("followup"))
                {
                    //System.out.println("in chance");
                    Matcher chanceMatcher = Pattern.compile("\\w+(\\d\\d)").matcher(command);
                    if (!chanceMatcher.matches())
                    {
                        System.out.println("regex doesnt match when it should");
                    }
                    int chance = Integer.parseInt(chanceMatcher.group(1));
                    addOutput(tabbing + "if ((Math.floor(Math.random() * (100 - 1 + 1)) + 1) <= " + chance + "){", methodName);
                    tabbing += "    ";
                    startedBraces++;
                    addSimpleMessage(methodName);
                    startedBraces--;
                    tabbing = tabbing.substring(0, tabbing.length() - 4);
                    addOutput(tabbing + "}", "}");
                }
                else if (command.toLowerCase().contains("timeout"))
                {

                }
                else 
                {
                    System.out.println("uninterpreted:" + thisMatch);
                    addUninterpreted(thisMatch);
                }
            }
            else if (atIfMatcher.matches())
            {
                String ifvar = atIfMatcher.group(4).trim();
                String ifcheck = atIfMatcher.group(7).trim();
                String gotoString = atIfMatcher.group(10).trim();
                gotoString = gotoString.replaceAll("\\(", "");
                gotoString = gotoString.replaceAll("\\)", "");
                ifvar = ifvar.replaceAll("\\]", "");
                ifvar = ifvar.replaceAll("\\[", "");
                ifcheck = ifcheck.replaceAll("\\]", "");
                ifcheck = ifcheck.replaceAll("\\[", "");
                if (isInt(ifcheck))
                {
                    addOutput(tabbing + "if (getVar(\"" + ifvar + "\", null) == " + ifcheck + "){", "");
                    tabbing += "    ";
                    startedBraces++;
                    addMethodCall(gotoString, false);
                    startedBraces--;
                    tabbing = tabbing.substring(0, tabbing.length() - 4);
                    addOutput(tabbing + "}", "}");
                }
                else
                {
                    addOutput(tabbing + "if (getVar(\"" + ifvar + "\", null) == getVar(\"" + ifcheck + "\", null)){", "");
                    addMethodCall(gotoString, false);
                    tabbing = tabbing.substring(0, tabbing.length() - 4);
                    addOutput(tabbing + "}", "}");
                }
            }
            else if (atChangeVarMatcher.matches())
            {
                String toSet = atChangeVarMatcher.group(2).trim();
                String value = atChangeVarMatcher.group(5).trim();
                if (!isInt(value))
                {
                    value = "getVar(\"" + value + "\", 0)";
                }
                toSet = toSet.replace("]", "");
                toSet = toSet.replace("[", "");
                value = value.replace("]", "");
                value = value.replace("[", "");
                String changes = "";
                Matcher changesMatcher = Pattern.compile("(([+|-|\\*|\\/])\\s*(\\[((\\w|\\d|\\s|'|_\\/)+)\\]))").matcher(atChangeVarMatcher.group(0).trim());
                while (changesMatcher.find())
                {
                    String operator = changesMatcher.group(2);
                    String changeValue = changesMatcher.group(4);
                    if (!isInt(changeValue))
                    {
                        changeValue = "getVar(\"" + changeValue + "\", 0)";
                    }
                    changes += " " + operator + " " + changeValue; 
                }
                addOutput(tabbing + "setVar(\"" + toSet + "\", " + value + changes + ");", "setVar(\"" + toSet + "\", " + value + " " +  changes + ");");
            }
            else if (atSetVarMatcher.matches())
            {
                String toSet = atSetVarMatcher.group(2).trim();
                String value = atSetVarMatcher.group(5).trim();
                toSet = toSet.replace("]", "");
                toSet = toSet.replace("[", "");
                addOutput(tabbing + "setVar(\"" + toSet + "\", " + value + ");", "setVar(\"" + toSet + "\", " + value);
            }
            else if (atBracesMatcher.matches())
            {
                String command = atBracesMatcher.group(1);
                String params = atBracesMatcher.group(2);
                params = params.replaceAll("\\[", "");
                params = params.replaceAll("\\]", "");
                params = params.replaceAll("\\\\", "\\/");
                if (command.equalsIgnoreCase("playvideo"))
                {
                    addOutput(tabbing + "playVideo(\"" + params + "\", true);", "playVideo(\"" + params + "\");");
                }
                else if (command.equalsIgnoreCase("playaudio"))
                {
                    addOutput(tabbing + "playAudio(\"" + params + "\");", "playVideo(\"" + params + "\");");
                }
                else
                {
                    System.out.println("uninterpreted:" + thisMatch);
                    addUninterpreted(thisMatch);
                }
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
                else if (command.equalsIgnoreCase("orgasmrestricted"))
                {
                    addOutput(tabbing + "if (getVar(\"orgasmrestricted\", false){", "if (getVar(\"orgasmrestricted\", false){");
                    tabbing += "    ";
                    insideIf = true;
                    addSimpleMessage(commandArgs);
                }
                else if (command.equalsIgnoreCase("stroking"))
                {
                    addOutput(tabbing + "if (isStroking()){", "if (isStroking()){");
                    tabbing += "    ";
                    insideIf = true;
                    addSimpleMessage(commandArgs);
                }
                else if (command.equalsIgnoreCase("acceptanswer"))
                {
                    addOutput(tabbing + "else{", "else{");
                    tabbing += "    ";
                    insideIf = true;
                    addSimpleMessage(commandArgs);
                }
                else if (command.equalsIgnoreCase("systemmessage"))
                {
                    addOutput(tabbing + "SMessage(\"" + commandArgs + "\", 2, 0);", "SMessage(\"" + commandArgs + "\", 2, 0);");
                }
                else
                {
                    System.out.println("uninterpreted params" + thisMatch);
                    addUninterpreted(thisMatch);
                }
                //System.out.println(atMatcher.group(2));
            }
            else if (atMatcher.matches())
            {
                String command = atMatcher.group(2);
                command = command.trim();
                if (command.equalsIgnoreCase("end"))
                {
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
                else if (command.equalsIgnoreCase("edge") || command.equalsIgnoreCase("edgenohold"))
                {
                    addOutput(tabbing + "startEdging();", "startEdging();");
                }
                else if (command.equalsIgnoreCase("edgehold"))
                {
                    addOutput(tabbing + "holdEdge();", "holdEdge();");
                }
                else if (command.equalsIgnoreCase("nullresponse") | command.equalsIgnoreCase("rapidcodeon") | command.equalsIgnoreCase("rapidcodeoff")
                        | command.equalsIgnoreCase("afkon") | command.equalsIgnoreCase("afkoff"))
                {
                    //addOutput(tabbing + "startEdging();", "startEdging();");
                }
                else if (command.equalsIgnoreCase("randommodule"))
                {
                    addOutput(tabbing + "run(\"Structure\\Modules\\*.js\");" , "run(\"Structure\\Modules\\*.js\")");
                }
                else if (command.equalsIgnoreCase("rapidtexton"))
                {
                    addOutput(tabbing + "setTypeSpeed(\"FASTER\");" , "setTypeSpeed(\"FASTER\");");
                }
                else if (command.equalsIgnoreCase("rapidtextoff"))
                {
                    addOutput(tabbing + "setTypeSpeed(\"MEDIUM\");" , "setTypeSpeed(\"MEDIUM\");");
                }
                else if (command.equalsIgnoreCase("stopstroking"))
                {
                    addOutput(tabbing + "endStroking();" , "endStroking();");
                }
                else if (command.equalsIgnoreCase("moodup"))
                {
                    addOutput(tabbing + "increaseAnger(-2);" , "increaseAnger(-2);");
                }
                else if (command.equalsIgnoreCase("mooddown"))
                {
                    addOutput(tabbing + "increaseAnger(2);" , "increaseAnger(2);");
                }
                else if (command.equalsIgnoreCase("inchastity"))
                {
                    addOutput(tabbing + "if (getVar(\"inchastity\", false){", "if (getVar(\"inchastity\", false){");
                    tabbing += "    ";
                    insideIf = true;
                }
                else
                {
                    System.out.println("uninterpreted:" + thisMatch);
                    addUninterpreted(thisMatch);
                }
                //System.out.println(atParamsMatcher.group(2));
            }
            else 
            {
                System.out.println("uninterpreted:" + thisMatch);
                addUninterpreted(atCommand);
                System.out.println("Bad error atCommand");
            }
            
        }
        if (insideIf)
        {
            tabbing = tabbing.substring(0, tabbing.length() - 4);
            addOutput(tabbing + "}", "}");
        }
    }
    
    public abstract void endScope();

    protected String replaceVocabs(String toChange)
    {
        Pattern pattern = Pattern.compile("(#)([a-zA-Z0-9_]+)");
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
    
    public boolean isInt(String canParse)
    {
        try
        {
            Integer.parseInt(canParse);
            return true;
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
    }
    
}

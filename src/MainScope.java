import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainScope extends CodeScope
{
    private boolean inIfScope = false;
    private boolean addIfAfterScope = false;
    
    public MainScope()
    {
        super();
        addOutput(tabbing + "var exit = false;", "var exit = false;");
    }
    
    @Override
    public void addEnd()
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void addSimpleMessage(String simpleMessage)
    {
        if (addIfAfterScope && !inIfScope)
        {
            startIf();
        }
        addOutput(tabbing + "CMessage(\"" + replaceVocabs(simpleMessage) + "\");", simpleMessage);
    }
    
    @Override
    public void addComplexMessage(String message, String methodCall, boolean returnOut)
    {
        if (addIfAfterScope && !inIfScope)
        {
            startIf();
        }
        super.addComplexMessage(message, methodCall, returnOut);
    }
    
    //@Override
    public void addComplexMessage2(String message, String methodCall, boolean returnOut)
    {
        if (addIfAfterScope && !inIfScope)
        {
            startIf();
        }
        addSimpleMessage(message);
        Matcher thisMatcher = Pattern.compile("(([(" + RegexHelper.atGotoFunction + ")|(" + RegexHelper.atFunction + ")])+)").matcher(methodCall);
        if (!thisMatcher.matches())
        {
            System.out.println("bad error");
        }
        for (int i = 0; i < thisMatcher.groupCount(); i++)
        {
            System.out.println("Group " + i + ":" + thisMatcher.group(i) + ";");
        }
        String groupa = thisMatcher.group(4);
        String methodName = thisMatcher.group(5);
        if (methodName != null)
        {
            methodName = methodName.replaceAll("\\(", "");
            methodName = methodName.replaceAll("\\)", "");
            methodName = methodName.replaceAll(" ", "");
            methodName = methodName.replaceAll("'", "");
        }
        //TODO add actual method calls
        if (groupa.equalsIgnoreCase("goto"))
        {
            addMethodCall(methodName, returnOut);
            if (inIfScope)
            {
                addIfAfterScope = true;
            }
        }
        else if (groupa.toLowerCase().contains("chance"))
        {
            //System.out.println("in chance");
            Matcher chanceMatcher = Pattern.compile("\\w+(\\d\\d)").matcher(groupa);
            if (!chanceMatcher.matches())
            {
                System.out.println("regex doesnt match when it should");
            }
            int chance = Integer.parseInt(chanceMatcher.group(1));
            String originalTabbing = tabbing;
            addOutput(tabbing + "if ((Math.floor(Math.random() * (100 - 1 + 1)) + 1) <= " + chance + "){", methodCall);
            tabbing += "    ";
            addMethodCall(methodName, false);
            tabbing = originalTabbing;
            addOutput(tabbing + "}", "}");
            addOutput(tabbing + "else {", "else {");
            tabbing += "    ";
            startedBraces++;
        }
        else {
            addUninterpreted(methodCall);
        }
    }
    
    @Override
    public void addMethodCall(String methodName, boolean endScope)
    {
        if (addIfAfterScope && !inIfScope)
        {
            startIf();
        }
        addOutput(tabbing + methodName + "();", methodName);
        addOutput(tabbing + "exit = true;", "exit = true;");
        addIfAfterScope = true;
        
        if (endScope)
            endScope();
    }

    @Override
    public void endScope()
    {
        isOpen = false;
        for (int i = 0; i < startedBraces; i++)
        {
            tabbing = tabbing.substring(0, tabbing.length() - 4);
            addOutput(tabbing + "}", "}");
        }
    }
    
    public void startIf()
    {
        addOutput(tabbing + "if (!exit) {", "if (!exit) {");
        tabbing += "    ";
        startedBraces++;
        addIfAfterScope = false;
    }

    @Override
    public void addAtCommand(String atCommand)
    {
        if (addIfAfterScope && !inIfScope)
        {
            startIf();
        }
        super.addAtCommand(atCommand);
    }
    
    /*public void addResponse(String response)
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
            inIfScope = true;
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
            inIfScope = false;
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
            inIfScope = true;
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
            inIfScope = false;
        }
        
    }*/

    @Override
    public void addCallReturn(String methodName, boolean endScope)
    {
        if (addIfAfterScope && !inIfScope)
        {
            startIf();
        }
        addOutput(tabbing + "run(\"" + methodName + "\");", methodName);
        addOutput(tabbing + "exit = true;", "exit = true;");
        addIfAfterScope = true;
        
        if (endScope)
            endScope();
        
    }

}

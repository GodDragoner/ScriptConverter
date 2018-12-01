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
    
    private boolean breakCurrentLine = false;
    
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
            if (!parsedLine.thisRegex.equals(ParsedLine.lineRegex.RESPONSE) && scoping.peek().contains("response") &&
                    !(parsedLine.lineComponents.get(0) instanceof AtCommand && ((AtCommand) parsedLine.lineComponents.get(0)).commandName.toLowerCase().contains("response")))
            {
                popScoping();
            }
            if (scoping.peek().contains("if"))
            {
                popScoping();
            }
            if (parsedLine.lineComponents.size() > 1 && parsedLine.lineComponents.get(0) instanceof AtCommand && ((AtCommand)parsedLine.lineComponents.get(0)).commandName.equalsIgnoreCase("differentanswer"))
            {
                currentLineIndex++;
                continue;
            }
            else if (parsedLine.lineComponents.size() > 2 && parsedLine.lineComponents.get(1) instanceof AtCommand && ((AtCommand)parsedLine.lineComponents.get(1)).commandName.equalsIgnoreCase("differentanswer"))
            {
                currentLineIndex++;
                continue;
            }
            thisParsedLine = parsedLine;
            indexInCurrentLine = 0;
            if (parsedLine.thisRegex.equals(ParsedLine.lineRegex.UNINTERPRETED))
            {
                pushLine("--UNINTERPRETED LINE:" + parsedLine.line);
            }
            else if (parsedLine.thisRegex.equals(ParsedLine.lineRegex.BLANK))
            {
                pushLine("");
            }
            else {
                for (LineComponent lineComponent: parsedLine.lineComponents)
                {
                    if (breakCurrentLine)
                    {
                        breakCurrentLine = false;
                        break;
                    }
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
            pushModify((ModifyCommand) component);
        }
        else if (component instanceof IfStatement)
        {
            pushIfStatement((IfStatement) component);
        }
    }
    
    //This is where most commands need to be converted
    //This functions takes in the AtCommand object and will push the
    //javascript/TAJ version of the command
    private void pushCommand(AtCommand command)
    {
        String commandName = command.commandName;
        ArrayList<LineComponent> parameters = command.parameters;
        if (parameters.size() == 0)
        {
            //Commands without parameters
            //Put any commands with no parameters in this switch and
            //if they have a output version in javascript
            //push the line with the converted javascript version
            switch (commandName.toLowerCase())
            {
                case "adddomme":
                    pushLine("addContact(1);");
                    break;
                case "acceptanswer":
                    pushLine("else");
                    pushScoping("if:responseAcceptAnswer");
                    break;
                case "addcontact1":
                    pushLine("addContact(2);");
                    break;
                case "addcontact2":
                    pushLine("addContact(3);");
                    break;
                case "addcontact3":
                    pushLine("addContact(4);");
                    break;
                case "removedomme":
                    pushLine("removeContact(1);");
                    break;
                case "removecontact1":
                    pushLine("removeContact(2);");
                    break;
                case "removecontact2":
                    pushLine("removeContact(3);");
                    break;
                case "removecontact3":
                    pushLine("removeContact(4);");
                    break;
                case "afkon":
                    pushLine("setAFK(true);");
                    break;
                case "afkoff":
                    pushLine("setAFK(false);");
                    break;
                case "increaseorgasmchance":
                    pushLine("increaseOrgasmChance(8);");
                    break;
                case "inchastity":
                    pushLine("if (getVar(\"chastityon\", false))");
                    pushScoping("if:inchastity");             
                    break;
                case "badmood":
                    pushLine("if (getApathyMoodIndex() >= 75)");
                    pushScoping("if:badmood");             
                    break;
                case "edgetoruin":
                    pushLine("edgeToRuin();");            
                    break;
                case "edgetoruinhold":
                    pushLine("edgeToRuinHold();");            
                    break;
                case "goodmood":
                    pushLine("if (getApathyMoodIndex() <= 25)");
                    pushScoping("if:goodmood");             
                    break;
                case "neutralmood":
                    pushLine("if (getApathyMoodIndex() > 25 && getApathyMoodIndex() < 75)");
                    pushScoping("if:neutralmood");             
                    break;
                case "decreaseorgasmchance":
                    pushLine("increaseOrgasmChance(-8);");
                    break;
                case "edge":
                    pushLine("startEdging();");
                    break;
                case "increaseruinchance":
                    pushLine("increaseRuinChance(8);");
                    break;
                case "decreaseruinchance":
                    pushLine("increaseRuinChance(-8);");
                    break;
                case "info":
                    pushLine("//");
                case "rapidcodeon":
                    break;
                case "rapidcodeoff":
                    break;
                case "nullresponse":
                    break;
                case "systemmessage":
                    break;
                case "differentanswer":
                    break;
                    //handled in pushMessage
                case "contact1":
                    break;
                case "contact2":
                    break;
                case "contact3":
                    break;
                case "showsoftcoreimage":
                    pushLine("showTaggedImage(4, [\"softcore\"]);");
                    break;
                case "showblowjobimage":
                    pushLine("showTaggedImage(4, [\"blowjob\"]);");
                    break;
                case "showlesbianimage":
                    pushLine("showTaggedImage(4, [\"lesbian\"]);");
                    break;
                case "showhardcoreimage":
                    pushLine("showTaggedImage(4, [\"hardcore\"]);");
                    break;
                case "playvideo":
                    pushLine("playVideo(\"Videos\" + java.io.File.separator + \"*.*\");");
                    break;
                case "end":
                    pushLine("return;");
                    break;
                case "startstroking":
                    pushLine("Stroking();");
                    break;
                case "moodup":
                    pushLine("increaseAnger(3)");
                    break;
                //TODO THIS MIGHT NEED TO BE CHANGED TO SOMETHING ELSE???
                case "dommelevelup":
                    pushLine("increaseAnger(4)");
                    break;
                //TODO THIS MIGHT NEED TO BE CHANGED TO SOMETHING ELSE???
                case "apathylevelup":
                    pushLine("increaseAnger(4)");
                    break;
                    //TODO DON'T CURRENTLY KNOW WHAT TO DO HERE
                case "newdommeslideshow":
                    pushLine("//TODO: New domme slide show");
                    break;
                case "slideshowoff":
                    pushLine("//TODO: Turn Slideshow off");
                    break;
                case "slideshowon":
                    pushLine("//TODO: Turn Slideshow on");
                    break;
                case "unlockimages":
                    pushLine("unlockImages();");
                    break;
                case "endtease":
                    pushLine("endSession();");
                    break;
                case "rapidtextoff":
                    pushLine("setRapidText(false);");
                    break;
                case "rapidtexton":
                    pushLine("setRapidText(true);");
                    break;
                case "gotodommelevel":
                    pushLine("if (getApathyLevel <= 2)");
                    pushScoping("if:dommelevel1");
                    pushCommand(new AtCommand("@goto2(dommeLevel1)"));
                    popScoping();
                    pushLine("else if (getApathyLevel <= 4)");
                    pushScoping("if:dommelevel1");
                    pushCommand(new AtCommand("@goto2(dommeLevel2)"));
                    popScoping();
                    pushLine("else if (getApathyLevel <= 6)");
                    pushScoping("if:dommelevel1");
                    pushCommand(new AtCommand("@goto2(dommeLevel3)"));
                    popScoping();
                    pushLine("else if (getApathyLevel <= 8)");
                    pushScoping("if:dommelevel1");
                    pushCommand(new AtCommand("@goto2(dommeLevel4)"));
                    popScoping();
                    pushLine("else if (getApathyLevel <= 10)");
                    pushScoping("if:dommelevel1");
                    pushCommand(new AtCommand("@goto2(dommeLevel5)"));
                    popScoping();
                    
                    break;
                case "decideorgasm":
                    pushLine("var orgasmResult = decideOrgasm();");
                    pushLine("if (orgasmResult == 2)");
                    pushScoping("if:alloworgasm");
                    pushCommand(new AtCommand("@goto2(Orgasm_Allow)"));
                    popScoping();
                    pushLine("else if (orgasmResult == 1)");
                    pushScoping("if:ruinorgasm");
                    pushCommand(new AtCommand("@goto2(Orgasm_Ruin)"));
                    popScoping();
                    pushLine("else if (orgasmResult == 0)");
                    pushScoping("if:denyorgasm");
                    pushCommand(new AtCommand("@goto2(Orgasm_Deny)"));
                    popScoping();
                    break;
                default:
                    pushLine("--" + command.toString());
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
            //eventually alphabetize these for convenience of finding them
            switch (commandName.toLowerCase())
            {
                case "slideshow":
                    pushLine("//" + command.toString());
                    break;
                case "call2":
                    pushLine("run(\"" + parameters.get(0).toString() + "\");");
                    pushLine("return;");
                    break;  
                case "call":
                    int temporaryLineCounter = indexInCurrentLine;
                    pushRestOfLine();
                    indexInCurrentLine = temporaryLineCounter;
                    pushLine("run(\"" + parameters.get(0).toString() + "\");");
                    pushLine("return;");
                    break;  
                case "callreturn":
                    int temporaryLineCounter2 = indexInCurrentLine;
                    pushRestOfLine();
                    indexInCurrentLine = temporaryLineCounter2;
                    pushLine("run(\"" + parameters.get(0).toString() + "\");");
                    break;  
                case "callrandom":
                    pushLine("run(\"" + parameters.get(0).toString() + "\" + java.io.File.separator + \"*.*\");");
                    pushLine("return;");
                    break;
                case "multipleedges":
                    if (parameters.size() == 2)
                    {
                        pushLine("var amountEdges = " + parameters.get(0).toString().trim() + ";");
                        pushLine("DoEdges(amountEdges, amountEdges, 0);");
                    }
                    else 
                    {
                        pushLine("if (randomInteger(1, 100) <= " + parameters.get(2).toString().trim() + ")");
                        pushScoping("if:multipleedges");
                        pushLine("var amountEdges = " + parameters.get(0).toString().trim() + ";");
                        pushLine("DoEdges(amountEdges, amountEdges, 0);");
                        popScoping();
                        pushLine("else");
                        pushScoping("else:multipleedges");
                        pushLine("StartEdging()");
                        popScoping();
                    }
                    break;
                //needs to execute commands after goto before
                case "goto":
                    int temporaryLineCounter3 = indexInCurrentLine;
                    pushRestOfLine();
                    indexInCurrentLine = temporaryLineCounter3;
                    if (parameters.size() == 1)
                    {
                        pushLine(parameters.get(0).toString().replaceAll(" ", "_") + "();");
                        pushLine("return;");
                    }
                    else {
                        String output = "switch(random(";
                        for (int i = 0; i < parameters.size(); i++)
                        {
                            if (i != 0)
                            {
                                output += ", ";
                            }
                            output += "\"" + parameters.get(i).toString() + "\"";
                        }
                        output += "))";
                        pushLine(output);
                        pushScoping("goto:switch");
                        for (int i = 0; i < parameters.size(); i++)
                        {
                            pushLine("case \"" + parameters.get(i) + "\":");
                            pushLine(parameters.get(i).toString().replaceAll(" ", "_") + "();");
                            pushLine("break;");
                        }
                        popScoping();
                    }
                    break; 
                    //This is just called by other commands
                case "goto2":
                    if (parameters.size() == 1)
                    {
                        pushLine(parameters.get(0).toString().replaceAll(" ", "_") + "();");
                        pushLine("return;");
                    }
                    else {
                        String output = "switch(random(";
                        for (int i = 0; i < parameters.size(); i++)
                        {
                            if (i != 0)
                            {
                                output += ", ";
                            }
                            output += "\"" + parameters.get(i).toString() + "\"";
                        }
                        output += "))";
                        pushLine(output);
                        pushScoping("goto:switch");
                        for (int i = 0; i < parameters.size(); i++)
                        {
                            pushLine("case: \"" + parameters.get(i).toString() + "\"");
                            pushLine(parameters.get(i).toString().replaceAll(" ", "_") + "();");
                            pushLine("break;");
                        }
                        popScoping();
                    }
                    break;  
                case "group":
                    String toOutput6 = "if (inGroup(";
                    for (int i = 0; i < parameters.get(0).toString().length(); i++)
                    {
                        char c =  parameters.get(0).toString().toLowerCase().replaceAll("d", "5").charAt(i);
                        int dommeIndex = Integer.parseInt(c + "");
                        if (dommeIndex == 5)
                        {
                            dommeIndex = 1;
                        }
                        else
                        {
                            dommeIndex++;
                        }
                        toOutput6 += dommeIndex;
                        if (parameters.get(0).toString().length() > (i + 1))
                        {
                            toOutput6 += ", ";
                        }
                    }
                    toOutput6 += "))";
                    pushLine(toOutput6);
                    pushScoping("if:ingroup");
                    
                    break;
                case "checkflag":
                    String toOutput = "if(";
                    for (int i = 0; i < parameters.size(); i++)
                    {
                        if (i > 0)
                        {
                            toOutput += " && ";
                        }
                        toOutput += "getVar(\"" + parameters.get(i).toString() + "\", false)";
                    }
                    toOutput += ")";
                    pushLine(toOutput);
                    pushScoping("if:checkflag");
                    pushCommand(new AtCommand("@Goto2(" + parameters.get(0).toString() + ")"));
                    popScoping();
                    break;
                case "deletevar":
                    for (int i = 0; i < parameters.size(); i++)
                    {
                        pushLine("delVar(\"" + parameters.get(i).toString() + "\");");
                    }
                    break;
                case "deleteflag":
                    for (int i = 0; i < parameters.size(); i++)
                    {
                        pushLine("delVar(\"" + parameters.get(i).toString() + "\");");
                    }
                    break;
                case "edgemode":
                    pushLine("setVar(\"edgingmode\", \"" + parameters.get(0).toString() + "\");");
                    if (parameters.size() > 1)
                    {
                        pushLine("setVar(\"edginggoto\", \"" + parameters.get(1).toString() + "\")");
                    }
                    break;
                case "flag":
                    String toOutput4 = "if(";
                    for (int i = 0; i < parameters.size(); i++)
                    {
                        if (i > 0)
                        {
                            toOutput4 += " && ";
                        }
                        toOutput4 += "getVar(\"" + parameters.get(i).toString() + "\", false)";
                    }
                    toOutput4 += ")";
                    pushLine(toOutput4);
                    pushScoping("if:flag");             
                    break;
                case "flagor":
                    String toOutput5 = "if(";
                    for (int i = 0; i < parameters.size(); i++)
                    {
                        if (i > 0)
                        {
                            toOutput5 += " || ";
                        }
                        toOutput5 += "getVar(\"" + parameters.get(i).toString() + "\", false)";
                    }
                    toOutput5 += ")";
                    pushLine(toOutput5);
                    pushScoping("if:flag");             
                    break;
                case "responseyes":
                    String outputResponse = "";
                    if (scoping.peek().equalsIgnoreCase("response:answer" + (answerCounter - 1)))
                    {
                        popScoping();
                        outputResponse += "else if (answer" + (answerCounter-1) + ".isLike(\"yes\", \"yea\", \"yep\"))";
                    }
                    else
                    {
                        outputResponse += "if (answer" + (answerCounter-1) + ".isLike(\"yes\", \"yea\", \"yep\"))";
                    }
                    pushLine(outputResponse);
                    pushScoping("response:answer" + (answerCounter-1));
                    pushCommand(new AtCommand("@goto2(" + parameters.get(0).toString() + ")"));
                    
                    break;
                case "responseno":
                    String outputResponse2 = "";
                    if (scoping.peek().equalsIgnoreCase("response:answer" + (answerCounter - 1)))
                    {
                        popScoping();
                        outputResponse2 += "else if (answer" + (answerCounter-1) + ".isLike(\"no\", \"nope\", \"nah\", \"not\"))";
                    }
                    else
                    {
                        outputResponse2 += "if (answer" + (answerCounter-1) + ".isLike(\"no\", \"nope\", \"nah\", \"not\"))";
                    }
                    pushLine(outputResponse2);
                    pushScoping("response:answer" + (answerCounter-1));
                    pushCommand(new AtCommand("@goto2(" + parameters.get(0).toString() + ")"));
                    
                    break;
                    //will probably implement this later. For now left out
                case "removeteasetime":
                    break;
                case "ruinsorgasm":
                    String toOutput7 = "if (";
                    for (int i = 0; i < parameters.size(); i++)
                    {
                        if (i != 0)
                        {
                            toOutput7 += " || ";
                        }
                        if (parameters.get(i).toString().toLowerCase().equals("never"))
                        {
                            toOutput7 += "getRuinChance() == 0";
                        }
                        else if (parameters.get(i).toString().toLowerCase().equals("often"))
                        {
                            toOutput7 += "getRuinChance() >= 80";
                        }
                        else if (parameters.get(i).toString().toLowerCase().equals("rarely") || parameters.get(i).toString().toLowerCase().equals("not"))
                        {
                            toOutput7 += "getRuinChance() <= 20";
                        }
                        else if (parameters.get(i).toString().toLowerCase().equals("sometimes"))
                        {
                            toOutput7 += "(getRuinChance() >= 30 && getRuinChance() <= 70)";
                        }
                    }
                    toOutput7 += ")";
                    pushLine(toOutput7);
                    pushScoping("if:ruinsorgasm");
                    break;
                case "allowsorgasm":
                    String toOutput8 = "if (";
                    for (int i = 0; i < parameters.size(); i++)
                    {
                        if (i != 0)
                        {
                            toOutput8 += " || ";
                        }
                        if (parameters.get(i).toString().toLowerCase().equals("never"))
                        {
                            toOutput8 += "getOrgasmChance() == 0";
                        }
                        else if (parameters.get(i).toString().toLowerCase().equals("often"))
                        {
                            toOutput8 += "getOrgasmChance() >= 80";
                        }
                        else if (parameters.get(i).toString().toLowerCase().equals("rarely") || parameters.get(i).toString().toLowerCase().equals("not"))
                        {
                            toOutput8 += "getOrgasmChance() <= 20";
                        }
                        else if (parameters.get(i).toString().toLowerCase().equals("sometimes"))
                        {
                            toOutput8 += "(getOrgasmChance() >= 30 && getOrgasmChance() <= 70)";
                        }
                    }
                    toOutput8 += ")";
                    pushLine(toOutput8);
                    pushScoping("if:allowsorgasm");
                    break;
                case "interrupt":
                    int temporaryLineCounter4 = indexInCurrentLine;
                    pushRestOfLine();
                    indexInCurrentLine = temporaryLineCounter4;
                    pushCommand(new AtCommand("@callreturn(Interrupt/" + parameters.get(0).toString() + ")"));
                    break;
                case "miniscript":
                    pushCommand(new AtCommand("@callreturn(Custom/Miniscripts/" + parameters.get(0).toString().trim() + ");"));
                    break;
                case "notflag":
                    String toOutput2 = "if(";
                    for (int i = 0; i < parameters.size(); i++)
                    {
                        if (i > 0)
                        {
                            toOutput2 += " && ";
                        }
                        toOutput2 += "!getVar(\"" + parameters.get(i).toString() + "\", false)";
                    }
                    toOutput2 += ")";
                    pushLine(toOutput2);
                    pushScoping("if:notflag");
                    break;
                case "setflag":
                    for (int i = 0; i < parameters.size(); i++)
                    {
                        pushLine("setVar(\"" + parameters.get(i).toString() + "\", true);");
                    }
                    break;
                case "tempflag":
                    for (int i = 0; i < parameters.size(); i++)
                    {
                        pushLine("setTempVar(\"" + parameters.get(i).toString() + "\", true);");
                    }
                    break;
                case "setdate":
                    String toOutput3 = "setDate(\"" + parameters.get(0).toString() + "\")";
                    String timeAmount;
                    if (parameters.get(1).toString().toLowerCase().contains("seconds"))
                    {
                        timeAmount = parameters.get(1).toString().toLowerCase().replaceAll("seconds", "").trim();
                        if (!timeAmount.equals("0"))
                        {
                            toOutput3 += ".addSeconds(" + timeAmount + ");";
                        }
                        else
                        {
                            toOutput3 += ";";
                        }
                    }
                    else if (parameters.get(1).toString().toLowerCase().contains("minutes"))
                    {
                        timeAmount = parameters.get(1).toString().toLowerCase().replaceAll("minutes", "").trim();
                        if (!timeAmount.equals("0"))
                        {
                            toOutput3 += ".addMinutes(" + timeAmount + ");";
                        }
                        else
                        {
                            toOutput3 += ";";
                        }
                    }
                    else if (parameters.get(1).toString().toLowerCase().contains("hours"))
                    {
                        timeAmount = parameters.get(1).toString().toLowerCase().replaceAll("hours", "").trim();
                        if (!timeAmount.equals("0"))
                        {
                            toOutput3 += ".addHours(" + timeAmount + ");";
                        }
                        else
                        {
                            toOutput3 += ";";
                        }
                    }
                    else if (parameters.get(1).toString().toLowerCase().contains("days"))
                    {
                        timeAmount = parameters.get(1).toString().toLowerCase().replaceAll("days", "").trim();
                        if (!timeAmount.equals("0"))
                        {
                            toOutput3 += ".addDays(" + timeAmount + ");";
                        }
                        else
                        {
                            toOutput3 += ";";
                        }
                    }
                    else if (parameters.get(1).toString().toLowerCase().contains("weeks"))
                    {
                        timeAmount = parameters.get(1).toString().toLowerCase().replaceAll("weeks", "").trim();
                        if (!timeAmount.equals("0"))
                        {
                            toOutput3 += ".addDays(" + timeAmount + " * 7);";
                        }
                        else
                        {
                            toOutput3 += ";";
                        }
                    }
                    else if (parameters.get(1).toString().toLowerCase().contains("months"))
                    {
                        timeAmount = parameters.get(1).toString().toLowerCase().replaceAll("months", "").trim();
                        if (!timeAmount.equals("0"))
                        {
                            toOutput3 += ".addMonths(" + timeAmount + ");";
                        }
                        else
                        {
                            toOutput3 += ";";
                        }
                    }
                    else if (parameters.get(1).toString().toLowerCase().contains("years"))
                    {
                        timeAmount = parameters.get(1).toString().toLowerCase().replaceAll("years", "").trim();
                        if (!timeAmount.equals("0"))
                        {
                            toOutput3 += ".addYears(" + timeAmount + ");";
                        }
                        else
                        {
                            toOutput3 += ";";
                        }
                    }
                    pushLine(toOutput3);
                    break;
                case "setmodule":
                    pushLine("setVar(\"moduletorun\", \"" + parameters.get(0).toString() + "\")");
                case "showimage":
                    pushLine("getLocalTeasePicture(\"images\" + java.io.File.separator + \"" + parameters.get(0).toString() + "\");");
                    break;
                case "playaudio":
                    pushLine("playAudio(\"Audio\" + java.io.File.separator + \"" + parameters.get(0).toString() + "\");");
                    break;
                case "playvideo":
                    pushLine("playVideo(\"Videos\" + java.io.File.separator + \"" + parameters.get(0).toString() + "\");");
                    break;
                case "wait":
                    pushLine("Wait(" + parameters.get(0).toString() + ");");
                    break;
                default:
                    if (commandName.toLowerCase().contains("chance"))
                    {
                        String chanceAmount = StringHelper.removeChars(commandName.toLowerCase(), "chance");
                        pushLine("if (randomInteger(1, 100) <= " + chanceAmount + ")");
                        pushScoping("if:chance");
                        pushCommand(new AtCommand("@Goto2(" + parameters.get(0).toString() + ")"));
                        popScoping();
                    }
                    else {
                        pushLine("--" + command.toString());
                    }
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
        
        boolean gettingInput = false;
        //Check the command before this message and if its SystemMessage make this a SMessage instead of a CMessage
        String outputMessage = "";
        if (indexInCurrentLine >= 1)
        {
            boolean answerDone = false;
            if (thisParsedLine.lineComponents.get(indexInCurrentLine - 1) instanceof AtCommand && ((AtCommand)thisParsedLine.lineComponents.get(indexInCurrentLine - 1)).commandName.equalsIgnoreCase("info"))
            {
                pushToCurrent(message.content);
                return;
            }
            if (thisParsedLine.lineComponents.get(indexInCurrentLine - 1) instanceof AtCommand)
            {
                AtCommand command = (AtCommand) thisParsedLine.lineComponents.get(indexInCurrentLine - 1);
                if (command.commandName.toLowerCase().equals("contact1") || command.commandName.toLowerCase().equals("contact2") || command.commandName.toLowerCase().equals("contact3"))
                {
                    outputMessage += "SMessage(";
                    answerDone = true;
                }
            }
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
                    if (!answerDone)
                    {
                        outputMessage += "CMessage(";
                    }
                }
            }
        }
        else
        {
            //Here, we check if the next line is a response. If so this needs to be getting input instead of sending a message
            if (parsedInput.size() > currentLineIndex + 1)
            {
                boolean answerSet = false;
                if (thisParsedLine.lineComponents.size() > (indexInCurrentLine + 1) && thisParsedLine.lineComponents.get(indexInCurrentLine + 1) instanceof AtCommand && 
                        (((AtCommand) thisParsedLine.lineComponents.get(indexInCurrentLine + 1)).commandName.toLowerCase().contains("responseyes") ||
                                ((AtCommand) thisParsedLine.lineComponents.get(indexInCurrentLine + 1)).commandName.toLowerCase().contains("responseno")))
                {
                    outputMessage += "let answer" + answerCounter + " = getInput(";
                    gettingInput = true;
                    answerCounter++;
                    answerSet = true;
                }
                ParsedLine tempLine = parsedInput.get(currentLineIndex + 1);
                if (tempLine.lineComponents.size() >= 1 && ! answerSet)
                {
                    if (tempLine.thisRegex.equals(ParsedLine.lineRegex.RESPONSE) || (tempLine.lineComponents.get(0) instanceof AtCommand && 
                            (((AtCommand)tempLine.lineComponents.get(0)).commandName.toLowerCase().contains("responseyes") || ((AtCommand)tempLine.lineComponents.get(0)).commandName.toLowerCase().contains("responseno"))))
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
                    if (!answerSet)
                    {
                        outputMessage += "CMessage(";
                    }
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
            if (counter != 0 && !(comp instanceof FollowUp))
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
            else if (comp instanceof FollowUp)
            {
                outputMessage += ");";
                pushLine(outputMessage);
                pushLine("if (randomInteger(0, 100) <= " + ((FollowUp)comp).percent + ")");
                pushScoping("if:followup");
                outputMessage = "CMessage(\"" + ((FollowUp)comp).followUp + "\");";
                pushLine(outputMessage);
                popScoping();
                return;
            }
            counter++;
        }
        if (indexInCurrentLine > 0 && thisParsedLine.lineComponents.get(indexInCurrentLine - 1) instanceof AtCommand)
        {
            AtCommand command = (AtCommand) thisParsedLine.lineComponents.get(indexInCurrentLine - 1);
            if (command.commandName.toLowerCase().equals("contact1"))
            {
                outputMessage += ", -1, 2);";
            }
            else if (command.commandName.toLowerCase().equals("contact2"))
            {
                outputMessage += ", -1, 3);";
            }
            else if (command.commandName.toLowerCase().equals("contact3"))
            {
                outputMessage += ", -1, 4);";
            }
            else 
            {
                outputMessage += ");";
            }
        }
        else
        {
            outputMessage += ");";
        }
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
                    int temporaryLineCounter = indexInCurrentLine;
                    pushRestOfLine();
                    indexInCurrentLine = temporaryLineCounter;
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
            //This could probably be cleaner but it works. Handles the rare case that @differentanswer is the second command because systemmessage is first
            if (parsedInput.get(tempIndex).lineComponents.size() > 1 && parsedInput.get(tempIndex).lineComponents.size() > 1 && parsedInput.get(tempIndex).lineComponents.get(1) instanceof AtCommand)
            {
                if (((AtCommand)parsedInput.get(tempIndex).lineComponents.get(1)).commandName.equalsIgnoreCase("differentanswer"))
                {
                    ParsedLine differentAnswerLine = parsedInput.get(tempIndex);
                    int temporaryLineCounter = indexInCurrentLine;
                    pushRestOfLine();
                    indexInCurrentLine = temporaryLineCounter;
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
    
    //used to push the rest of the line so this part can go at the end.
    private void pushRestOfLine()
    {
        if (indexInCurrentLine == thisParsedLine.lineComponents.size() - 1)
        {
            return;
        }
        breakCurrentLine = true;
        indexInCurrentLine++;
        for (int i = indexInCurrentLine; i < thisParsedLine.lineComponents.size(); i++)
        {
            pushOutput(thisParsedLine.lineComponents.get(i));
            indexInCurrentLine++;
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
    
    private void pushModify(ModifyCommand modifyCommand)
    {
        String commandName = modifyCommand.commandName;
        String toOutput = "setVar(\"" + modifyCommand.toChange + "\", ";
        for (int i = 0; i < modifyCommand.argumentsList.size(); i++)
        {
            if (StringHelper.isInteger(modifyCommand.argumentsList.get(i).trim()))
            {
                toOutput += modifyCommand.argumentsList.get(i).trim();
            }
            else
            {
                toOutput += "getVar(\"" + modifyCommand.argumentsList.get(i).trim() + "\", 0)";
            }
            if (modifyCommand.operatorsList.size() > i)
            {
                toOutput += " " + modifyCommand.operatorsList.get(i) + " ";
            }
        }
        toOutput += ");";
        pushLine(toOutput);
    }
    
    private void pushIfStatement(IfStatement ifStatement)
    {
        String toOutput = "If (";
        if (!StringHelper.isInteger(ifStatement.condition1))
        {
            toOutput += "getVar(\"" + ifStatement.condition1 + "\", 0)";
        }
        else
        {
            toOutput += ifStatement.condition1;
        }
        toOutput += " " + ifStatement.comparator + " ";
        if (!StringHelper.isInteger(ifStatement.condition2))
        {
            toOutput += "getVar(\"" + ifStatement.condition2 + "\", 0)";
        }
        else
        {
            toOutput += ifStatement.condition2;
        }
        toOutput += ")";
        pushLine(toOutput);
        pushScoping("ifstatement");
        pushCommand(new AtCommand("@goto2(" + ifStatement.gotoMethod + ")"));
        popScoping();
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
    
    private void pushToCurrent(String toPush)
    {
        if (outputJavascript.size() >= 1)
        {
            outputJavascript.set(outputJavascript.size() - 1, outputJavascript.get(outputJavascript.size() - 1) + toPush);
        }
        else
        {
            throw new IllegalStateException("Output javascript is empty!!");
        }
    }
    
    /*private String processArgument(String argument)
    {
        
    }*/
    
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

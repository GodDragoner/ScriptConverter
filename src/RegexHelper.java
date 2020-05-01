import java.util.regex.Pattern;

//This class is where all regexes are stored regarding the format of the parsing
//for TAI
public class RegexHelper
{
    //This top block is outdated 
    
    public static final String messageChar = "\\w|\\.|,|-|<|>|#|'|\\/|\\s|\\/|!|\\\\|\\.|\\?|(:\\))|(:\\()";
    public static final String atGotoFunction = "@(\\w+\\d*)(\\((((" + messageChar + "|_|\\*)+)\\s*,?)+\\))\\s*";
    public static final String atBracesFunction = "@(\\w+\\d*)(\\[(\\w|\\s|'|_|\\d|\\/|\\\\|\\.)+\\])\\s*";
    public static final String atFunction = "@(\\w+\\d*)";
    public static final String atFunctionWithParams = "\\s*@(\\w+\\d*)\\s+([" + RegexHelper.messageChar + "]+)\\s*";
    public static final String atChangeVarFunction = "@ChangeVar(\\[((\\w|\\d|\\s|'|_|\\/)+)\\])\\s*=(\\[((\\w|\\d|\\s|'|_\\/)+)\\])\\s*(([+|-|\\*|\\/])\\s*(\\[((\\w|\\d|\\s|'|_\\/)+)\\]))*";
    public static final String atIfFunction = "@(\\w+\\d*)((\\[(\\w|\\d|\\s|'|_|\\/)+\\])\\s*=(\\[((\\w|\\d|'|_\\/)+)\\])\\s*(Then)(\\(((\\w|\\d|\\s|'|_\\/)+)\\)))";
    public static final String responseChar = "[\\w|\\d|']";
    public static final String atSetFunction = "@SetVar(\\[(\\w|\\d|\\s|'|_|\\/)+\\])\\s*=(\\[((\\w|\\d|\\s|'|_\\/)+)\\])\\s*";
    public static final String ifAtFunction = atFunction + "\\s*" + "(\\s*(" + RegexHelper.messageChar + ")+\\s*)" + "\\s*([" + RegexHelper.atGotoFunction + "|" + RegexHelper.atFunctionWithParams + "|" + 
            RegexHelper.atSetFunction + "|" + RegexHelper.atBracesFunction + "|" + RegexHelper.atFunction + "|" + RegexHelper.atIfFunction + "|" + RegexHelper.atChangeVarFunction + "])+\\s*";
    public static final String allAtCommands = "\\s*([" + RegexHelper.atGotoFunction + "|" + RegexHelper.atFunctionWithParams + "|" + RegexHelper.atSetFunction + "|" + RegexHelper.atBracesFunction + "|" + RegexHelper.atFunction + "|" + RegexHelper.atIfFunction + "|" + RegexHelper.atChangeVarFunction + "])+\\s*";
    public static final String hashCommand = "#()";
    
    
    public static final String punctuationInsideWord = "('|\\(|\\)|\\/|\\*|\\.\\.\\.|-)";
    public static final String letter = "\\w";
    public static final String word = "((" + letter + "|" + punctuationInsideWord + ")+|-|\\.\\.\\.)";
    public static final String argumentWord = "((" + letter + "|('|\\/|\\*|\\.\\.\\.|-))+|-|\\.\\.\\.)";
    public static final String commandText = letter + "(" + letter + "|" + punctuationInsideWord + ")*";
    public static final String punctuation = "\\.||\\,|\\!|\\?|:|%|,,|'|(\\.)\\.+";
    public static final String lessInclusivePunctuation = "((,,|\\.|\\!|:|\\?|'|(\\.)\\.+|%)*)";
    public static final String vocab = "(#(" + letter + "+))";
    public static final String emoji = ":\\)|:\\(|:D|;\\)";
    public static final String path = "((\\w)+(\\\\|/))+((\\w+\\s?)+|\\w*\\*)(\\.(\\w)+)?";
    public static final String hashFunction = "(#\\w+(\\(|\\[)\\s?\\w+(\\s?,\\s?\\w+\\s?)*(\\)|\\]))";
    //public static final String variable = "#Var\\[\\s*\\w+\\s*\\]";
    public static final String complexWord = "(" + hashFunction + "|" + argumentWord + "|" + vocab + ")";
    
    public static final String argument = "(((" + complexWord + "\\s?" + lessInclusivePunctuation + ")((\\s|-|\\.\\.\\.)(" + complexWord + "\\s?" + lessInclusivePunctuation + "))*)|" + path + ")";
    public static final String operator = "(\\+|-|\\/|\\*)";
    public static final String comparator = "(<=|>=|=|<|>)";
    public static final String formatter = "<(\\s?" + word + "|" + operator + "\\s?)+>";


    public static final String noComment = "[^\\\\\\/]";

    public static final String simplePhrase = "((\"|\')?(" + word + "|" + vocab + "|" + emoji + ")(\"|\\.+|\')?(" + punctuation + ")*(\\s+|$))+";
    public static final String randomText = "(@RT(\\(|\\[)(" + argument + "\\s*(,\\s*" + argument + "\\s?)*)(\\)|\\]))";  
    public static final String followUp = "@FollowUp(\\d\\d)?\\(\\s*" + argument + "\\s*\\)";
    public static final String phrase = "((\"|\')?(" + word + "|" + vocab + "|" + emoji + "|" + randomText + "|" + formatter + "|" + followUp + "|" + hashFunction + ")(" + punctuation + ")*(\"|\\.+|\')?(\\s)?(" + punctuation + ")*(\\s+|$|\\.+|-))+";
    public static final String atCommandSimple = "(?!@RT)(?!@If)(@(" + argumentWord + "))";
    public static final String atCommandArgs = "(?!@RT\\()(?!@If(\\(|\\[))\\s*(" + atCommandSimple + "(\\(|\\[)(\\s?" + argument + "\\s?(,\\s?" + argument + "\\s?)*)(\\)|\\]))";
    public static final String atCommandModify = "(?!@RT\\()(" + atCommandArgs + "\\s?=\\s?\\["+ argument + "\\]\\s?(" + operator + "\\s?\\[" + argument + "\\])*)";
    public static final String ifFunction = "(@If\\[" + argument + "\\]\\s?" + comparator + "\\s?\\[" + argument + "\\]\\s?Then\\(" + argument + "\\))";
    public static final String anyAtCommand = "((" + ifFunction + ")|(" + atCommandModify + ")|(" + atCommandArgs + ")|(" + atCommandSimple + "))";
    //public static final String commandsLine = "((" + anyAtCommand + ")\\s*)+";

    public static final String commandsLine = "((" + anyAtCommand + ")\\s*)+";

    public static final String sendMessage = "((" + phrase + ")((\\s?" + noComment + " (" + commandsLine + ")\\s?(" + phrase + ")?)*))";
    public static final String basicSendMessage = "^[^\\[(@](?!.*@(?!RT)(?!FollowUp))[^\\[(].*";
    public static final String methodStart = "(\\(((" + word + "\\s*)+)\\)\\s*(" + commandsLine + ")?)";
    public static final String messageAfterCommand = "((" + commandsLine + ")\\s?" + noComment + "(" + sendMessage + "))";
    //public static final String response = "(\\[((" + word + "|" + vocab + ")\\s*)+(,\\s*((" + word + "|" + vocab + ")\\s*)+)*\\])\\s*(" + sendMessage + "|" + commandsLine + "|" + messageAfterCommand + ")?";


    public static final String response = "^(\\s*\\[.*\\])(.*)";

    //Patterns
    public static final Pattern BASIC_MESSAGE_PATTERN = Pattern.compile(basicSendMessage);
    public static final Pattern SEND_MESSAGE_PATTERN = Pattern.compile(sendMessage);
    public static final Pattern COMMAND_LINE_PATTERN = Pattern.compile(commandsLine);
    public static final Pattern METHOD_START_PATTERN = Pattern.compile(methodStart);
    public static final Pattern MESSAGE_AFTER_COMMAND_PATTERN = Pattern.compile(messageAfterCommand);
    public static final Pattern RESPONSE_PATTERN = Pattern.compile(response);
}   

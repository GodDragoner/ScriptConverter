
public class RegexHelper
{
    public static final String messageChar = "\\w|\\.|,|-|<|>|#|'|\\/|\\s|\\/|!|\\\\|\\.|\\?|(:\\))|(:\\()";
    public static final String atGotoFunction = "@(\\w+\\d*)(\\((((" + messageChar + "|_)+)\\s*,?)+\\))\\s*";
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
}

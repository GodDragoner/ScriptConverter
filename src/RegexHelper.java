
public class RegexHelper
{
    public static final String messageChar = "\\w|\\.|,|<|>|#|'|\\/|\\s|\\?|:\\)|:\\(";
    
    public static final String atGotoFunction = "@(\\w+\\d*)(\\((\\w|\\s|')+\\))\\s*";
    public static final String atFunction = "@(\\w+\\d*)";
    public static final String atFunctionWithParams = "\\s*@(\\w+\\d*)\\s*([" + RegexHelper.messageChar + "]+)\\s*";
    public static final String responseChar = "[\\w|\\d|']";
}

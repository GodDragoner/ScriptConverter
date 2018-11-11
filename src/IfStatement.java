import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IfStatement extends LineComponent
{
    public String condition1;
    public String comparator;
    public String condition2;
    public String gotoMethod;
    public IfStatement(String content)
    {
        super(content);
        // TODO Auto-generated constructor stub
        System.out.println("If start:" + content);
        String arguments = "(\\(|\\[)" + RegexHelper.argument + "(\\)|\\])";
        Matcher argumentsMatcher = Pattern.compile(arguments).matcher(content);
        int counter = 0;
        while (argumentsMatcher.find())
        {
            if (counter == 0)
            {
                condition1 = StringHelper.removeChars(argumentsMatcher.group().trim(), "(",")","[","]");
            }
            else if (counter == 1)
            {
                condition2 = StringHelper.removeChars(argumentsMatcher.group().trim(), "(",")","[","]");
            }
            else 
            {
                gotoMethod = StringHelper.removeChars(argumentsMatcher.group().trim(), "(",")","[","]");
            }
            counter++;
        }
        Matcher comparator = Pattern.compile(RegexHelper.comparator).matcher(content);
        if (comparator.find())
        {
            this.comparator = comparator.group();
        }
        else
        {
            throw new IllegalArgumentException("Comparator not found in If!!");
        }
        System.out.println("Condition1:" + condition1);
        System.out.println("Comparator:" + this.comparator);
        System.out.println("Condition2:" + condition2);
        System.out.println("GoToMethod:" + gotoMethod);
    }
    
    @Override
    public String toString()
    {
        return "IfFunction: If(" + condition1 + comparator + condition2 +"){Goto(" + gotoMethod +")}";
    }

}

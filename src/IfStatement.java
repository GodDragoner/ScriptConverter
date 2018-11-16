import java.util.regex.Matcher;
import java.util.regex.Pattern;


//Represents an if statement in TAI. 
//Condition1, condition2, and goto method are as follows
//If(condition1 comparator condition2) goto(gotomethod)
//Where condition1, comparator, condition2, and gotomethod could be as follows
//If(x = 2) goto(AV_test)
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
        //System.out.println("If start:" + content);
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
    }
    
    @Override
    public String toString()
    {
        return "IfFunction: If(" + condition1 + comparator + condition2 +"){Goto(" + gotoMethod +")}";
    }

}

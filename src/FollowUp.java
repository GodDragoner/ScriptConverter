import java.util.regex.Matcher;
import java.util.regex.Pattern;


//Represents a followup command in TAI, one of the 2
//@commands (RT and FollowUp) that are allowed inside a message
public class FollowUp extends LineComponent
{
    public String followUp;
    public int percent;
    public FollowUp(String content)
    {
        super(content);
        // TODO Auto-generated constructor stub
        String percent = "(?<=(@FollowUp))(\\d\\d)(?=(\\())";
        Matcher percentMatcher = Pattern.compile(percent).matcher(content);
        
        String parameter = "(?<=(\\())\\s?" + RegexHelper.argument + "\\s?(?=(\\)))";
        Matcher parameterMatcher = Pattern.compile(parameter).matcher(content);
        if (percentMatcher.find())
        {
            this.percent = Integer.parseInt(percentMatcher.group());
        }
        else
        {
            this.percent = -1;
        }
        if (parameterMatcher.find())
        {
            followUp = parameterMatcher.group();
        }
    }

    @Override
    public String toString()
    {
        return "FollowUp:" + content;
    }
}

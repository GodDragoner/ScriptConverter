import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Message extends LineComponent
{
    public ArrayList<LineComponent> messageComponents;
    public Message(String message)
    {
        super(message);
        messageComponents = new ArrayList<LineComponent>();
        String phraseComponent = "(" + RegexHelper.simplePhrase + "|" + RegexHelper.randomText + "|" + RegexHelper.formatter + "|" + RegexHelper.variable + "|" + RegexHelper.followUp + "|" + RegexHelper.hashFunction + ")";
        Matcher phraseComponentMatcher = Pattern.compile(phraseComponent).matcher(message);
        while (phraseComponentMatcher.find())
        {
            if (phraseComponentMatcher.group().matches(RegexHelper.simplePhrase))
            {
                messageComponents.add(new Phrase(phraseComponentMatcher.group()));
            }
            else if (phraseComponentMatcher.group().matches(RegexHelper.randomText))
            {
                messageComponents.add(new RandomText(phraseComponentMatcher.group()));
            }
            else if (phraseComponentMatcher.group().matches(RegexHelper.formatter))
            {
                messageComponents.add(new Formatter(phraseComponentMatcher.group()));
            }
            else if (phraseComponentMatcher.group().matches(RegexHelper.variable))
            {
                messageComponents.add(new Variable(phraseComponentMatcher.group()));
            }
            else if (phraseComponentMatcher.group().matches(RegexHelper.followUp))
            {
                messageComponents.add(new FollowUp(phraseComponentMatcher.group()));
            }
            else if (phraseComponentMatcher.group().matches(RegexHelper.hashFunction))
            {
                messageComponents.add(new HashFunction(phraseComponentMatcher.group()));
            }
        }
    }
    public String toString()
    {
        String toReturn = "Message";
        for(LineComponent comp: messageComponents)
        {
            toReturn += "\n" + comp.toString();
        }
        return toReturn;
        //return "Message:" + content;
    }
}

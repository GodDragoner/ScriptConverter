import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RandomText extends LineComponent
{
    
    public ArrayList<Phrase> phrases;
    public RandomText(String content)
    {
        super(content);
        // TODO Auto-generated constructor stub
        phrases = new ArrayList<Phrase>();
        Matcher rtMatcher = Pattern.compile(RegexHelper.randomText).matcher(content);
        if (!rtMatcher.matches())
        {
            throw new IllegalArgumentException("String does not match randomText regex");
        }
        else
        {
            String argumentRegex = "((\\(|\\[)|\\s?)(\\s?" + RegexHelper.argument + "\\s?)((\\)|\\])|\\s?,)";
            Matcher argumentMatcher = Pattern.compile(argumentRegex).matcher(content);
            while (argumentMatcher.find())
            {
                //System.out.println("Argument:" + argumentMatcher.group());
                phrases.add(new Phrase(argumentMatcher.group().trim()));
            }
        }
    }

    @Override
    public String toString()
    {
        return "RandomText:" + content;
    }
}

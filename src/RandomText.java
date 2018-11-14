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
                String tempString = StringHelper.removeChars(argumentMatcher.group().trim(), "(", ")");
                tempString = tempString.replaceAll(",,", "-2232-");
                tempString = tempString.replaceAll(",", "");
                tempString = tempString.replaceAll("-2232-", ",");
                phrases.add(new Phrase(tempString));
            }
        }
    }

    @Override
    public String toString()
    {
        String toReturn = "";
        for (Phrase phrase: phrases)
        {
            toReturn += "<" + phrase.toString() + ">";
        }
        return "RandomText:" + toReturn;
    }
}

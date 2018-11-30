import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Represents the @RT command from TAI. 
//Has an arraylist of phrases representing the possible
//output values
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
        String toReturn = "RandomText(";
        for (int i = 0; i < phrases.size(); i++)
        {
            toReturn += "\"" + phrases.get(i).toString() + "\"";
            if (phrases.size() > (i + 1))
            {
                toReturn += ", ";
            }
        }
        toReturn += ")";
        return toReturn;
    }
}

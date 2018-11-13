import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Response extends LineComponent
{
    public ArrayList<String> responses;
    public Response(String content)
    {
        super(content);
        // TODO Auto-generated constructor stub
        responses = new ArrayList<String>();
        
        String argumentRegex = "((\\(|\\[)|\\s?)(\\s?(" + RegexHelper.word + "|" + RegexHelper.vocab + ")\\s?)((\\)|\\])|\\s?,)";
        
        Matcher argumentMatcher = Pattern.compile(argumentRegex).matcher(content);
        while (argumentMatcher.find())
        {
            System.out.println("Argument:" + argumentMatcher.group(4).trim());
            responses.add(argumentMatcher.group(4).trim());
        }
        
    }
    public String toString()
    {
        String toReturn = "";
        for (String response:responses)
        {
            toReturn += "<" + response + ">";
        }
        return "Response:" + toReturn;
    }

}

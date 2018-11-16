import java.util.regex.Matcher;
import java.util.regex.Pattern;

//A phrase represents plain text that will be output to the user. It may contain
//vocabularies and thus, these are converted to the TAJ vocabulary system.
public class Phrase extends LineComponent
{
    public String message;
    public Phrase(String message)
    {
        super(message);
        // TODO Auto-generated constructor stub
        Matcher vocabMatcher = Pattern.compile(RegexHelper.vocab).matcher(content);
        while (vocabMatcher.find())
        {
            String updatedVocab = "%" + vocabMatcher.group(2) + "%";
            message = message.replaceFirst("#" + vocabMatcher.group(2), updatedVocab);
        }
        message = message.replaceAll("\"", "\\\\\"");
        this.message = message;
    }
    
    public String toString()
    {
        return "Phrase:" + message;
    }
}

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        this.message = message;
    }
    
    public String toString()
    {
        return "Phrase:" + message;
    }
}

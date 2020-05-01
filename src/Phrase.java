import java.util.regex.Matcher;
import java.util.regex.Pattern;

//A phrase represents plain text that will be output to the user. It may contain
//vocabularies and thus, these are converted to the TAJ vocabulary system.
public class Phrase extends LineComponent {
    public String message;

    public Phrase(String message) {
        super(message);

        Matcher vocabMatcher = Pattern.compile(RegexHelper.vocab).matcher(content);

        while (vocabMatcher.find()) {
            String vocab = vocabMatcher.group(2);


            String updatedVocab;
            //Stupid spicy vocab
            if(vocab.equalsIgnoreCase("DT")) {
                updatedVocab = "";
            } else {
                updatedVocab = "%" + vocab + "%";
            }

            message = message.replaceFirst("#" + vocab, updatedVocab);
        }

        message = message.replaceAll("\'", "\\\\\'");
        message = message.replaceAll("\"", "\\\\\"");
        this.message = message;
    }

    public String toString() {
        return message;
    }
}

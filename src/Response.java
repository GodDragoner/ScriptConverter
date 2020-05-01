import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Response extends LineComponent {
    public ArrayList<String> responses;

    public Response(String content) {
        super(content);

        responses = new ArrayList<>();

        String argumentRegex = "([^\\[,\\]]*)";

        Matcher argumentMatcher = Pattern.compile(argumentRegex).matcher(content);

        while (argumentMatcher.find()) {
            String argument = argumentMatcher.group(1).trim();

            if (argument.length() > 0) {
                responses.add(argumentMatcher.group(1).trim());
            }
        }
    }

    public String toString() {
        String toReturn = "";
        for (String response : responses) {
            toReturn += "<" + response + ">";
        }
        return "Response:" + toReturn;
    }

}

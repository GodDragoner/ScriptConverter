import java.io.*;
import java.util.ArrayList;
import java.util.regex.Matcher;

//The guts of all of the parsing happens here
public class ScriptAnalyzer {

    //Gets called from the driver to parse all of the input from the file and put it into an arraylist of parsed lines
    public StringBuffer analyze(File input, File output) throws FileNotFoundException {
        ArrayList<ParsedLine> parsedInput = new ArrayList<ParsedLine>();
        if (!input.exists()) {
            throw new FileNotFoundException();
        }

        try {
            FileReader fileReader = new FileReader(input);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                System.out.println("Line: " + line);

                if (line.contains("RandomText")) {
                    line = line.replaceAll("RandomText", "RT");
                }

                Matcher sendMessageMatcher = RegexHelper.SEND_MESSAGE_PATTERN.matcher(line);
                Matcher commandsMatcher = RegexHelper.COMMAND_LINE_PATTERN.matcher(line);
                Matcher methodMatcher = RegexHelper.METHOD_START_PATTERN.matcher(line);
                Matcher messageAfterCommandMatcher = RegexHelper.MESSAGE_AFTER_COMMAND_PATTERN.matcher(line);
                Matcher responseMatcher = RegexHelper.RESPONSE_PATTERN.matcher(line);

                if (methodMatcher.matches()) {
                    System.out.println("Method Start in analyzer: " + line);
                    parsedInput.add(new ParsedLine(line, ParsedLine.lineRegex.METHOD));
                } else if (sendMessageMatcher.matches()) {
                    System.out.println("Send Message in analyzer: " + line);
                    parsedInput.add(new ParsedLine(line, ParsedLine.lineRegex.SENDMESSAGE));
                } else if (!line.contains("@RT") && commandsMatcher.matches()) {
                    System.out.println("Commands line line in analyzer: " + line);
                    parsedInput.add(new ParsedLine(line, ParsedLine.lineRegex.COMMANDS));
                } else if (messageAfterCommandMatcher.matches()) {
                    System.out.println("messageAfterCommandMatcher in analyzer: " + line);
                    parsedInput.add(new ParsedLine(line, ParsedLine.lineRegex.MESSAGEAFTERCOMMAND));
                } else if (responseMatcher.matches()) {
                    System.out.println("Response line in analyzer: " + line);
                    parsedInput.add(new ParsedLine(line, ParsedLine.lineRegex.RESPONSE));
                } else {
                    if (!line.trim().equals("")) {
                        System.out.println("Uninterpreted line in analyzer: " + line);
                        parsedInput.add(new ParsedLine(line, ParsedLine.lineRegex.UNINTERPRETED));
                    } else {
                        System.out.println("Blank line in analyzer: " + line);
                        parsedInput.add(new ParsedLine(line, ParsedLine.lineRegex.BLANK));
                    }
                }
            }

            fileReader.close();
            OutputGenerator generator = new OutputGenerator(parsedInput);
            generator.generateOutput();
            return generator.getOutputBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Not currently returning anything to write to the file
        //Will be implemented later
        return null;
    }
}

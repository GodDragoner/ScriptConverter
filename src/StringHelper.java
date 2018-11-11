
public class StringHelper
{
    public static String removeChars(String removeFrom, String ... toRemove)
    {
        String toReturn = removeFrom;
        for (int i = 0; i < toRemove.length; i++)
        {
            toReturn = toReturn.replace(toRemove[i], "");
        }
        return toReturn.trim();
    }
}

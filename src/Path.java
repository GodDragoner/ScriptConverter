
public class Path extends LineComponent
{
    public String path;
    public Path(String content)
    {
        super(content);
        content = content.replaceAll("\\\\", "\" + java.io.File.separator + \"");
        content = content.replaceAll("/", "\" + java.io.File.separator + \"");
        content = content.replaceAll(".txt", ".js");
        path = content;
    }
    
    @Override
    public String toString()
    {
        return path;
    }
}

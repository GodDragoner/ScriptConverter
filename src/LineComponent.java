
public abstract class LineComponent
{
    public String content;
    public LineComponent(String content)
    {
        this.content = content;
    }
    @Override
    public String toString()
    {
        return content;
    }
}

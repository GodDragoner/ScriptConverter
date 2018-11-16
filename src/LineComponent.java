
//A Line component is the abstract base class for all line components
//A ParsedLine consists of a series of these line components that represent
//Individual tokens from TAI
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

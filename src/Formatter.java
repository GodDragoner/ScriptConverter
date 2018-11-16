
//Represents a formatter in TAI <b></b> would register as 2
//separate formatters. This is not yet implemented for TAJ
//With chatutils
public class Formatter extends LineComponent
{

    public Formatter(String content)
    {
        super(content);
        // TODO Auto-generated constructor stub
    }

    @Override
    public String toString()
    {
        return "Formatter:" + content;
    }
}


//Represents a followup command in TAI, one of the 2
//@commands (RT and FollowUp) that are allowed inside a message
public class FollowUp extends LineComponent
{

    public FollowUp(String content)
    {
        super(content);
        // TODO Auto-generated constructor stub
    }

    @Override
    public String toString()
    {
        return "FollowUp:" + content;
    }
}

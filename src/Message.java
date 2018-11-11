
public class Message extends LineComponent
{
    public Message(String message)
    {
        super(message);
        
    }
    public String toString()
    {
        return "Message:" + content;
    }
}

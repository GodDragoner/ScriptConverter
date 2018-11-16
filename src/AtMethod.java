
//Represents the beginning of a new method or the location
//for a goto statement in TAI
public class AtMethod extends LineComponent
{
    public String methodName;
    public AtMethod(String content)
    {
        super(content);
        // TODO Auto-generated constructor stub
        this.methodName = content.trim().replaceAll(" ", "_");
    }
    
    public String toString()
    {
        return "Method:" + content;
    }
}

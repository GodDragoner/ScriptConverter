
public class AtMethod extends LineComponent
{
    public String methodName;
    public AtMethod(String content)
    {
        super(content);
        // TODO Auto-generated constructor stub
        this.methodName = content;
    }
    
    public String toString()
    {
        return "Method:" + content;
    }
}

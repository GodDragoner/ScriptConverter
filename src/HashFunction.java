
//A hash function is a type of function in TAI that uses a # to
//start the function instead of an @. These are also typically
//allowed inside of messages Ex: #random(1, 7)
public class HashFunction extends LineComponent
{

    public HashFunction(String content)
    {
        super(content);
        // TODO Auto-generated constructor stub
    }
    
    @Override
    public String toString()
    {
        return "HashFunction:" + content;
    }
}

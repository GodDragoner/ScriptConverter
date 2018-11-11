public class FunctionScope extends CodeScope
{
    
    public FunctionScope(String name)
    {
        super();
        addOutput("function " + name + "()", name);
        addOutput("{", "{");
        tabbing = "    ";
    }
    @Override
    public void addEnd()
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void addMethodCall(String methodName, boolean endScope)
    {
        methodName = methodName.trim();
        methodName = methodName.replaceAll(" ", "");
        //System.out.println("method " + methodName);
        addOutput(tabbing + methodName + "();", methodName);
        addOutput(tabbing + "return;", "return;");
        if (endScope)
        endScope();
    }
    
    @Override
    public void endScope()
    {
        addOutput("}", "}");
        isOpen = false;
    }
    @Override
    public void addCall(String methodName, boolean endScope)
    {
        methodName = methodName.trim();
        //System.out.println("method " + methodName);
        addOutput(tabbing + "run(\"" + methodName + "\");", methodName);
        addOutput(tabbing + "return;", "return;");
        if (endScope)
        endScope();
    }
    

}

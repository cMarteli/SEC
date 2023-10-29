package edu.curtin.myapp;

public class ApiImpl implements AppPluginApi
{
    private MyApp obj;
    public ApiImpl(MyApp obj)
    {
        this.obj = obj;
    }
    
    @Override
    public String getInfo()
    {
        return obj.getInfo();
    }
}

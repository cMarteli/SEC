package edu.curtin.myplugins.pluginA;

//importing core app
import edu.curtin.myapp.*;

public class PluginA implements AppPlugin
{
    @Override
    public void startPlugin(AppPluginApi api) 
    {
        System.out.println(api.getInfo());    
    }
}

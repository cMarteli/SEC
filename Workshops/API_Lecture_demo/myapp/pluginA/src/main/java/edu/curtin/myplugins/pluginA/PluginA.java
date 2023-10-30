package edu.curtin.myplugins.pluginA;

//importing core app
import edu.curtin.myapp.*;

public class PluginA implements AppPlugin
{
    @Override
    public void startPlugin(AppPluginApi api) 
    {
		System.out.println("This is the PluginA API doing this!!!");
        System.out.println(api.getInfo());    
    }
}

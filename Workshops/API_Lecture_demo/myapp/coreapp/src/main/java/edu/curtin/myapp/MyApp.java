package edu.curtin.myapp;

import java.util.*;

public class MyApp
{
    public static void main(String[] args)
    {
        new MyApp().run(args);    
    }
    
    private String info = "";
    public String getInfo() { return info; }
    
    public void run(String[] args) 
    {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter some info: ");
        info = sc.nextLine();
        
        var plugins = new ArrayList<AppPlugin>();
        for(String arg : args)
        {
            try
            {
                Class<?> pluginClass = Class.forName(arg);
                plugins.add((AppPlugin) pluginClass.getConstructor().newInstance());
            }
            catch(ReflectiveOperationException | ClassCastException e)
            {
                System.out.println("Error!" + e.getClass().getName() + " :" + e.getMessage());
            }        
        }
        
        ApiImpl apiImpl = new ApiImpl(this);
        
        for(AppPlugin plugin : plugins)
        {
            plugin.startPlugin(apiImpl);
        }
    }
    
}

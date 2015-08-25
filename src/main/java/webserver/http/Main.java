package webserver.http;

/*
 * This class instantiates the server. It extracts the port number from the config file and starts the server on that port
 */

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;

import javax.swing.ImageIcon;

import Utilities.ConfigManager;



public class Main {
	
	
	static Server server;
	
	public static void main(String[] args) 
	{
		if(SystemTray.isSupported())
		{
			final TrayIcon trayIcon = new TrayIcon(new ImageIcon(System.getProperty("user.dir")+File.separator + "adobe.png").getImage());
			final SystemTray systemTray = SystemTray.getSystemTray();
			final PopupMenu popUp = new PopupMenu();
			MenuItem stopServer = new MenuItem("Stop Server");
			popUp.add(stopServer);
			trayIcon.setPopupMenu(popUp);
			try {
				systemTray.add(trayIcon);
				trayIcon.displayMessage("Started", "Server started, Right Click Me To Stop The Server", MessageType.INFO);
			} catch (AWTException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			stopServer.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					stop();
				}
			});
			
		}
		ConfigManager configManager = new ConfigManager();
		int portNumber = configManager.getPortNumber();
		server = new Server(portNumber);
		while(server.startListening()==-1)
		{
			portNumber++;
			server = new Server(portNumber);
		}
		
		configManager.setLastUsedPortNumber(portNumber);
		
		
	}
	
	static void stop()
	{
		server.stop();
	}
	static Image createImage(String path, String description) {
        URL imageURL = Main.class.getResource(path);
         
        if (imageURL == null) {
            System.err.println("Resource not found: " + path);
            return null;
        } else {
            return (new ImageIcon(imageURL, description)).getImage();
        }
    }
	

}

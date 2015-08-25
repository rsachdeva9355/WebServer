package Utilities;

/*
 * A class to manage all the file operations. Creating, deleting, viewing files.
 */

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

public class FileManager {
	public static void createLogFile(String date)
	{
		try {
			String path = "Logs"+File.separator+(Calendar.getInstance().get(Calendar.MONTH)+1);
			File folder = new File(path);
			folder.mkdirs();
			File logFile = new File(path+File.separator+Calendar.getInstance().get(Calendar.DAY_OF_MONTH)+".txt");
			logFile.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.print("Logging error");
		}
	}
	
	public File getFile(String fileName)
	{
		File file;
		
		String dir = new ConfigManager().getDefaultDirectory();
		String cur = System.getProperty("user.dir");
		fileName = getCorrectFileName(fileName);
		file = new File(cur + File.separator + dir + File.separator + fileName);
		
			return file;
	}
	
	public File getErrorFile(String fileName)
	{
		File file;
		
		String dir = new ConfigManager().getErrorDirectory();
		String cur = System.getProperty("user.dir");
		fileName = getCorrectFileName(fileName);
		file = new File(cur + File.separator + dir + File.separator + fileName);
		return file;
	}
	
	public String getCorrectFileName(String file)
	{
		String parts[] = file.split("/");
		String result = "";
		for (int i=0;i<parts.length;i++) 
		{
			if(i>0 && (parts[i].equals("")|| parts[i].equals(null)))
			{
				continue;
			}
			if(i==parts.length-1)
				result+=parts[i];
			else
				result+=parts[i]+File.separator;
		}
		return result;
	}
}

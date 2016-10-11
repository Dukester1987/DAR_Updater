package dar_updater;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import javax.swing.JOptionPane;


public class DownloadFile {

    private static String ACTUAL_SW_VERSION;
    private static String CHANGELOG_PATH;
    private static String SERVER_ADDRESS;
    private static boolean runupdate = false;

    public static void main(String[] args) {
        for (String arg : args) {            
            if(arg.equals("-runupdate")){
                runupdate=true;
            }              
        }
        initproperties();
        readXMLFile file = new readXMLFile();
        downloadManager mngr = new downloadManager(SERVER_ADDRESS);
        try {
            if(mngr.download(new URL(SERVER_ADDRESS+"/UPDATE/changelog/version_manifest.xml"))){
               file.readFile("changelog/version_manifest.xml"); 
               if(!file.version.equals(ACTUAL_SW_VERSION)){
                   //start updating
                   int j = 0;
                   if(!runupdate){
                       j = JOptionPane.showConfirmDialog(null, "There is a new version of Software available\nDo you want to update to version "+file.version+" now?", "new version "+file.version+"",JOptionPane.YES_NO_OPTION);
                   }
                   if(j==0){ // yes update
                       mngr.download(new URL(SERVER_ADDRESS+"/UPDATE/changelog/changelog.txt"));
                       Gui gui = new Gui(file, mngr);
                       gui.setLocationRelativeTo(null);
                       gui.setVisible(true);
                   }
               } else {
                   System.exit(0);
               }               
            }
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        }
        


    }

    private static void initproperties() {
        FileInputStream input = null;
        try {
            input = new FileInputStream("./changelog/version_manifest.properties");        
            Properties props = new Properties();
            props.load(input);
            ACTUAL_SW_VERSION = props.getProperty("app.version");
            SERVER_ADDRESS = props.getProperty("app.address");
            CHANGELOG_PATH = props.getProperty("app.changelog");
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                input.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

}
 


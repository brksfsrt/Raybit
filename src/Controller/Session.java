package Controller;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.IOException;

/**
 * Created by brkfsrt on 28/11/16.
 */
public class Session {
    private static Session ourInstance = new Session();
    private boolean isSessionInitiated = false;

    private String userName;
    private String path;
    private FTPClient client;
    private boolean isRoot;

    public static Session getInstance() {
        return ourInstance;
    }

    private Session() {

    }
    public boolean isSessionExist(){
        return isSessionInitiated;
    }
    public void start(){
        isSessionInitiated = true;
        client.enterLocalPassiveMode();

        try {
            client.setFileType(FTP.BINARY_FILE_TYPE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void destroy(){
        isSessionInitiated = false;
        try {
            client.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void setUserName(String name){
        this.userName = name;
    }
    public void setPath(String path){
        this.path = path;
    }
    public void setClient(FTPClient client){
        this.client = client;
        client.setControlEncoding("UTF-8");
    }
    public void setIsRoot(Boolean root){this.isRoot = root;}
    public boolean getIsRoot(){return this.isRoot;}
    public FTPClient getClient(){
        return this.client;
    }
}

package Controller;

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
    }
    public FTPClient getClient(){
        return this.client;
    }
}

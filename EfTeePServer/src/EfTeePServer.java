import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.Authority;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.UserManager;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;
import org.apache.ftpserver.usermanager.SaltedPasswordEncryptor;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.apache.ftpserver.usermanager.impl.WritePermission;

import java.io.File;
import java.io.IOException;
import java.net.BindException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Abhinav on 11-05-2015.
 */
public class EfTeePServer {

    static FtpServerFactory serverFactory;
    static FtpServer ftpServer;
    static Scanner kbd;
    static int port = 2222;

    public EfTeePServer() {
    }

    public static void main(String[] args) {
        FtpServerFactory serverFactory = new FtpServerFactory();
        ListenerFactory factory = new ListenerFactory();

        PropertiesUserManagerFactory umf = new PropertiesUserManagerFactory();
        umf.setPasswordEncryptor(new SaltedPasswordEncryptor());
        UserManager um = umf.createUserManager();
        BaseUser user = new BaseUser();
        user.setName("abhinav");
        user.setPassword("kumar");
        user.setHomeDirectory("C:/Users/Abhinav/Code/Java/FTPFolder");
        List<Authority> authorities = new ArrayList<Authority>();
        authorities.add(new WritePermission());
        user.setAuthorities(authorities);
        try {
            um.save(user);
        } catch (FtpException e) {
            e.printStackTrace();
        }
        serverFactory.setUserManager(um);

        kbd = new Scanner(System.in);
        System.out.println("Enter the port where you want to start the FTP Server: ");
        port = kbd.nextInt();
        factory.setPort(port);
        serverFactory.addListener("default", factory.createListener());

        FtpServer server = serverFactory.createServer();
        try {
            server.start();
        } catch (FtpException e) {
            e.printStackTrace();
        }
    }
}

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPReply;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by Abhinav on 09-05-2015.
 */
public class ConnectionForm {
    private JTextField textField1;
    private JPanel panel1;
    private JTextField textField2;
    private JTextField textField3;
    private JButton connectButton;
    private JButton clearButton;
    private JLabel statuslabel;

    String serverAddress = "localhost";
    String username;
    String password;
    Boolean ftpError;
    Integer serverPort = 2222;

    FTPClient ftp;
    FTPClientConfig ftpconfig;

    ConnectionForm() {
        ftp = new FTPClient();
        ftpconfig = new FTPClientConfig();
        //ftpconfig.set
        ftp.configure(ftpconfig);

        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] server = textField1.getText().split(":");
                serverAddress = server[0];
                serverPort = Integer.parseInt(server[1]);
                username = textField2.getText();
                password = textField3.getText();
                int replyCode;

                try {
                    statuslabel.setText("Connecting to " + serverAddress);
                    ftp.connect(serverAddress,serverPort);
                    statuslabel.setText(ftp.getReplyString());
                    replyCode = ftp.getReplyCode();

                    if(!FTPReply.isPositiveCompletion(replyCode)) {
                        ftp.disconnect();
                        statuslabel.setText("Connection refused.");
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException ee) {
                            ee.printStackTrace();
                        }
                        System.exit(1);
                    }

                    // Connected so login
                    if(!ftp.login(username,password)){
                        statuslabel.setText("Login failed. Try again.");
                    } else {
                        String[] k = new String[2];
                        k[0] = serverAddress;
                        k[1] = username;
                        ControlForm.main(k,ftp);
                    }
                } catch(IOException ee) {
                    ftpError = true;
                    ee.printStackTrace();
                }
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("EfTeeP");
        frame.setContentPane(new ConnectionForm().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}

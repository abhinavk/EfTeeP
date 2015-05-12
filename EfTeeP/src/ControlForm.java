import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

/**
 * Created by Abhinav on 09-05-2015.
 */
public class ControlForm {
    private JButton uploadAFileButton;
    private JPanel panel1;
    private JButton downloadSelectedFileButton;
    private JButton deleteSelectedFileButton;
    private JList list1;
    private JButton refreshFileListButton;
    private JLabel status;
    private JLabel topstatus;

    FTPClient ftp;
    FTPFile[] files;

    public static void main(String[] args, FTPClient f) {
        JFrame frame = new JFrame("ControlForm");
        frame.setContentPane(new ControlForm(f,args).panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    ControlForm(FTPClient f, String[] args) {
        ftp = f;
        topstatus.setText("Connected to " + args[0] + " as " + args[1]);
        refreshFileListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    files = ftp.listFiles();
                    list1.setListData(files);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

            }
        });

        deleteSelectedFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!list1.isSelectionEmpty()) {
                    try {
                        ftp.deleteFile(files[list1.getSelectedIndex()].getName());
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    refreshFileListButton.doClick();
                } else {
                    status.setText("Please select a file first.");
                }
            }
        });

        downloadSelectedFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!list1.isSelectionEmpty()) {
                    try {
                        JFileChooser jFileChooser = new JFileChooser();
                        jFileChooser.showSaveDialog(panel1);
                        OutputStream outputStream = new FileOutputStream(jFileChooser.getSelectedFile());
                        ftp.retrieveFile(files[list1.getSelectedIndex()].getName(), outputStream);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    refreshFileListButton.doClick();
                } else {
                    status.setText("Please select a file first.");
                }
            }
        });

        uploadAFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jFileChooser = new JFileChooser();
                jFileChooser.showOpenDialog(panel1);
                try {
                    InputStream inputStream = new FileInputStream(jFileChooser.getSelectedFile());
                    try {
                        ftp.storeFile(jFileChooser.getSelectedFile().getName(),inputStream);
                    } catch (IOException e1) {
                        status.setText("Error in uploading.");
                        e1.printStackTrace();
                    }
                } catch (FileNotFoundException e1) {
                    status.setText("File not found.");
                    e1.printStackTrace();
                }
                refreshFileListButton.doClick();
            }
        });

        refreshFileListButton.doClick();
    }
}

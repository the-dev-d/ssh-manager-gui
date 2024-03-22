package sshmanager.utils;


import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JProgressBar;

import sshmanager.service.SSHDB;

public class ServerPing implements Runnable{

    private SSHDB sshdb;
    private DefaultListModel<SSHDB> listModel;
    private JProgressBar progressBar;

    public ServerPing(SSHDB sshdb, DefaultListModel<SSHDB> listModel, JProgressBar progressBar) {
        this.sshdb = sshdb;
        this.listModel = listModel;
        this.progressBar = progressBar;
    }
    
    public static Thread check(SSHDB sshdb, DefaultListModel<SSHDB> listModel, JProgressBar progressBar) throws Exception {
        ServerPing serverPing = new ServerPing(sshdb, listModel, progressBar);
        Thread thread = new Thread(serverPing);
        thread.start();
        return thread;
    }

    public void run() {
        Socket socket = null;
        try {
            InetSocketAddress sshdbAddress = new InetSocketAddress(sshdb.getIp(), sshdb.getPort());
            socket = new Socket();
            socket.connect(sshdbAddress, 3000);
            sshdb.setName(sshdb.getName() + " - active");
            listModel.addElement(sshdb);

        }catch(IOException e){
            sshdb.setName(sshdb.getName() + " - inactive");
            listModel.addElement(sshdb);
        }
        catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(socket != null)
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            progressBar.setValue(progressBar.getValue() + 1);
        }
    }
}

package sshmanager.frames;

import java.awt.EventQueue;
import java.awt.event.*;
import java.io.IOException;
import java.sql.SQLException;

import javax.swing.JFrame;
import java.awt.GridLayout;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JPanel;
import javax.swing.JList;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.List;
import java.awt.FlowLayout;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import sshmanager.service.SSHDatabase;
import sshmanager.utils.ServerPing;
import sshmanager.service.SSHDB;
import javax.swing.JProgressBar;

public class MainApp{

	private JFrame frame;
	private JTextField ipTextField;
	private JTextField usernameTextField;
	private JTextField nameTextField;
	private JTextField textField;
	private SSHDatabase sshDatabase;
	private DefaultListModel<SSHDB> model;
	private JProgressBar progressBar;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainApp window = new MainApp();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}


	public MainApp() throws Exception{
		
		sshDatabase = new SSHDatabase();
		
		initialize();
	}


	private void initialize() throws Exception {
		frame = new JFrame();
		frame.setBounds(100, 100, 683, 515);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frame.getContentPane().add(tabbedPane);
		
		JPanel pingTab = new JPanel();
		pingTab.setBorder(new EmptyBorder(10,10,10,10));
		tabbedPane.addTab("Ping Server", null, pingTab, null);
		GridBagLayout gbl_pingTab = new GridBagLayout();
		gbl_pingTab.columnWidths = new int[]{650, 0};
		gbl_pingTab.rowHeights = new int[]{217, 217, 0};
		gbl_pingTab.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_pingTab.rowWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		pingTab.setLayout(gbl_pingTab);
		
		model = new DefaultListModel<SSHDB>();
		JList<SSHDB> list = new JList<SSHDB>(model);
		progressBar = new JProgressBar(0, 0);
		pingServers();

		GridBagConstraints gbc_list = new GridBagConstraints();
		gbc_list.gridheight = 8;
		gbc_list.fill = GridBagConstraints.BOTH;
		gbc_list.insets = new Insets(0, 0, 5, 0);
		gbc_list.gridx = 0;
		gbc_list.gridy = 0;
		pingTab.add(list, gbc_list);
		
		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.gridheight = 2;
		gbc_panel.anchor = GridBagConstraints.SOUTH;
		gbc_panel.fill = GridBagConstraints.HORIZONTAL;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 8;
		pingTab.add(panel, gbc_panel);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JButton connectBtn = new JButton("Connect");
		connectBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				SSHDB sshdb = list.getSelectedValue();
				if(sshdb == null) {
					return;
				}
				try {
					sshdb = sshDatabase.getSSHDB(sshdb.getId());
					ProcessBuilder processBuilder = new ProcessBuilder("gnome-terminal", "--", "bash", "-c", "ssh " + sshdb.getUsername() + "@"+ sshdb.getIp());
					System.out.println("\"ssh " + sshdb.getUsername() + "@"+ sshdb.getIp() + "\"");
					processBuilder.start();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		panel.add(connectBtn);
		
		JButton deleteButton = new JButton("Delete");
		deleteButton.addActionListener(
			new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					SSHDB sshdb = list.getSelectedValue();
					if(sshdb == null) {
						return;
					}
					sshDatabase.deleteSSHDB(sshdb.getId());
					model.removeElement(sshdb);
				}
			}
		);
		panel.add(deleteButton);
		
		JButton refreshBtn = new JButton("Refresh");
		refreshBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					pingServers();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});	
		panel.add(refreshBtn);
		panel.add(progressBar);
		
		JPanel addTab = new JPanel();
		tabbedPane.addTab("Add New", null, addTab, null);
		addTab.setLayout(new GridLayout(0, 1, 0, 0));
		
		JPanel panel_1 = new JPanel();
		addTab.add(panel_1);
		
		JLabel lblNewLabel = new JLabel("IP Address");
		panel_1.add(lblNewLabel);
		
		ipTextField = new JTextField();
		ipTextField.setColumns(10);
		panel_1.add(ipTextField);
		
		JPanel panel_2 = new JPanel();
		addTab.add(panel_2);
		
		JLabel lblNewLabel_1 = new JLabel("Username");
		panel_2.add(lblNewLabel_1);
		
		usernameTextField = new JTextField();
		panel_2.add(usernameTextField);
		usernameTextField.setColumns(10);
		
		JPanel panel_3 = new JPanel();
		addTab.add(panel_3);
		
		JLabel lblNewLabel_2 = new JLabel("Name");
		panel_3.add(lblNewLabel_2);
		
		nameTextField = new JTextField();
		nameTextField.setColumns(10);
		panel_3.add(nameTextField);
		
		JPanel panel_4 = new JPanel();
		addTab.add(panel_4);
		
		ButtonGroup group = new ButtonGroup();
		
		JRadioButton rdbtnNewRadioButton = new JRadioButton("Password");
		rdbtnNewRadioButton.setSelected(true);
		panel_4.add(rdbtnNewRadioButton);
		
		JRadioButton rdbtnNewRadioButton_1 = new JRadioButton("Public Key");
		panel_4.add(rdbtnNewRadioButton_1);
		
		group.add(rdbtnNewRadioButton_1);
		group.add(rdbtnNewRadioButton);
		
		JPanel panel_5 = new JPanel();
		addTab.add(panel_5);
		
		rdbtnNewRadioButton.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				panel_5.setVisible(true);
				
			}
		});
		rdbtnNewRadioButton_1.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				panel_5.setVisible(false);
				
			}
		});
		
		
		JLabel lblNewLabel_3 = new JLabel("Password");
		panel_5.add(lblNewLabel_3);
		
		textField = new JTextField();
		textField.setColumns(10);
		panel_5.add(textField);
		
		JPanel panel_6 = new JPanel();
		addTab.add(panel_6);
		panel_6.setLayout(new GridLayout(0, 1, 0, 0));
		
		JPanel panel_7 = new JPanel();
		panel_6.add(panel_7);
		
		JButton submitBtn = new JButton("Save");
		panel_7.add(submitBtn);

		JLabel errorLabel = new JLabel("");
		errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
		panel_6.add(errorLabel);
		
		submitBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				String name = nameTextField.getText().trim();
				String ip = ipTextField.getText().trim();
				String username = usernameTextField.getText().trim();
				String password = textField.getText().trim();
				Boolean isPassword = rdbtnNewRadioButton.isSelected();
				if(name.isEmpty() || ip.isEmpty() || username.isEmpty()) {
					errorLabel.setText("Please fill all fields");
					return;
				}
				errorLabel.setText("");
				SSHDB sshdb = new SSHDB();
				sshdb.setName(name);
				sshdb.setIp(ip);
				sshdb.setUsername(username);
				sshdb.setAuthType(isPassword ? 1 : 2);
				if(isPassword && password.isEmpty()) {
					errorLabel.setText("Please fill all fields");
					return;
				}
				else if(isPassword) {
					sshdb.setPassword(password);
				}
				sshdb.setPort(22);
				boolean res =  sshDatabase.addSSHDB(sshdb);
				if(!res) {
					errorLabel.setText("Error saving data");
					return;
				}
				errorLabel.setText("Saved successfully");
				nameTextField.setText("");
				ipTextField.setText("");
				usernameTextField.setText("");
				textField.setText("");
			}
		});
		
	}

	void pingServers() throws Exception {
		model.clear();
		progressBar.setValue(0);
		List<SSHDB> sshdbs = sshDatabase.getNamesAndIP();
		progressBar.setMaximum(sshdbs.size());
		for(SSHDB sshdb : sshdbs) {
			ServerPing.check(sshdb, model, progressBar);
		}
	}

}

package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;

import javax.swing.Box;
import javax.swing.JFileChooser;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JLabel;
import javax.swing.Timer;

import javax.swing.ButtonGroup;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Scanner;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JCheckBox;

import urlReader.UrlHandler;
import javax.swing.JMenuItem;

public class Main {

	private JFrame frame;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private final ButtonGroup buttonGroup_1 = new ButtonGroup();
	private JTextField domainField;
	private JTextField roomField;
	private JTextField configField;
	private final ButtonGroup buttonGroup_2 = new ButtonGroup();
	private Thread urlThread;
	private JLabel statusLabel;
	private int broj = 10;
	private Timer timer;
	private Timer timer1;
	private JButton btnStop;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main window = new Main();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Main() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		initFiles();
		frame = new JFrame();
		frame.setTitle(Messages
				.getString("Main.frmAudienceResponseSystem.title")); //$NON-NLS-1$
		frame.setBounds(200, 100, 500, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		final JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);

		JLabel lblNewLabel = new LocLabel("Main.lblNewLabel.text");
		lblNewLabel.setText(Messages.getString("Main.lblNewLabel.text_1")); //$NON-NLS-1$
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel.setBounds(31, 15, 154, 14);
		panel.add(lblNewLabel);

		final JRadioButton defDomain = new JRadioButton(Messages.getString("Main.rdbtnHttpwwwauressorg.text")); //$NON-NLS-1$
		defDomain.setBounds(35, 48, 158, 23);
		buttonGroup_1.add(defDomain);
		panel.add(defDomain);

		JRadioButton cusDomain = new JRadioButton("");
		buttonGroup_1.add(cusDomain);
		cusDomain.setBounds(35, 74, 21, 21);
		panel.add(cusDomain);

		domainField = new JTextField();
		domainField.setBounds(55, 74, 193, 20);
		domainField.setText("");
		panel.add(domainField);
		domainField.setColumns(10);

		Scanner s;
		try {
			s = new Scanner(Paths.get("settings.txt"));
                        String domena = "";
                        
                        // Ucitaj custom domenu
                        if (s.hasNextLine()) {
                            domena = s.nextLine();
                        }
                        
                        // Provjeri je li domena defaultna ili custom zadana
                        if (domena.equals("http://www.auress.org/") || domena.equals("")) {
				defDomain.doClick();
			} else {
				cusDomain.doClick();
				domainField.setText(domena);
			}
			s.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		JLabel lblNewLabel_1 = new LocLabel("Main.lblNewLabel_1.text"); //$NON-NLS-1$
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel_1.setBounds(328, 15, 84, 23);
		panel.add(lblNewLabel_1);

		roomField = new JTextField();
		roomField.setText("");
		roomField.setBounds(328, 49, 86, 20);
		panel.add(roomField);
		roomField.setColumns(10);

		JSeparator separator = new JSeparator();
		separator.setBounds(10, 129, 464, 2);
		panel.add(separator);

		JLabel label = new LocLabel("Main.label.text"); //$NON-NLS-1$
		label.setFont(new Font("Tahoma", Font.BOLD, 14));
		label.setBounds(31, 159, 118, 14);
		panel.add(label);

		final JRadioButton plainText = new LocRadioButton(
				"Main.rdbtnPlainText.text"); //$NON-NLS-1$
		plainText.setSelected(true);
		buttonGroup_2.add(plainText);
		plainText.setBounds(35, 200, 109, 23);
		panel.add(plainText);

		final JRadioButton configFile = new JRadioButton("");
		buttonGroup_2.add(configFile);
		configFile.setBounds(35, 226, 21, 23);
		panel.add(configFile);

		configField = new JTextField();
		configField.setText("");
		configField.setBounds(55, 230, 130, 20);
		panel.add(configField);
		configField.setColumns(10);

		JButton btnFindConfigFile = new LocButton("Main.btnFindConfigFile.text"); //$NON-NLS-1$
		btnFindConfigFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				final JFileChooser fc = new JFileChooser();
				int returnVal = fc.showOpenDialog(frame);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					try {
						Scanner sc = new Scanner(file);
						PrintWriter pw = new PrintWriter("tempConfig.txt");
						while (sc.hasNextLine()) {
							pw.write(sc.nextLine()
									+ System.getProperty("line.separator"));
						}
						sc.close();
						pw.close();
						String[] arr = file.toString().split("\\\\");
						configField.setText(arr[arr.length - 1]);
						configFile.doClick();
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		btnFindConfigFile.setBounds(84, 260, 175, 23);
		panel.add(btnFindConfigFile);

		JButton btnNewButton = new LocButton("Main.btnNewButton.text"); //$NON-NLS-1$
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new NewConfig();
			}
		});
		btnNewButton.setBounds(84, 288, 175, 23);
		panel.add(btnNewButton);

		final JCheckBox userId = new LocCheckBox("Main.chckbxSenderId.text"); //$NON-NLS-1$
		userId.setSelected(true);
		userId.setBounds(328, 200, 97, 23);
		panel.add(userId);

		final JCheckBox message = new LocCheckBox("Main.chckbxMessage.text"); //$NON-NLS-1$
		message.setSelected(true);
		message.setBounds(328, 226, 97, 23);
		panel.add(message);

		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(10, 337, 464, 2);
		panel.add(separator_1);

		JLabel lblInputControl = new LocLabel("Main.lblInputControl.text"); //$NON-NLS-1$
		lblInputControl.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblInputControl.setBounds(31, 359, 154, 23);
		panel.add(lblInputControl);

		ActionListener al = new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (arg0.getActionCommand().equals("TimerCommand")) {
					statusLabel.setText(Messages.getString("Main.statusLabel1")
							+ " " + broj + " "
							+ Messages.getString("Main.statusLabel2"));
					broj--;
					if (broj == 0) {
						timer.stop();
						broj = 10;
						statusLabel.setText(Messages
								.getString("Main.statusLabel3"));
						urlThread.start();
					}
					return;
				}
				String domena = null;
				if (defDomain.isSelected()) {
					domena = "http://www.auress.org/";
				} else {
					domena = domainField.getText();
				}
				boolean pisi = false;
				
                                try {
					if (!domena.startsWith("http://"))
						domena = "http://" + domena;
					if (!domena.endsWith("/"))
						domena = domena + "/";
					URL proba = new URL(domena);
					pisi = true;
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null,
							Messages.getString("Main.urlError"));
					return;
				}
				
                                PrintWriter pw = null;
				try {
					pw = new PrintWriter("settings.txt");
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
                                if (pisi)
					pw.write(domena);
				String nl = System.getProperty("line.separator");
				if (plainText.isSelected()) {
					pw.write(nl + "Plain");
				} else {
					pw.write(nl + "Config");
				}
				if (userId.isSelected()) {
					pw.write(nl + "UserId");
				} else {
					pw.write(nl + "NoUserId");
				}
				if (message.isSelected()) {
					pw.write(nl + "Message");
				} else {
					pw.write(nl + "NoMessage");
				}
				pw.close();
                                

				String soba = roomField.getText();
				try {
					URL myUrl = new URL(domena + soba + "/zadnjiOdgovor.txt");
					urlThread = new Thread(new UrlHandler(myUrl));
					timer.start();
					timer.setActionCommand("TimerCommand");
				} catch (MalformedURLException e2) {
					JOptionPane.showMessageDialog(null,
							Messages.getString("Main.urlError"));
				}
			}
		};

		ActionListener al1 = new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (arg0.getActionCommand().equals("TimerCommand")) {
					statusLabel.setText(Messages.getString("Main.statusLabel1")
							+ " " + broj + " "
							+ Messages.getString("Main.statusLabel2"));
					broj--;
					if (broj == 0) {
						timer1.stop();
						broj = 10;
						statusLabel.setText(Messages
								.getString("Main.statusLabel3"));
						urlThread.start();
					}
					return;
				}
				try {
					PrintWriter pw = new PrintWriter("newMessage.txt");
					pw.close();
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					PrintWriter pw = new PrintWriter("oldMessage.txt");
					pw.close();
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				String domena = null;
				if (defDomain.isSelected()) {
					domena = "http://www.auress.org/";
				} else {
					domena = domainField.getText();
				}
				boolean pisi = false;
				try {
					if (!domena.startsWith("http://"))
						domena = "http://" + domena;
					if (!domena.endsWith("/"))
						domena = domena + "/";
					URL proba = new URL(domena);
					pisi = true;
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null,
							Messages.getString("Main.urlError"));
					return;
				}
				PrintWriter pw = null;
				try {
					pw = new PrintWriter("settings.txt");
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (pisi)
					pw.write(domena);
				String nl = System.getProperty("line.separator");
				if (plainText.isSelected()) {
					pw.write(nl + "Plain");
				} else {
					pw.write(nl + "Config");
				}
				if (userId.isSelected()) {
					pw.write(nl + "UserId");
				} else {
					pw.write(nl + "NoUserId");
				}
				if (message.isSelected()) {
					pw.write(nl + "Message");
				} else {
					pw.write(nl + "NoMessage");
				}
				pw.close();

				String soba = roomField.getText();
				try {
					URL myUrl = new URL(domena + soba + "/studentMessages.txt");
					urlThread = new Thread(new UrlHandler(myUrl));
					timer1.start();
					timer1.setActionCommand("TimerCommand");
				} catch (MalformedURLException e2) {
					JOptionPane.showMessageDialog(null,
							Messages.getString("Main.urlError"));
				}
			}
		};

		timer = new Timer(1000, al);
		timer1 = new Timer(1000, al1);

		JButton btnStart = new LocButton("Main.btnStart.text"); //$NON-NLS-1$
		btnStart.addActionListener(al);
		btnStart.setBounds(35, 414, 89, 23);
		panel.add(btnStart);

		btnStop = new LocButton("Main.btnStop.text"); //$NON-NLS-1$
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UrlHandler.setGotovo();
				if (timer.isRunning())
					timer.stop();
				statusLabel.setText(Messages
						.getString("Main.lblToInitiate.text"));
			}
		});
		btnStop.setBounds(173, 414, 89, 23);
		panel.add(btnStop);

		JButton btnResend = new LocButton("Main.btnResend.text"); //$NON-NLS-1$
		btnResend.setBounds(308, 414, 143, 23);
		btnResend.addActionListener(al1);
		panel.add(btnResend);

		JSeparator separator_2 = new JSeparator();
		separator_2.setBounds(10, 459, 464, 2);
		panel.add(separator_2);

		statusLabel = new LocLabel("Main.lblToInitiate.text"); //$NON-NLS-1$
		statusLabel.setBounds(64, 490, 360, 14);
		panel.add(statusLabel);

		JMenuBar menuBar = new JMenuBar();
		menuBar.add(Box.createHorizontalGlue());
		frame.setJMenuBar(menuBar);

		JMenu mnLanguage = new LocMenu("Main.mnLanguage.text");
		menuBar.add(mnLanguage);

		JRadioButtonMenuItem rdbtnmntmCroatian = new JRadioButtonMenuItem(
				Messages.getString("Main.rdbtnmntmCroatian.text"));
		rdbtnmntmCroatian.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Locale.setDefault(new Locale("hr", "HR"));
				Messages.changeBundle();
				Messages.fire();
			}
		});
		buttonGroup.add(rdbtnmntmCroatian);
		mnLanguage.add(rdbtnmntmCroatian);

		JRadioButtonMenuItem rdbtnmntmEnglish = new JRadioButtonMenuItem(
				Messages.getString("Main.rdbtnmntmEnglish.text")); //$NON-NLS-1$
		rdbtnmntmEnglish.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Locale.setDefault(new Locale("en", "US"));
				Messages.changeBundle();
				Messages.fire();
			}
		});
		rdbtnmntmEnglish.setSelected(true);
		buttonGroup.add(rdbtnmntmEnglish);
		mnLanguage.add(rdbtnmntmEnglish);

		JMenu mnAbout = new LocMenu("Main.mnAbout.text"); //$NON-NLS-1$
		menuBar.add(mnAbout);

		JMenuItem mntmNewMenuItem = new LocMenuItem("Main.mntmNewMenuItem.text"); //$NON-NLS-1$
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new About();
			}
		});
		mnAbout.add(mntmNewMenuItem);
	}

	public void initFiles() {
		File f = new File(Paths.get("settings.txt").toString());
		try {
			if (!f.isFile()) {
				PrintWriter pw = new PrintWriter("settings.txt");
				pw.write("http://www.auress.org/");
				pw.close();
			}
			//Runtime.getRuntime().exec("attrib +H settings.txt");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		f = new File(Paths.get("tempConfig.txt").toString());
		f.deleteOnExit();
		try {
			if (!f.isFile()) {
				PrintWriter pw = new PrintWriter("tempConfig.txt");
				pw.close();
			}
			//Runtime.getRuntime().exec("attrib +H tempConfig.txt");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		f = new File(Paths.get("oldMessage.txt").toString());
		f.deleteOnExit();
		try {
			if (!f.isFile()) {
				PrintWriter pw = new PrintWriter("oldMessage.txt");
				pw.close();
			}
			//Runtime.getRuntime().exec("attrib +H oldMessage.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		f = new File(Paths.get("newMessage.txt").toString());
		f.deleteOnExit();
		try {
			if (!f.isFile()) {
				PrintWriter pw = new PrintWriter("newMessage.txt");
				pw.close();
			}
			//Runtime.getRuntime().exec("attrib +H newMessage.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

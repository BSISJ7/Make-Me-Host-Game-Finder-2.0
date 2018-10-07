import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.ListModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.MenuBar;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;

import javax.swing.Timer;

public class GameList {
	protected static JFrame mainFrame;
	private JPanel mainContainer;
	private JButton clipboardButton;
	
	private JList<String> incomingList;
	private JList<String> gameList;
	private JList<String> queueList;
	
	private JRadioButtonMenuItem minTrayOption;
	private JRadioButtonMenuItem disableTrayOption;
	private JRadioButtonMenuItem exitTrayOption;
	protected JRadioButtonMenuItem disableMessages;
	protected JRadioButtonMenuItem enableMessages;
	
	private boolean minToTray = true;
	private boolean disableTray = false;
	
	private GameTrayIcon trayIcon;
	
	private GetGameName getGameName;

	protected static DefaultListModel<String> gameListModel;
	protected static DefaultListModel<String> incomingListModel;
	protected static DefaultListModel queueListModel;
	
	
	public GameList(GetGameName setGameName){
		getGameName = setGameName;
		mainFrame = new JFrame("MMH Game Finder");
		mainContainer = new JPanel();
		mainContainer.setLayout(new GridBagLayout());
		mainFrame.add(mainContainer);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setup();
		setupFrame();
		mainFrame.setVisible(true);
		mainFrame.pack();
		try {
			SystemTray.getSystemTray().add(GameTrayIcon.icon);
		} catch (AWTException e) {}
	}
	
	public void setupFrame(){
		mainFrame.addWindowListener(new WindowListener(){
			public void windowActivated(WindowEvent arg0){}
			public void windowClosed(WindowEvent arg0) {}
			public void windowClosing(WindowEvent arg0) {}
			public void windowDeactivated(WindowEvent arg0) {}
			public void windowDeiconified(WindowEvent arg0) {
			}
			public void windowIconified(WindowEvent arg0) {
				if(minToTray)
					mainFrame.setVisible(false);
			}
			public void windowOpened(WindowEvent arg0) {}
			
		});
	}
	
	public void setup(){
		GridBagConstraints gridConst = new GridBagConstraints();
		
		JButton exitButton = new JButton("Exit");
		exitButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				System.exit(0);
			}
		});
		exitButton.setPreferredSize(new Dimension(130,30));

		gameListModel = new DefaultListModel<String>();
		gameList = new JList<String>(gameListModel);
		
		incomingListModel = new DefaultListModel<String>();
		incomingList = new JList<String>(incomingListModel);

		queueListModel = new DefaultListModel<String>();
		queueList = new JList<String>(queueListModel);
		
		JSeparator incDivide = new JSeparator();
		incDivide.setOrientation(SwingConstants.VERTICAL);
		incDivide.setPreferredSize(new Dimension(1,600));
		incDivide.setOpaque(false);
		incDivide.setBackground(Color.white);
		incDivide.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		
		
		JSplitPane incomingPanel = new JSplitPane();
		incomingPanel.setLeftComponent(incomingList);
		incomingPanel.setRightComponent(queueList);
		incomingPanel.setPreferredSize(new Dimension(400,600));
		incomingPanel.setMinimumSize(new Dimension(400,600));
		incomingPanel.setOpaque(false);
		incomingPanel.setVisible(true);
		incomingPanel.setDividerLocation(200);
		
		JScrollPane incomingListContainer = new JScrollPane(incomingPanel);
		incomingListContainer.setPreferredSize(new Dimension(400,200));
		incomingListContainer.setMinimumSize(new Dimension(400,600));
		incomingListContainer.getViewport().setOpaque(false);
		
		JScrollPane gameListContainer = new JScrollPane(gameList);
		gameListContainer.setPreferredSize(new Dimension(400,200));
		gameListContainer.setMinimumSize(new Dimension(400,600));
		gameListContainer.getViewport().setOpaque(false);

		JTabbedPane gameTabbedPane = new JTabbedPane();
		gameTabbedPane.add("Game List", gameListContainer);
		gameTabbedPane.add("Incoming List", incomingListContainer);
		
		gridConst.fill = 1;
		gridConst.gridx = 0;
		gridConst.gridy = 0;
		mainContainer.add(gameTabbedPane, gridConst);

		setupMenuBar();

		//Setup the clipboard button
		clipboardButton = new JButton("Copy To Clipboard");
		clipboardButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){ 
				StringSelection clipboardText = null;
				if(!gameList.isSelectionEmpty() && gameList.isShowing()){
					clipboardText = new StringSelection((String) gameListModel.getElementAt(gameList.getSelectedIndex()));
				}
				else if(!incomingList.isSelectionEmpty() && incomingList.isShowing()){
					clipboardText = new StringSelection((String) incomingListModel.getElementAt(incomingList.getSelectedIndex()));						
				}
				try{
					Clipboard clipBoard = Toolkit.getDefaultToolkit().getSystemClipboard();
					clipBoard.setContents(clipboardText, clipboardText);
				}catch(NullPointerException NPE){System.out.println("Null Info");}
			}
		});
		
		gridConst.gridx = 0;
		gridConst.gridy = 1;
		mainContainer.add(clipboardButton, gridConst);

		gridConst.gridx = 0;
		gridConst.gridy = 2;
		mainContainer.add(exitButton, gridConst); 
		
		try {
			trayIcon = new GameTrayIcon(this);
			trayIcon.setupIcon();
			getGameName.setTrayIcon(trayIcon);
		}catch (AWTException e) {
		}catch (InterruptedException e) {}
	}

	public static void copyToClipBoard(String gameName){
		StringSelection clipboardText = new StringSelection(gameName);
		Clipboard clipBoard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipBoard.setContents(clipboardText, clipboardText);
	}
	
	public void minimizePanel(){
		mainContainer.setVisible(false);
	}
	
	public void setupMenuBar(){
		enableMessages = new JRadioButtonMenuItem("Enable Messages");
		disableMessages = new JRadioButtonMenuItem("Disable Messages");

		ButtonGroup messageGroup = new ButtonGroup();
		messageGroup.add(enableMessages);
		messageGroup.add(disableMessages);
		enableMessages.setSelected(true);
		
		JMenu messagesMenu = new JMenu("Messages");
		messagesMenu.add(enableMessages);
		messagesMenu.add(disableMessages);
		
		ButtonGroup optionsGroup = new ButtonGroup();
		minTrayOption = new JRadioButtonMenuItem ("Minimize To Tray"); 
		exitTrayOption = new JRadioButtonMenuItem ("Exit To Tray"); 
		disableTrayOption = new JRadioButtonMenuItem ("Disable Tray Icon");

		optionsGroup.add(minTrayOption); 
		optionsGroup.add(exitTrayOption); 
		optionsGroup.add(disableTrayOption);
		minTrayOption.setSelected(true);

		minTrayListener();
		
		JMenu optionsMenu = new JMenu("Options");
		optionsMenu.add(minTrayOption);
		optionsMenu.add(exitTrayOption); 
		optionsMenu.add(disableTrayOption);

		JMenuBar menuBar = new JMenuBar();
		menuBar.add(messagesMenu);
		menuBar.add(optionsMenu);
		
		mainFrame.setJMenuBar(menuBar);
	}

	public void minTrayListener(){

		ActionListener menuListener = new ActionListener(){
			public void actionPerformed(ActionEvent event){
				String source =  event.getActionCommand();
				if(disableTray)
					SystemTray.getSystemTray().remove(GameTrayIcon.icon);
				
				if(source.equals("enableMin")){
					minToTray = true;
					disableTray = false;
				} 
				else if(source.equals("enableExit")){
					minToTray = false;
					disableTray = false;
				}

				else if(source.equals("disableTray")){
					minToTray = false;
					disableTray = true;
					SystemTray.getSystemTray().remove(GameTrayIcon.icon);
				}
				
				else if(source.equals("enableMsgs")){
					enableMessages.setSelected(true);
					disableMessages.setSelected(false);
					trayIcon.displayMessage = true;
				}
				
				else if(source.equals("disableMsgs")){
					disableMessages.setSelected(true);
					enableMessages.setSelected(false);
					trayIcon.displayMessage = false;
					trayIcon.displayMessage("WIBBLE", "WOBBLEs");
				}

			}
		};

		enableMessages.setActionCommand("enableMsgs");
		enableMessages.addActionListener(menuListener); 
		disableMessages.setActionCommand("disableMsgs");
		disableMessages.addActionListener(menuListener); 
		
		minTrayOption.setActionCommand("enableMin");
		minTrayOption.addActionListener(menuListener); 
		exitTrayOption.setActionCommand("enableExit");
		exitTrayOption. addActionListener (menuListener); 
		disableTrayOption.setActionCommand("disableTray");
		disableTrayOption. addActionListener (menuListener);
	}
	
	public void updateGameList(){
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				Timer updateListTimer = new Timer(500, new ActionListener(){
					public void actionPerformed(ActionEvent event){
						getGameName.getData();
						gameList.repaint();
						incomingList.repaint();
						queueList.repaint();
					}
				});
				updateListTimer.start();
				updateListTimer.setDelay(4000);
			}
		});
	}

}

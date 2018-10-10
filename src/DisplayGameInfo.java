import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dimension;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.RowFilter;
import javax.swing.border.TitledBorder;


public class DisplayGameInfo extends JFrame {

	private JPanel contentPane;
	private JTable table;
	private JScrollPane scrollPane;
	private DefaultTableModel tModel;
	private ArrayList<String> nameFilterList = new ArrayList<String>();
	private JTextArea textFilter;
    private JFrame mainFrame;
    private GameTrayIcon trayIcon;

    private List<GameInfo> prevGameList = new ArrayList<>();

    Function<GameInfo, Boolean> isGameInList = gameInfo ->
            prevGameList.stream().anyMatch(game ->game.getBotName().equals(gameInfo.getBotName()));
    Function<GameInfo, Boolean> isNameOnFilterList = gameInfo -> nameFilterList.stream().anyMatch(nameFilter -> Pattern.compile(Pattern.quote(nameFilter),
            Pattern.CASE_INSENSITIVE).matcher(gameInfo.getGameName()).find());

	protected JRadioButtonMenuItem disableMessages;
	protected JRadioButtonMenuItem enableMessages;
	private JRadioButtonMenuItem minTrayOption;
	private JRadioButtonMenuItem disableTrayOption;
	private JRadioButtonMenuItem exitTrayOption;

	private boolean disableTray = false;
	private boolean minToTray = false;

	/**
	 * Create the frame.
	 */
	public DisplayGameInfo(String GameName) {
		super(GameName);
		
		trayIcon = new GameTrayIcon();
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel textFilterBorder = new JPanel();
		textFilterBorder.setBorder(new TitledBorder(null, "Text Filter", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		textFilterBorder.setBounds(0, 0, 683, 57);
		contentPane.add(textFilterBorder);
		textFilterBorder.setLayout(null);
		
		textFilter = new JTextArea();
		textFilter.setPreferredSize(new Dimension(4, 19));
		textFilter.setLineWrap(true);
		textFilter.setBounds(8, 14, 669, 36);
		textFilterBorder.add(textFilter);
		textFilter.addKeyListener(new KeyListener(){
			public void keyPressed(KeyEvent arg0) {}
			public void keyReleased(KeyEvent arg0) {
				applyTableFilter();
			}
			public void keyTyped(KeyEvent arg0) {}
		});
		
		Button test = new Button("Test BUtton");
		
		JButton saveFilter = new JButton ("Save Filter");
		saveFilter.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
			ArrayList<String> list =  new ArrayList<String>();
			list.add(textFilter.getText());
			GNSaveLoad.saveList(list);
			}
		});
		JButton loadFilter = new JButton ("Load Filter");
		loadFilter.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
			ArrayList<String> list =  GNSaveLoad.loadList();
			if(!list.isEmpty())
				textFilter.setText(list.get(0));
			}
		});
		
		JMenu optionsMenu = new JMenu("Save/Load");
		optionsMenu.add(saveFilter);
		optionsMenu.add(loadFilter);

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

		ActionListener menuListener = event -> {
            String source =  event.getActionCommand();
            if(disableTray && !source.equals("disableTray")) {
                try {
                    SystemTray.getSystemTray().add(trayIcon.icon);
                    disableTray = false;
                } catch (AWTException e) {e.printStackTrace();}
            }

            if(source.equals("enableMin")){
                minToTray = true;
            }
            else if(source.equals("enableExit")){
                minToTray = false;
            }

            else if(source.equals("disableTray")){
                minToTray = false;
                SystemTray.getSystemTray().remove(trayIcon.icon);
                disableTray = true;
            }

            else if(source.equals("enableMsgs")){
                enableMessages.setSelected(true);
                disableMessages.setSelected(false);
            }

            else if(source.equals("disableMsgs")){
                disableMessages.setSelected(true);
                enableMessages.setSelected(false);
                trayIcon.displayMessage("WIBBLE", "WOBBLES");
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
		
		JMenu trayOptions = new JMenu("Options");
		trayOptions.add(minTrayOption);
		trayOptions.add(exitTrayOption); 
		trayOptions.add(disableTrayOption);

		
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(optionsMenu);
		menuBar.add(messagesMenu);
		menuBar.add(trayOptions);
		
		this.setJMenuBar(menuBar);
		
		mainFrame = this;
		addWindowListener(new WindowListener(){
			public void windowActivated(WindowEvent arg0){}
			public void windowClosed(WindowEvent arg0) {}
			public void windowClosing(WindowEvent arg0) {}
			public void windowDeactivated(WindowEvent arg0) {}
			public void windowDeiconified(WindowEvent arg0) {
				mainFrame.setVisible(true);
			}
			public void windowIconified(WindowEvent arg0) {
				mainFrame.setVisible(false);
			}
			public void windowOpened(WindowEvent arg0) {}
			
		});
		
		trayIcon.setDispInfo(this);
		trayIcon.setWindow(mainFrame);
		
		try {
			trayIcon.setupIcon();
			SystemTray.getSystemTray().add(trayIcon.icon);
		} catch (AWTException e) {e.printStackTrace();}

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 477);
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 68, 683, 349);
		contentPane.add(panel);
		
		panel.setLayout(null);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 5, 663, 333);
		panel.add(scrollPane);

		
		tModel = new DefaultTableModel();
		table = new JTable(tModel){
			private static final long serialVersionUID = 1L;
			public boolean isCellEditable(int row, int column) { return false; }
		};
		table.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK), "Copy");
		table.getActionMap().put("Copy", new AbstractAction() {
		      public void actionPerformed(ActionEvent e) {
		    	  int y = table.getSelectedColumn();
		    	  
		    	  if(table.getSelectedRow() != -1 && table.getSelectedColumn() != -1){
			    	  int x = table.convertRowIndexToModel(table.getSelectedRow());
			    	  StringSelection clipBoardData = new StringSelection((String) tModel.getValueAt(x, y));
			    	  try{
							Clipboard clipBoard = Toolkit.getDefaultToolkit().getSystemClipboard();
							clipBoard.setContents(clipBoardData, clipBoardData);
						}catch(NullPointerException NPE){System.out.println("Null Info");}
		    	  }
		      }
		});

		scrollPane.setViewportView(table);
		panel.add(table.getTableHeader(), BorderLayout.PAGE_START);
		tModel.addColumn("Bot Name");
		tModel.addColumn("Server Location");
		tModel.addColumn("Running");
		tModel.addColumn("Current Game");
		tModel.addColumn("Owner");
		tModel.addColumn("Ingame");
		table.setFillsViewportHeight(true);
		table.setName("MMH Game Data");
		
	    table.getColumnModel().getColumn(0).setPreferredWidth(170);
	    table.getColumnModel().getColumn(1).setPreferredWidth(150);
	    table.getColumnModel().getColumn(2).setPreferredWidth(100);
	    table.getColumnModel().getColumn(3).setPreferredWidth(340);
	    table.getColumnModel().getColumn(4).setPreferredWidth(180);
	    table.getColumnModel().getColumn(5).setPreferredWidth(80);
	}
	
	public void addGame(GameInfo gameInfo){
		tModel.addRow(new Object[]{gameInfo.getBotName(), gameInfo.getServer(), gameInfo.getRunning(),
				gameInfo.getGameName(), gameInfo.getOwner(), gameInfo.getInGame()});
	}
	
	public void updateGames(List<GameInfo> gameList){
		while(tModel.getRowCount() > gameList.size()){
			tModel.removeRow(tModel.getRowCount()-1);
		}
		while(tModel.getRowCount() < gameList.size()){
			tModel.addRow(new Object[]{"","","","","",""});
		}

		int index = 0;
		String gameAnnouncment = "";
		List<GameInfo> newGames = new ArrayList<>();
		for(GameInfo gameInfo : gameList){

            if(gameInfo.getGameName().length() > 0 && !isGameInList.apply(gameInfo) && isNameOnFilterList.apply(gameInfo)){
				gameAnnouncment += gameInfo.getGameName()+"\n";
			}
            if(gameInfo.getGameName().length() > 0 && isNameOnFilterList.apply(gameInfo)){
                newGames.add(gameInfo);
            }

			tModel.setValueAt(gameInfo.getBotName(), index, 0);
			tModel.setValueAt(gameInfo.getServer(), index, 1);
			tModel.setValueAt(gameInfo.getRunning(), index, 2);
			tModel.setValueAt(gameInfo.getGameName(), index, 3);
			tModel.setValueAt(gameInfo.getOwner(), index, 4);
			tModel.setValueAt(gameInfo.getInGame(), index++, 5);
		}
		if(gameAnnouncment.trim().length() > 0 && enableMessages.isSelected()) {
			trayIcon.displayMessage(gameAnnouncment);
		}
		prevGameList = newGames.stream().collect(Collectors.toList());
		applyTableFilter();
	}
	
	public void applyTableFilter(){
		int x = table.getSelectedRow();
		int y = table.getSelectedColumn();
		
		nameFilterList.clear();
		String[] getStrings = textFilter.getText().split("\\|");
		for(String aString : getStrings){
			nameFilterList.add(aString);
		}

		TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<DefaultTableModel>(tModel);
		table.setRowSorter(sorter);
	    RowFilter<DefaultTableModel, Object> rf = RowFilter.regexFilter("(?i)"+textFilter.getText().replace("\\", "\\\\"),3);
	    sorter.setRowFilter(rf);
		
		table.revalidate();
		table.repaint();
		if(x != -1 && y != -1 && x <= table.getRowCount()){
			table.changeSelection(x, y, false, false);
		}
	}
}

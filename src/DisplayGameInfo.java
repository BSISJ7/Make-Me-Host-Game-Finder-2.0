import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dimension;
import java.awt.EventQueue;
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
import java.util.Collection;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;
import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JRadioButtonMenuItem;
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
	private GameTrayIcon trayIcon;
	private ArrayList<String> gameList = new ArrayList<String>();
	private StringChecker checkString;
	private JTextArea textFilter;
	protected JFrame mainFrame;
	

	/**
	 * Create the frame.
	 */
	public DisplayGameInfo(String GameName) {
		super(GameName);
		
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
		//optionsMenu.add(test);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(optionsMenu);
		
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
		
		trayIcon = new GameTrayIcon(mainFrame);
		checkString = new StringChecker();
		
		try {
			trayIcon.setupIcon();
			SystemTray.getSystemTray().add(GameTrayIcon.icon);
		} catch (AWTException e) {e.printStackTrace();} catch (InterruptedException e) {e.printStackTrace();}
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 477);
		
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 68, 683, 349);
		contentPane.add(panel);
		
		String[] columns = {"Game Name", "Host", "Ingame"};
		panel.setLayout(null);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 5, 663, 333);
		panel.add(scrollPane);

		
		tModel = new DefaultTableModel(); 
		table = new JTable(tModel){
			private static final long serialVersionUID = 1L;
			public boolean isCellEditable(int row, int column) { return false; };
		};
		table.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK), "Copy");
		table.getActionMap().put("Copy", new AbstractAction() {
		      public void actionPerformed(ActionEvent e) {
		    	  int x = table.getSelectedRow();
		    	  int y = table.getSelectedColumn();
		    	  
		    	  
		    	  if(table.getSelectedRow() != -1 && table.getSelectedColumn() != -1){
			    	  x = table.convertRowIndexToModel(table.getSelectedRow());
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
				gameInfo.getCurrentGame(), gameInfo.getOwner(), gameInfo.getInGame()});
	}
	
	public void updateGames(ArrayList<GameInfo> gameInfo){
		while(tModel.getRowCount() > gameInfo.size()){
			tModel.removeRow(tModel.getRowCount()-1);
		}
		while(tModel.getRowCount() < gameInfo.size()){
			tModel.addRow(new Object[]{"","","","","",""});
		}

		int index = 0;
		String newGame = "";
		for(GameInfo info : gameInfo){
			for(int x = 0; x < tModel.getRowCount(); x++){
				//Checks if its the same bot and the game name has changed
				if(info.getBotName().equalsIgnoreCase((String)tModel.getValueAt(x, 0)) && !info.getCurrentGame().equalsIgnoreCase((String)tModel.getValueAt(x, 3))){
					if(checkString.checkForString((info.getCurrentGame()), gameList) && info.getCurrentGame().trim().length() > 0){
						newGame += info.getCurrentGame()+"\n";
					}
				}
			}

			tModel.setValueAt(info.getBotName(), index, 0);
			tModel.setValueAt(info.getServer(), index, 1);
			tModel.setValueAt(info.getRunning(), index, 2);
			tModel.setValueAt(info.getCurrentGame(), index, 3);
			tModel.setValueAt(info.getOwner(), index, 4);
			tModel.setValueAt(info.getInGame(), index++, 5);
		}
		if(newGame.trim().length() > 0)
			trayIcon.displayMessage(newGame);
		
		applyTableFilter();
	}
	
	public void applyTableFilter(){
		int x = table.getSelectedRow();
		int y = table.getSelectedColumn();
		
		gameList.clear();
		String[] getStrings = textFilter.getText().split("\\|");
		for(String aString : getStrings){
			gameList.add(aString);
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

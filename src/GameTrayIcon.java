import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.Panel;
import java.awt.TrayIcon;

import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.plaf.metal.MetalIconFactory;

import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;



public class GameTrayIcon {

	protected TrayIcon icon;
	private JFrame mainFrame;
	private DisplayGameInfo displayGameInfo;
	
	public GameTrayIcon() {}
	
	public void setWindow(JFrame frame) {
		mainFrame = frame;
	}
	
	public void setDispInfo(DisplayGameInfo displayGameInfo) {
		this.displayGameInfo = displayGameInfo;
	}
	
	public void setupIcon() {
		Icon defaultIcon = MetalIconFactory.getTreeComputerIcon();
		Image img = new BufferedImage(defaultIcon.getIconWidth(), defaultIcon.getIconHeight(), BufferedImage.TYPE_4BYTE_ABGR);
        defaultIcon.paintIcon(new Panel(), img.getGraphics(), 0, 0);
		
		icon = new TrayIcon(img, "MMH Game Finder", setupPopup());
		icon.addActionListener(event -> SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				if(mainFrame.getExtendedState() == JFrame.ICONIFIED){
					int state = mainFrame.getExtendedState();
					state = state & JFrame.MAXIMIZED_BOTH;
					mainFrame.setExtendedState(state);
					mainFrame.toFront();
					mainFrame.requestFocus();
					mainFrame.repaint();
					mainFrame.setVisible(true);
					mainFrame.setAlwaysOnTop(true);
					mainFrame.setAlwaysOnTop(false);
					mainFrame.toFront();
				}
				else{
					mainFrame.toFront();
				}
			}
		}));
	}
	
	public void displayMessage(String gameName, String incomingGames){
		if(displayGameInfo.enableMessages.isSelected()){
			icon.displayMessage("Game Hosted", "Game Name(s): "+ gameName+
					"\nIncoming Games: "+incomingGames, TrayIcon.MessageType.INFO);
		}
	}
	
	public void displayMessage(String gameName){
		if(displayGameInfo.enableMessages.isSelected()){
			System.out.println("Displaying Message: ");
			icon.displayMessage("Game Hosted", "Game Name(s): "+ gameName, TrayIcon.MessageType.INFO);
		}
	}
	
	public PopupMenu setupPopup(){
		PopupMenu popup = new PopupMenu();
		
		MenuItem disableMessages = new MenuItem("Disable Messages");
		disableMessages.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				displayGameInfo.disableMessages.setSelected(true);
			}
		});

		MenuItem enableMessages = new MenuItem("Enable Messages");
		enableMessages.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				displayGameInfo.enableMessages.setSelected(true);
			}
		});
		
		MenuItem esc = new MenuItem("Exit");
		esc.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				System.exit(0);
			}
		});
		
		popup.add(enableMessages);
		popup.add(disableMessages);
		popup.add(esc);
		return popup;
	}
}

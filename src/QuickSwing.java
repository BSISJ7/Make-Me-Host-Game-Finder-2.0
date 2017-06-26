import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.font.TextAttribute;
import java.io.File;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class QuickSwing{

	private JButton button;
	private JLabel label;
	
	private String message;
	
	private Font font = new Font("Times New Roman", Font.LAYOUT_LEFT_TO_RIGHT, 18);
	private JPanel panel;
	
	public QuickSwing(){}
	
	public QuickSwing(String message, final String image){
		this.message = message;
		
		this.label = new JLabel(){
			protected void paintComponent(Graphics g){
				g.drawImage(new ImageIcon(image).getImage(), 0, 0, 180, 40, null);
				g.drawString(setStringFont(), 5, 25);
			}
		};
		
		this.button = new JButton(){
			protected void paintComponent(Graphics g){
				g.drawImage(new ImageIcon(image).getImage(), 0, 0, 180, 40, null);
				g.drawString(setStringFont(), 5, 25);
			}
		};
		
		this.panel = new JPanel(){
			protected void paintComponent(Graphics g){
				g.drawImage(new ImageIcon(image).getImage(), 0, 0, 180, 40, null);
				g.drawString(setStringFont(), 5, 25);
			}
		};
	}
	
	public QuickSwing(String message, final String image, final Color textColor){
		this.message = message;

		final Color setColor = textColor;
		
		System.out.println("Colors: " + textColor);
		
		this.label = new JLabel(){
			protected void paintComponent(Graphics g){
				g.setColor(setColor);
				g.drawImage(new ImageIcon(image).getImage(), 0, 0, 180, 40, null);
				g.drawString(setStringFont(), 5, 25);
			}
		};
		
		this.button = new JButton(){
			protected void paintComponent(Graphics g){
				g.setColor(setColor);
				g.drawImage(new ImageIcon(image).getImage(), 0, 0, 180, 40, null);
				g.drawString(setStringFont(), 5, 25);
			}
		};
		
		this.panel = new JPanel(){
			protected void paintComponent(Graphics g){
				g.setColor(setColor);
				g.drawImage(new ImageIcon(image).getImage(), 0, 0, 180, 40, null);
				g.drawString(setStringFont(), 5, 25);
			}
		};
	}
	
	public QuickSwing(String message, final String image, final int width, final int height, Color textColor){
		this.message = message;
		
		final Color setColor = textColor;
		System.out.println("Colors: " + setColor);
		
		this.label = new JLabel(){
			protected void paintComponent(Graphics g){
				//Dimension d = getSize();
				g.setColor(setColor);
				g.drawImage(new ImageIcon(image).getImage(), 0, 0, width, height, null);
				g.drawString(setStringFont(), 5, 25);
			}
		};
		
		this.button = new JButton(){
			protected void paintComponent(Graphics g){
				//Dimension d = getSize();
				g.setColor(setColor);
				g.drawImage(new ImageIcon(image).getImage(), 0, 0, width, height, null);
				g.drawString(setStringFont(), 5, 25);
			}
		};
		
		this.panel = new JPanel(){
			protected void paintComponent(Graphics g){
				g.setColor(setColor);
				g.drawImage(new ImageIcon(image).getImage(), 0, 0, width, height, null);
				g.drawString(setStringFont(), 5, 25);
			}
		};
	}
	
	
	public QuickSwing(String message, final String image, final int width, final int height){
		this.message = message;
		
		this.label = new JLabel(){
			protected void paintComponent(Graphics g){
				//Dimension d = getSize();
				g.drawImage(new ImageIcon(image).getImage(), 0, 0, width, height, null);
				g.drawString(setStringFont(), 5, 25);
			}
		};
		
		this.button = new JButton(){
			protected void paintComponent(Graphics g){
				//Dimension d = getSize();
				g.drawImage(new ImageIcon(image).getImage(), 0, 0, width, height, null);
				g.drawString(setStringFont(), 5, 25);
			}
		};
		
		this.panel = new JPanel(){
			protected void paintComponent(Graphics g){
				g.drawImage(new ImageIcon(image).getImage(), 0, 0, width, height, null);
				g.drawString(setStringFont(), 5, 25);
			}
		};
	}
	
	
	public JButton getButton(final String message, final ImageIcon image, final int width, final int height){
		this.message = message;
		
		button = new JButton();
		
		button = new JButton(){
			protected void paintComponent(Graphics g){
				g.drawImage(image.getImage(), 0, 0, width, height, null);
				g.drawString(setStringFont(message), 5, 25);
			}
		};
		
		button.setPreferredSize(new Dimension(width, height));
		return button;
	}
	
	
	public JButton getButton(final String message, final String image, final int width, final int height){
		this.message = message;
		
		button = new JButton();
		
		button = new JButton(){
			protected void paintComponent(Graphics g){
				g.drawImage(new ImageIcon(image).getImage(), 0, 0, width, height, null);
				g.drawString(setStringFont(message), 5, 25);
			}
		};
		
		button.setPreferredSize(new Dimension(width, height));
		return button;
	}
	
	public JButton getButton(){
		return button;
	}
	
	public JLabel getLabel(){
		return label;
	}

	public JPanel getPanel(){
		return panel;
	}
	
	public JLabel getLabel(final String message, final String image, final int width, final int height){
		this.message = message;
		
		label = new JLabel();
		
		label = new JLabel(){
			protected void paintComponent(Graphics g){
				g.drawImage(new ImageIcon(image).getImage(), 0, 0, width, height, null);
				g.drawString(setStringFont(message), 5, 25);
			}
		};
		
		label.setPreferredSize(new Dimension(width, height));
		return label;
	}
	
	public JLabel getLabel(final String message, final ImageIcon image, final int width, final int height){
		this.message = message;
		
		label = new JLabel();
		
		label = new JLabel(){
			protected void paintComponent(Graphics g){
				g.drawImage(image.getImage(), 0, 0, width, height, null);
				g.drawString(setStringFont(message), 5, 25);
			}
		};
		
		label.setPreferredSize(new Dimension(width, height));
		return label;
	}
	

	public JPanel getPanel(final String image, final int width, final int height){
		if(checkExt(image).equalsIgnoreCase("gif")){
			final Image gifImage = Toolkit.getDefaultToolkit().createImage(image);
			this.message = "";
			this.panel = new JPanel(){
				  public void paintComponent(Graphics g) 
				  {
				    super.paintComponent(g);
				    Dimension d = getSize();
				    if (image != null)
				    {
				      g.drawImage(gifImage, 0, 0, width, height, this);
				    }
				  }
			    };
		}
		else{
			this.message = "";
			this.panel = new JPanel(){
				protected void paintComponent(Graphics g){
					g.drawImage(new ImageIcon(image).getImage(), 0, 0, width, height, null);
				}
			};
		}
		panel.setPreferredSize(new Dimension(width, height));
		return panel;
	}
	
	
	public JPanel getPanel(final ImageIcon image, final int width, final int height){
		this.message = "";
		this.panel = new JPanel(){
			protected void paintComponent(Graphics g){
				g.drawImage(image.getImage(), 0, 0, width, height, null);
			}
		};
		panel.setPreferredSize(new Dimension(width, height));
		return panel;
	}
	
	public AttributedCharacterIterator setStringFont(String message){
		try{
			AttributedString aString = new AttributedString(message);
			aString.addAttribute(TextAttribute.FONT, font, 0, message.length()); 
			aString.addAttribute(TextAttribute.FOREGROUND, Color.WHITE, 0, message.length());
			AttributedCharacterIterator name2 =  aString.getIterator();
			return name2;
		}catch(IllegalArgumentException IAE){
			AttributedString aString = new AttributedString(" ");
			aString.addAttribute(TextAttribute.FONT, font, 0, 1); 
			aString.addAttribute(TextAttribute.FOREGROUND, Color.WHITE, 0, 1);
			AttributedCharacterIterator name2 =  aString.getIterator();
			return name2;
		}catch(NullPointerException IAE){
			AttributedString aString = new AttributedString(" ");
			aString.addAttribute(TextAttribute.FONT, font, 0, 1); 
			aString.addAttribute(TextAttribute.FOREGROUND, Color.WHITE, 0, 1);
			AttributedCharacterIterator name2 =  aString.getIterator();
			return name2;
		}
	}
	
	public String checkExt(String fileName){		
		return fileName.substring(fileName.length()-3, fileName.length());
	}
	
	public AttributedCharacterIterator setStringFont(){
		try{
			AttributedString aString = new AttributedString(message);
			aString.addAttribute(TextAttribute.FONT, font, 0, message.length()); 
			aString.addAttribute(TextAttribute.FOREGROUND, Color.WHITE, 0, message.length());
			AttributedCharacterIterator name2 =  aString.getIterator();
			return name2;
		}catch(IllegalArgumentException IAE){
			AttributedString aString = new AttributedString(" ");
			aString.addAttribute(TextAttribute.FONT, font, 0, 1); 
			aString.addAttribute(TextAttribute.FOREGROUND, Color.WHITE, 0, 1);
			AttributedCharacterIterator name2 =  aString.getIterator();
			return name2;
		}catch(NullPointerException IAE){
			AttributedString aString = new AttributedString(" ");
			aString.addAttribute(TextAttribute.FONT, font, 0, 1); 
			aString.addAttribute(TextAttribute.FOREGROUND, Color.WHITE, 0, 1);
			AttributedCharacterIterator name2 =  aString.getIterator();
			return name2;
		}
	}
}

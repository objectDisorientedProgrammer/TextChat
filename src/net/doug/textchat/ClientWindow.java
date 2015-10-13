package net.doug.textchat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Stack;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.localarea.network.doug.WebsiteTimerGUIelement;

public class ClientWindow
{
	private JFrame mainWindow;
	private JPanel mainPanel;
	private String frameTitle = "TextChat";
	private int frameWidth = 300;
	private int frameHeight = 600;
	private String author = "Doug Chidester";
	private String version = " v0.0.4b";
	
	private PropertiesFrame properties;
	
	private JTextArea chatArea;
	private String welcomeMessage = "\tWelcome to TextChat!\nType '/help' to get started\n\n";
	private int chatboxRows = 12;
	private int chatboxColumns = 30;
	private JScrollPane chatAreaScrollPane;
	
	private JTextField chatbox;
	//private ArrayList<String> chatHistory;
	private Stack<String> chatHistory;
	private int historyCounter = 0;
	private String[] commandList = { "/help", "/hist [clear]", "/setcolor [text] [background]", "/setname name", "/clear"};
	
	private String username = "default";

	public ClientWindow()
	{
		super();
		mainWindow = new JFrame(frameTitle);
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		properties = new PropertiesFrame(mainWindow, this);
		mainPanel = new JPanel(new GridLayout(0, 1, 5, 5));
		
		//chatHistory = new ArrayList<String>(10); TODO
		chatHistory = new Stack<String>();
		
		createAndShowGUI();
		
		
		mainWindow.pack();
		mainWindow.setLocationRelativeTo(null);
		// show mainWindow
		mainWindow.setVisible(true);
	}

	private void createAndShowGUI()
	{
		// add buttons
		JPanel buttonPanel = new JPanel(new FlowLayout());
		
		// chat textfield
		chatbox = new JTextField(15);
		chatbox.addActionListener(new ChatListener());
		chatbox.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent arg0) {}
			@Override
			public void keyReleased(KeyEvent arg0) {}
			@Override
			public void keyPressed(KeyEvent ke) {
				if(ke.getKeyCode() == KeyEvent.VK_UP)
					chatbox.setText(chatHistory.peek());
			}
		});
		buttonPanel.add(chatbox);
		
		// send button
		JButton send = new JButton("Send");
		send.addActionListener(new ChatListener());
		buttonPanel.add(send);
		
		JButton propertiesButton = new JButton("properties");
		propertiesButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// make class PropertiesFrame...
				//if(frame not created yet)
				//  propFrame = new PropertiesFrame(reference to this) // hide on close
				//else
				//  setvisible(true)
				properties.showFrame(true);
			}
		});
		buttonPanel.add(propertiesButton);
		
		// add chat box
		JPanel chatPanel = new JPanel(new BorderLayout(2, 2));
		
		// text area
		chatArea = new JTextArea(/*welcomeMessage, */chatboxRows, chatboxColumns);
		chatArea.append(welcomeMessage); // will auto-scroll without welcomeMessage in constructor
		chatArea.setEditable(false);
		chatAreaScrollPane = new JScrollPane(chatArea);
		chatAreaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		chatPanel.add(chatAreaScrollPane);
		
		mainPanel.add(chatPanel);
		mainPanel.add(buttonPanel);
		
		mainWindow.add(mainPanel);
	}
	
	private void createAndAddMenuBar()
	{
		JMenuBar menuBar = new JMenuBar();
		mainWindow.setJMenuBar(menuBar);
		
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);
		menuBar.add(fileMenu);
		
		JMenuItem saveMenuItem = new JMenuItem("Save");
		saveMenuItem.setMnemonic(KeyEvent.VK_S);
		saveMenuItem.setIcon(new ImageIcon(this.getClass().getResource(imagePath + "save.png")));
		saveMenuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				//writeToFile(filenameTextfield.getText()); // File -> Save
			}
		});
		fileMenu.add(saveMenuItem);
		
		JMenuItem quitMenuItem = new JMenuItem("Quit", new ImageIcon(this.getClass().getResource(imagePath+"exit.png")));
		quitMenuItem.setMnemonic(KeyEvent.VK_Q);
		quitMenuItem.addActionListener(new ActionListener()
		{
            public void actionPerformed(ActionEvent e)
            {
                // save data and close program if user clicks: File -> Quit
            	//writeToFile(filenameTextfield.getText());
                mainWindow.dispose();
            }
		});
		fileMenu.add(quitMenuItem);
		
		JMenu optionsMenu = new JMenu("Options");
		optionsMenu.setMnemonic(KeyEvent.VK_O);
		menuBar.add(optionsMenu);
		
		JMenu helpMenu = new JMenu("Help");
		helpMenu.setMnemonic(KeyEvent.VK_H);
		menuBar.add(helpMenu);
		
		JMenuItem helpMenuItem = new JMenuItem("Getting Started", new ImageIcon(this.getClass().getResource(imagePath+"help.png")));
		helpMenuItem.setMnemonic(KeyEvent.VK_G);
		helpMenuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// show basic use instructions if user clicks: Help -> Getting Started
                JOptionPane.showMessageDialog(null, "halp msg", "Usage",
						JOptionPane.PLAIN_MESSAGE, new ImageIcon(this.getClass().getResource(imagePath+"help64.png")));
			}
		});
		helpMenu.add(helpMenuItem);
		
		JMenuItem aboutMenuItem = new JMenuItem("About", new ImageIcon(this.getClass().getResource(imagePath+"about.png")));
		aboutMenuItem.setMnemonic(KeyEvent.VK_A);
		aboutMenuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// show author and version if user clicks: Help -> About
				JOptionPane.showMessageDialog(null, "Created by " + author + "\nVersion " + version, "About",
						JOptionPane.INFORMATION_MESSAGE, new ImageIcon(this.getClass().getResource(imagePath+"person.png")));
			}
		});
		helpMenu.add(aboutMenuItem);
	}
	
	/**
	 * Create a formatted string to add to the command history.
	 * @param addThis - string to append.
	 * @return The string created.
	 */
	private String makeHistoryString(String addThis)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("  ");
		sb.append(++historyCounter);
		sb.append(": ");
		sb.append(addThis);
		
		return sb.toString();
	}
	
	private void evaluateCommand(String[] args)
	{
		if(args[0].trim().equalsIgnoreCase("/setname"))
			username = args[1].trim();
		else if(args[0].trim().equalsIgnoreCase("/setcolor"))
		{
			String textColor = args[1].trim();
			String bgColor = null;
			if(args.length > 2)
			{
				bgColor = args[2].trim();
				bgColor.toLowerCase();
				// background color options
				setBackgroundColor(stringToColor(bgColor));
				// TODO update comboboxes in propertiesFrame
			}
			textColor.toLowerCase();
			// text color options
			setTextColor(stringToColor(textColor));
			// TODO update comboboxes in propertiesFrame
		}
		else if(args[0].trim().equalsIgnoreCase("/hist"))
			if(args[1].trim().equalsIgnoreCase("clear"))
			{
				chatHistory.clear();
				historyCounter = 0;
			}
	}
	
	/**
	 * Convert a color string to a usable color.
	 * @param s - name of color as string
	 * @return Color
	 */
	private Color stringToColor(String s)
	{
		Color choice = null;
		switch(s)
		{
		case "black": choice = Color.black; break;
		case "grey": choice = Color.gray; break;
		case "gray": choice = Color.gray; break;
		case "red": choice = Color.red; break;
		case "pink": choice = Color.pink; break;
		case "orange": choice = Color.orange; break;
		case "green": choice = Color.green; break;
		case "blue": choice = Color.blue; break;
		case "magenta": choice = Color.magenta; break;
		case "cyan": choice = Color.cyan; break;
		case "white": choice = Color.white; break;
		default: break;
		}
		return choice;
	}
	
	/**
	 * Print a list of commands.
	 */
	private void printHelp()
	{
		chatArea.append("\nList of commands:\n");
		for(String s : commandList)
			chatArea.append(s+"\n");
		chatArea.append("\n");
	}
	
	public void setTextColor(Color fg)
	{
		if(chatArea.getBackground() == fg)
			chatArea.append("\nCannot set text & background color to the same thing.\n"); // almost certain this will never trigger (as of oct 1, 2014)
		else
			chatArea.setForeground(fg);
	}
	
	public void setBackgroundColor(Color bg)
	{
		if(chatArea.getForeground() == bg)
			chatArea.append("\nCannot set text & background color to the same thing.\n");
		else
			chatArea.setBackground(bg);
	}
	
	public void setUsername(String name)
	{
		this.username = name;
	}
	
	/**
	 * ChatListener class. Handles chatbox and send button.
	 * @author Doug
	 *
	 */
	private class ChatListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			String message = chatbox.getText();
			String parse[] = message.split(" ");
			
			if(parse.length >= 2)
				evaluateCommand(parse);
			else if(parse[0].trim().equalsIgnoreCase("/hist"))
			{
				//chatHistory.add(makeHistoryString(chatbox.getText()));
				//chatHistory.push(makeHistoryString(chatbox.getText()));//TODO
				chatHistory.push(chatbox.getText());
				for(String s : chatHistory)
					chatArea.append(makeHistoryString(s)+"\n");
			}
			else if(parse[0].trim().equalsIgnoreCase("/help"))
				printHelp();
			else if(parse[0].trim().equalsIgnoreCase("/setcolor")) // set default colors
			{
				chatArea.setForeground(Color.black);
				chatArea.setBackground(Color.white);
			}
			else if(parse[0].trim().equalsIgnoreCase("/clear")) // delete all current text
				chatArea.setText(null);
				
			message = "["+username+"] "+chatbox.getText()+"\n";
			chatArea.append(message);
			//chatHistory.add(makeHistoryString(chatbox.getText()));
			//chatHistory.push(makeHistoryString(chatbox.getText()));
			chatHistory.push(chatbox.getText());//TODO
			
			chatbox.setText("");
		}
	}
}

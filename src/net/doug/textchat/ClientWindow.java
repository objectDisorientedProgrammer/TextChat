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

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ClientWindow
{
	private JFrame mainWindow;
	private JPanel mainPanel;
	private String frameTitle = "TextChat";
	private int frameWidth = 300;
	private int frameHeight = 600;
	private String author = "Doug Chidester";
	private String version = " v0.0.3b";
	
	private PropertiesFrame properties;
	
	private JTextArea chatArea;
	private String welcomeMessage = "\tWelcome to TextChat!\nType '/help' to get started\n";
	private int chatboxRows = 12;
	private int chatboxColumns = 30;
	private JScrollPane chatAreaScrollPane;
	
	private JTextField chatbox;
	//private ArrayList<String> chatHistory;
	private Stack<String> chatHistory;
	private int historyCounter = 0;
	private String[] commandList = { "/help", "/hist [clear]", "/setcolor [text] [background]", "/setname name"};
	
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
				switch(bgColor)
				{
				case "black": chatArea.setBackground(Color.black); break;
				case "grey": chatArea.setBackground(Color.gray); break;
				case "gray": chatArea.setBackground(Color.gray); break;
				case "red": chatArea.setBackground(Color.red); break;
				case "orange": chatArea.setBackground(Color.orange); break;
				case "green": chatArea.setBackground(Color.green); break;
				case "pink": chatArea.setBackground(Color.pink); break;
				case "blue": chatArea.setBackground(Color.blue); break;
				case "purple": chatArea.setBackground(Color.magenta); break;
				case "cyan": chatArea.setBackground(Color.cyan); break;
				case "white": chatArea.setBackground(Color.white); break;

				default: chatArea.append("Invalid background color\n"); break;
				}
			}
			textColor.toLowerCase();
			// text color options
			switch(textColor)
			{
			case "black": chatArea.setForeground(Color.black); break;
			case "grey": chatArea.setForeground(Color.gray); break;
			case "gray": chatArea.setForeground(Color.gray); break;
			case "red": chatArea.setForeground(Color.red); break;
			case "pink": chatArea.setForeground(Color.pink); break;
			case "orange": chatArea.setForeground(Color.orange); break;
			case "green": chatArea.setForeground(Color.green); break;
			case "blue": chatArea.setForeground(Color.blue); break;
			case "purple": chatArea.setForeground(Color.magenta); break;
			case "cyan": chatArea.setForeground(Color.cyan); break;
			case "white": chatArea.setForeground(Color.white); break;
			case "hacker": // easter egg
				chatArea.setBackground(Color.black);
				chatArea.setForeground(Color.green);
				break;
			default: chatArea.append("Invalid text color\n"); break;
			}
		}
		else if(args[0].trim().equalsIgnoreCase("/hist"))
			if(args[1].trim().equalsIgnoreCase("clear"))
			{
				chatHistory.clear();
				historyCounter = 0;
			}
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
	
	public void setName(String name)
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
				
			message = "["+username+"] "+chatbox.getText()+"\n";
			chatArea.append(message);
			//chatHistory.add(makeHistoryString(chatbox.getText()));
			//chatHistory.push(makeHistoryString(chatbox.getText()));
			chatHistory.push(chatbox.getText());//TODO
			
			chatbox.setText("");
		}
	}
}

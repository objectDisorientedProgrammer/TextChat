/*
	A simple text based chat application.
    Copyright (C) 2014  Douglas Chidester

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package textchat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ClientWindow
{
	private JFrame mainWindow;
	private JPanel mainPanel;
	private String frameTitle = "TextChat";
	private final int frameWidth = 450;
	private final int frameHeight = 400;
	private String author = "Doug C";
	private String version = " v0.0.9";

	private PropertiesFrame properties;

	private JTextArea chatArea;
	private String welcomeMessage = "\tWelcome to TextChat!\nType '/help' to get started.\n\n";
	private int chatboxRows = 12;
	private int chatboxColumns = 30;
	private JScrollPane chatAreaScrollPane;

	private JTextField chatbox;
	// use an arraylist so we can cycle up/down through history
	private ArrayList<String> chatHistory;
	private int chatHistoryMaxBuffer = 50;
	private int historyIndex = -1;
	private int historyCounter = 0;
	private String[] commandList = { "/help", "/hist [clear]",
			"/setcolor [text] [background]", "/setname name", "/clear" };

	private String username = "default";
	private String imagePath = "/images/"; // path in jar file

	public ClientWindow()
	{
		super();
		mainWindow = new JFrame(frameTitle);
		mainWindow.setSize(frameWidth, frameHeight);
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		properties = new PropertiesFrame(this);
		mainPanel = new JPanel(new BorderLayout(10, 10));

		createAndAddMenuBar();

		chatHistory = new ArrayList<String>(chatHistoryMaxBuffer);

		createAndShowGUI();

		//mainWindow.pack();
		mainWindow.setLocationRelativeTo(null);
		// set the chatbox as default focus (i.e. user can type immediately)
		chatbox.requestFocusInWindow();
		// show mainWindow
		mainWindow.setVisible(true);
	}

	public JFrame getWindow()
	{
		return mainWindow;
	}

	private void createAndShowGUI()
	{
		// add buttons
		JPanel buttonPanel = new JPanel(new BorderLayout(10, 10));

		// chat textfield
		chatbox = new JTextField(15);
		chatbox.addActionListener(new ChatListener());
		chatbox.addKeyListener(new KeyListener()
		{
			@Override
			public void keyTyped(KeyEvent arg0)
			{
			}

			@Override
			public void keyReleased(KeyEvent arg0)
			{
			}

			@Override
			public void keyPressed(KeyEvent ke)
			{
				String command = "End of recent commands";
				if(ke.getKeyCode() == KeyEvent.VK_UP)
				{
					// get a previous message
					if (historyIndex > 0)
						--historyIndex;
					else
						historyIndex = 0;
					command = chatHistory.get(historyIndex);
					chatbox.setText(command);
				}
				else if(ke.getKeyCode() == KeyEvent.VK_DOWN)
				{
					// get a next message
					if (historyIndex < chatHistory.size() - 1)
						++historyIndex;
					else
						historyIndex = chatHistory.size() - 1;
					command = chatHistory.get(historyIndex);
					chatbox.setText(command);
				}
			}
		});
		buttonPanel.add(chatbox, BorderLayout.CENTER);

		// send button
		JButton send = new JButton("Send");
		send.addActionListener(new ChatListener());
		buttonPanel.add(send, BorderLayout.EAST);

		JButton propertiesButton = new JButton("properties");
		propertiesButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// make class PropertiesFrame...
				// if(frame not created yet)
				// propFrame = new PropertiesFrame(reference to this) // hide on
				// close
				// else
				// setvisible(true)
				properties.showFrame(true);
			}
		});
		//buttonPanel.add(propertiesButton);

		// add chat box
		JPanel chatPanel = new JPanel(new BorderLayout(10, 10));

		// text area
		chatArea = new JTextArea(chatboxRows, chatboxColumns);
		chatArea.append(welcomeMessage); // will auto-scroll without
											// welcomeMessage in constructor
		chatArea.setEditable(false);
		chatAreaScrollPane = new JScrollPane(chatArea);
		chatAreaScrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		chatPanel.add(chatAreaScrollPane);

		mainPanel.add(chatPanel, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		mainWindow.add(mainPanel);
	}

	private void createAndAddMenuBar()
	{
		JMenuBar menuBar = new JMenuBar();
		mainWindow.setJMenuBar(menuBar);

		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);
		menuBar.add(fileMenu);

		JMenuItem quitMenuItem = new JMenuItem("Quit", new ImageIcon(this
				.getClass().getResource(imagePath + "exit.png")));
		quitMenuItem.setMnemonic(KeyEvent.VK_Q);
		quitMenuItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				// save data and close program if user clicks: File -> Quit
				// writeToFile(filenameTextfield.getText());

				// request the properties Frame to cleanup/save and exit
				properties.stopExecution();
				mainWindow.dispose();
			}
		});
		fileMenu.add(quitMenuItem);

		// options menu item
		 JMenu optionsMenu = new JMenu("Options");
		 optionsMenu.setMnemonic(KeyEvent.VK_O);
		 menuBar.add(optionsMenu);
		 
		 JMenuItem propertiesItem = new JMenuItem("Properties");
		 propertiesItem.setMnemonic(KeyEvent.VK_P);
		 propertiesItem.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					properties.showFrame(true);
				}
			});
		 optionsMenu.add(propertiesItem);
		 
		 
		 // help menu item
		JMenu helpMenu = new JMenu("Help");
		helpMenu.setMnemonic(KeyEvent.VK_H);
		menuBar.add(helpMenu);

		JMenuItem helpMenuItem = new JMenuItem("Getting Started",
				new ImageIcon(this.getClass().getResource(
						imagePath + "help.png")));
		helpMenuItem.setMnemonic(KeyEvent.VK_G);
		helpMenuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// show basic use instructions if user clicks: Help -> Getting
				// Started
				JOptionPane.showMessageDialog(
						null,
						"Work in progress...",
						"Usage",
						JOptionPane.PLAIN_MESSAGE,
						new ImageIcon(this.getClass().getResource(
								imagePath + "help64.png")));
			}
		});
		helpMenu.add(helpMenuItem);

		JMenuItem aboutMenuItem = new JMenuItem("About", new ImageIcon(this
				.getClass().getResource(imagePath + "about.png")));
		aboutMenuItem.setMnemonic(KeyEvent.VK_A);
		aboutMenuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// show author and version if user clicks: Help -> About
				JOptionPane.showMessageDialog(
						null,
						"Created by " + author + "\nVersion " + version,
						"About",
						JOptionPane.INFORMATION_MESSAGE,
						new ImageIcon(this.getClass().getResource(
								imagePath + "person.png")));
			}
		});
		helpMenu.add(aboutMenuItem);
	}

	/**
	 * Create a formatted string to add to the command history.
	 * 
	 * @param addThis
	 *            - string to append.
	 * @return The string created.
	 */
	private String makeHistoryString(String addThis)
	{
		return " " + (++historyCounter) + ": " + addThis;
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
		} else if(args[0].trim().equalsIgnoreCase("/hist"))
			if(args[1].trim().equalsIgnoreCase("clear"))
			{
				chatHistory.clear();
				historyCounter = 0;
			}
	}

	/**
	 * Convert a color string to a usable color.
	 * 
	 * @param s - name of color as string
     *
	 * @return Color
	 */
	private Color stringToColor(String s)
	{
		Color choice = null;
		switch(s)
		{
			case "black":
				choice = Color.black;
				break;
			case "gray":
			case "grey":
				choice = Color.gray;
				break;
			case "red":
				choice = Color.red;
				break;
			case "pink":
				choice = Color.pink;
				break;
			case "orange":
				choice = Color.orange;
				break;
			case "green":
				choice = Color.green;
				break;
			case "blue":
				choice = Color.blue;
				break;
			case "magenta":
				choice = Color.magenta;
				break;
			case "cyan":
				choice = Color.cyan;
				break;
			case "white":
			default:
                choice = Color.white;
				break;
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
			chatArea.append(s + "\n");
		chatArea.append("\n");
	}

	public void setTextColor(Color fg)
	{
		if(chatArea.getBackground() == fg)
			chatArea.append("\nCannot make text & background color to the same.\n");
		else
			chatArea.setForeground(fg);
	}

	public void setBackgroundColor(Color bg)
	{
		if(chatArea.getForeground() == bg)
			chatArea.append("\nCannot make text & background color to the same.\n");
		else
			chatArea.setBackground(bg);
	}

	public void setUsername(String name)
	{
		this.username = name;
	}
	
	public String getUsername()
	{
		return this.username;
	}

	/**
	 * ChatListener class. Handles chatbox and send button.
	 * 
	 * @author Doug
	 *
	 */
	private class ChatListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			String message = chatbox.getText();
			message = message.trim();
			String parse[] = message.split(" ");

			if(parse.length >= 2)
				evaluateCommand(parse);
			// print command history
			else if(parse[0].trim().equalsIgnoreCase("/hist"))
			{
				for(String s : chatHistory)
					chatArea.append(makeHistoryString(s) + "\n");
			}
			// show help options
			else if(parse[0].trim().equalsIgnoreCase("/help"))
				printHelp();
			// set default colors
			else if(parse[0].trim().equalsIgnoreCase("/setcolor"))
			{
				chatArea.setForeground(Color.black);
				chatArea.setBackground(Color.white);
			}
			// delete all text from the chat area
			else if(parse[0].trim().equalsIgnoreCase("/clear"))
				chatArea.setText("");

			// save the message to the history buffer
			chatHistory.add(message);
			historyIndex = chatHistory.size(); // do not use -1 here since the history scrolling logic adjusts the index
			
			chatArea.append("[" + username + "] " + chatbox.getText() + "\n");

			chatbox.setText("");
		}
	}
}

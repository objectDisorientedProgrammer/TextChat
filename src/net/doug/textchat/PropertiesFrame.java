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
package net.doug.textchat;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class PropertiesFrame
{
	private JFrame propertiesFrame;
	private JPanel propertiesPanel;
	private String frameTitle = "Properties";
	private int frameWidth = 300;
	private int frameHeight = 200;
	
	private ClientWindow client;
	
	private JTextField nameTF;
	private String defaultName = "default username";
	private JButton saveButton;
	private JComboBox<String> textColorChoices;
	private JComboBox<String> bgColorChoices;
	private String[] colors = {"black", "grey", "red", "pink", "orange", "green", "blue", "magenta", "cyan", "white"};
	
	
	public PropertiesFrame(JFrame parent, ClientWindow main)
	{
		super();
		client = main;
		
		propertiesFrame = new JFrame(frameTitle);
		propertiesFrame.setLocationRelativeTo(parent);
		propertiesFrame.setSize(frameWidth, frameHeight);
		propertiesFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		
		propertiesPanel = new JPanel(new FlowLayout());
		
		createUI();
		addUI();
		
		
	}
	
	/**
	 * Add UI elements to a panel and the panel to a frame.
	 */
	private void addUI()
	{
		propertiesPanel.add(nameTF);
		propertiesPanel.add(textColorChoices);
		propertiesPanel.add(bgColorChoices);
		propertiesPanel.add(saveButton);
		
		propertiesFrame.add(propertiesPanel);
	}

	/**
	 * Create UI elements and set their funtionality.
	 */
	private void createUI()
	{
		nameTF = new JTextField(defaultName, 20);
		nameTF.setToolTipText("Enter a username here.");
		nameTF.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// set username when user hits <enter>
				String name = nameTF.getText();
				if(name != null && !name.equalsIgnoreCase("") && !name.equalsIgnoreCase(defaultName))
					client.setUsername(name);
			}
		});
		
		saveButton = new JButton("Apply");
		saveButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent ae)
			{
				client.setTextColor(stringToColor( (String) textColorChoices.getSelectedItem()));
				client.setBackgroundColor(stringToColor( (String) bgColorChoices.getSelectedItem()));
			}
		});
		
		textColorChoices = new JComboBox<String>(colors);
		textColorChoices.setToolTipText("Text Color");
		
		bgColorChoices = new JComboBox<String>(colors);
		bgColorChoices.setToolTipText("Background Color");
		bgColorChoices.setSelectedIndex(colors.length - 1); // set to show 'white' as bg color
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
	 * Show and hide the JFrame.
	 * @param isVisible - true to show, false to hide.
	 */
	public void showFrame(boolean isVisible)
	{
		propertiesFrame.setVisible(isVisible);
	}
}

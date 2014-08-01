package net.doug.textchat;

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
	private String[] colors = {"black", "grey", "red", "pink", "orange", "green", "blue", "purple", "cyan", "white"};
	
	
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
		
		saveButton = new JButton("Apply");
		saveButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent ae)
			{
				String name = nameTF.getText();
				if(name != null && name.equalsIgnoreCase("") && name.equalsIgnoreCase(defaultName))
					client.setName(name);
				
				// TODO add code to send values of comboboxes to client
				/*
				 * Maybe a better way to do this part is to have booleans for each component...
				 * if one of the components change, set that bool & only update that one
				 * 
				 * OR
				 * have each component with its own actionListener to update clientwindow
				 */
			}
		});
		
		textColorChoices = new JComboBox<String>(colors);
		textColorChoices.setToolTipText("Text Color");
		
		bgColorChoices = new JComboBox<String>(colors);
		bgColorChoices.setToolTipText("Background Color");
		bgColorChoices.setSelectedIndex(colors.length - 1); // set to show 'white' as bg color
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

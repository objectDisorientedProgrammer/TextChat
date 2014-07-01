package net.doug.textchat;

public class TextChatMain
{
	public static void main(String[] args)
	{
		javax.swing.SwingUtilities.invokeLater(new Runnable()
		{
            public void run()
            {
            	new ClientWindow();
            }
        });
	}
}

package View;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.JTextArea;
import javax.swing.JScrollBar;

public class SearchEngineUI {

	private JFrame frmLuceneSearchEngine;
	private JTextField searchQuery;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SearchEngineUI window = new SearchEngineUI();
					window.frmLuceneSearchEngine.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public SearchEngineUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmLuceneSearchEngine = new JFrame();
		frmLuceneSearchEngine.setTitle("Lucene Search Engine");
		frmLuceneSearchEngine.setBounds(100, 100, 450, 300);
		frmLuceneSearchEngine.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmLuceneSearchEngine.getContentPane().setLayout(null);
		
		JButton btnNewButton = new JButton("Search");
		btnNewButton.setForeground(UIManager.getColor("Focus.color"));
		btnNewButton.setBounds(307, 45, 103, 30);
		frmLuceneSearchEngine.getContentPane().add(btnNewButton);
		btnNewButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String query = searchQuery.getText();
				if (query!=null && query.length()>0) {
					//search
				}else {
					
				}
				
			}
		});
		
		searchQuery = new JTextField();
		searchQuery.setBounds(26, 46, 268, 26);
		frmLuceneSearchEngine.getContentPane().add(searchQuery);
		searchQuery.setColumns(10);
		
		JTextArea textArea = new JTextArea();
		textArea.setBounds(30, 103, 380, 148);
		frmLuceneSearchEngine.getContentPane().add(textArea);
		
		JScrollBar scrollBarVertical = new JScrollBar();
		scrollBarVertical.setBounds(395, 103, 15, 148);
		frmLuceneSearchEngine.getContentPane().add(scrollBarVertical);
		
		JScrollBar scrollBarHorizontal = new JScrollBar();
		scrollBarHorizontal.setOrientation(JScrollBar.HORIZONTAL);
		scrollBarHorizontal.setBounds(100, 155, 15, 96);
		frmLuceneSearchEngine.getContentPane().add(scrollBarHorizontal);
		
		
		
	
	}
}

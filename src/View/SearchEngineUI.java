/**
 *
 * @author nazifa115
 */

package View;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

import Controller.ActionListeners.BuildIndexButtonListener;
import Controller.ActionListeners.SearchButtonActionListener;
import Model.Constants;

public class SearchEngineUI{

	private JFrame frmLuceneSearchEngine;
	private JTextField searchQuery;
	private JTextArea resultTextArea;
	public JComboBox<String> selectDataSetComboBox;
	private JButton btnBuildIndex;
	private JButton searchButton;

	public String getSearchQuery() {
		System.out.println("seSearchQuery: " + searchQuery.getText());
		return this.searchQuery.getText();
	}

	public void setSearchQuery(JTextField searchQuery) {
		this.searchQuery = searchQuery;
	}

	public JTextArea getResultTextArea() {
		return resultTextArea;
	}

	public void setResultTextArea(JTextArea resultTextArea) {
		this.resultTextArea = resultTextArea;
	}

	public JButton getSearchButton() {
		return searchButton;
	}

	public void setSearchButton(JButton searchButton) {
		this.searchButton = searchButton;
	}

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

		selectDataSetComboBox = new JComboBox<String>();
		selectDataSetComboBox.setToolTipText("Select any one of the following datasets");
		selectDataSetComboBox.setModel(new DefaultComboBoxModel<String>(new String[] { "Cranfield", "Medline" }));
		selectDataSetComboBox.setMaximumRowCount(2);
		selectDataSetComboBox.setBounds(30, 22, 268, 27);
		frmLuceneSearchEngine.getContentPane().add(selectDataSetComboBox);
		selectDataSetComboBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Constants.DATASET = selectDataSetComboBox.getSelectedItem().toString();
				System.out.println("from UI: " + Constants.DATASET);
				Constants.INPUTFILESDIRECTORY = Constants.ROOTDIR + Constants.DATASET;
				System.out.println("from UI: " + Constants.INPUTFILESDIRECTORY);
			}
		});

		btnBuildIndex = new JButton("Build Index");
		btnBuildIndex.addActionListener(new BuildIndexButtonListener());
		btnBuildIndex.setForeground(UIManager.getColor("Focus.color"));
		btnBuildIndex.setBounds(303, 21, 105, 30);
		frmLuceneSearchEngine.getContentPane().add(btnBuildIndex);

		searchQuery = new JTextField();
		searchQuery.setBounds(30, 61, 268, 26);
		frmLuceneSearchEngine.getContentPane().add(searchQuery);
		searchQuery.setColumns(10);

		resultTextArea = new JTextArea();
		resultTextArea.setBounds(30, 103, 380, 148);
		resultTextArea.setEditable(false);
		frmLuceneSearchEngine.getContentPane().add(resultTextArea);
		resultTextArea.setAutoscrolls(true);

		searchButton = new JButton("Search");
		searchButton.setForeground(UIManager.getColor("Focus.color"));
		searchButton.setBounds(303, 60, 105, 30);
		frmLuceneSearchEngine.getContentPane().add(searchButton);

		searchButton.addActionListener(new SearchButtonActionListener(SearchEngineUI.this, resultTextArea));

	}

}

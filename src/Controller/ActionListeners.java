package Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.JTextArea;

import Model.Constants;
import View.SearchEngineUI;

public class ActionListeners {
	public static class BuildIndexButtonListener implements ActionListener {

		String dataset;

		public BuildIndexButtonListener() {

			this.dataset = Constants.DATASET;
			System.out.println(this.dataset + " is selected");
		}

		private void createIndexFolderIfNonExisting() {
			/*
			 * if (dataset.contains("Medline")) { Constants.INPUTFILESDIRECTORY
			 * = Constants.INPUTFILESDIRECTORY + "Medline1033Dataset"; } else {
			 * Constants.INPUTFILESDIRECTORY = Constants.INPUTFILESDIRECTORY +
			 * "CranfieldDataset"; }
			 */

			File indexFilesDirectory = new File(Constants.INDEXFILEDIRECTORY + Constants.DATASET);
			if (!indexFilesDirectory.exists()) {
				System.out.println(indexFilesDirectory + " does not exist. Creating Directory...");
				try {
					File tempFile = new File(indexFilesDirectory + Constants.DATASET);
					System.out.println(tempFile.getParentFile().getCanonicalPath() + " created.");
				} catch (IOException e) {
					System.out.println(e);
					e.printStackTrace();
				}
			}
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			createIndexFolderIfNonExisting();

			if (Constants.INPUTFILESDIRECTORY != null) {
				if (Constants.DATASET == null) {
					new Indexer(Constants.INPUTFILESDIRECTORY + Constants.Cranfield_DATASET,
							Constants.INDEXFILEDIRECTORY);
				} else if (Constants.DATASET.equalsIgnoreCase("Cranfield")) {
					new Indexer(Constants.INPUTFILESDIRECTORY, Constants.INDEXFILEDIRECTORY);
				}
			}
		}
	}

	public static class SearchButtonActionListener implements ActionListener {
		String queryText;
		JTextArea resultArea;
		BufferedWriter writer = null;
		SearchEngineUI ui;

		public SearchButtonActionListener(SearchEngineUI seui, JTextArea rTArea) {
			System.out.println("Query text: " + queryText);
			this.ui = seui;
			this.resultArea = rTArea;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			resultArea.removeAll();
			this.queryText = ui
					.getSearchQuery();/*
										 * The mistake I did here was to set the
										 * 'queryText variable in the
										 * constructor of
										 * SearchButtonActionListener. I thought
										 * the listener gets instantiated every
										 * time the 'search' button is clicked.
										 * Which is a fundamental mistake. The
										 * listener gets initiated only when the
										 * SearchEngineUI.initialize() is
										 * called, which in this case is called
										 * only once in my main(). Took me a
										 * while to debug the error why I wasn't
										 * being able to pass the search query
										 * on to the listener class.
										 */
			resultArea.setText("Showing result for query text: " + queryText);
			if (queryText.length() > 0) {
				search(queryText);
			}
		}

		private void search(String queryText2) {
			writer = initWriter();
		}

		private BufferedWriter initWriter() {
			String path = Constants.OUTPUTFILESDIRECTORY + Constants.DATASET + "_results.txt";
			try {
				if (Files.exists(Paths.get(path))) {
					writer = new BufferedWriter(new FileWriter(path, true));
				} else {
					File f = new File(path);
					writer = new BufferedWriter(new FileWriter(f.getCanonicalPath(), true));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return writer;
		}
	}
}

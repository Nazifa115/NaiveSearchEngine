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
					new Indexer(Constants.INPUTFILESDIRECTORY + Constants.Cranfield_DATASET, Constants.INDEXFILEDIRECTORY);
				}else  if (Constants.DATASET.equalsIgnoreCase("Cranfield")) {
					new Indexer(Constants.INPUTFILESDIRECTORY, Constants.INDEXFILEDIRECTORY);
				}
			}
		}
	}

	public static class SearchButtonActionListener implements ActionListener {
		String queryText;
		JTextArea resultArea;
		BufferedWriter writer = null;
		
		public SearchButtonActionListener(String qText, JTextArea rTArea) {
			this.queryText = qText;
			this.resultArea = rTArea;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			resultArea.removeAll();
			resultArea.setText("Showing result:");
			if (queryText.length() > 0){
				search(queryText);
			}
		}

		private void search(String queryText2) {
			writer = initWriter();
		}

		private BufferedWriter initWriter() {
			String path = Constants.OUTPUTFILESDIRECTORY+ Constants.DATASET + "_results.txt";
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

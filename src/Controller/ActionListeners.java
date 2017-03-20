package Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import Model.Constants;
import Model.Indexer;
import View.SearchEngineUI;
import edu.wright.dase.model.CONSTANTS;

public class ActionListeners {
	public static class BuildIndexButtonListener implements ActionListener {

		String dataset;

		public BuildIndexButtonListener() {
			
			this.dataset = Constants.DATASET;
			System.out.println(this.dataset + " is selected");
		}

		private void createIndexFolderIfNonExisting() {
			/*if (dataset.contains("Medline")) {
				Constants.INPUTFILESDIRECTORY = Constants.INPUTFILESDIRECTORY + "Medline1033Dataset";
			} else {
				Constants.INPUTFILESDIRECTORY = Constants.INPUTFILESDIRECTORY + "CranfieldDataset";
			}*/

			File indexFilesDirectory = new File(Constants.INDEXFILEDIRECTORY + Constants.DATASET);
			if (!indexFilesDirectory.exists()) {
				System.out.println(
						Constants.INDEXFILEDIRECTORY + Constants.DATASET + " does not exist. Creating Directory...");
				try {
					File file = File.createTempFile("Indices", null);
					//CONSTANTS.INDEXPATH = file.getParentFile().getCanonicalPath() + "/Indexes";

					System.out.println(file.getParentFile().getCanonicalPath() + " created.");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}

		@Override
		public void actionPerformed(ActionEvent e) {
			createIndexFolderIfNonExisting();

			if (Constants.INPUTFILESDIRECTORY != null) {
				new Indexer(Constants.INPUTFILESDIRECTORY, Constants.INDEXFILEDIRECTORY);
			}
		}

	}

	public static class SearchButtonActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// String path = Constants.RESULTSPATH + "/result.txt";
			String path = "/result.txt";
			BufferedWriter writer = null;
			try {
				if (Files.exists(Paths.get(path))) {

					writer = new BufferedWriter(new FileWriter(path));
				} else {
					File f = new File(path);
					writer = new BufferedWriter(new FileWriter(f.getCanonicalPath(), true));
				}
			} catch (IOException ex1) {
				ex1.printStackTrace();
			}
		}
	}
}

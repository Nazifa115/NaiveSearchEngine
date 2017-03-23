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

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

import Model.Constants;
import Model.CustomAnalyzer;
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
		
		int counter = 0;
		int totalDcsRetrieved = 0;
		IndexSearcher searcher = null;
		MultiFieldQueryParser mfqparser = null;

		public SearchButtonActionListener(SearchEngineUI seui, JTextArea rTArea) {
			System.out.println("Query text: " + queryText);
			this.ui = seui;
			this.resultArea = rTArea;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			resultArea.removeAll();
			try {
				searcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(Paths.get(Constants.INDEXFILEDIRECTORY))));
				System.out.println("Total indexed docs: " + searcher.getIndexReader().numDocs());
			} catch (IOException e1) {
			}
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

		private void search(String s) {
			writer = initWriter();
			try {
				writer.write(".Q " + (counter + 1) + "\n " + s + "\n");
				TopDocs topDocs = performSearch(s);
				ScoreDoc docs[] = topDocs.scoreDocs;
				int totalHits = topDocs.totalHits;
				System.out.println(totalHits + " docs have been retrieved.");

				writer.close();
				if (docs.length>0) {
					displayResults(topDocs,docs);
				}else {
					System.out.println("No documents contain the given query.");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		private void displayResults(TopDocs topDocs, ScoreDoc[] docs) {
			try {
				writer = initWriter();
				writer.write("Id \t OutputDocs \t Score \n");
				double score =-1;
				for (int i = 0; i < docs.length; i++) {
					Document doc = getDocumentAttributes(docs[i].doc);
					if (docs[i].score > 0.1 && docs[i].score!=score) {
						score = docs[i].score; /*Why does the same doc gets retrieved multiple times?*/
						//System.out.println("doc: " + doc.get("id") + " score: " + docs[i].score);
						writer.write(counter + 1 + "\t" + doc.get("id") + docs[i].score + "\n");
					}
				}
				counter++;
				writer.write("\t\t" + "\n");
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		private Document getDocumentAttributes(int docID) {
			try {
				return searcher.doc(docID);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		private TopDocs performSearch(String queryString) {
			/*Build Query*/
			Query query = buildQuery(queryString);
			/*Search Query*/
			TopDocs topDocs = null;
			try {
				topDocs = searcher.search(query, 100000);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return topDocs;
			
		}

		private Query buildQuery(String queryString) {
			mfqparser = new MultiFieldQueryParser(new String[] { "title", "abstract", "content" }, new CustomAnalyzer());
			/*mfqparser = new MultiFieldQueryParser(new String[] { "title", "abstract", "content" }, new StandardAnalyzer());*/
			Query query;
			try {
				query = mfqparser.parse(queryString);
				System.out.println("Query after build: " + query.toString());
				return query;
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		private BufferedWriter initWriter() {
			/*String path = Constants.OUTPUTFILESDIRECTORY + Constants.DATASET + "_StandardAnalyzer_results.txt";*/
			String path = Constants.OUTPUTFILESDIRECTORY + Constants.DATASET + "_CustomAnalyzer_results.txt";
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

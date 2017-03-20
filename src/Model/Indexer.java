package Model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FilenameUtils;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;


public class Indexer {
	private IndexWriter indexwriter;
	private File inputFilesDirectory;
	private File indexFilesDirectory;

	public Indexer(String iNPUTFILESDIRECTORY, String iNDEXPATH) {
		this.inputFilesDirectory = new File(iNPUTFILESDIRECTORY);

		this.indexFilesDirectory = new File(iNDEXPATH);
		rebuildIndex();
		closeIndexWriter();
	}

	private void closeIndexWriter() {
		if (indexwriter != null) {
			try {
				indexwriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void rebuildIndex() {

		getIndexWriter();

		String path = inputFilesDirectory.toString();
		// logger.info("Started Indexing using text files of " + path + "
		// folder.");

		try {
			Files.walk(Paths.get(path)).forEach((Path filepath) -> {
				if (Files.isRegularFile(filepath)) {
					String extension = FilenameUtils.getExtension(filepath.toString());
					if (extension.toLowerCase().equals("txt")) {
						try {
							updateIndex(filepath.toFile());
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
						// logger.error("Not a text file. " + filepath.toString());
					}
				} else {
					// logger.error("Not File: " + filepath);
				}
			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// logger.info("Indexing successfull. ");

	}

	private void updateIndex(File file) throws IOException {
		IndexWriter writer = getIndexWriter();
		writer.addDocument(getDocument(file));
	}
	/**
	 * Take a text file as input and convert that file as Lucene Document
	 * 
	 * content[0] = title[.T ], content[1] = abstract[.A ], content[2] =
	 * branch[.B ], content[3] = Words [.W ]
	 * 
	 * @param File
	 * @return Document
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private Document getDocument(File file) throws FileNotFoundException, IOException {
		Document doc = new Document();
		String[] Contents = new String[4];
		if (Constants.DATASET.contains("Cranfield")) {
			Contents = FileContent.getCranfieldFileContentSpilted(file);
		} else {
			Contents = FileContent.getMedlineFileContentSpilted(file);
		}
		
		//Access documents through this ID
		doc.add(new StringField("id", file.getCanonicalPath(), Field.Store.YES));
		
		Field titleField = new TextField("title", Contents[0], Field.Store.NO);
		titleField.setBoost((float) 3);
		doc.add(titleField);

		Field abstractField = new TextField("abstract", Contents[1], Field.Store.NO);
		abstractField.setBoost((float) 2);
		doc.add(abstractField);
		
		Field contentField = new TextField("content", Contents[3], Field.Store.NO);
		contentField.setBoost((float) 1);
		doc.add(contentField);

		return doc;
	}

	private IndexWriter getIndexWriter() {
		if (indexwriter == null) {

			Directory _indexFilesDirectory;
			try {
				_indexFilesDirectory = FSDirectory.open(indexFilesDirectory.toPath());

				System.out.println("index Dir: " + _indexFilesDirectory.toString());

				// use the inputFilesDirectory
				//IndexWriterConfig indexwriterconfig = new IndexWriterConfig(new StandardAnalyzerCustomed());
				IndexWriterConfig indexwriterconfig = new IndexWriterConfig(new CustomAnalyzer());
				indexwriter = new IndexWriter(_indexFilesDirectory, indexwriterconfig);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return indexwriter;
	}

}

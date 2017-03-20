package Model;

import org.apache.lucene.analysis.StopwordAnalyzerBase;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.CharArraySet;


public class CustomAnalyzer extends StopwordAnalyzerBase {
	public static final int DEFAULT_MAX_TOKEN_LENGTH = 255;

	private int maxTokenLength = DEFAULT_MAX_TOKEN_LENGTH;

	 /** An unmodifiable set containing some common English words that are usually not
	  useful for searching. */
	public static final CharArraySet STOP_WORDS_SET = StopAnalyzer.ENGLISH_STOP_WORDS_SET;
	 // public static final CharArraySet STOP_WORDS_SET = (CharArraySet) StopAnalyzer.ENGLISH_STOP_WORDS_SET;
	@Override
	protected TokenStreamComponents createComponents(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}

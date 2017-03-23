package Model;

import org.apache.lucene.analysis.StopwordAnalyzerBase;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.en.EnglishMinimalStemFilter;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;

import java.io.IOException;
import java.io.Reader;
import java.util.Collection;

import org.apache.lucene.analysis.CharArraySet;

public class CustomAnalyzer extends StopwordAnalyzerBase {
	public static final int DEFAULT_MAX_TOKEN_LENGTH = 255;

	private int maxTokenLength = DEFAULT_MAX_TOKEN_LENGTH;

	/**
	 * An unmodifiable set containing some common English words that are usually
	 * not useful for searching.
	 */
	public static final CharArraySet STOP_WORDS_SET = StopAnalyzer.ENGLISH_STOP_WORDS_SET;

	/**
	 * Builds an analyzer with the given stop words.
	 * 
	 * @param stopWords
	 *            stop words
	 */
	public CustomAnalyzer(CharArraySet stopwords) {
		super(stopwords);
	}

	/**
	 * Builds an analyzer with the default stop words ({@link #STOP_WORDS_SET}).
	 */
	public CustomAnalyzer() {
		this(STOP_WORDS_SET);
	}

	/**
	 * Builds an analyzer with the stop words from the given reader.
	 * 
	 * @see WordlistLoader#getWordSet(Reader)
	 * @param stopwords
	 *            Reader to read stop words from
	 */
	public CustomAnalyzer(Reader stopwords) throws IOException {
		this(loadStopwordSet(stopwords));
	}

	/**
	 * Set maximum allowed token length. If a token is seen that exceeds this
	 * length then it is discarded. This setting only takes effect the next time
	 * tokenStream or tokenStream is called.
	 */
	public void setMaxTokenLength(int length) {
		maxTokenLength = length;
	}

	/**
	 * @see #setMaxTokenLength
	 */
	public int getMaxTokenLength() {
		return maxTokenLength;
	}

	@Override
	protected TokenStreamComponents createComponents(String arg0) {
	    final StandardTokenizer src = new StandardTokenizer();
	    src.setMaxTokenLength(maxTokenLength);
	    TokenStream tok = new StandardFilter(src);
	    //make all lowercase
	    tok = new LowerCaseFilter(tok);
	    //remove stopwords
	    tok = new StopFilter(tok, stopwords);
	    //stemming
	    tok = new EnglishMinimalStemFilter(tok);
	    return new TokenStreamComponents(src, tok) {
	      @Override
	      protected void setReader(final Reader reader) {
	        src.setMaxTokenLength(CustomAnalyzer.this.maxTokenLength);
	        super.setReader(reader);
	      }
	    };
	  }

}

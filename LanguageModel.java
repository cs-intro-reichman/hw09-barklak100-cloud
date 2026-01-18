import java.util.HashMap;
import java.util.Random;

public class LanguageModel {

    // The map of this model.
    // Maps windows to lists of charachter data objects.
    HashMap<String, List> CharDataMap;
    
    // The window length used in this model.
    int windowLength;
    
    // The random number generator used by this model. 
	private Random randomGenerator;

    /** Constructs a language model with the given window length and a given
     *  seed value. Generating texts from this model multiple times with the 
     *  same seed value will produce the same random texts. Good for debugging. */
    public LanguageModel(int windowLength, int seed) {
        this.windowLength = windowLength;
        randomGenerator = new Random(seed);
        CharDataMap = new HashMap<String, List>();
    }

    /** Constructs a language model with the given window length.
     * Generating texts from this model multiple times will produce
     * different random texts. Good for production. */
    public LanguageModel(int windowLength) {
        this.windowLength = windowLength;
        randomGenerator = new Random();
        CharDataMap = new HashMap<String, List>();
    }

    /** Builds a language model from the text in the given file (the corpus). */
	public void train(String fileName) {
        String window = ""; 
        In in = new In(fileName);

        for (int i = 0; i < windowLength; i++) {
            if (!in.isEmpty()) {
                window += in.readChar();
        }
    }

        while (!in.isEmpty()) {
        char c = in.readChar(); 
        List probs = CharDataMap.get(window); 

        if (probs == null) { 
            probs = new List(); 
            CharDataMap.put(window, probs); 
        }

        probs.update(c); 
        window = window.substring(1) + c;
    }

    for (List probs : CharDataMap.values()) {
        calculateProbabilities(probs); 
    }
} 
    void calculateProbabilities(List probs) {
        int totalCount = 0;
        for (int i = 0; i < probs.getSize(); i++) {
            totalCount += probs.get(i).count;
        }

        double cumulative = 0;
        for (int i = 0; i < probs.getSize(); i++) {
            CharData cd = probs.get(i);
            cd.p = (double) cd.count / totalCount; 
            cumulative += cd.p; 
            cd.cp = cumulative; 
        }
        
        if (probs.getSize() > 0) {
            probs.get(probs.getSize() - 1).cp = 1.0;
        }
    }

    char getRandomChar(List probs) {
        double r = randomGenerator.nextDouble(); 
        
        for (int i = 0; i < probs.getSize(); i++) {
            CharData cd = probs.get(i);
            if (r < cd.cp) { 
                return cd.chr; 
            }
        }
        return probs.get(probs.getSize() - 1).chr;
    }

    public String generate(String initialText, int textLength) {
        if (initialText.length() < windowLength) {
            return initialText; 
        }

        String generatedText = initialText;
        String window = initialText.substring(initialText.length() - windowLength);

        while (generatedText.length() < textLength) { 
            List probs = CharDataMap.get(window);
        
            if (probs != null) {
                char nextChar = getRandomChar(probs); 
                generatedText += nextChar; 
                window = generatedText.substring(generatedText.length() - windowLength);
            } else {
                break; 
            }
        }
        return generatedText;
    }

    public static void main(String[] args) {
        int windowLength = Integer.parseInt(args[0]); 
        String initialText = args[1]; 
        int generatedTextLength = Integer.parseInt(args[2]); 
        boolean randomGeneration = args[3].equals("random"); 
        String fileName = args[4]; 

        LanguageModel lm;
        if (randomGeneration) {
            lm = new LanguageModel(windowLength); 
        } else {
            lm = new LanguageModel(windowLength, 20); 
        }

        lm.train(fileName); 
        System.out.println(lm.generate(initialText, generatedTextLength)); 
    }
}
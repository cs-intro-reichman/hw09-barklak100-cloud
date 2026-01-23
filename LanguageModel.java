import java.util.HashMap;
import java.util.Random;

public class LanguageModel {

    HashMap<String, List> CharDataMap; // Maps windows to lists of character data [cite: 180]
    int windowLength;
    private Random randomGenerator;

    public LanguageModel(int windowLength, int seed) {
        this.windowLength = windowLength;
        randomGenerator = new Random(seed);
        CharDataMap = new HashMap<String, List>();
    }

    public LanguageModel(int windowLength) {
        this.windowLength = windowLength;
        randomGenerator = new Random();
        CharDataMap = new HashMap<String, List>();
    }

    /** Builds the model from the corpus. [cite: 199, 373] */
    public void train(String fileName) {
        String window = ""; 
        In in = new In(fileName);

        // Forming the first window [cite: 189, 380]
        for (int i = 0; i < windowLength; i++) {
            if (!in.isEmpty()) {
                window += in.readChar();
            }
        }

        // Processing the text one char at a time [cite: 188, 382]
        while (!in.isEmpty()) {
            char c = in.readChar(); 
            List probs = CharDataMap.get(window); 

            if (probs == null) { 
                probs = new List(); 
                CharDataMap.put(window, probs); 
            }

            probs.update(c); // Increments count or adds first [cite: 397]
            window = window.substring(1) + c; // Advances the window [cite: 398]
        }

        // Calculating probabilities for all lists [cite: 401, 405]
        for (List probs : CharDataMap.values()) {
            calculateProbabilities(probs); 
        }
    }

    /** Computes p and cp for every CharData object in the list. [cite: 117, 120] */
    public void calculateProbabilities(List probs) {
        int totalCount = 0;
        // Sum total characters in the list [cite: 122]
        for (int i = 0; i < probs.getSize(); i++) {
            totalCount += probs.get(i).count;
        }

        double cumulativeP = 0;
        for (int i = 0; i < probs.getSize(); i++) {
            CharData cd = probs.get(i);
            cd.p = (double) cd.count / totalCount; // Individual probability [cite: 123]
            cumulativeP += cd.p;
            cd.cp = cumulativeP; // Cumulative probability [cite: 123]
        }
    }

    /** Draws a character at random using Monte Carlo. [cite: 137, 144] */
    public char getRandomChar(List probs) {
        double r = randomGenerator.nextDouble(); // Uses the randomGenerator field [cite: 259]
        for (int i = 0; i < probs.getSize(); i++) {
            CharData cd = probs.get(i);
            if (r < cd.cp) { // Monte Carlo stopping condition [cite: 140]
                return cd.chr;
            }
        }
        return probs.get(probs.getSize() - 1).chr; // Fallback to last character
    }

    /** Generates random text based on the learned model. [cite: 206, 234] */
    public String generate(String initialText, int textLength) {
        if (initialText.length() < windowLength) {
            return initialText; // Termination condition [cite: 228]
        }

        StringBuilder generatedText = new StringBuilder(initialText);
        String window = initialText.substring(initialText.length() - windowLength);

        while (generatedText.length() < textLength) { 
            List probs = CharDataMap.get(window);
            if (probs != null) {
                char nextChar = getRandomChar(probs); 
                generatedText.append(nextChar); 
                window = generatedText.substring(generatedText.length() - windowLength);
            } else {
                break; // Stop if window not found [cite: 233]
            }
        }
        return generatedText.toString();
    }

    public static void main(String[] args) {
        int windowLength = Integer.parseInt(args[0]); 
        String initialText = args[1]; 
        int generatedTextLength = Integer.parseInt(args[2]); 
        boolean randomGeneration = args[3].equals("random"); 
        String fileName = args[4]; 

        LanguageModel lm = randomGeneration ? new LanguageModel(windowLength) : new LanguageModel(windowLength, 20);
        lm.train(fileName); 
        System.out.println(lm.generate(initialText, generatedTextLength)); 
    }
}
import java.util.HashMap;
import java.util.Random;

public class LanguageModel {

    HashMap<String, List> CharDataMap;
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

    /** Builds the language model from the corpus. */
    public void train(String fileName) {
        In in = new In(fileName);
        String window = "";
        
        // Initial window creation [cite: 381]
        for (int i = 0; i < windowLength; i++) {
            if (!in.isEmpty()) window += in.readChar();
        }

        while (!in.isEmpty()) {
            char c = in.readChar();
            List probs = CharDataMap.get(window);
            if (probs == null) {
                probs = new List();
                CharDataMap.put(window, probs);
            }
            probs.update(c);
            window = window.substring(1) + c; // Slide window [cite: 398]
        }

        for (List probs : CharDataMap.values()) {
            calculateProbabilities(probs);
        }
    }

    /** Calculates probabilities and cumulative probabilities. */
    public void calculateProbabilities(List probs) {
        int total = 0;
        for (int i = 0; i < probs.getSize(); i++) {
            total += probs.get(i).count; // Total occurrences [cite: 122]
        }
        double cumulative = 0;
        for (int i = 0; i < probs.getSize(); i++) {
            CharData cd = probs.get(i);
            cd.p = (double) cd.count / total; // [cite: 123]
            cumulative += cd.p;
            cd.cp = cumulative; // [cite: 119]
        }
    }

    public char getRandomChar(List probs) {
        double r = randomGenerator.nextDouble(); // [cite: 259]
        for (int i = 0; i < probs.getSize(); i++) {
            if (r < probs.get(i).cp) return probs.get(i).chr; // [cite: 140]
        }
        return probs.get(probs.getSize() - 1).chr;
    }

    /** Generates random text from this model. */
    public String generate(String initialText, int textLength) {
        if (initialText.length() < windowLength) {
            return initialText; // [cite: 227, 228]
        }

        String generatedText = initialText;
        // Sets the initial window to the last windowLength characters [cite: 230]
        String window = initialText.substring(initialText.length() - windowLength);

        // Runs until the generated text reaches the length of (textLength + initialText.length)
        while (generatedText.length() < textLength + initialText.length()) {
            List probs = CharDataMap.get(window); // [cite: 217]

            if (probs == null) {
                break; // Stops if the current window is not found in the map [cite: 233]
            }

            char nextChar = getRandomChar(probs);
            generatedText += nextChar; // Appends the character to the generated text [cite: 220]
            
            window = generatedText.substring(generatedText.length() - windowLength);
        }

        return generatedText;
    }

    public static void main(String[] args) {
        int windowLength = Integer.parseInt(args[0]);
        String initialText = args[1];
        int generatedTextLength = Integer.parseInt(args[2]);
        boolean randomGeneration = args[3].equals("random");
        String fileName = args[4];

        LanguageModel lm = randomGeneration ? 
            new LanguageModel(windowLength) : new LanguageModel(windowLength, 20);

        lm.train(fileName);
        System.out.println(lm.generate(initialText, generatedTextLength));
    }
}
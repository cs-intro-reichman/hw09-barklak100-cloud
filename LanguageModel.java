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

    public void train(String fileName) {
        In in = new In(fileName);
        String window = "";
        
        // Reads characters until window reaches windowLength 
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
            window = window.substring(1) + c; // Advances the window 
        }

        for (List probs : CharDataMap.values()) {
            calculateProbabilities(probs);
        }
    }

    public void calculateProbabilities(List probs) {
        int total = 0;
        for (int i = 0; i < probs.getSize(); i++) {
            total += probs.get(i).count; // Summing total count
        }
        double cumulative = 0;
        for (int i = 0; i < probs.getSize(); i++) {
            CharData cd = probs.get(i);
            cd.p = (double) cd.count / total; // Computing individual probability 
            cumulative += cd.p;
            cd.cp = cumulative; // Setting cumulative probability 
        }
    }

    public char getRandomChar(List probs) {
        double r = randomGenerator.nextDouble(); // Monte Carlo
        for (int i = 0; i < probs.getSize(); i++) {
            if (r < probs.get(i).cp) return probs.get(i).chr;
        }
        return probs.get(probs.getSize() - 1).chr;
    }

    /** Generates random text as requested [cite: 206] */
    public String generate(String initialText, int textLength) {
        if (initialText.length() < windowLength) {
            return initialText;
        }
        
        String generatedText = initialText;
        String window = initialText.substring(initialText.length() - windowLength);
        
        // Loop using the requested condition
        while (generatedText.length() < textLength + initialText.length()) {
            List probs = CharDataMap.get(window);
            if (probs == null) {
                break; 
            }
            char nextChar = getRandomChar(probs); 
            generatedText += nextChar; 
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
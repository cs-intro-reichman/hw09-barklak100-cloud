import java.util.HashMap;
import java.util.Random;

public class LanguageModel {

    HashMap<String, List> probabilities; 
    
    int windowLength;
    private Random randomGenerator;

    public LanguageModel(int windowLength, int seed) {
        this.windowLength = windowLength;
        randomGenerator = new Random(seed);
        probabilities = new HashMap<String, List>(); 
    }

    public LanguageModel(int windowLength) {
        this.windowLength = windowLength;
        randomGenerator = new Random();
        probabilities = new HashMap<String, List>();
    }

    public void train(String fileName) {
        String window = ""; 
        In in = new In(fileName); 

        // יצירת החלון הראשוני
        for (int i = 0; i < windowLength; i++) {
            if (!in.isEmpty()) {
                window += in.readChar();
            }
        }
        
        if (window.length() < windowLength) return;

        while (!in.isEmpty()) {
            char c = in.readChar(); 
            List probs = probabilities.get(window); 

            if (probs == null) {
                probs = new List();
                probabilities.put(window, probs); 
            }

            probs.update(c);
            window = window.substring(1) + c;
        }

        for (List probs : probabilities.values()) {
            calculateProbabilities(probs);
        }
    }

    public void calculateProbabilities(List probs) {
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
    }

    public char getRandomChar(List probs) {
        double r = randomGenerator.nextDouble(); 
        for (int i = 0; i < probs.getSize(); i++) {
            if (r < probs.get(i).cp) {
                return probs.get(i).chr;
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

        while (generatedText.length() < textLength + initialText.length()) {
            List probs = probabilities.get(window); 

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
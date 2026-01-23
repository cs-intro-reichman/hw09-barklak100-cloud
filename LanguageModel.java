import java.util.HashMap;
import java.util.Random;

public class LanguageModel {
    private HashMap<String, List> CharDataMap;
    private int windowLength; 
    private Random randomGenerator; 

    public LanguageModel(int windowLength) {
        this.windowLength = windowLength;
        this.CharDataMap = new HashMap<>();
        this.randomGenerator = new Random();
    }

    public LanguageModel(int windowLength, int seed) {
        this.windowLength = windowLength;
        this.CharDataMap = new HashMap<>();
        this.randomGenerator = new Random(seed);
    }

    public void calculateProbabilities(List probs) {
        int totalCount = 0;
        for (int i = 0; i < probs.size(); i++) {
            totalCount += probs.get(i).count;
        }
        
        double cumulativeP = 0;
        for (int i = 0; i < probs.size(); i++) {
            CharData cd = probs.get(i);
            cd.p = (double) cd.count / totalCount;
            cumulativeP += cd.p;
            cd.cp = cumulativeP;
        }
    }

    public char getRandomChar(List probs) {
        double r = randomGenerator.nextDouble(); 
        for (int i = 0; i < probs.size(); i++) {
            if (probs.get(i).cp > r) {
                return probs.get(i).chr;
            }
        }
        return probs.get(probs.size() - 1).chr;
    }

    public void train(String fileName) {
        In in = new In(fileName);
        String window = "";
        
        for (int i = 0; i < windowLength; i++) {
            window += in.readChar();
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
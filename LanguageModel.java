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
        String window = "";
        In in = new In(fileName);
        // בניית החלון הראשוני [cite: 380-381]
        for (int i = 0; i < windowLength; i++) {
            if (!in.isEmpty()) window += in.readChar();
        }
        // עיבוד שאר הקובץ [cite: 382]
        while (!in.isEmpty()) {
            char c = in.readChar();
            List probs = CharDataMap.get(window);
            if (probs == null) {
                probs = new List();
                CharDataMap.put(window, probs);
            }
            probs.update(c);
            window = window.substring(1) + c; // הזזת החלון [cite: 398]
        }
        // השלב שמתקן את ה-Train: חישוב הסתברויות לכל הרשימות במפה 
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
        if (probs.getSize() > 0) { // תיקון לדיוק הסתברות מצטברת [cite: 119]
            probs.get(probs.getSize() - 1).cp = 1.0;
        }
    }

    char getRandomChar(List probs) {
        double r = randomGenerator.nextDouble();
        for (int i = 0; i < probs.getSize(); i++) {
            if (r < probs.get(i).cp) return probs.get(i).chr;
        }
        return probs.get(probs.getSize() - 1).chr;
    }

    // הלוגיקה שלך שעבדה מצוין
    public String generate(String initialText, int textLength) {
        if (initialText.length() < windowLength) return initialText;
        String generatedText = initialText;
        String window = initialText.substring(initialText.length() - windowLength);
        while (generatedText.length() < textLength + initialText.length()) {
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
}
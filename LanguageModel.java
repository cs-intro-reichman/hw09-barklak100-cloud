import java.util.HashMap;
import java.util.Random;

public class LanguageModel 
{
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
    public LanguageModel(int windowLength, int seed) 
    {
        this.windowLength = windowLength;
        this.randomGenerator = new Random(seed);
        this.CharDataMap = new HashMap<String, List>();
    }

    /** Constructs a language model with the given window length.
     * Generating texts from this model multiple times will produce
     * different random texts. Good for production. */
    public LanguageModel(int windowLength) 
    {
        this.windowLength = windowLength;
        this.randomGenerator = new Random();
        this.CharDataMap = new HashMap<String, List>();
    }

    /** Builds a language model from the text in the given file (the corpus). */
	public void train(String fileName) 
    {
        In in = new In(fileName);
        String window = "";
        char c;

        // Creating a first window in the correct size
        for (int i = 0; i < this.windowLength; i++)
        {
            c = in.readChar();
            window += c;
        }

        while (!in.isEmpty())
        {
            c = in.readChar(); // Reading character after the window to add to the map

            List probs = this.CharDataMap.get(window); // Getting the window's list from the CharDataMap

            if (probs == null) // If current windows doesnt exist in the map create it else just add the value into the window
            {
                probs = new List();
                this.CharDataMap.put(window, probs);
            }

            probs.update(c); // Adding char into the window value
            window = window.substring(1) + c; // Creating the new window
        }

        // Caculate all the new probabilities of all the lists in the map
        for (List probs : this.CharDataMap.values())
            calculateProbabilities(probs);
    }

    // Computes and sets the probabilities (p and cp fields) of all the
	// characters in the given list. */
	void calculateProbabilities(List probs) 
    {				
        if (probs.getSize() == 0) // Making sure the list isnt empty
            return;

        CharData[] arr = probs.toArray(); 

        // Counting total characters in the list
        int countTotalChars = 0;
        for (int i = 0; i < arr.length; i++)
        {
            countTotalChars += arr[i].count; 
        }

        // Creating cp and p for each element in the list
        double currentCp = 0;
        for (int i = 0; i < arr.length; i++)
        {
            arr[i].p = (double) arr[i].count / countTotalChars; 
            
            currentCp += arr[i].p;
            arr[i].cp = currentCp;
        }

        // Setting last elemnt cp as 1.0
        arr[arr.length - 1].cp = 1.0;
    } 

    // Returns a random character from the given probabilities list.
	char getRandomChar(List probs) 
    {
        if (probs.getSize() == 0) // Making sure the list isnt empty
            return ' ';
        
        double randomNum = this.randomGenerator.nextDouble();
        CharData[] arr = probs.toArray();

        for (int i = 0; i < arr.length; i++)
        {
            if (randomNum < arr[i].cp)
                return arr[i].chr;
        }

        return arr[arr.length - 1].chr; // Returning last elemnt as it contains 1.0 which is bigger then randomNum
	}

    /**
	 * Generates a random text, based on the probabilities that were learned during training. 
	 * @param initialText - text to start with. If initialText's last substring of size numberOfLetters
	 * doesn't appear as a key in Map, we generate no text and return only the initial text. 
	 * @param numberOfLetters - the size of text to generate
	 * @return the generated text
	 */
	public String generate(String initialText, int textLength) 
    {
        if (initialText.length() < this.windowLength) // if initialText smaller then windowLength return the initialText
            return initialText;
     
        int firstLength = initialText.length(); // Creating length if initialText to add to textLength to get correct length for the loop
        String window = initialText.substring(firstLength - this.windowLength); // Setting the first window, length of windowLength from last letters in initialText
        while (initialText.length() < textLength + firstLength)
        {
            List probs = this.CharDataMap.get(window); // Getting the list of probs of current window

            if (probs == null) // If window isnt in in the map return current initialText
                return initialText;

            char current = getRandomChar(probs); // Get the next letter

            initialText += current; // Add current letter to the text
            window = window.substring(1) + current; // Change window for next run
        }

        return initialText;
	}

    /** Returns a string representing the map of this language model. */
	public String toString() 
    {
		StringBuilder str = new StringBuilder();
		for (String key : this.CharDataMap.keySet()) 
        {
			List keyProbs = this.CharDataMap.get(key);
			str.append(key + " : " + keyProbs + "\n");
		}
		return str.toString();
	}

    public static void main(String[] args) 
    {
        int windowLength = Integer.parseInt(args[0]); 
        String initialText = args[1]; 
        int generatedTextLength = Integer.parseInt(args[2]); 
        Boolean randomGeneration = args[3].equals("random"); 
        String fileName = args[4]; 

        // Create the LanguageModel object 
        LanguageModel lm; 
        if (randomGeneration) 
            lm = new LanguageModel(windowLength); 
        else 
            lm = new LanguageModel(windowLength, 20); 

        // Trains the model, creating the map. 
        lm.train(fileName); 
        
        // Generates text, and prints it. 
        System.out.println(lm.generate(initialText, generatedTextLength)); 
    }
}
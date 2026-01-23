/** A linked list of character data objects. */
public class List {

    // Points to the first node in this list [cite: 45]
    private Node first;

    // The number of elements in this list [cite: 78]
    private int size;
    
    /** Constructs an empty list. [cite: 78] */
    public List() {
        first = null;
        size = 0;
    }
    
    /** Returns the number of elements in this list. [cite: 78] */
    public int getSize() {
          return size;
    }

    /** Returns the CharData of the first element in this list. [cite: 78] */
    public CharData getFirst() {
        if (first == null) {
            return null;
        }
        return first.cp; // Accesses the CharData object via the 'cp' field in Node
    }

    /** Adds a CharData object with the given character to the beginning of this list. [cite: 89] */
    public void addFirst(char chr) {
        CharData newCharData = new CharData(chr);
        Node newNode = new Node(newCharData, first);
        first = newNode;
        size++; 
    }

    /** Textual representation of this list. [cite: 90, 91] */
    public String toString() {
        if (size == 0) return "()";
        StringBuilder sb = new StringBuilder("(");
        Node current = first;
        while (current != null) {
            // Uses CharData's toString which provides the (c count p cp) format [cite: 91]
            sb.append(current.cp.toString()).append(" ");
            current = current.next;
        }
        // Removes the trailing space and closes with double parentheses to match expected output
        return "(" + sb.toString().trim() + ")";
    }

    /** Returns the index of the first CharData object with the same chr value, or -1.  */
    public int indexOf(char chr) {
        int index = 0;
        Node current = first;
        while (current != null) {
            if (current.cp.chr == chr) { 
                return index;
            }
            current = current.next;
            index++;
        }
        return -1;
    }

    /** Updates the counter or adds a new character. [cite: 98, 99, 100] */
    public void update(char chr) {
        int index = indexOf(chr);
        if (index != -1) {
            // Character exists, increment its count [cite: 98]
            CharData cd = get(index);
            cd.count++;
        } else {
            // Character doesn't exist, add to beginning [cite: 99, 100]
            addFirst(chr); 
        }
    }

    /** Removes the CharData object with the given chr. [cite: 93, 94] */
    public boolean remove(char chr) {
        Node prev = null;
        Node current = first;
        while (current != null) {
            if (current.cp.chr == chr) {
                if (prev == null) {
                    first = first.next;
                } else {
                    prev.next = current.next;
                }
                size--;
                return true; // Successfully removed [cite: 93]
            }
            prev = current;
            current = current.next;
        }
        return false; // Character not found [cite: 94]
    }

    /** Returns the CharData object at the specified index. [cite: 95, 96] */
    public CharData get(int index) {
        if (index < 0 || index >= size) { 
            throw new IndexOutOfBoundsException(); // [cite: 96]
        }
        Node current = first;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.cp; 
    }

    /** Returns an array of CharData objects. [cite: 78] */
    public CharData[] toArray() {
        CharData[] arr = new CharData[size];
        Node current = first;
        int i = 0;
        while (current != null) {
            arr[i++] = current.cp;
            current = current.next;
        }
        return arr;
    }

    /** Returns an iterator starting at the given index. [cite: 97] */
    public ListIterator listIterator(int index) {
        if (index < 0 || index > size) return null; 
        Node current = first;
        int i = 0;
        while (i < index && current != null) {
            current = current.next;
            i++;
        }
        return new ListIterator(current);
    }
}
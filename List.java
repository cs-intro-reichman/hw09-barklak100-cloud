/** A linked list of character data objects. */
public class List {

    // Points to the first node in this list
    private Node first;

    // The number of elements in this list
    private int size;
    
    /** Constructs an empty list. */
    public List() {
        first = null;
        size = 0;
    }
    
    /** Returns the number of elements in this list. */
    public int getSize() {
          return size;
    }

    /** Returns the CharData of the first element in this list. */
    public CharData getFirst() {
        if (first == null) {
            return null;
        }
        return first.cp; 
    }

    /** Adds a CharData object with the given character to the beginning of this list. */
    public void addFirst(char chr) {
        CharData newCharData = new CharData(chr);
        Node newNode = new Node(newCharData, first);
        first = newNode;
        size++; 
    }

    /** Textual representation of this list. */
    public String toString() {
        if (size == 0) return "()";
        StringBuilder result = new StringBuilder("(");
        Node current = first;
        while (current != null) {
            result.append(current.cp.toString()).append(" "); 
            current = current.next;
        }
        // הסרת הרווח האחרון וסגירה
        return result.toString().trim() + ")";
    }

    /** Returns the index of the first CharData object in this list that has the same chr value. */
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

    /** Updates the counter or adds a new character. */
    public void update(char chr) {
        Node current = first;
        // חיפוש התו ברשימה
        while (current != null) {
            if (current.cp.chr == chr) {
                // אם נמצא - מעדכנים מונה ועוצרים
                current.cp.count++; 
                return; 
            }
            current = current.next;
        }
        // אם לא נמצא - מוסיפים חדש להתחלה
        addFirst(chr);
    }

    /** Returns the CharData object at the specified index. */
    public CharData get(int index) {
        if (index < 0 || index >= size) { 
            throw new IndexOutOfBoundsException();
        }
        Node current = first;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.cp; 
    }

    /** Removes the CharData object with the given chr. */
    public boolean remove(char chr) {
        if (first == null) return false;

        // מקרה מיוחד: התו נמצא בצומת הראשון
        if (first.cp.chr == chr) { 
            first = first.next;
            size--; 
            return true;
        }

        Node prev = first;
        Node current = first.next;
        while (current != null) {
            if (current.cp.chr == chr) { 
                prev.next = current.next; 
                size--; 
                return true;
            }
            prev = current;
            current = current.next;
        }
        return false; 
    }

    /** Returns an array of CharData objects. */
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

    /** Returns an iterator starting at the given index. */
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
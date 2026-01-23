/** A linked list of character data objects. */
public class List {

    private Node first; // Points to the first node in this list
    private int size;   // The number of elements in this list
    
    public List() {
        first = null;
        size = 0;
    }

    public int getSize() {
          return size;
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

    /** Adds a CharData object with the given character to the beginning of this list. */
    public void addFirst(char chr) {
        CharData newCharData = new CharData(chr);
        Node newNode = new Node(newCharData, first);
        first = newNode;
        size++; 
    }

    /** Returns the index of the first CharData object with the given chr value. */
    public int indexOf(char chr) {
        Node current = first;
        int index = 0;
        while (current != null) {
            if (current.cp.chr == chr) { 
                return index;
            }
            current = current.next;
            index++;
        }
        return -1;
    }

    /** Updates the counter or adds a new character to the list. */
    public void update(char chr) {
        int index = indexOf(chr);
        if (index != -1) {
            CharData cd = get(index);
            cd.count++;
        } else {
            addFirst(chr); 
        }
    }

    /** Removes the CharData object with the given chr. */
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
                return true;
            }
            prev = current;
            current = current.next;
        }
        return false;
    }

    public String toString() {
        if (size == 0) return "()";
        StringBuilder sb = new StringBuilder("(");
        Node current = first;
        while (current != null) {
            sb.append(current.cp.toString()).append(" ");
            current = current.next;
        }
        return sb.toString().trim() + ")";
    }
}
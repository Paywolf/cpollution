public class Map<Key, Value> {
    private final LinkedList<Entry> list = new LinkedList<>(); // holds all information about items in the map
    public class Entry {
        // this class holds key and value in a single object
        Key key;
        Value value;

        Entry(Key key, Value value) {
            this.key = key;
            this.value = value;
        }
    }


    public void put(Key key, Value value) {
        for(Entry entry: list){ // iterates through the linked list
            if(entry.key.equals(key)){
                entry.value = value; // if already key exists set its value
                return;
            }
        }

        // if key not there, add new entry
        list.add(new Entry(key, value));
    }

    public void putIfAbsent(Key key, Value value) {
        if(get(key)!=null)return;
        put(key, value);
    }

    public Value get(Key key) {
        for(Entry entry: list){ // iterates through the linked list
            if(entry.key.equals(key)){
                return entry.value; // return the since key matches
            }
        }
        return null; // null if not found
    }

    public LinkedList<Entry> entries() {
        return list; // return the entries list
    }
}

package HMS.Model;

import HMS.Control.ReceptionistController;

public class IDNamePair {
    private final int id;
    private final String name;

    public IDNamePair(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof IDNamePair) {
            IDNamePair o = (IDNamePair) obj;
            return o.getName().equals(this.getName()) && o.getId() == this.getId();
        }
        return false;
    }
}

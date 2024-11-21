package Model;

import java.io.Serial;
import java.io.Serializable;

public abstract class Square implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String name;

    public Square(String name) {
        this.name = name;
    }

    public abstract void landOn(Player player);

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

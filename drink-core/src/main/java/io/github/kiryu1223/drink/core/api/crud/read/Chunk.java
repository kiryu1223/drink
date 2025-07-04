package io.github.kiryu1223.drink.core.api.crud.read;

import java.util.List;

public class Chunk<T> {
    private final List<T> values;
    private boolean end = false;

    public Chunk(List<T> values) {
        this.values = values;
    }

    public List<T> getValues() {
        return values;
    }

    public boolean isEnd() {
        return end;
    }

    public void end() {
        end = true;
    }
}

package io.github.kiryu1223.drink.core.api.crud.read;

import java.util.List;

public class Chunk<T> {
    private final List<T> result;
    private boolean end = false;

    public Chunk(List<T> result) {
        this.result = result;
    }

    /**
     * 获取本批次返回值
     */
    public List<T> getResult() {
        return result;
    }

    public boolean isEnd() {
        return end;
    }

    /**
     * 结束返回
     */
    public void end() {
        end = true;
    }
}

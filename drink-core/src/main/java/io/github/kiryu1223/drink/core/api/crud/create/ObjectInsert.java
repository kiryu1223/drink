/*
 * Copyright 2017-2024 noear.org and authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.kiryu1223.drink.core.api.crud.create;

import io.github.kiryu1223.drink.base.IConfig;

import java.util.List;

/**
 * 新增过程对象
 *
 * @author kiryu1223
 * @since 3.0
 */
public class ObjectInsert<T> extends InsertBase<ObjectInsert<T>, T> {
    private final List<T> ts;
    private final Class<T> tableType;

    public ObjectInsert(IConfig config, List<T> ts) {
        super(config);
        this.ts = ts;
        this.tableType = (Class<T>) ts.get(0).getClass();
    }

    @Override
    public long executeRows(boolean autoIncrement) {
        if (ts == null || ts.isEmpty()) {
            log.warn("insert列表为空");
            return 0;
        }
        return executeInsert(ts, autoIncrement);
    }

    @Override
    protected List<T> getObjects() {
        return ts;
    }

    @Override
    protected Class<T> getTableType() {
        return tableType;
    }
}

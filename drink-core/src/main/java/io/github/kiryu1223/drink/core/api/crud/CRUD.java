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
package io.github.kiryu1223.drink.core.api.crud;


import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.core.api.crud.update.UpdateBase;
import io.github.kiryu1223.drink.core.sqlBuilder.ISqlBuilder;
import org.slf4j.Logger;

/**
 * @author kiryu1223
 * @since 3.0
 */
public abstract class CRUD<C> {
    protected abstract IConfig getConfig();

    /**
     * 获取Sql语句
     *
     * @return Sql
     */
    protected abstract String toSql();

    protected void tryPrintSql(Logger log, String sql) {
        if (getConfig().isPrintSql()) {
            log.info("==> {}", sql);
        }
    }

//    protected void tryPrintUseDs(Logger log, String ds)
//    {
//        if (getConfig().isPrintUseDs())
//        {
//            log.info("Current use datasource: {}", ds == null ? "default" : ds);
//        }
//    }

    protected void tryPrintBatch(Logger log, long count) {
        if (getConfig().isPrintBatch()) {
            log.info("DataSize: {} Use batch execute", count);
        }
    }

    protected void tryPrintNoBatch(Logger log, long count) {
        if (getConfig().isPrintBatch()) {
            log.info("DataSize: {} Use normal execute", count);
        }
    }

    protected abstract ISqlBuilder getSqlBuilder();

    public C DisableFilter(String filterId) {
        getSqlBuilder().addIgnoreFilterId(filterId);
        return (C) this;
    }

    public C DisableFilterAll(boolean condition) {
        getSqlBuilder().setIgnoreFilterAll(condition);
        return (C) this;
    }

    public C DisableFilterAll() {
        getSqlBuilder().setIgnoreFilterAll(true);
        return (C) this;
    }
}

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
package io.github.kiryu1223.drink.core;

/**
 * 配置项
 *
 * @author kiryu1223
 * @since 3.0
 */
public class Option {
    /**
     * 是否忽略没有条件的update
     */
    private boolean ignoreUpdateNoWhere = false;
    /**
     * 是否忽略没有条件的delete
     */
    private boolean ignoreDeleteNoWhere = false;
    /**
     * 是否打印sql
     */
    private boolean printSql = true;

    private boolean printValues;

    private boolean printTotal;

    private boolean printUpdate;

    private boolean printTime;

    /**
     * 是否忽略没有条件的update
     */
    public boolean isIgnoreUpdateNoWhere() {
        return ignoreUpdateNoWhere;
    }

    /**
     * 设置是否忽略没有条件的update
     */
    public void setIgnoreUpdateNoWhere(boolean ignoreUpdateNoWhere) {
        this.ignoreUpdateNoWhere = ignoreUpdateNoWhere;
    }

    /**
     * 是否忽略没有条件的delete
     */
    public boolean isIgnoreDeleteNoWhere() {
        return ignoreDeleteNoWhere;
    }

    /**
     * 设置是否忽略没有条件的delete
     */
    public void setIgnoreDeleteNoWhere(boolean ignoreDeleteNoWhere) {
        this.ignoreDeleteNoWhere = ignoreDeleteNoWhere;
    }

    /**
     * 是否打印sql
     */
    public boolean isPrintSql() {
        return printSql;
    }

    /**
     * 设置是否打印sql
     */
    public void setPrintSql(boolean printSql) {
        this.printSql = printSql;
    }

    public boolean isPrintValues()
    {
        return printValues;
    }

    public void setPrintValues(boolean printValues)
    {
        this.printValues = printValues;
    }

    public boolean isPrintTotal()
    {
        return printTotal;
    }

    public void setPrintTotal(boolean printTotal)
    {
        this.printTotal = printTotal;
    }

    public boolean isPrintUpdate() {
        return printUpdate;
    }

    public void setPrintUpdate(boolean printUpdate) {
        this.printUpdate = printUpdate;
    }

    public boolean isPrintTime() {
        return printTime;
    }

    public void setPrintTime(boolean printTime) {
        this.printTime = printTime;
    }
}

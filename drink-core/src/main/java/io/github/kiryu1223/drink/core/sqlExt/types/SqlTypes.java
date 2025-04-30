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
package io.github.kiryu1223.drink.core.sqlExt.types;


import io.github.kiryu1223.drink.base.sqlExt.ISqlKeywords;

/**
 * SQL类型
 *
 * @author kiryu1223
 * @since 3.0
 */
public abstract class SqlTypes<T> implements ISqlKeywords {
    public static Varchar varchar() {
        return varchar(255);
    }

    public static Varchar varchar(int length) {
        return new Varchar(length);
    }

    public static Char Char() {
        return Char(4);
    }

    public static Char Char(int length) {
        return new Char(length);
    }
}

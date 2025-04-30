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
package io.github.kiryu1223.drink.core.exception;

import io.github.kiryu1223.drink.base.DbType;

/**
 * 没有找到SqlExtensionExpression注解异常
 *
 * @author kiryu1223
 * @since 3.0
 */
public class SqlFuncExtNotFoundException extends RuntimeException {
    public SqlFuncExtNotFoundException(DbType type) {
        super("No corresponding SqlExtensionExpression annotation found for database type: " + type);
    }
}

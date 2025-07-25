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
import io.github.kiryu1223.drink.base.log.ISqlLogger;
import io.github.kiryu1223.drink.base.session.SqlValue;
import io.github.kiryu1223.drink.core.sqlBuilder.ISqlBuilder;

import java.util.List;

/**
 * @author kiryu1223
 * @since 3.0
 */
public abstract class CRUD<C> {
    protected abstract IConfig getConfig();

    /**
     * 获取Sql语句
     */
    protected abstract String toSql();

    protected abstract C DisableFilter(String filterId);

    protected abstract C DisableFilterAll(boolean condition);

    protected abstract C DisableFilterAll();
}

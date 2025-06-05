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
package io.github.kiryu1223.drink.core.api;

import io.github.kiryu1223.expressionTree.expressions.annos.Getter;
import io.github.kiryu1223.expressionTree.expressions.annos.Setter;
import io.github.kiryu1223.expressionTree.util.ReflectUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 匿名查询结果，请不要继承这个类
 *
 * @author kiryu1223
 * @since 3.0
 */
@Getter
@Setter
public abstract class Result
{
    @Override
    public final String toString()
    {
        List<String> strings = new ArrayList<>();
        for (Field field : getClass().getDeclaredFields())
        {
            String fieldName = field.getName();
            if (fieldName.startsWith("val$")) continue;
            strings.add(fieldName + "=" + ReflectUtil.getFieldValue(this, fieldName));
        }
        return "(" + String.join(",", strings) + ")";
    }

    @Override
    public final int hashCode() {
        return super.hashCode();
    }

    @Override
    public final boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    protected final Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    protected final void finalize() throws Throwable {
        super.finalize();
    }
}

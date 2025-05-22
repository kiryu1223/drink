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
package io.github.kiryu1223.drink.func.types;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.exception.DrinkException;
import io.github.kiryu1223.drink.base.expression.ISqlExpression;
import io.github.kiryu1223.drink.base.expression.ISqlSingleValueExpression;
import io.github.kiryu1223.drink.base.sqlExt.BaseSqlExtension;
import io.github.kiryu1223.drink.base.transform.Transformer;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 类型转换函数扩展
 *
 * @author kiryu1223
 * @since 3.0
 */
public class TypeCastExtension extends BaseSqlExtension {
    @Override
    public ISqlExpression parse(IConfig config, Method method, List<ISqlExpression> args,boolean[] useSuper) {
        ISqlExpression typeExpression = args.get(1);
        if (typeExpression instanceof ISqlSingleValueExpression) {
            ISqlSingleValueExpression singleValueExpression = (ISqlSingleValueExpression) typeExpression;
            Object value = singleValueExpression.getValue();
            if (value instanceof Class<?>) {
                Class<?> type = (Class<?>) value;
                Transformer transformer = config.getTransformer();
                return transformer.typeCast(args.get(0), type);
            }
            throw new DrinkException(String.format("%s必须是Class<?>",value));
        }
        throw new DrinkException(String.format("%s必须是可求值的",typeExpression));
    }
}

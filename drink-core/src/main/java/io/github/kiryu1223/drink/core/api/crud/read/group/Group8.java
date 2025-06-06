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
package io.github.kiryu1223.drink.core.api.crud.read.group;

/**
 * @author kiryu1223
 * @since 3.0
 */
public class Group8<Key, T1, T2, T3, T4, T5, T6, T7, T8> extends Group7<Key, T1, T2, T3, T4, T5, T6, T7> {
    public T8 value8;

//    /**
//     * 等价于聚合函数COUNT(expr)
//     *
//     * @param expr 指定字段的lambda表达式(强制要求参数为<b>lambda表达式</b>，不可以是<span style='color:red;'>方法引用</span>以及<span style='color:red;'>匿名对象</span>)
//     * @return 符合的条数
//     */
//    public <R> long count(Func8<T1, T2, T3, T4, T5, T6, T7, T8, R> expr) {
//        boom();
//        return 0;
//    }
//
//    /**
//     * 等价于聚合函数SUM(expr)
//     *
//     * @param expr 指定字段的lambda表达式(强制要求参数为<b>lambda表达式</b>，不可以是<span style='color:red;'>方法引用</span>以及<span style='color:red;'>匿名对象</span>)
//     * @return 总和的值，类型为BigDecimal
//     */
//    public <R> R sum(Func8<T1, T2, T3, T4, T5, T6, T7, T8, R> expr) {
//        boom();
//        return (R) new Object();
//    }
//
//    /**
//     * 等价于聚合函数AVG(expr)
//     *
//     * @param expr 指定字段的lambda表达式(强制要求参数为<b>lambda表达式</b>，不可以是<span style='color:red;'>方法引用</span>以及<span style='color:red;'>匿名对象</span>)
//     * @return 平均的值，类型为BigDecimal
//     */
//    public BigDecimal avg(Func8<T1, T2, T3, T4, T5, T6, T7, T8, Number> expr) {
//        boom();
//        return BigDecimal.ZERO;
//    }
//
//    /**
//     * 等价于聚合函数MAX(expr)
//     *
//     * @param expr 指定字段的lambda表达式(强制要求参数为<b>lambda表达式</b>，不可以是<span style='color:red;'>方法引用</span>以及<span style='color:red;'>匿名对象</span>)
//     * @return 最大值，类型与lambda的返回值一致
//     */
//    public <R> R max(Func8<T1, T2, T3, T4, T5, T6, T7, T8, R> expr) {
//        boom();
//        return (R) new Object();
//    }
//
//    /**
//     * 等价于聚合函数MIN(expr)
//     *
//     * @param expr 指定字段的lambda表达式(强制要求参数为<b>lambda表达式</b>，不可以是<span style='color:red;'>方法引用</span>以及<span style='color:red;'>匿名对象</span>)
//     * @return 最小值，类型与lambda的返回值一致
//     */
//    public <R> R min(Func8<T1, T2, T3, T4, T5, T6, T7, T8, R> expr) {
//        boom();
//        return (R) new Object();
//    }
//
//    public <R> String groupJoin(Func8<T1, T2, T3, T4, T5, T6, T7, T8, R> expr) {
//        boom();
//        return "";
//    }
}

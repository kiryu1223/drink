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
package io.github.kiryu1223.drink.base.sqlExt;


import io.github.kiryu1223.drink.base.exception.Winner;

import java.math.BigDecimal;

/**
 * @author kiryu1223
 * @since 3.0
 */
public interface IAggregation {

    interface Over {
        @SqlExtensionExpression(template = "COUNT(*) {super}")
        default long count() {
            return Winner.error();
        }

        @SqlExtensionExpression(template = "COUNT({r}) {super}")
        default <R> long count(R r) {
            return Winner.error();
        }

        @SqlExtensionExpression(template = "SUM({r}) {super}")
        default <R> R sum(R r) {
            return Winner.error();
        }

        @SqlExtensionExpression(template = "AVG({r}) {super}")
        default BigDecimal avg(Number r) {
            return Winner.error();
        }

        @SqlExtensionExpression(template = "MAX({r}) {super}")
        default <R> R max(R r) {
            return Winner.error();
        }

        @SqlExtensionExpression(template = "MIN({r}) {super}")
        default <R> R min(R r) {
            return Winner.error();
        }

        @SqlExtensionExpression(template = "ROW_NUMBER() {super}")
        default long rowNumber() {
            return Winner.error();
        }

        @SqlExtensionExpression(template = "RANK() {super}")
        default long rank() {
            return Winner.error();
        }

        @SqlExtensionExpression(template = "DENSE_RANK() {super}")
        default long denseRank() {
            return Winner.error();
        }

        @SqlExtensionExpression(template = "PERCENT_RANK() {super}")
        default double percentRank() {
            return Winner.error();
        }

        @SqlExtensionExpression(template = "NTILE({n}) {super}")
        default long ntile(long n) {
            return Winner.error();
        }

        @SqlExtensionExpression(template = "CUME_DIST({n}) {super}")
        default double cumeDist() {
            return Winner.error();
        }

        @SqlExtensionExpression(template = "LAG({expr}) {super}")
        default <R> R lag(R expr) {
            return Winner.error();
        }

        @SqlExtensionExpression(template = "LAG({expr},{offset}) {super}")
        default <R> R lag(R expr, long offset) {
            return Winner.error();
        }

        @SqlExtensionExpression(template = "LAG({expr},{offset},{defaultValue}) {super}")
        default <R> R lag(R expr, long offset, R defaultValue) {
            return Winner.error();
        }

        @SqlExtensionExpression(template = "LEAD({expr}) {super}")
        default <R> R lead(R expr) {
            return Winner.error();
        }

        @SqlExtensionExpression(template = "LEAD({expr},{offset}) {super}")
        default <R> R lead(R expr, long offset) {
            return Winner.error();
        }

        @SqlExtensionExpression(template = "LEAD({expr},{offset},{defaultValue}) {super}")
        default <R> R lead(R expr, long offset, R defaultValue) {
            return Winner.error();
        }

        @SqlExtensionExpression(template = "FIRST_VALUE({expr}) {super}")
        default <R> R firstValue(R expr) {
            return Winner.error();
        }

        @SqlExtensionExpression(template = "LAST_VALUE({expr}) {super}")
        default <R> R lastValue(R expr) {
            return Winner.error();
        }

        @SqlExtensionExpression(template = "NTH_VALUE({expr},{n}) {super}")
        default <R> R nthValue(R expr, long n) {
            return Winner.error();
        }
    }

    @SqlExtensionExpression(template = "OVER ()")
    default Over over() {
        return Winner.error();
    }

    @SqlExtensionExpression(template = "OVER (PARTITION BY {partition})")
    default <P> Over over(P partition) {
        return Winner.error();
    }

    @SqlExtensionExpression(template = "OVER (PARTITION BY {partition} ORDER BY {orderBy})")
    default <P, O> Over over(P partition, O orderBy) {
        return Winner.error();
    }

    @SqlExtensionExpression(template = "OVER (PARTITION BY {partition} ORDER BY {orderBy} ROWS BETWEEN {start} AND {end})")
    default <P, O> Over over(P partition, O orderBy, Rows start, Rows end) {
        return Winner.error();
    }

    @SqlExtensionExpression(template = "OVER (PARTITION BY {partition} ORDER BY {orderBy} RANGE BETWEEN {start} AND {end})")
    default <P, O> Over over(P partition, O orderBy, Range start, Range end) {
        return Winner.error();
    }

    @SqlExtensionExpression(template = "COUNT(*)")
    default long count() {
        return Winner.error();
    }

    @SqlExtensionExpression(template = "COUNT({r})")
    default <R> long count(R r) {
        return Winner.error();
    }

    @SqlExtensionExpression(template = "SUM({r})")
    default <R> R sum(R r) {
        return Winner.error();
    }

    @SqlExtensionExpression(template = "AVG({r})")
    default BigDecimal avg(Number r) {
        return Winner.error();
    }

    @SqlExtensionExpression(template = "MAX({r})")
    default <R> R max(R r) {
        return Winner.error();
    }

    @SqlExtensionExpression(template = "MIN({r})")
    default <R> R min(R r) {
        return Winner.error();
    }

    @SqlExtensionExpression(template = "", extension = GroupJoinExtension.class)
    default <R> String groupJoin(R r) {
        return Winner.error();
    }

    @SqlExtensionExpression(template = "", extension = GroupJoinExtension.class)
    default <R> String groupJoin(String delimiter, R r) {
        return Winner.error();
    }
}

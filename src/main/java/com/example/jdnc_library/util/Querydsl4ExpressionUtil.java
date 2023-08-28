package com.example.jdnc_library.util;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;

public class Querydsl4ExpressionUtil {

    public static <T> BooleanExpression eq(Long left, NumberPath<Long> right) {
        if (left == null) {
            return Expressions.TRUE;
        } else {
            return right.eq(left);
        }
    }

    public static <T> BooleanExpression eq(NumberPath<Long> left, Long right) {
        if (right == null) {
            return Expressions.TRUE;
        } else {
            return left.eq(right);
        }
    }

    public static <T> BooleanExpression eq(String left, StringPath right) {
        if (left == null) {
            return Expressions.TRUE;
        } else {
            return right.eq(left);
        }
    }

    public static <T> BooleanExpression eq(StringPath left, String right) {
        if (right == null) {
            return Expressions.TRUE;
        } else {
            return left.eq(right);
        }
    }

    public static <T> BooleanExpression contains(StringPath left, String right) {
        if (right == null) {
            return Expressions.TRUE;
        } else {
            return left.contains(right);
        }
    }

    public static <T> BooleanExpression contains(String left, StringPath right) {
        if (left == null) {
            return Expressions.TRUE;
        } else {
            return right.contains(left);
        }
    }
}

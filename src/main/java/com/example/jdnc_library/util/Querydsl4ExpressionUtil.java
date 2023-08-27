package com.example.jdnc_library.util;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;

public class Querydsl4ExpressionUtil {

    public static <T> Predicate eq(Long left, NumberPath<Long> right) {
        if (left == null) {
            return Expressions.TRUE;
        } else {
            return right.eq(left);
        }
    }

    public static <T> Predicate eq(NumberPath<Long> left, Long right) {
        if (right == null) {
            return Expressions.TRUE;
        } else {
            return left.eq(right);
        }
    }

    public static <T> Predicate eq(String left, StringPath right) {
        if (left == null) {
            return Expressions.TRUE;
        } else {
            return right.eq(left);
        }
    }

    public static <T> Predicate eq(StringPath left, String right) {
        if (right == null) {
            return Expressions.TRUE;
        } else {
            return left.eq(right);
        }
    }

    public static <T> Predicate contains(StringPath left, String right) {
        if (right == null) {
            return Expressions.TRUE;
        } else {
            return left.contains(right);
        }
    }

    public static <T> Predicate contains(String left, StringPath right) {
        if (left == null) {
            return Expressions.TRUE;
        } else {
            return right.contains(left);
        }
    }
}

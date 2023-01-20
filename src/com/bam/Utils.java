package com.bam;

import java.util.Comparator;

public class Utils {

    public  static <T> Element<T> min(T[] values,Comparator<T> comparator){
        return getMinOrMaxValueIndex(values,comparator);
    }

    public static <T> Element<T> max(T[] values, Comparator<T> comparator){
        return getMinOrMaxValueIndex(values, comparator);
    }

    private static <T> Element<T> getMinOrMaxValueIndex(T[] elements, Comparator<T> comparator){

        T value = elements[0];
        int index = 0;
        for (int i = 0; i < elements.length; i++) {
            int v = comparator.compare(value, elements[i]);
            if (v > 0){
                value = elements[i];
                index = i;
            }
        }
        return new Element<>(index, value);
    }

    public record Element<T>(int index, T value) {
    }
}

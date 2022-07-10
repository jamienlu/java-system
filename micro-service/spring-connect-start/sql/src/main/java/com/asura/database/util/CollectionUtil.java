package com.asura.database.util;

import java.util.ArrayList;
import java.util.List;

public class CollectionUtil {
    public static <T> List<List<T>>  splitCollections(List<T> list, int size) {
        List<List<T>> result = new ArrayList<List<T>>();
        int listSize = list.size();
        int toIndex = size;
        for (int i = 0; i < list.size(); i += size) {
            if (i + size > listSize) {
                toIndex = listSize - i;
            }
            List<T> newList = list.subList(i, i + toIndex);
            result.add(newList);
        }
        return result;
    }
}

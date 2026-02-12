package com.ems.utils;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class IdGeneratorUtil {

    public static String generateNextAvailableId(List<String> existingIds, String prefix) {
        Set<Integer> numbers = new TreeSet<>();
        int prefixLength = prefix.length();

        for (String id : existingIds) {
            if (id != null && id.startsWith(prefix)) {
                try {
                    int num = Integer.parseInt(id.substring(prefixLength));
                    numbers.add(num);
                } catch (NumberFormatException ignored) {}
            }
        }

        int next = 1;
        for (int num : numbers) {
            if (num != next) break;
            next++;
        }

        return String.format("%s%03d", prefix, next);
    }
}

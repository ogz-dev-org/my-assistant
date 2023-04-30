package com.ogz.mailassistance.utils;

import java.util.*;
import java.util.stream.IntStream;

public class Date {
    private static String[] months ={"Jan", "Feb", "Mar","Apr", "May", "Jun","Jul", "Agu", "Sep","Oct","Nov","Dec"};
    public static int monthNum (String month){
        return IntStream.range(0, months.length)
                .filter(i -> month.equals(months[i]))
                .findFirst().orElse(-1);
    }
}

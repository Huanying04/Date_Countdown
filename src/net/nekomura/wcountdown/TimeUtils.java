package net.nekomura.wcountdown;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class TimeUtils {
    public static boolean isTimeFormat(String dfstr) {
        boolean b;

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm");

        try {
            df.setLenient(false);
            df.parse(dfstr);
            b = true;
        } catch (ParseException e) {
            b = false;
        }

        return b;
    }

    public static String keepTwoDigit(long num) {
        if(num < 10 && num >= 0) return "0" + num;
        else return String.valueOf(num);
    }
}
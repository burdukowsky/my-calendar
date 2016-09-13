package org.sobcorp.mycalendar;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Станислав on 19.10.2015.
 */
public class Math
{
    // четное ли?
    public boolean isChetnoe(int n) {
        if ((n % 2) == 0) {
            return true;
        } else {
            return false;
        }
    }

    // возвращает день недели первого числа заданного месяца
    public int getStartDay(int curYear, int curMonth) {
        Calendar newCal = new GregorianCalendar();
        newCal.set(curYear, curMonth - 1, 1, 0, 0, 0);
        newCal.setTime(newCal.getTime());
        int startDay = newCal.get(Calendar.DAY_OF_WEEK);
        if (startDay == 1) {
            startDay = 7;
        } else {
            startDay--;
        }
        return startDay;
    }

    // возвращает номер текущего дня недели
    public int getNumberOfCurDayOfWeek() {
        Calendar calendar = Calendar.getInstance();
        Calendar newCal = new GregorianCalendar();
        newCal.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        newCal.setTime(newCal.getTime());
        int startDay = newCal.get(Calendar.DAY_OF_WEEK);
        if (startDay == 1) {
            startDay = 7;
        } else {
            startDay--;
        }
        return startDay;
    }

    // возвращает длину заданного месяца
    public int getLengthOfMonth(int curYear, int curMonth) {
        int[] daysInMonths = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        GregorianCalendar c = new GregorianCalendar();
        daysInMonths[1] += c.isLeapYear(curYear) ? 1 : 0;
        return daysInMonths[curMonth - 1];
    }
}

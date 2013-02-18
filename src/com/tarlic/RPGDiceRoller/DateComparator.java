package com.tarlic.RPGDiceRoller;

import java.sql.Date;
import java.util.Comparator;

public class DateComparator implements Comparator<Date> {
    public int compare(Date date1, Date date2) {
        //return date1.compareTo(date2);
    	return date2.compareTo(date1);
    }
}

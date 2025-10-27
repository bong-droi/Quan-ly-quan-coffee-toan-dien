/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author DELL-PC
 */
public class Date_Helper {

    static final SimpleDateFormat DATE_FORMATER = new SimpleDateFormat("dd/MM/yyyy");

    /**
     * * * @param date 
     *
     *
     * @param date
     * @param pattern *  * @return Date
   
     *
     * @return date
     */
    public static Date toDate(String date, String... pattern) {
        try {
            if (pattern.length > 0) {

                DATE_FORMATER.applyPattern(pattern[0]);

            }
            if (date == null) {
                return Date_Helper.now();
            }
            System.out.println(date);
            return DATE_FORMATER.parse(date);
        } catch (ParseException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * * 
     *
     * * @param pattern * @return String 
     * @param date
     * @param pattern
     * @return String
     */
    public static String toString(Date date, String... pattern) {
        if (pattern.length > 0) {
            DATE_FORMATER.applyPattern(pattern[0]);
        }
        if (date == null) {
            date = Date_Helper.now();
        }
        return DATE_FORMATER.format(date);
    }

    /**
     * * * @return Date 
     *
     * @return date
     */
    public static Date now() {
        return new Date();
    }

    /**
     * * * @param date 
     *
     *
     * @param date
     * @param days  * @return Date 
     * @return date
     */
    public static Date addDays(Date date, int days) {
        date.setTime(date.getTime() + days * 24 * 60 * 60 * 1000);
        return date;
    }

    /**
     * * * @param days 
     * * @return Date 
     *
     * @param days
     * @return date
     */
    public static Date add(int days) {
        Date now = Date_Helper.now();
        now.setTime(now.getTime() + days * 24 * 60 * 60 * 1000);
        return now;
    }
}

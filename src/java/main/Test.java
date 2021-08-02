/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.ParseException;

/**
 *
 * @author Dell
 */
public class Test {

    public static boolean validateJavaDate(String strDate) throws java.text.ParseException {
        if (strDate.trim().equals("")) {
            return true;
        } else {
            SimpleDateFormat sdfrmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            sdfrmt.setLenient(false);
            try {
                Date date = sdfrmt.parse(strDate);
                System.out.println(date + " is valid date format");
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:ss");
                System.out.println(simpleDateFormat.format(date));
            } catch (ParseException e) {
                System.out.println(strDate + " is Invalid Date format");
                return false;
            }
            return true;
        }
    }

    public static void main(String args[]) throws java.text.ParseException {

        validateJavaDate("abc");
        validateJavaDate("2021-10-10T19:19:19.123Z");
        validateJavaDate("12,29,2016");
    }

}

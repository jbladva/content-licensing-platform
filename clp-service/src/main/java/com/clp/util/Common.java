package com.clp.util;

import lombok.extern.slf4j.Slf4j;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class Common {
    public static boolean isValidMobileNo(String mobileNo) {
        return digitSizeChecker(mobileNo, 10);
    }
    public static boolean digitSizeChecker(String number, int size) {
        Pattern ptrn = Pattern.compile("^\\d{" + size + "}$");
        Matcher match = ptrn.matcher(String.valueOf(number));
        return (match.find() && match.group().equals(number.toString()));
    }
    public static boolean isValidEmail(String email) {
        String emailRegex = "^(.+)@(\\S+)$";
        return email.matches(emailRegex);
    }
    public static boolean isValidEirCode(String eirCode)
    {
         if(eirCode.length() < 7)
             return false;
         return true;
    }
    public static boolean isValidPassword(String password)
    {
        return password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,20}$");
    }
}

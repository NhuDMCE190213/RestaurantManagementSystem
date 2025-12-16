/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package constant;

import static constant.Constants.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;

/**
 *
 * @author Dai Minh Nhu - CE190213
 */
public class CommonFunction {

    public static boolean isEmptyString(String str) {
        return str == null || str.equals("");
    }

    public static String getVNDString(String str) {

        if (str.indexOf("%") > 0) {
            return str;
        }

        String oldStr = str;
        String newStr = "";
        String tmpStr = "";

        while (oldStr.length() > 0) {

            if (oldStr.length() > 3) {
                tmpStr = oldStr.substring(oldStr.length() - 3, oldStr.length());
                oldStr = oldStr.substring(0, oldStr.length() - 3);
            } else {
                tmpStr = oldStr;
                oldStr = "";
            }

            newStr = tmpStr + "." + newStr;

        }

        newStr = newStr.substring(0, newStr.length() - 1);
        newStr = newStr + " VND";

        return newStr;
    }

    public static String stringConvertDateTime(String str) {
        String[] strs = str.split("T");

        if (strs.length >= 2) {
            return String.format("%s %s", strs[0], strs[1]).trim();
        } else {
            return str;
        }
    }

    public static int getTotalPages(int countItems) {
        return (int) Math.ceil((double) countItems / Constants.MAX_ELEMENTS_PER_PAGE);
    }

    public static boolean validateString(String str, int limitLength) {
        if (limitLength < 0) limitLength = Integer.MAX_VALUE;
        
        return !(str == null || str.isEmpty()) && str.length() <= limitLength;
    }

    public static boolean validateInteger(int num, boolean allowNegative, boolean allowZero, boolean allowPositive) {
        if (!allowNegative && num < 0) {
            return false;
        }
        if (!allowZero && num == 0) {
            return false;
        }
        if (!allowPositive && num > 0) {
            return false;
        }

        return true;
    }

    public static String upperCaseFirstLetter(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static String addEDtoEverything(String str) {
        String temp = str.substring(str.length() - 1, str.length());
        return str + (temp.equalsIgnoreCase("t") || temp.equalsIgnoreCase("d") ? "ed" : "d");
    }

    public static int checkErrorSQL(SQLException ex) {
        if (ex.getErrorCode() == DUPLICATE_KEY) {  //duplicate key
            System.err.println("DUPLICATE KEY");
            return -DUPLICATE_KEY;
        } else if (ex.getErrorCode() == FOREIGN_KEY_VIOLATION) {  //foreign key violation
            System.err.println("Foreign key violation");
            return -FOREIGN_KEY_VIOLATION;
        } else if (ex.getErrorCode() == NULL_INSERT_VIOLATION) {  //null insert violation
            System.err.println("Null insert violation");
            return -NULL_INSERT_VIOLATION;
        }  else if (ex.getErrorCode() == UNIQUE_INDEX) {  //null insert violation
            System.err.println("DUPLICATE UNIQUE");
            return -UNIQUE_INDEX;
        }
        
        return 0;
    }
    
    public static void setPopup(HttpServletRequest request, boolean status, String message) {
        HttpSession session = request.getSession(false);
        session.setAttribute("popupStatus", status);
        session.setAttribute("popupMessage", message);
    }

    public static void removePopup(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            request.setAttribute("popupStatus", session.getAttribute("popupStatus"));
            request.setAttribute("popupMessage", session.getAttribute("popupMessage"));

            session.removeAttribute("popupStatus");
            session.removeAttribute("popupMessage");
        }
    }

    public static String getSqlErrorCode(int temp_code) {
        if (temp_code + Constants.DUPLICATE_KEY == 0) {                //check trung code/key
            return "DUPLICATE_KEY";
        } else if (temp_code + Constants.FOREIGN_KEY_VIOLATION == 0) {
            return "FOREIGN_KEY_VIOLATION";
        } else if (temp_code + Constants.NULL_INSERT_VIOLATION == 0) {
            return "NULL_INSERT_VIOLATION";
        }  else if (temp_code + Constants.UNIQUE_INDEX == 0) {
            return "DUPLICATE_UNIQUE"; 
        }

        return "Unknow Error Code:" + temp_code;
    }
}

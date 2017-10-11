package com.NamohTours.Service;

public class ValidationToolBox {


    public static boolean validateEmailId(String emailId) {
        if (emailId.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")) {
            return true;
        }
        return false;
    }


    public static boolean validateMobNo(String mobNo) {
        if (mobNo.matches("\\d{10}")) {
            return true;
        }
        return false;
    }


    public static boolean validateFirstName(String FirstName) {
        if (FirstName.matches("([a-zA-Z]){3,}")) {
            return true;
        }

        return false;
    }

    public static boolean validateLastName(String LastName) {
        if (LastName.matches("([a-zA-Z]){3,}")) {
            return true;
        }

        return false;
    }


    public static boolean validatePassword(String password) {
        if (password.matches(".{4,}")) {
            return true;
        }

        return false;
    }

    public static boolean validatePasswordAndConfirmPwd(String password, String confirm) {
        if (password.matches(confirm)) {
            return true;
        }

        return false;
    }


    public static boolean validateFullName(String Name) {

        if (Name.matches("^([A-Za-z\\s]+[A-Za-z]+)$")) {
            return true;
        }

        return false;
    }


}

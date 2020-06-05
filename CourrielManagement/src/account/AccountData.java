/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package account;

/**
 *
 * @author Perfection
 */
public class AccountData {

    public static String id, fullName, password, mail, number;

    public static String getId() {
        return id;
    }

    public static void setId(String id) {
        AccountData.id = id;
    }

    public static String getFullName() {
        return fullName;
    }

    public static void setFullName(String fullName) {
        AccountData.fullName = fullName;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        AccountData.password = password;
    }

    public static String getMail() {
        return mail;
    }

    public static void setMail(String mail) {
        AccountData.mail = mail;
    }

    public static String getNumber() {
        return number;
    }

    public static void setNumber(String number) {
        AccountData.number = number;
    }
}

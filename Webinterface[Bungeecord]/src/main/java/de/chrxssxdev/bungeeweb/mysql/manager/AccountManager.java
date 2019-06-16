package de.chrxssxdev.bungeeweb.mysql.manager;

import de.chrxssxdev.bungeeweb.BungeeWeb;
import de.chrxssxdev.bungeeweb.Enums.AccountType;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

public class AccountManager {

    public static boolean accountExists(String name) {
        try {
            PreparedStatement ps = BungeeWeb.getMySQL().getConnection().prepareStatement("SELECT * FROM accounts WHERE USERNAME=?");
            ps.setObject(1, name);

            ResultSet rs = ps.executeQuery();
            boolean exists = rs.next();

            ps.closeOnCompletion();
            rs.close();

            return exists;

        } catch (SQLException e) {
            BungeeWeb.getInstance().getLogger().log(Level.WARNING, "[SYSTEM-SQL] ERROR: " + e.getMessage());
        }
        return false;
    }

    public static void createAccount(String name, String password, AccountType group) {
        if (accountExists(name)) return;

        try {
            PreparedStatement ps = BungeeWeb.getMySQL().getConnection().prepareStatement("INSERT INTO accounts (USERNAME, PASSWORD, USERGROUP) VALUES (?,?,?)");
            ps.setObject(1, name);
            try {
                ps.setObject(2, convertToMd5(password));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            ps.setObject(3, group.getName());

            ps.execute();
            ps.closeOnCompletion();

        } catch (SQLException e) {
            BungeeWeb.getInstance().getLogger().log(Level.WARNING, "[SYSTEM-SQL] ERROR: " + e.getMessage());
        }
    }

    public static void deleteAccount(String name) {
        if(!accountExists(name)) return;

        try {
            PreparedStatement ps = BungeeWeb.getMySQL().getConnection().prepareStatement("DELETE FROM accounts WHERE USERNAME=?");
            ps.setObject(1, name);

            ps.execute();
            ps.closeOnCompletion();

        } catch (SQLException e) {
            BungeeWeb.getInstance().getLogger().log(Level.WARNING, "[SYSTEM-SQL] ERROR: " + e.getMessage());
        }
    }

    public static void changePassword(String name, String password) {
        if(!accountExists(name)) return;

        try {
            PreparedStatement ps = BungeeWeb.getMySQL().getConnection().prepareStatement("UPDATE accounts SET PASSWORD=? WHERE USERNAME=?");
            ps.setObject(1, convertToMd5(password));
            ps.setObject(2, name);

            ps.execute();
            ps.closeOnCompletion();

        } catch (SQLException | UnsupportedEncodingException e) {
            BungeeWeb.getInstance().getLogger().log(Level.WARNING, "[SYSTEM-SQL] ERROR: " + e.getMessage());
        }
    }

    private static String convertToMd5(String md5) throws UnsupportedEncodingException {
        StringBuffer sb = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes("UTF-8"));
            sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString(array[i] & 255 | 256).substring(1, 3));
            }
            return sb.toString();
        }
        catch (NoSuchAlgorithmException md) {
            return sb.toString();
        }
    }
}

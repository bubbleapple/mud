package Utils.UserAuth;

import Config.Configure;
import IO.IO;
import Utils.DB.SingleConnection;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.*;

public class AuthUtils {
    public static boolean checkUserName(String userName, IO out, Connection con) throws SQLException {
        // check duplicate username in db
        PreparedStatement stmt = con
                .prepareStatement("select count(*) from user where username = ?");
        stmt.setString(1, userName);
        ResultSet rs = stmt.executeQuery();
        rs.next();
        if(rs.getInt(1) > 0) {
            out.print("Duplicate username, please change to another one:");
            return false;
        }

        return true;
    }

    public static String hashPassword(String pw) {
        byte[] salt = Configure.SALT;
        KeySpec spec = new PBEKeySpec(pw.toCharArray(), salt, 65536, 128);
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hash = factory.generateSecret(spec).getEncoded();
            return new String(hash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }
}

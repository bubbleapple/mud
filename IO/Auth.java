package IO;

import Model.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import static Model.User.createUser;
import static Model.User.getUserByAuth;
import static Model.User.getUserById;
import static Utils.UserAuth.AuthUtils.checkUserName;
import static Utils.UserAuth.AuthUtils.hashPassword;

public class Auth {
    private BufferedReader input;
    private IO out;
    private Connection con;

    public Auth(BufferedReader input, IO out, Connection con) {
        this.input = input;
        this.out = out;
        this.con = con;
    }

    public User auth() {
        String line;
        while(true) {
            try {
                line = input.readLine();
                if(line.equalsIgnoreCase("login")) {
                    return login();
                } else if(line.equalsIgnoreCase("register")) {
                    return register();
                } else if(line.equalsIgnoreCase("QUIT")) {
                    return null;
                } else {
                    out.print("Invalid command! Please type in login or register:");
                }
            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private User register() throws IOException, SQLException {
        out.print("Username:");
        String userName = input.readLine();
        while(!checkUserName(userName, out, con)) {
            userName = input.readLine();
        }

        out.print("Password:");
        // TODO: display * for any input char of password
        String passWord = hashPassword(input.readLine());
        if(passWord == null) {
            System.err.println("Error in hashing the password");
            return null;
        }
        int userId = createUser(con, userName, passWord);
        return getUserById(con, userId);
    }

    private User login() throws IOException {
        out.print("Username:");
        String userName = input.readLine();

        out.print("Password:");
        // TODO: display * for any input char of password
        String passWord = hashPassword(input.readLine());

        if(passWord == null) {
            System.err.println("Error in hashing the password");
            return null;
        }

        try {
            return getUserByAuth(con, userName, passWord);
        } catch (SQLException e) {
            out.print("Can't find user, please type in again.");
            return login();
        }
    }
}

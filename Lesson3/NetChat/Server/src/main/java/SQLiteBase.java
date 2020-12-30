import java.sql.*;
import java.sql.Connection;

public class SQLiteBase {
    public static Connection connection;
    public static Statement statement;
    public static PreparedStatement preparedStatement;
    public static String nickname;
    public static ResultSet resultSet;


    public static void main(String[] args) {
        try {
            connect();
            showInfo();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:users.db");
            statement = connection.createStatement();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static void disconnect() {
        try {
            statement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    public static synchronized void showInfo() throws SQLException {
        resultSet = statement.executeQuery("SELECT * FROM users;");
        while (resultSet.next()) {
            System.out.println(resultSet.getString("login") + " " + resultSet.getString("password") + " " + resultSet.getString("nickname"));
        }

    }

    public static synchronized String checkLoginPassword(String login, String password) throws SQLException {
        try {
            resultSet = statement.executeQuery("SELECT * FROM users;");
            while (resultSet.next()) {
                if (resultSet.getString("login").equals(login.trim()) && (resultSet.getString("password").equals(password.trim()))) {
                    nickname = resultSet.getString("nickname");
                    return "Success";
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            resultSet.close();
            disconnect();
        }
        return "Denied";
    }

    public static synchronized void changeNick (String newNick, String oldNick){
        try {
            changeNickStmtPattern(oldNick);
            preparedStatement.setString(1, newNick);
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static synchronized void addUser(String login, String password, String nickname) throws SQLException {
        addUserStmtPattern();
        preparedStatement.setString(1, login);
        preparedStatement.setString(2, password);
        preparedStatement.setString(3, nickname);
        preparedStatement.executeUpdate();
    }

    public static void changeNickStmtPattern(String oldNick) throws SQLException {
        preparedStatement = connection.prepareStatement("UPDATE users SET nickname = ? WHERE nickname = ?;");
        preparedStatement.setString(2, oldNick);
    }

    public static void addUserStmtPattern() throws SQLException {
        preparedStatement = connection.prepareStatement("INSERT INTO users (login, password, nickname) VALUES (?, ?, ?);");
    }

}

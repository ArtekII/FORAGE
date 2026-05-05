package dao;
import java.sql.*;

public class UtilDAO {
    private String url;
    private String user;
    private String password;
    private String database;
    private int port;

    public String getPassword() {
        return password;
    }
    public int getPort() {
        return port;
    }
    public String getUrl() {
        return url;
    }
    public String getUser() {
        return user;
    }

    public String getDatabase() {
        return database;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public void setPort(int port) {
        this.port = port;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public void setUser(String user) {
        this.user = user;
    }
    public void setDatabase(String database) {
        this.database = database;
    }

    public Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return DriverManager.getConnection(getUrl() + ":" + getPort() + "/" + getDatabase(), getUser(), getPassword());
    } 

}
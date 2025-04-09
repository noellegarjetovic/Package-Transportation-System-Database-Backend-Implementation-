package student;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB {
	private static final String username="sa";
    private static final String password="Noelle12";
    private static final String database="projekatSab";
    private static final int port=1433;
    private static final String server="localhost";
    private static final String connectionUrl = "jdbc:sqlserver://"+server+":"+port+";databaseName="+database;
    private Connection connection;

    public Connection getConnection(){
        return connection;
}

    private DB(){
        try{
            connection=DriverManager.getConnection(connectionUrl, username, password);
        }catch(SQLException e){}
    }


    private static DB db=null;
    public static DB getInstance(){
        if(db==null)
            db=new DB();
        return db;
    }
}

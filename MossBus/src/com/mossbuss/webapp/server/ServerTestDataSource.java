/*
 * Created on 2006/01/05
 *
 */
package com.mossbuss.webapp.server;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.Statement;


import java.util.logging.Logger;

import javax.sql.DataSource;

public class ServerTestDataSource implements DataSource {

    private static final String Username = "root";
    private static final String Password = "password";
    
    /**
     * 
     */
	/* (non-Javadoc)
     * @see javax.sql.DataSource#getConnection()
     */
    public Connection getConnection() throws SQLException {

        Connection Con = null;
        //String url = "jdbc:jtds:sqlserver://"+IPAddress+":1433/"+DBName+";user="+Username+";password="+Password;
        String url = "jdbc:mysql://127.0.0.1/mossbuss?autoReconnect=true&zeroDateTimeBehavior=convertToNull";
        String name = "root";
        String password = "password";
        int attempt = 0;
        while((Con == null) && (attempt < 2)) {
            try {
                Con = DriverManager.getConnection(url,name,password);
            } catch(SQLException e) {
            	System.out.println("DB Error: "+e.getMessage());
                e.printStackTrace();
            }
            attempt++;
        }
        if(Con == null) {
            throw new SQLException("Error connecting to database");
        }
        return Con;
        
    }

    /* (non-Javadoc)
     * @see javax.sql.DataSource#getConnection(java.lang.String, java.lang.String)
     */
    public Connection getConnection(String arg0, String arg1) throws SQLException {
        return getConnection();
    }

    /* (non-Javadoc)
     * @see javax.sql.DataSource#getLogWriter()
     */
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    /* (non-Javadoc)
     * @see javax.sql.DataSource#setLogWriter(java.io.PrintWriter)
     */
    public void setLogWriter(PrintWriter arg0) throws SQLException {

    }

    /* (non-Javadoc)
     * @see javax.sql.DataSource#setLoginTimeout(int)
     */
    public void setLoginTimeout(int arg0) throws SQLException {

    }

    /* (non-Javadoc)
     * @see javax.sql.DataSource#getLoginTimeout()
     */
    public int getLoginTimeout() throws SQLException {
        return 0;
    }
    public static void main(String[] args) {
        try {
            ServerTestDataSource ds = new ServerTestDataSource();
            Connection con = ds.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("Select 1 as number");
            while(rs.next()) {
                long n = rs.getLong("number");
                System.out.println(n);
            }
            rs.close();
            stmt.close();
            con.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
	public boolean isWrapperFor(Class<?> arg0) throws SQLException {
		// TODO isWrapperFor
		return false;
	}
	public <T> T unwrap(Class<T> arg0) throws SQLException {
		// TODO unwrap
		return null;
	}
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		// TODO getParentLogger
		return null;
	}
	public ServerTestDataSource() throws ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
    }
}

package src.database;

import java.sql.*;

public class Oracle extends Database
{
  //Constructeurs.
  public Oracle(String p_dbIp, String p_dbPort, String p_dbSid, String p_dbPass, String p_dbUser)
  {
    this.a_dbIp = p_dbIp;
    this.a_dbPort = p_dbPort;
    this.a_dbSid = p_dbSid;
    this.a_dbPass = p_dbPass;
    this.a_dbType = "oracle";
    this.a_dbUser = p_dbUser;
    this.a_dbCon = null;
  }

  //Methodes.
  //Connecte à la DB.
  @Override
  public void connectDb()
  {
    String v_urlCon = null;
    try
    {
      Class.forName("oracle.jdbc.driver.OracleDriver");
      v_urlCon = "jdbc:oracle:thin:@"+this.a_dbIp+":"+this.a_dbPort+":"+this.a_dbSid;
      this.a_dbCon = DriverManager.getConnection(v_urlCon, this.a_dbUser, this.a_dbPass);
    }
    catch(ClassNotFoundException e)
    {
      e.printStackTrace();
    }
    catch(SQLException e)
    {
      e.printStackTrace();
    }
  }
}

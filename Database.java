import java.sql.*;

public class Database
{
  //Attributs.
  private String a_dbIp;
  private String a_dbPort;
  private String a_dbSid;
  private String a_dbPass;
  private String a_dbType;
  private String a_dbUser;
  private Connection a_dbCon;

  //Constructeurs.
  public Database(String p_dbIp, String p_dbPort, String p_dbSid, String p_dbPass, String p_dbType, String p_dbUser)
  {
    this.a_dbIp = p_dbIp;
    this.a_dbPort = p_dbPort;
    this.a_dbSid = p_dbSid;
    this.a_dbPass = p_dbPass;
    this.a_dbType = p_dbType;
    this.a_dbUser = p_dbUser;
    this.a_dbCon = null;
  }

  //Getters.
  public String getIp()
  {
    return this.a_dbIp;
  }
  public String getPort()
  {
    return this.a_dbPort;
  }
  public String getSid()
  {
    return this.a_dbSid;
  }
  public String getPass()
  {
    return this.a_dbPass;
  }
  public String getType()
  {
    return this.a_dbType;
  }
  public String getUser()
  {
    return this.a_dbUser;
  }
  public Connection getCon()
  {
    return this.a_dbCon;
  }

  //Connecte à la DB.
  public void connectDb()
  {
    String v_urlCon = null;
    try
    {
      if (this.a_dbType.compareToIgnoreCase("oracle") == 0)
      {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        v_urlCon = "jdbc:oracle:thin:@"+this.a_dbIp+":"+this.a_dbPort+":"+this.a_dbSid;
        this.a_dbCon = DriverManager.getConnection(v_urlCon, this.a_dbUser, this.a_dbPass);
      }
      else if (this.a_dbType.compareToIgnoreCase("postgres") == 0)
      {
        System.out.println("postgresDb"); //... A continuer ...
      }
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

  //Déconnecte de la DB.
  public void disconnectDb()
  {
    try
    {
      this.a_dbCon.close();
    }
    catch(SQLException e)
    {
      e.printStackTrace();
    }
  }

  //Récupère le statut de la connection à la DB (connecté/true déconnecté/false).
  public Boolean connectionState()
  {
    Boolean v_dbState = false;
    try
    {
      v_dbState = this.a_dbCon.isValid(3);
    }
    catch(SQLException e)
    {
      e.printStackTrace();
    }
    finally
    {
      return v_dbState;
    }
  }
}

package src.database;

//Importation des packages div.
import java.sql.*;

public abstract class Database
{
  //Attributs.
  protected String a_dbIp;
  protected String a_dbPort;
  protected String a_dbSid;
  protected String a_dbPass;
  protected String a_dbType;
  protected String a_dbUser;
  protected Connection a_dbCon;

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

  public abstract void connectDb();

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

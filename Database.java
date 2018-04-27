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
  protected String getIp()
  {
    return this.a_dbIp;
  }
  protected String getPort()
  {
    return this.a_dbPort;
  }
  protected String getSid()
  {
    return this.a_dbSid;
  }
  protected String getPass()
  {
    return this.a_dbPass;
  }
  protected String getType()
  {
    return this.a_dbType;
  }
  protected String getUser()
  {
    return this.a_dbUser;
  }
  protected Connection getCon()
  {
    return this.a_dbCon;
  }

  protected abstract void connectDb();

  //Déconnecte de la DB.
  protected void disconnectDb()
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
  protected Boolean connectionState()
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

/*# ==========================================================================================================================
# This abstract class correponds to a database object that allows to connect, disconnect from database. .
#
# Author : Alba Thomas (All4it)
#
# Creation date : 2018 April
#
# Revisions
# ------------------------------------------------------------------------------------------------------------------------
# Version | Date       | Author                            | Comments
# ------------------------------------------------------------------------------------------------------------------------
# 1.0     | 2018/05    | Alba Thomas (All4it)              | Initial version
#
# ------------------------------------------------------------------------------------------------------------------------
# ==========================================================================================================================
*/

package src.database;

import java.sql.*;

public abstract class Database
{
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

  //Disconnects from database.
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

  //Retrieves connection status to database.
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

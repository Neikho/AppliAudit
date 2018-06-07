package src.database;

import java.sql.*;

public class Query
{
  protected String sqlQuery;
  protected Statement a_sqlInstruct = null;
  protected ResultSet a_resQuery;
  protected ResultSetMetaData a_queryCols;

  public Query()
  {
    this.sqlQuery = null;
    this.a_sqlInstruct = null;
    this.a_resQuery = null;
    this.a_queryCols = null;
  }

  public String getSqlQuery()
  {
    return this.sqlQuery;
  }
  public Statement getSqlInstruc()
  {
    return this.a_sqlInstruct;
  }
  public ResultSet getQueryRes()
  {
    return this.a_resQuery;
  }
  public ResultSetMetaData getQueryCols()
  {
    return this.a_queryCols;
  }

  public void setSqlQuery(String sqlQuery)
  {
    this.sqlQuery = sqlQuery;
  }
  public void setSqlInstruc(Statement a_sqlInstruct)
  {
    this.a_sqlInstruct = a_sqlInstruct;
  }
  public void setQueryRes(ResultSet a_resQuery)
  {
    this.a_resQuery = a_resQuery;
  }
  public void setQueryCols(ResultSetMetaData a_queryCols)
  {
    this.a_queryCols = a_queryCols;
  }

//Executes and retrieves columns(metadata) of current query retrieved from TreeMap.
  public void execQuery(Database p_database, String p_query)
  {
    try
    {
      this.setSqlInstruc(p_database.getCon().createStatement());
      this.setQueryRes(this.getSqlInstruc().executeQuery(p_query));
      this.setQueryCols(this.getQueryRes().getMetaData());
    }
    catch(SQLException e)
    {
      e.printStackTrace();
    }
  }
}

public class Database
{
  //Attributs.
  private String a_dbIp;
  private String a_dbPort;
  private String a_dbName;
  private String a_dbPass;
  private String a_dbType;
  private String a_dbUser;

  //Constructeurs.
  public Database(String p_dbIp, String p_dbPort, String p_dbName, String p_dbPass, String p_dbType, String p_dbUser)
  {
    this.a_dbIp = p_dbIp;
    this.a_dbPort = p_dbPort;
    this.a_dbName = p_dbName;
    this.a_dbPass = p_dbPass;
    this.a_dbType = p_dbType;
    this.a_dbUser = p_dbUser;
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
  public String getName()
  {
    return this.a_dbName;
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
}

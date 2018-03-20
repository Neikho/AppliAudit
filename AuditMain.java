//PACKAGES.
import java.io.*;
import java.util.TreeMap;
import java.util.Map;
import java.sql.*;

public class AuditMain
{
    public static void main(String[] args)
    {
      //Variables pour lire le fichier de conf contenant les infos de la DB cible (IP, port, user, pas ...).
      String v_targetDBFilePath           = new String("targetDB.conf");
        File v_targetDBFile               = new File(v_targetDBFilePath);
      BufferedReader v_buff               = null;
        int v_isReading                   = 1;
      TreeMap<String, String> v_mapDBConf = new TreeMap<>();
        String v_paramMap1                = "";
        String v_paramMap2                = "";
        String v_concat                   = "";

      //Variables pour la connection à la DB cible.
      Database v_database;

      //Parcours et extrait les données du fichier de conf de la DB cible vers un TreeMap.
      //Paramètres de ce fichier de conf : DB_IP_ADDR, DB_PORT, DB_TYPE, DB_NAME, DB_USER, DB_PASS
      try
      {
        v_buff = new BufferedReader(new FileReader(v_targetDBFile));
      }
      catch (FileNotFoundException e)
      {
        System.out.println("(AuditMain.java) -> File not found : " + v_targetDBFilePath);
      }

      try
      {
        while ((v_isReading = v_buff.read()) != -1)
        {
            switch ((char) v_isReading)
            {
              case '=':
                v_paramMap1 = v_concat;
                v_concat= "";
                break;
              case ';':
                v_paramMap2 = v_concat;
                v_concat= "";
                break;
              case '\n':
                v_mapDBConf.put(v_paramMap1, v_paramMap2);
                v_concat = "";
                break;
              default:
                v_concat= v_concat + (char) v_isReading;
                break;
            }
        }
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
      finally
      {
        try
        {
          v_buff.close();
        }
        catch (IOException e)
        {
          e.printStackTrace();
        }
      }
      //Tests.
      v_database = new Database(v_mapDBConf.get("DB_IP_ADDR"), v_mapDBConf.get("DB_PORT"), v_mapDBConf.get("DB_NAME"), v_mapDBConf.get("DB_PASS"), v_mapDBConf.get("DB_TYPE"), v_mapDBConf.get("DB_USER"));
      System.out.println(v_database.getIp());
      System.out.println(v_database.getPort());
      System.out.println(v_database.getType());

      //Connection to database.
      System.out.println(v_database.connectionState());
      v_database.connectDb();
      System.out.println(v_database.connectionState());
      v_database.disconnectDb();
      System.out.println(v_database.connectionState());
    }
}

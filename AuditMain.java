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
      BufferedReader v_buffDbConf         = null;
      TreeMap<String, String> v_mapDBConf = new TreeMap<>();
      //Variables pour lire le fichier des queries à executer.
      String v_targetQueriesFilePath      = new String("queriesToExec.sql");
        File v_targetQueriesFile          = new File(v_targetQueriesFilePath);
      BufferedReader v_buffQueries        = null;
      TreeMap<Integer, String> v_mapQueries = new TreeMap<>();
        Integer v_compteur                      = 1;
      //Variables communes pour lire fichier de conf et lire le fichier des queries.
      int v_isReading                   = 1;
      String v_concat                   = "";
      String v_paramMap1                = "";
      String v_paramMap2                = "";
      //Variables pour la connection à la DB cible.
      Database v_database;

      //Parcours et extrait les données du fichier de conf de la DB cible vers un TreeMap.
      //Paramètres de ce fichier de conf : DB_IP_ADDR, DB_PORT, DB_TYPE, DB_SID, DB_USER, DB_PASS
      try
      {
        v_buffDbConf = new BufferedReader(new FileReader(v_targetDBFile));
      }
      catch (FileNotFoundException e)
      {
        System.out.println("(AuditMain.java) -> File not found : " + v_targetDBFilePath);
      }

      try
      {
        while ((v_isReading = v_buffDbConf.read()) != -1)
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
          v_buffDbConf.close();
        }
        catch (IOException e)
        {
          e.printStackTrace();
        }
      }


      //Parcours et extrait les données du fichier des queries vers un TreeMap.
      try
      {
        v_buffQueries = new BufferedReader(new FileReader(v_targetQueriesFile));
      }
      catch (FileNotFoundException e)
      {
        System.out.println("(AuditMain.java) -> File not found : " + v_targetQueriesFilePath);
      }

      try
      {
        Boolean v_eoqEncountered = true;
        while ((v_isReading = v_buffQueries.read()) != -1)
        {
            switch ((char) v_isReading)
            {
              case ';':
                v_concat = v_concat + (char) v_isReading;
                v_eoqEncountered = true;
                break;
              case '\n':
                if (v_eoqEncountered)
                  v_mapQueries.put(v_compteur, v_concat);
                  v_concat = "";
                  v_compteur = v_compteur + 1;
                if (!v_eoqEncountered)
                  v_concat = v_concat + (char) v_isReading;
                break;
              default:
                v_concat = v_concat + (char) v_isReading;
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
          v_buffQueries.close();
        }
        catch (IOException e)
        {
          e.printStackTrace();
        }
      }
      //Tests.
      v_database = new Database(v_mapDBConf.get("DB_IP_ADDR"), v_mapDBConf.get("DB_PORT"), v_mapDBConf.get("DB_SID"), v_mapDBConf.get("DB_PASS"), v_mapDBConf.get("DB_TYPE"), v_mapDBConf.get("DB_USER"));
      //Connection to database.
      for(Map.Entry<Integer,String> entry : v_mapQueries.entrySet())
      {
        Integer key = entry.getKey();
        String value = entry.getValue();
        System.out.println(key + " => " + value);
      }
      System.out.println(v_database.connectionState());
      v_database.connectDb();
      System.out.println(v_database.connectionState());
      v_database.disconnectDb();
      System.out.println(v_database.connectionState());
    }
}

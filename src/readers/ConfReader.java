package src.readers;

//Importation des packages divers.
import java.io.*;
import java.util.TreeMap;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfReader
{
  public static TreeMap<String, String> getConf()
  {
    //Variables pour lire le fichier de conf contenant les infos de la DB cible (IP, port, user, pas ...).
    BufferedReader v_buffDbConf           = null;
      int v_isReading                     = 1;
    TreeMap<String, String> v_mapDBConf   = new TreeMap<>();
      String v_paramMap1                  = "";
      String v_paramMap2                  = "";
    String v_concat                       = "";
    String v_targetDBFilePath             = new String("./conf/targetDB.conf");
    File v_targetDBFile                   = new File(v_targetDBFilePath);
    String[] supportedDb = {"Postgres", "Oracle", "Mysql"};
    //Parcours et extrait les données du fichier de conf de la DB cible vers un TreeMap.
    //Paramètres de ce fichier de conf : DB_IP_ADDR, DB_PORT, DB_TYPE, DB_SID, DB_USER, DB_PASS, DB_CLIENT, AUDIT_AUTHOR
    try
    {
      v_buffDbConf = new BufferedReader(new FileReader(v_targetDBFile));
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
    //Check valeur pour db_type correcte pour instanciation dynamique.
    if(!Arrays.asList(supportedDb).contains(v_mapDBConf.get("DB_TYPE")))
    {
      try
      {
        throw new Exception("Erreur dans targetDB.conf, la valeur indiquée pour DB_TYPE n'est pas dans le tableau des [VALEURS POSSIBLES :...]. Attention à bien respecter la case.");
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
    }
    return v_mapDBConf;
  }

  public static TreeMap<Integer, String> getQueries()
  {
    //Variables pour lire le fichier des queries à executer.
    BufferedReader v_buffQueries          = null;
    TreeMap<Integer, String> v_mapQueries = new TreeMap<>();
      Integer v_compteur                  = 0;
      String v_toAddMap                   = "";
    String v_concat                       = "";
    String v_targetQueriesFilePath        = new String("./conf/queriesToExec.sql");
    File v_targetQueriesFile              = new File(v_targetQueriesFilePath);
    Pattern p = Pattern.compile("\\#\\[Q([0-9]+)\\]");   // the pattern to search for

    //Parcours et extrait les données du fichier des queries vers un TreeMap.
    try
    {
      v_buffQueries = new BufferedReader(new FileReader(v_targetQueriesFile));
      v_concat = "";
      v_toAddMap = "";
      while ((v_concat = v_buffQueries.readLine()) != null)
      {
        v_toAddMap = v_toAddMap + v_concat;
        Matcher m = p.matcher(v_concat);
        if(m.find())
        {
          System.out.println(m.group(1));
          v_toAddMap = v_toAddMap.replaceAll("\\#\\[Q[0-9]+\\]", "");
          v_compteur  = Integer.parseInt(m.group(1));
          System.out.println(v_toAddMap + " COMPTEUR : " + v_compteur);
        }
        if (v_concat.contains(";"))
        {
          v_toAddMap = v_toAddMap.replace(";", "");
          v_mapQueries.put(v_compteur, v_toAddMap);
          v_toAddMap = "";
        }
      }
    }
    catch (FileNotFoundException e)
    {
      System.out.println("(AuditMain.java) -> File not found : " + v_targetQueriesFile.getPath());
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
    return v_mapQueries;
  }
}

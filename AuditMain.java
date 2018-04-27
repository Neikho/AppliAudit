//Importation des packages divers.
import java.io.*;
import java.util.TreeMap;

public class AuditMain
{
    public static void main(String[] args)
    {
      //Variables pour lire le fichier de conf contenant les infos de la DB cible (IP, port, user, pas ...).
      String v_targetDBFilePath             = new String("targetDB.conf");
        File v_targetDBFile                 = new File(v_targetDBFilePath);
      BufferedReader v_buffDbConf           = null;
        int v_isReading                     = 1;
      TreeMap<String, String> v_mapDBConf   = new TreeMap<>();
        String v_paramMap1                  = "";
        String v_paramMap2                  = "";
      //Variables pour lire le fichier des queries à executer.
      String v_targetQueriesFilePath        = new String("queriesToExec.sql");
        File v_targetQueriesFile            = new File(v_targetQueriesFilePath);
      BufferedReader v_buffQueries          = null;
      TreeMap<Integer, String> v_mapQueries = new TreeMap<>();
        Integer v_compteur                  = 1;
        String v_toAddMap                   = "";
      //Variables communes pour lire fichier de conf et lire le fichier des queries.
      String v_concat                       = "";
      //Variables pour la connection et execution vers la DB cible.
      Oracle v_database = null;
      //Variables pour XML.
      String v_xmlFilePath                  = new String("auditResult.xml");
        File v_xmlFile                      = new File(v_xmlFilePath);

      //Parcours et extrait les données du fichier de conf de la DB cible vers un TreeMap.
      //Paramètres de ce fichier de conf : DB_IP_ADDR, DB_PORT, DB_TYPE, DB_SID, DB_USER, DB_PASS
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

      //Parcours et extrait les données du fichier des queries vers un TreeMap.
      try
      {
        v_buffQueries = new BufferedReader(new FileReader(v_targetQueriesFile));
        v_concat = "";
        v_toAddMap = "";
        while ((v_concat = v_buffQueries.readLine()) != null)
        {
          v_toAddMap = v_toAddMap + v_concat;
          if (v_concat.contains(";"))
          {
            v_toAddMap = v_toAddMap.replace(";", "");
            v_mapQueries.put(v_compteur, v_toAddMap);
            v_compteur = v_compteur + 1;
            v_toAddMap = "";
          }
        }
      }
      catch (FileNotFoundException e)
      {
        System.out.println("(AuditMain.java) -> File not found : " + v_targetQueriesFilePath);
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

      //Check le type de BDD et crée un objet de type Database qui permet de se connecter à une base...
      if(v_mapDBConf.get("DB_TYPE").equals("oracle"))
        v_database = new Oracle(v_mapDBConf.get("DB_IP_ADDR"), v_mapDBConf.get("DB_PORT"), v_mapDBConf.get("DB_SID"), v_mapDBConf.get("DB_PASS"), v_mapDBConf.get("DB_USER"));
      else
      {
        System.out.println("Le type de base de données n'est pas reconnu par l'application.");
        System.exit(0);
      }

      //Se connecte à la base.
      v_database.connectDb();
      if (v_database.connectionState())
        System.out.println("Connected to Database.");
      else
        System.out.println("Error, not connected to Database.");

      //Appel de XmlBuilder.
      XmlBuilder.main(v_xmlFile, v_database, v_mapQueries);

      //Appel de HtmlBuilder.
      HtmlBuilder.main(v_xmlFile, v_database);

      //Se déconnecte de la base.
      v_database.disconnectDb();
      if (!v_database.connectionState())
        System.out.println("Disconnected from Database.");
      else
        System.out.println("Error, still connected to Database.");
    }
}

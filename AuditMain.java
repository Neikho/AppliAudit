//PACKAGES.
import java.io.*;
import java.util.TreeMap;
import java.util.Map;
import java.sql.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

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
      Database v_database;
      Statement v_state = null;
      ResultSet v_resQuery;
      ResultSetMetaData v_resQueryData;
      //Variables pour XML.
      String v_xmlFilePath                  = new String("auditResult.xml");
        File v_xmlFile                      = new File(v_xmlFilePath);
      Integer v_compteur_2                  = 1;

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

      //Objet de type Databse, permet de se connecter à une base...
      v_database = new Database(v_mapDBConf.get("DB_IP_ADDR"), v_mapDBConf.get("DB_PORT"), v_mapDBConf.get("DB_SID"), v_mapDBConf.get("DB_PASS"), v_mapDBConf.get("DB_TYPE"), v_mapDBConf.get("DB_USER"));

      //Se connecte à la base.
      v_database.connectDb();
      if (v_database.connectionState())
        System.out.println("Connected to Database.");
      else
        System.out.println("Error, not connected to Database.");

      //Parcours et execute les queries extraites du fichier.
      try
      {
        //Créer ou écrase le fichier auditResult.xml
        if(v_xmlFile.exists())
        {
          v_xmlFile.delete();
          v_xmlFile.createNewFile();
        }
        else
          v_xmlFile.createNewFile();
        //Variables pour écriture du document XML.
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.newDocument();
        //Création du noeud racine du doc XML.
        Element rootElement = doc.createElement("queries");
        doc.appendChild(rootElement);
        //Parcoure du TreeMap contenant les queries à executer.
        v_state = v_database.getCon().createStatement();
        for(Map.Entry<Integer,String> entry : v_mapQueries.entrySet())
        {
          Integer v_key = entry.getKey();
          String v_query = entry.getValue();
          System.out.println(v_key + " => " + v_query);

          //Création du sous noeud de queries query.
          Element query = doc.createElement("query");
          rootElement.appendChild(query);
          //Attribution d'un attribut id_query à ce sous noeud.
          Attr attr = doc.createAttribute("id_query");
          attr.setValue(String.valueOf(v_key));
          query.setAttributeNode(attr);
          //Création du sous noeud de query rows.
          Element rows = doc.createElement("rows");
          query.appendChild(rows);

          //Execute et récupère les colonnes (metadata) de la query en cours récupérée du TreeMap.
          v_resQuery = v_state.executeQuery(v_query);
          v_resQueryData = v_resQuery.getMetaData();
          System.out.println("\n**********************************");

          //Parcoure les rows de la query en cours.
          while(v_resQuery.next())
          {
            //Création du sous noeud de rows row et affectation d'un attribut incrémenté id_row.
            Element row = doc.createElement("row");
            rows.appendChild(row);
            Attr attr2 = doc.createAttribute("id_row");
            attr2.setValue(String.valueOf(v_key)+"."+String.valueOf(v_compteur_2));
            row.setAttributeNode(attr2);

            //Récupère la donnée chaque colonne de la row en cours.
            //Parcoure chaque colonne de la row en cours.
            for(int i = 1; i <= v_resQueryData.getColumnCount(); i++)
            {
              //AJout du sous noed de row colonne et du sous noeud de colonne valeur et ajout du nom de la colonne en cours et sa valeur.
              Element colonne = doc.createElement("colonne");
              colonne.appendChild(doc.createTextNode(v_resQueryData.getColumnName(i)));
              row.appendChild(colonne);
              Element valeur = doc.createElement("valeur");
              if (v_resQuery.getString(v_resQueryData.getColumnName(i)) != null)
                valeur.appendChild(doc.createTextNode(v_resQuery.getString(v_resQueryData.getColumnName(i))));
              colonne.appendChild(valeur);
            }
            v_compteur_2 = v_compteur_2 + 1;
          }
          v_compteur_2 = 1;
          System.out.println("\n**********************************");
        }
        //Ecrit le contenu dans le fichier XML.
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(v_xmlFile);
        transformer.transform(source, result);

        //AFFICHAGE DANS CONSOLE POUR DEBUGGER A SUPPRIMER.
        StreamResult consoleResult = new StreamResult(System.out);
        transformer.transform(source, consoleResult);
      }
      catch(SQLException e)
      {
        e.printStackTrace();
      }
      catch(IOException e)
      {
        e.printStackTrace();
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }

      //Se déconnecte de la base.
      v_database.disconnectDb();
      if (!v_database.connectionState())
        System.out.println("Disconnected from Database.");
      else
        System.out.println("Error, still connected to Database.");
    }
}

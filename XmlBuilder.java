//Importation des packages divers.
import java.io.*;
import java.util.TreeMap;
import java.util.Map;
import java.sql.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class XmlBuilder
{
  public static void main(File p_xmlFile, Database p_database, TreeMap<Integer, String> p_mapQueries)
  {
    Statement v_state = null;
    ResultSet v_resQuery;
    ResultSetMetaData v_resQueryData;
    Integer v_compteur_2 = 1;
    //Parcours et execute les queries extraites du fichier.
    try
    {
      //Créer ou écrase le fichier auditResult.xml
      if(p_xmlFile.exists())
      {
        p_xmlFile.delete();
        p_xmlFile.createNewFile();
      }
      else
        p_xmlFile.createNewFile();
      //Variables pour écriture du document XML.
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.newDocument();
      //Création du noeud racine du doc XML.
      Element rootElement = doc.createElement("queries");
      doc.appendChild(rootElement);
      //Parcoure du TreeMap contenant les queries à executer.
      v_state = p_database.getCon().createStatement();
      for(Map.Entry<Integer,String> entry : p_mapQueries.entrySet())
      {
        //v_key = query key value, v_query = the query text.
        Integer v_key = entry.getKey();
        String v_query = entry.getValue();
        System.out.println(v_key + " => " + v_query);

        //Execute et récupère les colonnes (metadata) de la query en cours récupérée du TreeMap.
        v_resQuery = v_state.executeQuery(v_query);
        v_resQueryData = v_resQuery.getMetaData();
        System.out.println("\n**********************************");

        //Création du sous noeud de racine -> query et affectation d'un attribut incrémenté id_query.
        Element query = doc.createElement("query");
        rootElement.appendChild(query);
        Attr attr2 = doc.createAttribute("id_query");
        attr2.setValue(String.valueOf(v_key));
        query.setAttributeNode(attr2);
        //Check si query ne retourne aucune row, alors on affecte quand même la structure (colonnes) afin de récupérer le squelette pour builder le fichier HTML.
        if(!v_resQuery.isBeforeFirst())
        {
          //Création du sous noeud de query row et affectation d'un attribut incrémenté id_row.
          Element row = doc.createElement("row");
          query.appendChild(row);
          Attr attr3 = doc.createAttribute("id_row");
          attr3.setValue(String.valueOf(v_key)+"."+String.valueOf(v_compteur_2));
          row.setAttributeNode(attr3);

          //Récupère chaque colonne de la row en cours, ajout de ces colonnes au fichier XML.
          for(int i = 1; i <= v_resQueryData.getColumnCount(); i++)
          {
            //AJout du sous noeud de row valeur et ajout du nom de la colonne en cours en attribut et sa valeur en valeur.
            Element valeur = doc.createElement("valeur");
            row.appendChild(valeur);
            Attr attr4 = doc.createAttribute("col");
            attr4.setValue(v_resQueryData.getColumnName(i).toString());
            valeur.setAttributeNode(attr4);
          }
        }
        //
        //Parcoure les rows de la query en cours.
        while(v_resQuery.next())
        {
          //Création du sous noeud de query row et affectation d'un attribut incrémenté id_row.
          Element row = doc.createElement("row");
          query.appendChild(row);
          Attr attr3 = doc.createAttribute("id_row");
          attr3.setValue(String.valueOf(v_key)+"."+String.valueOf(v_compteur_2));
          row.setAttributeNode(attr3);

          //Récupère la donnée chaque colonne de la row en cours.
          //Parcoure chaque colonne de la row en cours.
          for(int i = 1; i <= v_resQueryData.getColumnCount(); i++)
          {
            //AJout du sous noeud de row valeur et ajout du nom de la colonne en cours en attribut et sa valeur en valeur.
            Element valeur = doc.createElement("valeur");
            row.appendChild(valeur);
            Attr attr4 = doc.createAttribute("col");
            attr4.setValue(v_resQueryData.getColumnName(i).toString());
            valeur.setAttributeNode(attr4);
            if (v_resQuery.getString(v_resQueryData.getColumnName(i)) != null)
              valeur.appendChild(doc.createTextNode(v_resQuery.getString(v_resQueryData.getColumnName(i))));
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
      StreamResult result = new StreamResult(p_xmlFile);
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
      transformer.transform(source, result);

      //AFFICHAGE DANS CONSOLE POUR DEBUGGER A SUPPRIMER.
      //StreamResult consoleResult = new StreamResult(System.out);
      //transformer.transform(source, consoleResult);
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
  }
}

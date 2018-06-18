/*# ==========================================================================================================================
# This class builds the xml file containing executed queries and their result.
#
# Author : Alba Thomas (All4it)
#
# Creation date : 2018 June
#
# Revisions
# ------------------------------------------------------------------------------------------------------------------------
# Version | Date       | Author                            | Comments
# ------------------------------------------------------------------------------------------------------------------------
# 1.0     | 2018/06    | Alba Thomas (All4it)              | Initial version
#
# ------------------------------------------------------------------------------------------------------------------------
# ==========================================================================================================================
*/
package src.builders;

import src.database.*;
import java.io.*;
import java.util.TreeMap;
import java.util.Map;

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
  public static void main(Database p_database, TreeMap<Integer, String> p_mapQueries)
  {
    Integer v_compteur_2 = 1;
    String v_xmlFilePath                    = new String("./outputs/auditResult.xml");
      File v_xmlFile                        = new File(v_xmlFilePath);
    Query queryObj = new Query();
    //Summary : Browse and executes queries extracted from file.
    try
    {
      //Creates or overwrites the file auditResult.xml
      if(v_xmlFile.exists())
      {
        v_xmlFile.delete();
        v_xmlFile.createNewFile();
      }
      else
        v_xmlFile.createNewFile();
      //Variables for XML output document.
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.newDocument();
      //Creates root Node of XML document.
      Element rootElement = doc.createElement("queries");
      doc.appendChild(rootElement);
      //Browses TreeMap containing queries to execute.
      for(Map.Entry<Integer,String> entry : p_mapQueries.entrySet())
      {
        //v_key = query key value, v_query = the query text.
        Integer v_key = entry.getKey();
        String v_query = entry.getValue();

        //Executes the query
        queryObj.execQuery(p_database, v_query);

        //Creates nodes under root node (query) and affects to it an attibrute containing id_query to xml file.
        Element query = doc.createElement("query");
        rootElement.appendChild(query);
        Attr attr2 = doc.createAttribute("id_query");
        attr2.setValue(String.valueOf(v_key));
        query.setAttributeNode(attr2);
        //Checks if the query returns no row, if so then still affects the structure (metadata) in order to retrieve the skeleton to build the html file.
        if(!queryObj.getQueryRes().isBeforeFirst())
        {
          //Creates nodes under query node (row) and affects to it an incremented attribute id_row to xml file.
          Element row = doc.createElement("row");
          query.appendChild(row);
          Attr attr3 = doc.createAttribute("id_row");
          attr3.setValue(String.valueOf(v_key)+"."+String.valueOf(v_compteur_2));
          row.setAttributeNode(attr3);

          //Retrieves each columns of current row, and adds them to xml file.
          for(int i = 1; i <= queryObj.getQueryCols().getColumnCount(); i++)
          {
            //Adds node under row node (valeur), and affects to it the value and an attribute corresponding to current column to xml file.
            Element valeur = doc.createElement("valeur");
            row.appendChild(valeur);
            Attr attr4 = doc.createAttribute("col");
            attr4.setValue(queryObj.getQueryCols().getColumnName(i).toString());
            valeur.setAttributeNode(attr4);
          }
        }
        //Browses rows of current query.
        while(queryObj.getQueryRes().next())
        {
          //Creates nodes under query node (row) and affects to it an incremented attribute id_row to xml file.
          Element row = doc.createElement("row");
          query.appendChild(row);
          Attr attr3 = doc.createAttribute("id_row");
          attr3.setValue(String.valueOf(v_key)+"."+String.valueOf(v_compteur_2));
          row.setAttributeNode(attr3);

          //Browses and retrieves each column of current row.
          for(int i = 1; i <= queryObj.getQueryCols().getColumnCount(); i++)
          {
            //Creates nodes under query node (row) and affects to it an incremented attribute id_row to xml file.
            Element valeur = doc.createElement("valeur");
            row.appendChild(valeur);
            Attr attr4 = doc.createAttribute("col");
            attr4.setValue(queryObj.getQueryCols().getColumnName(i).toString());
            valeur.setAttributeNode(attr4);
            if (queryObj.getQueryRes().getString(queryObj.getQueryCols().getColumnName(i)) != null)
              valeur.appendChild(doc.createTextNode(queryObj.getQueryRes().getString(queryObj.getQueryCols().getColumnName(i))));
          }
          v_compteur_2 = v_compteur_2 + 1;
        }
        v_compteur_2 = 1;
      }
      //Writes the content into xml file.
      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();

      DOMSource source = new DOMSource(doc);
      StreamResult result = new StreamResult(v_xmlFile);
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
      transformer.transform(source, result);

      //AFFICHAGE DANS CONSOLE POUR DEBUGGER A SUPPRIMER.
      //StreamResult consoleResult = new StreamResult(System.out);
      //transformer.transform(source, consoleResult);
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

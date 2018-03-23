//Importation des packages.
import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class HtmlBuilder
{
  public static void main(File p_xmlFile)
  {
    try
    {
      //Parseur et Document.
      final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      final DocumentBuilder builder = factory.newDocumentBuilder();
      final Document document = builder.parse(p_xmlFile);

      //Récupère le noeud racine du document XML (queries).
      final Node n_queries = document.getDocumentElement();
      //Récupère la liste des noeuds enfants (query) du noeud racine (queries).
      final NodeList n_queries_child = n_queries.getChildNodes();
      for(int a = 0; a < n_queries_child.getLength(); a++)
      {
        //Récupère le noeud enfant query un par un, et son attribut (id_query).
        if(n_queries_child.item(a).getNodeType() == Node.ELEMENT_NODE)
        {
          Element n_query = (Element) n_queries_child.item(a);
          System.out.println("ID_QUERY : " + n_query.getAttribute("id_query"));
          //Récupère la liste des noeuds enfants (row) du noeud query en cours.
          final NodeList n_query_child = n_query.getChildNodes();
          //Check si aucune données n'est retournée.
          if(n_query_child.getLength() == 0)
            System.out.println("PAS DE FILS -> INDIQUER NO DATA FOUND.\n");
          for(int b = 0; b < n_query_child.getLength(); b++)
          {
            //Récupère le noeud enfant row un par un, et son attribut (id_row).
            if(n_query_child.item(b).getNodeType() == Node.ELEMENT_NODE)
            {
              Element n_row =  (Element) n_query_child.item(b);
              System.out.println("ROW_ID : " + n_row.getAttribute("id_row"));
              //Récupère la liste des noeufs enfants (valeur) du noeud row en cours.
              final NodeList n_row_child = n_row.getChildNodes();
              for(int c = 0; c < n_row_child.getLength(); c++)
              {
                //Récupère le noeud enfant valeur un par un, la valeur retournée par la query et son attribut col (qui correspond au nom de la colonne).
                if(n_row_child.item(c).getNodeType() == Node.ELEMENT_NODE)
                {
                  Element n_valeur = (Element) n_row_child.item(c);
                  System.out.println("COLONNE : " + n_valeur.getAttribute("col"));
                  System.out.println("VALEUR : " + n_valeur.getTextContent());
                }
              }
            }
          }
        }
      }
    }
    catch (final ParserConfigurationException e)
    {
        e.printStackTrace();
    }
    catch (final SAXException e)
    {
        e.printStackTrace();
    }
    catch (final IOException e)
    {
        e.printStackTrace();
    }

  }
}

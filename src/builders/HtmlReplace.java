package src.builders;

import java.io.*;
import java.util.regex.*;
import src.database.*;
import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.io.BufferedWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class HtmlReplace
{
  public static void main(String FILENAME)
  {
    BufferedReader br = null;
		FileReader fr = null;
    String sCurrentLine;
    String _newFile = "";
    Pattern p = Pattern.compile("\\$\\{torep\\_([0-9]+)\\}");
		try
    {
			fr = new FileReader(FILENAME);
			br = new BufferedReader(fr);
			while ((sCurrentLine = br.readLine()) != null)
      {
				System.out.println(sCurrentLine);
        Matcher m = p.matcher(sCurrentLine);
        while (m.find())
        {
          int idToRep = Integer.parseInt(m.group(1));
          System.out.println(m.group(1));
          String _htmlTab = getValuesFromId(m.group(1));

          String _toRep = "\\$\\{torep\\_"+ idToRep +"\\}";

          //_htmlTab = _htmlTab.replaceAll("\\$", "\\\\\\$");
          _htmlTab = _htmlTab.replaceAll("\\$", "\\\\\\$");
          System.out.println(_htmlTab);
          sCurrentLine = sCurrentLine.replaceAll(_toRep, _htmlTab);
          System.out.println(sCurrentLine);
        }
        _newFile = _newFile + sCurrentLine +"\n";
			}

      try (BufferedWriter bw = new BufferedWriter(new FileWriter("./outputs/definitiveHtml.html")))
      {
        bw.write(_newFile);
        System.out.println("Done");
      }
      catch (IOException e)
      {
        e.printStackTrace();
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
				if (br != null)
					br.close();
				if (fr != null)
					fr.close();
			}
      catch (IOException ex)
      {
				ex.printStackTrace();
			}
		}
  }

  public static String getValuesFromId(String idQuery)
  {
    String _htmlTab = new String("<table>");
    String _thead = new String("<thead><tr>");
    String _tbody = new String("<tbody>");
    boolean _needAddHEad = true;
    int _nbRow = 0;
    int _nbCell = 0;
    String _retIfSoloValue = "";
    //Parseur et Document.
    try
    {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = factory.newDocumentBuilder();
      Document document = builder.parse("./outputs/auditResult.xml");
      Element n_row = null;

      //Récupère le noeud racine du document XML (queries).
      Node _rootNode = document.getDocumentElement();
      //Récupère la liste des noeuds enfants (query) du noeud racine (queries).
      NodeList _rootChildNodes = _rootNode.getChildNodes();

      for(int i = 0; i < _rootChildNodes.getLength(); i++)
      {
        if(_rootChildNodes.item(i).getNodeType() == Node.ELEMENT_NODE)
        {
          Element _queryNode = (Element) _rootChildNodes.item(i);
          if(_queryNode.getAttribute("id_query").equals(idQuery))
          {
            _nbRow = 0;
            NodeList _queryChildNodes = _rootChildNodes.item(i).getChildNodes();
            for(int j = 0; j < _queryChildNodes.getLength(); j++)
            {
              if(_queryChildNodes.item(j).getNodeType() == Node.ELEMENT_NODE)
              {
                _nbCell = 0;
                _nbRow ++;
                _tbody = _tbody + "<tr>";
                NodeList _rowQueryChildNodes = _queryChildNodes.item(j).getChildNodes();
                for(int k = 0; k < _rowQueryChildNodes.getLength(); k++)
                {
                  if(_rowQueryChildNodes.item(k).getNodeType() == Node.ELEMENT_NODE)
                  {
                    _nbCell ++;
                    Element _valeurRowQuery = (Element) _rowQueryChildNodes.item(k);
                    //System.out.println(_valeurRowQuery.getAttribute("col") + " /// " + _valeurRowQuery.getTextContent());
                    if(_needAddHEad)
                    {
                      _thead = _thead + "<th>" + _valeurRowQuery.getAttribute("col") + "</th>";
                    }
                    _tbody = _tbody + "<td>" + _valeurRowQuery.getTextContent() + "</td>";
                    _retIfSoloValue = _valeurRowQuery.getTextContent();
                  }
                }
                _needAddHEad = false;
                _tbody = _tbody + "</tr>";
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
  System.out.println("NBROW : " + _nbRow + " NBCELL : " + _nbCell);

  if(_nbRow == 1 && _nbCell == 1)
  {
    //_htmlTab =  retourner la valeur seule.
    System.out.println("SOLOSLOSLSOLOOOOOOOOOOOOOOOOOOO");
    _htmlTab = _retIfSoloValue;
  }
  else
  {
    _thead = _thead + "</tr></thead>";
    _tbody = _tbody + "</tbody>";
    _htmlTab = _htmlTab + _thead + _tbody + "</table>";
  }
  System.out.println("BYYYYYYYYYYYYYYYYYYE");
  return _htmlTab;
  }
}

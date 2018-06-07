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
      //Reads hmtl output file.
			while ((sCurrentLine = br.readLine()) != null)
      {
        Matcher m = p.matcher(sCurrentLine);
        //Checks if current line matches with regex.
        while (m.find())
        {
          //If matches then extracts torep value and replaces it by html table containing corresponding query results.
          int idToRep = Integer.parseInt(m.group(1));
          String _htmlTab = getValuesFromId(m.group(1));

          String _toRep = "\\$\\{torep\\_"+ idToRep +"\\}";

          _htmlTab = _htmlTab.replaceAll("\\$", "\\\\\\$");
          sCurrentLine = sCurrentLine.replaceAll(_toRep, _htmlTab);
        }
        _newFile = _newFile + sCurrentLine +"\n";
			}

      try (BufferedWriter bw = new BufferedWriter(new FileWriter("./outputs/definitiveHtml.html")))
      {
        bw.write(_newFile);
        bw.close();
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

//This method browses values from xml file and builds corresponding html table to retrurn.
  public static String getValuesFromId(String idQuery)
  {
    String _htmlTab = new String("<table>");
    String _thead = new String("<thead><tr>");
    String _tbody = new String("<tbody>");
    boolean _needAddHEad = true;
    int _nbRow = 0;
    int _nbCell = 0;
    String _retIfSoloValue = "";
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

//Checks if only one value is returned by query, if so then it doesn't create a table.
  if(_nbRow == 1 && _nbCell == 1)
  {
    _htmlTab = _retIfSoloValue;
  }
  else
  {
    _thead = _thead + "</tr></thead>";
    _tbody = _tbody + "</tbody>";
    _htmlTab = _htmlTab + _thead + _tbody + "</table>";
  }
  return _htmlTab;
  }

  //This methodes build up the summary from definitiveHtml.html file
  public static void prepareSummary(String p_FILENAME)
  {
    BufferedReader br = null;
    FileReader fr = null;
    String sCurrentLine;
    Pattern p = Pattern.compile("\\$\\{torep\\_(summary)\\}");
    Pattern _h1 = Pattern.compile("\\<h1\\>([a-zA-Z0-9\\s\\$\\{\\_\\}\\,\\.\\/]+)\\</h1\\>");
    Pattern _h2 = Pattern.compile("\\<h2\\>([a-zA-Z0-9\\.\\s\\$\\{\\_\\}\\,\\/]+)\\</h2\\>");
    Pattern _h3 = Pattern.compile("\\<h3\\>([a-zA-Z0-9\\.\\s\\$\\{\\_\\}\\,\\/]+)\\</h3\\>");
    Sommaire summary = new Sommaire();
    String _newFile = "";
    try
    {
      fr = new FileReader("./outputs/definitiveHtml.html");
      br = new BufferedReader(fr);
      //Reads hmtl definitive output file.
      while ((sCurrentLine = br.readLine()) != null)
      {
        Matcher mh1 = _h1.matcher(sCurrentLine);
        Matcher mh2 = _h2.matcher(sCurrentLine);
        Matcher mh3 = _h3.matcher(sCurrentLine);

        //Extracts from matcher h1 and h2 value to add them to summary.
        while(mh1.find())
        {
          summary.addH1(mh1.group(1));
        }
        while(mh2.find())
        {
          summary.addH2(mh2.group(1));
        }
        while(mh3.find())
        {
          summary.addH3(mh3.group(1));
        }
      }
      fr = new FileReader("./outputs/definitiveHtml.html");
      br = new BufferedReader(fr);
      //Replaces bind variable by the build summary
      while ((sCurrentLine = br.readLine()) != null)
      {
        Matcher m = p.matcher(sCurrentLine);
        if(m.find())
        {
          String _toRep = "\\$\\{torep\\_summary\\}";
          sCurrentLine = sCurrentLine.replaceAll(_toRep, summary.buildSummary());
        }
        _newFile = _newFile + sCurrentLine +"\n";
      }
      try (BufferedWriter bw = new BufferedWriter(new FileWriter("./outputs/definitiveHtml.html")))
      {
        bw.write(_newFile);
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
}

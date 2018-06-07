package src.builders;

import src.database.*;
import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.time.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class HtmlBuilder
{
  public static void main(Database p_database, String p_client, String p_author)
  {
    String v_xmlFilePath                    = new String("./outputs/auditResult.xml");
      File v_xmlFile                        = new File(v_xmlFilePath);
    String v_auditOutputFileName             = new String("./outputs/auditOutput.html");
      File v_auditOutputFile                 = new File(v_auditOutputFileName);
      FileWriter v_auditOutputFileB;
      BufferedWriter bw = null;
    try
    {
      //Writes html file.
      v_auditOutputFile.createNewFile();
      v_auditOutputFileB = new FileWriter(v_auditOutputFile);
      bw = new BufferedWriter(v_auditOutputFileB);
      bw.write("<!DOCTYPE html>"+
      "<html>"+
        "<head>"+
          "<meta charset=\"utf-8\" />"+
          "<title>Audit ALL4IT</title>"+
          "<link rel=\"stylesheet\" type=\"text/css\" href=\"css/style.css\">"+
        "</head>"+
        "<body>"+
          "<section class =\"page\" id = \"homepage\">"+
            "<img src=\"imgs/"+p_client+".png\" id=\"logoClient\">"+
            "<p id = \"auditTitle\">Audit de la base ${torep_1}</p>"+
            "<br/><br/><br/><br/><br/><br/><p>Auteur : "+p_author+"</p>"+
            "<p>Date : "+java.time.LocalDate.now()+"</p>"+
            "<p>Version : 1.0</p>"+
            "<p>Diffusion : "+p_client+"</p><br/><br/<br/><br/>"+
            "<p>Avertissement : </p><p style=\"color: red;\">Ce document ne peut être reproduit ou diffusé sans autorisation préalable d’ALL4IT Ce document contient des informations confidentielles relatives au client  Ces informations ne doivent être utilisées que dans le cadre du projet en cours et doivent être considérées comme confidentielles.</p>"+
          "</section>"+
          "<section class =\"page\" id = \"summary\">"+
            "<p>${torep_summary}</p>"+
          "</section>"+
          "<section class=\"page\">"+
              "<div class=\"nobreak\">Volume global de la database ${torep_2}</div>"+
              "<div class=\"nobreak\">Infos générales sur les sessions ${torep_3}</div>"+
              "<div class=\"nobreak\">Nombre de sessions par machine ${torep_4}</div>"+
              "<div class=\"nobreak\">Cache hit ratio ${torep_5}</div>"+
              "<div class=\"nobreak\" id=\"1\"><h1>1 CPU, RAM</h1></div>"+
              "<div class=\"nobreak\" id=\"2\"><h1>2 Hit ratio de la PGA</h1>${torep_6}</div>"+
              "<div class=\"nobreak\" id=\"2.1\"><h2>2.1 Nombre de locks</h2>${torep_7}</div>"+
              "<div class=\"nobreak\" id=\"2.2\"><h2>2.2 Objets lockes</h2>${torep_8}</div>"+
              "<div class=\"nobreak\" id=\"2.2.1\"><h3>2.2.1 Tables lockees</h3></div>"+
              "<div class=\"nobreak\" id=\"2.2.2\"><h3>2.2.2 Sessions lockees</h3></div>"+
              "<div class=\"nobreak\" id=\"2.2.3\"><h3>2.2.3 Sessions lockees</h3></div>"+
              "<div class=\"nobreak\" id=\"2.2.4\"><h3>2.2.4 Sessions lockees</h3></div>"+
              "<div class=\"nobreak\" id=\"2.2.5\"><h3>2.2.5 Sessions lockees</h3></div>"+
              "<div class=\"nobreak\" id=\"2.2.6\"><h3>2.2.6 Sessions lockees</h3></div>"+
              "<div class=\"nobreak\" id=\"2.2.7\"><h3>2.2.7 Sessions lockees</h3></div>"+
              "<div class=\"nobreak\" id=\"2.2.8\"><h3>2.2.8 Sessions lockees</h3></div>"+
              "<div class=\"nobreak\" id=\"2.2.9\"><h3>2.2.9 Sessions lockees</h3></div>"+
              "<div class=\"nobreak\" id=\"2.2.10\"><h3>2.2.10 Sessions lockees</h3></div>"+
              "<div class=\"nobreak\" id=\"2.2.11\"><h3>2.2.11 Sessions lockees</h3></div>"+
              "<div class=\"nobreak\" id=\"2.3\"><h2>2.3 Objets lockes</h2>${torep_8}</div>"+
              "<div class=\"nobreak\" id=\"2.4\"><h2>2.4 Objets lockes</h2>${torep_8}</div>"+
              "<div class=\"nobreak\" id=\"2.5\"><h2>2.5 Objets lockes</h2>${torep_8}</div>"+
              "<div class=\"nobreak\" id=\"2.6\"><h2>2.6 Objets lockes</h2>${torep_8}</div>"+
              "<div class=\"nobreak\" id=\"2.7\"><h2>2.7 Objets lockes</h2>${torep_8}</div>"+
              "<div class=\"nobreak\" id=\"2.8\"><h2>2.8 Objets lockes</h2>${torep_8}</div>"+
              "<div class=\"nobreak\" id=\"2.9\"><h2>2.9 Objets lockes</h2>${torep_8}</div>"+
              "<div class=\"nobreak\" id=\"2.10\"><h2>2.10 Objets lockes</h2>${torep_8}</div>"+
              "<div class=\"nobreak\" id=\"2.11\"><h2>2.11 Objets lockes</h2>${torep_8}</div>"+
              "<div class=\"nobreak\" id=\"3\"><h1>3 Sessions</h1></div>"+
              "<div class=\"nobreak\" id=\"3.1\"><h2>3.1 Sessions ouvertes</h2></div>"+
              "<div class=\"nobreak\" id=\"4\"><h1>4 Contenu table combo_chk</h1>${torep_9}</div>"+
              "<div class=\"nobreak\" id=\"4.1\"><h2>4.1 Contenu table banques</h2>${torep_10}</div>"+
            "</section>"+
        "</body>"+
      "</html>");
      bw.close();
      HtmlReplace.main(v_auditOutputFileName);
      HtmlReplace.buildSummary(v_auditOutputFileName);
    }
    catch (final IOException e)
    {
        e.printStackTrace();
    }

  }
}

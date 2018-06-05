package src.builders;

//Importation des packages divers.
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
          "<section class=\"page\">"+
              "<div class=\"nobreak\">Volume global de la database ${torep_2}</div>"+
              "<div class=\"nobreak\">Infos générales sur les sessions ${torep_3}</div>"+
              "<div class=\"nobreak\">Nombre de sessions par machine ${torep_4}</div>"+
              "<div class=\"nobreak\">Cache hit ratio ${torep_5}</div>"+
              "<div class=\"nobreak\">Hit ratio de la PGA ${torep_6}</div>"+
              "<div class=\"nobreak\">Nombre de locks ${torep_7}</div>"+
              "<div class=\"nobreak\">Objets lockes ${torep_8}</div>"+
              "<div class=\"nobreak\">Contenu table combo_chk ${torep_9}</div>"+
              "<div class=\"nobreak\">Contenu table banques ${torep_10}</div>"+
            "</section>"+
        "</body>"+
      "</html>");
      bw.close();
      HtmlReplace.main(v_auditOutputFileName);
    }
    catch (final IOException e)
    {
        e.printStackTrace();
    }

  }
}

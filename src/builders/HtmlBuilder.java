package src.builders;

//Importation des packages divers.
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

public class HtmlBuilder
{
  public static void main(Database p_database)
  {
    String v_xmlFilePath                    = new String("./outputs/auditResult.xml");
      File v_xmlFile                        = new File(v_xmlFilePath);
    String v_auditOutputFileName             = new String("./outputs/auditOutput.html");
      File v_auditOutputFile                 = new File(v_auditOutputFileName);
      FileWriter v_auditOutputFileB;
      BufferedWriter bw = null;
      //for test
      System.out.println(p_database.getIp());
      System.out.println(v_auditOutputFile.getPath() + v_auditOutputFile.getPath());
    try
    {
      //Ecrit en tete HTML dans le fichier.
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
            "<img src=\"imgs/2018-01-05.png\" id=\"logoA4i\"><br/>"+
            "Audit de la base ${torep_1}<br/>"+
            "<p>Auteur : XXXXX XXXXXXXXX</p>"+
            "<p>Date : XXXXX XXXXXXXXX</p>"+
            "<p>Version : XXXXX XXXXXXXXX</p>"+
            "<p>Diffusion : XXXXX XXXXXXXXX</p>"+
          "</section>"+
          "<section class=\"page\">"+
      //"\n\t\t<br style=\"page-break-before: always;\">"+
            "<p>Volume global de la database ${torep_2}</p>"+
            "<p>Infos générales sur les sessions ${torep_3}</p>"+
            "<p>Nombre de sessions par machine ${torep_4}</p>"+
            "<p>Cache hit ratio ${torep_5}</p>"+
            "<p>Hit ratio de la PGA ${torep_6}</p>"+
            "<p>Nombre de locks ${torep_7}</p>"+
            "<p>Objets lockes ${torep_8}</p>"+
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

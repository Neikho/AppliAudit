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
      "\n<html>"+
      "\n\t<head>"+
      "\n\t\t<meta charset=\"utf-8\" />"+
      "\n\t\t<title>Audit ALL4IT</title>"+
      "\n\t\t<style type = \"text/css\"> .page{margin: auto; height: 29.7cm; width: 21cm; border: 2px solid red; color: red;}</style>"+
      "\n\t</head>"+
      "\n\t<body>"+

      "\n\t\t<section class=\"page\">"+
      "\n\t\t\t\t<img src=\"imgs/2018-01-05.png\" style=\"padding-left: 18cm; padding-top: 0.6cm; width: 10%; height: auto;\">"+
      "\n\t\t\t<p style=\"padding-top: 10cm;\">Audit de la base ${torep_1}</p>"+
      "<p style=\"padding-top: 10cm;\">Table COMBO_CHK ${torep_2}</p>"+
      "\n\t\t</section>"+
      "\n\t\t<br style=\"page-break-before: always;\">"+
      "\n\t\t<div class=\"page\">"+
      "\n\t\t\t<h1>HIUGIUGIUGUI</h1>"+
      "\n\t\t</div>");

      bw.write("\n\t</body>"+
      "\n</html>");
      bw.close();
      HtmlReplace.main(v_auditOutputFileName);
    }
    catch (final IOException e)
    {
        e.printStackTrace();
    }

  }
}

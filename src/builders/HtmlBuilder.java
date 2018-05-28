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
      "\n\t\t<style type = \"text/css\"> .page{margin: auto;} td{border-right: 0.3px solid white; padding-right: 3px;} table{text-align: right; background-color: black; color: white; font-size: 10px; border-spacing: 5px;} th{border-bottom: 0.3px solid white;}</style>"+
      //
      //"table{font-size: 10px; border-spacing: 1px; border-left: 0.5px solid black; border-left-style: dashed; border-bottom: 0.5px solid black;
      //border-bottom-style: dashed;} td, th{border-right: 0.5px solid black; border-right-style: dashed;} th{border-bottom: 0.5px solid black; border-bottom-style: dashed; border-top: 0.5px solid black; border-top-style: dashed;}</style>"+
      "\n\t</head>"+
      "\n\t<body>"+
      "<section class =\"page\" id = \"homepage\">"+
      "\n\t\t\t\t<img src=\"imgs/2018-01-05.png\" style=\"padding-left: 18cm; padding-top: 0.6cm; width: 5%; height: auto;\"><br/>"+
      "\n\t\t\tAudit de la base ${torep_1}<br/>"+
      "<p>Auteur : XXXXX XXXXXXXXX</p>"+
      "<p>Date : XXXXX XXXXXXXXX</p>"+
      "<p>Version : XXXXX XXXXXXXXX</p>"+
      "<p>Diffusion : XXXXX XXXXXXXXX</p>"+
      "</section>"+
      "\n\t\t<section class=\"page\">"+

      "<p>Table COMBO_CHK ${torep_2}</p>"+
      //"\n\t\t<br style=\"page-break-before: always;\">"+
      "\n\t\t\t<p>Deuxieme select combo ${torep_7}</p>"+
      "\n\t\t\t<p>Volume global de la database ${torep_5}</p>"+
      "\n\t\t\t<p>Requetes couteuses ${torep_3}</p>"+
      "\n\t\t</section>");

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

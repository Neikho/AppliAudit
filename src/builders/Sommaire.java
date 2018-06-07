package src.builders;

import java.util.*;

public class Sommaire
{
  //Attributes
  private String a_sommaire;
  private List<String> _h1List;
  private List<String> _h2List;
  private List<String> _h3List;

  //Constructor
  public Sommaire()
  {
    this.a_sommaire = "<p style = \"font-size: 30px;\">Sommaire:</p><ul>";
    this._h1List = new ArrayList<String>();
    this._h2List = new ArrayList<String>();
    this._h3List = new ArrayList<String>();
  }

  //Getters
  public String getFullSommaire()
  {
    return this.a_sommaire;
  }

  //Methods
  public void addH1(String _h1ToAdd)
  {
    this._h1List.add(_h1ToAdd);
  }

  public void addH2(String _h2ToAdd)
  {
    this._h2List.add(_h2ToAdd);
  }

  public void addH3(String _h3ToAdd)
  {
    this._h3List.add(_h3ToAdd);
  }

  public String buildSummary()
  {
    int compteur = 0;
    String lastMain = "";
    for(String test : this._h1List)
    {
      for(int i = 0; i < this._h1List.size(); i++)
      {
        //Checks h1 value (1 or 2 or 2 ....): usefull if h1 are misordering
        if(test.startsWith(String.valueOf(i+1)))
        {
          //If so create new title in summary
          this.a_sommaire = this.a_sommaire + "<li><a href=\"#"+test.substring(0)+"\" class=\"big\">"+test+"</a></li><ul>";
          for(String test2 : this._h2List)
          {
            lastMain = test2.substring(0);
            //Chekcs h2 value (2.1 or 2.2 or 2.3 ....)
            if(test2.startsWith(String.valueOf(i+1)))
            {
              if(lastMain.equals(String.valueOf(i+1)))
                compteur ++;
              else
                compteur = 1;
              //Creates new subtitle entry in summary
              this.a_sommaire = this.a_sommaire + "<li><a href=\"#"+test2.substring(0,4).replaceAll("\\s", "")+"\" class=\"medium\">"+test2+"</a></li><ul>";
              //Browses h3 List and build the summary accordingly
              for(String test3 : this._h3List)
              {
                if((test2.substring(0,4).replaceAll("\\s","")+".").equals(test3.substring(0,4).replaceAll("\\s", "")))
                  this.a_sommaire = this.a_sommaire + "<li><a href=\"#"+test3.substring(0,6).replaceAll("\\s", "")+"\" class=\"small\">"+test3+"</a></li>";
              }
              this.a_sommaire = this.a_sommaire + "</ul>";
            }
          }
          this.a_sommaire = this.a_sommaire + "</ul>";
        }
      }
    }
    this.a_sommaire = this.a_sommaire + "</ul>";
    return this.a_sommaire;
  }
}

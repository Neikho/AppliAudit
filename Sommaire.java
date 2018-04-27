public class Sommaire
{
  //Attributs
  private String a_sommaire;
  private String a_lastAdd;
  private Integer a_currentTitleSection;
  private Integer a_currentSubTitleSection;
  private Integer a_currentSubSubTitleSection;

  //Constructeurs
  public Sommaire()
  {
    this.a_sommaire = "Table des matières";
    this.a_lastAdd = "";
    this.a_currentTitleSection = 0;
    this.a_currentSubTitleSection = 0;
    this.a_currentSubSubTitleSection = 0;
  }

  //Getters
  public String getFullSommaire()
  {
    return this.a_sommaire;
  }

  //Méthodes
  public void addTitle(String p_title)
  {
    this.a_currentTitleSection = this.a_currentTitleSection + 1;
    this.a_currentSubTitleSection = 0;
    this.a_currentSubSubTitleSection = 0;
    this.a_sommaire = this.a_sommaire + "\n" + Integer.toString(this.a_currentTitleSection) + " " + p_title;
  }
  public void addSubTitle(String p_subTitle)
  {
    this.a_currentSubTitleSection = this.a_currentSubTitleSection + 1;
    this.a_sommaire = this.a_sommaire + "\n\t" + Integer.toString(this.a_currentTitleSection) + "." + Integer.toString(this.a_currentSubTitleSection) + " " + p_subTitle;
  }
  public void addSubSubTitle(String p_subSubTitle)
  {
    this.a_currentSubSubTitleSection = this.a_currentSubSubTitleSection + 1;
    this.a_sommaire = this.a_sommaire + "\n\t\t" + Integer.toString(this.a_currentTitleSection) + "." + Integer.toString(this.a_currentSubTitleSection) + "." + Integer.toString(this.a_currentSubSubTitleSection) + " " + p_subSubTitle;
  }
}

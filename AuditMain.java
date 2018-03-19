//PACKAGES.
import java.io.*;
import java.util.TreeMap;
import java.util.Map;

public class AuditMain
{
    public static void main(String[] args)
    {
      //Var for reading the file that contains the database conf (IP, port, user, pas ...).
      String v_dbConfFilePath             = new String("targetDB.conf");
        File v_targetDBFile               = new File(v_dbConfFilePath);
      BufferedReader v_buff               = null;
        int v_isReading                   = 1;
      TreeMap<String, String> v_mapDBConf = new TreeMap<>();
        String v_paramMap1                = "";
        String v_paramMap2                = "";
        String v_concat                   = "";

      //Browses and extract data from DB target conf file.
      //List of params in this file : DB_IP_ADDR, DB_PORT, DB_TYPE, DB_NAME, DB_USER, DB_PASS
      try
      {
        v_buff = new BufferedReader(new FileReader(v_targetDBFile));
      }
      catch (FileNotFoundException e)
      {
        System.out.println("(AuditMain.java) -> File not found : " + v_dbConfFilePath);
      }

      try
      {
        while ((v_isReading = v_buff.read()) != -1)
        {
            switch ((char) v_isReading)
            {
              case '=':
                v_paramMap1 = v_concat;
                v_concat= "";
                break;
              case ';':
                v_paramMap2 = v_concat;
                v_concat= "";
                break;
              case '\n':
                v_mapDBConf.put(v_paramMap1, v_paramMap2);
                v_concat = "";
                break;
              default:
                v_concat= v_concat + (char) v_isReading;
                break;
            }
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
          v_buff.close();
        }
        catch (IOException e)
        {
          e.printStackTrace();
        }
      }
      System.out.println(v_mapDBConf);
      System.out.println(v_mapDBConf.get("DB_IP_ADDR"));
    }
}

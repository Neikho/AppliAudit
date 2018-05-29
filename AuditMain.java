//Importation des packages divers.
import java.io.*;
import java.util.TreeMap;
import src.builders.*;
import src.readers.*;
import src.database.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class AuditMain
{
    public static void main(String[] args)
    {
      //Variables pour lire le fichier de conf contenant les infos de la DB cible (IP, port, user, pas ...).
      TreeMap<String, String> v_mapDBConf   = new TreeMap<>();
      //Variables pour lire le fichier des queries à executer.
        TreeMap<Integer, String> v_mapQueries = new TreeMap<>();
      //Variables pour la connection et execution vers la DB cible.
      Object v_database = null;

      //Recupère la conf de la DB cible et les queries.
      v_mapDBConf = ConfReader.getConf();
      v_mapQueries = ConfReader.getQueries();

      //instanciation dynamique en fonction du sgbd.
      try
      {
        String dbtype = "src.database."+v_mapDBConf.get("DB_TYPE");
        Class<?> clazz = Class.forName(dbtype);
        Class[] types = new Class[]{String.class, String.class, String.class, String.class, String.class};
        Constructor ct = clazz.getConstructor(types);
        v_database = ct.newInstance(new Object[]{v_mapDBConf.get("DB_IP_ADDR"), v_mapDBConf.get("DB_PORT"), v_mapDBConf.get("DB_SID"), v_mapDBConf.get("DB_PASS"), v_mapDBConf.get("DB_USER")});
        Method methodConn = clazz.getDeclaredMethod("connectDb");
        Method methodDisc = clazz.getSuperclass().getDeclaredMethod("disconnectDb");
        Method methodChkConn = clazz.getSuperclass().getDeclaredMethod("connectionState");

        //Se connecte à la base.
        methodConn.invoke(v_database);
        boolean dbStatus = (boolean) methodChkConn.invoke(v_database);
        if (dbStatus)
          System.out.println("Connected to Database.");
        else
          System.out.println("Error, not connected to Database.");

        //Appel de XmlBuilder.
        XmlBuilder.main((Database) v_database, v_mapQueries);

        //Appel de HtmlBuilder.
        HtmlBuilder.main((Database) v_database);

        //Se déconnecte de la base.
        methodDisc.invoke(v_database);
        dbStatus = (boolean) methodChkConn.invoke(v_database);
        if (!dbStatus)
          System.out.println("Disconnected from Database.");
        else
          System.out.println("Error, still connected to Database.");
      } catch (SecurityException e)
      {
          e.printStackTrace();
      } catch (IllegalArgumentException e)
      {
          e.printStackTrace();
      } catch (ClassNotFoundException e)
      {
          e.printStackTrace();
      } catch (InstantiationException e)
      {
          e.printStackTrace();
      } catch (IllegalAccessException e)
      {
          e.printStackTrace();
      } catch (NoSuchMethodException e)
      {
          e.printStackTrace();
      } catch (InvocationTargetException e)
      {
          e.printStackTrace();
      }
    }
}

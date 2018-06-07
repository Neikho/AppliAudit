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
      //Variable which contains target database informations (IP, port, user, pas ...).
      TreeMap<String, String> v_mapDBConf   = new TreeMap<>();
      //Variable which contains queries to execute.
      TreeMap<Integer, String> v_mapQueries = new TreeMap<>();

      Object v_database = null;

      //Affects target database informations and queries to execute into variables.
      v_mapDBConf = ConfReader.getConf();
      v_mapQueries = ConfReader.getQueries();

      try
      {
        //Dynamic instantiation corresponding to sgbd.
        String dbtype = "src.database."+v_mapDBConf.get("DB_TYPE");
        Class<?> clazz = Class.forName(dbtype);
        Class[] types = new Class[]{String.class, String.class, String.class, String.class, String.class};
        Constructor ct = clazz.getConstructor(types);
        v_database = ct.newInstance(new Object[]{v_mapDBConf.get("DB_IP_ADDR"), v_mapDBConf.get("DB_PORT"), v_mapDBConf.get("DB_SID"), v_mapDBConf.get("DB_PASS"), v_mapDBConf.get("DB_USER")});
        Method methodConn = clazz.getDeclaredMethod("connectDb");
        Method methodDisc = clazz.getSuperclass().getDeclaredMethod("disconnectDb");
        Method methodChkConn = clazz.getSuperclass().getDeclaredMethod("connectionState");

        //Connects to database.
        methodConn.invoke(v_database);
        boolean dbStatus = (boolean) methodChkConn.invoke(v_database);
        if (dbStatus)
          System.out.println("Connected to Database.");
        else
          System.out.println("Error, not connected to Database.");

        //Call to XmlBuilder.
        XmlBuilder.main((Database) v_database, v_mapQueries);

        //Call to HtmlBuilder.
        HtmlBuilder.main((Database) v_database, v_mapDBConf.get("DB_CLIENT"), v_mapDBConf.get("AUDIT_AUTHOR"));

        //Disconnects from database.
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

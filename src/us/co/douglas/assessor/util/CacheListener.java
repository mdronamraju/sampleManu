package us.co.douglas.assessor.util;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import net.sf.json.JSONArray;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bson.Document;
import org.codehaus.jackson.map.ObjectMapper;
import us.co.douglas.assessor.dao.AccountDAO;
import us.co.douglas.assessor.dao.AccountDAOImpl;
import us.co.douglas.assessor.model.NeighborhoodSale;
import us.co.douglas.assessor.model.Parcel;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by mdronamr on 2/2/16.
 */

public class CacheListener implements ServletContextListener {

    private static Log log = LogFactory.getLog(CacheListener.class);
    private static Boolean startThreads = false;

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        log.info("ServletContextListener destroyed");
    }

    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        log.info("ServletContextListener started");
        if (startThreads) {
            log.info("!!!!!!!!!!!!!!!!!! STARTING ALL THE THREADS !!!!!!!!!!!!!!!!!!");

            AllSearchableStringsThread allSearchableStringsThread = new AllSearchableStringsThread();
            Thread allSearchableStringsT = new Thread(allSearchableStringsThread);
            allSearchableStringsT.start();

            AllNeighborhoodSalesThread allNeighborhoodSalesThread = new AllNeighborhoodSalesThread();
            Thread allNeighborhoodSalesT = new Thread(allNeighborhoodSalesThread);
            allNeighborhoodSalesT.start();
        } else {
            log.info("!!!!!!!!!!!!!!!!!! ALL THREADS HAVE BEEN DISABLED !!!!!!!!!!!!!!!!!!");
            //List<String> allSearchableStrings = (List<String>)SerializeDeserializeUtil.deserialize("/Users/admin/development/jsonDocs/allSearchableStrings.ser");
            //MongoDBConnectionUtil.dropAllSearchableStringsCollection();
            //MongoDBConnectionUtil.insertAllSearchableStrings(allSearchableStrings);
            //String json = MongoDBConnectionUtil.getAllSearchableStringsList();
            //log.info("json: " + json);
            List<String> allSearchableStrings = (List<String>)SerializeDeserializeUtil.deserialize("/Users/admin/development/jsonDocs/allSearchableStrings.ser");
            InMemoryCache.getCacheMap().put("allSearchableStrings", allSearchableStrings);

            List<NeighborhoodSale> allNeighborhoodSales = (List<NeighborhoodSale>)SerializeDeserializeUtil.deserialize("/Users/admin/development/jsonDocs/allNeighborhoodSales.ser");
            InMemoryCache.getCacheMap().put("allNeighborhoodSales", allNeighborhoodSales);

        }
    }
}
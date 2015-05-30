package com.finanalyzer.util;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import com.finanalyzer.db.StockRatingsDb;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.tools.remoteapi.RemoteApiInstaller;
import com.google.appengine.tools.remoteapi.RemoteApiOptions;
import com.gs.collections.impl.list.mutable.FastList;

public class Tester
{
	private static final String SINGLE_QUERY_URL = "https://www.quandl.com/api/v1/datasets/%s%s.json?";
	
	public static void main(String[] args) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException
	{
		String username = "sreenathchakkingal04@gmail.com";
        String password = "shornapaapa";
        RemoteApiOptions options = new RemoteApiOptions()
            .server("finchakks.appspot.com", 443)
            .credentials(username, password);
        RemoteApiInstaller installer = new RemoteApiInstaller();
        try {
			installer.install(options);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        String message = "failed";
        try {
        	StockRatingsDb localDb = new StockRatingsDb();
            localDb.removeAllEntities();
        	DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
            Query query = new Query(localDb.getTableName());
            FastList<Entity> entities = FastList.newList(ds.prepare(query).asList(FetchOptions.Builder.withDefaults()));
            message = "Success";
        } finally {
            installer.uninstall();
            System.out.println(message);
        }
        
		
	}

}

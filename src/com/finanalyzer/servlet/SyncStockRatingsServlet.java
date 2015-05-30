package com.finanalyzer.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.finanalyzer.db.StockRatingsDb;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.tools.remoteapi.RemoteApiInstaller;
import com.google.appengine.tools.remoteapi.RemoteApiOptions;
import com.gs.collections.impl.list.mutable.FastList;

public class SyncStockRatingsServlet extends AbstractCoreServlet{

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String username = "sreenathchakkingal04@gmail.com";
        String password = "shornapaapa";
        RemoteApiOptions options = new RemoteApiOptions()
            .server("finchakks.appspot.com", 443)
            .credentials(username, password);
        RemoteApiInstaller installer = new RemoteApiInstaller();
        installer.install(options);
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
        }
        request.setAttribute("message", message);
        this.despatchTo(request, response, "sync.jsp");
		
	}

}

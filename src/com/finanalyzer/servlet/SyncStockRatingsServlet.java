package com.finanalyzer.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.finanalyzer.db.DbOperations;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;

public class SyncStockRatingsServlet extends AbstractCoreServlet{

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
//		AllScripsUtil allScripsUtil = AllScripsUtil.getInstance();
//		FastList<Entity> entitiesFromLegacyTable = allScripsUtil.retrieveAllEntities();
//		List<DbObject> dbObjects= FastList.newList(); 
//
//		for(Entity legacyEntity : entitiesFromLegacyTable)
//		{
//			String isin =(String) legacyEntity.getProperty("isin");
//			String stockName =(String)  legacyEntity.getProperty("stockName");
//			String bseId =(String)  legacyEntity.getProperty("bseId");
//			String nseId =(String)  legacyEntity.getProperty("nseId");
//			String fairValue =String.valueOf(legacyEntity.getProperty("fairValue"));
//			String industry =(String)  legacyEntity.getProperty("industry");
//			AllScriptsDbObject allScriptsDbObject = new AllScriptsDbObject(nseId, stockName, isin, bseId, fairValue, industry);
//			dbObjects.add(allScriptsDbObject);
//		}
		
//		DbOperations.getInstance().bulkInsert(dbObjects);
		List<Entity> retrieveEntities = DbOperations.getInstance().retrieveEntities(new Query("AllScrips"), true);
		int size = retrieveEntities.size();
        request.setAttribute("message", size);
        this.despatchTo(request, response, "sync.jsp");
		
	}

}

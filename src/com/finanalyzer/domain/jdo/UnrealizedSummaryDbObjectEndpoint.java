package com.finanalyzer.domain.jdo;

import com.finanalyzer.db.jdo.PMF;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.datanucleus.query.JDOCursorHelper;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Named;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

@Api(name = "unrealizedsummarydbobjectendpoint", version = "v1")
public class UnrealizedSummaryDbObjectEndpoint {

	/**
	 * This method lists all the entities inserted in datastore.
	 * It uses HTTP GET method and paging support.
	 *
	 * @return A CollectionResponse class containing the list of all entities
	 * persisted and a cursor to the next page.
	 */
//	@SuppressWarnings({ "unchecked", "unused" })
	@ApiMethod(name = "listUnrealizedSummaryDbObject")
	public CollectionResponse<UnrealizedSummaryDbObject> listUnrealizedSummaryDbObject(
//			@Nullable @Named("cursor") String cursorString,
//			@Nullable @Named("limit") Integer limit
			){

		PersistenceManager mgr = null;
		Cursor cursor = null;
		List<UnrealizedSummaryDbObject> execute = null;

		try {
			mgr = getPersistenceManager();
			Query query = mgr.newQuery(UnrealizedSummaryDbObject.class);
//			if (cursorString != null && cursorString != "") {
//				cursor = Cursor.fromWebSafeString(cursorString);
//				HashMap<String, Object> extensionMap = new HashMap<String, Object>();
//				extensionMap.put(JDOCursorHelper.CURSOR_EXTENSION, cursor);
//				query.setExtensions(extensionMap);
//			}
//
//			if (limit != null) {
//				query.setRange(0, limit);
//			}

			execute = (List<UnrealizedSummaryDbObject>) query.execute();
			cursor = JDOCursorHelper.getCursor(execute);
//			if (cursor != null)
//				cursorString = cursor.toWebSafeString();

			// Tight loop for fetching all entities from datastore and accomodate
			// for lazy fetch.
			for (UnrealizedSummaryDbObject obj : execute)
				;
		} finally {
			mgr.close();
		}

		return CollectionResponse.<UnrealizedSummaryDbObject> builder()
				.setItems(execute).
//				setNextPageToken(cursorString).
				build();
	}

	/**
	 * This method gets the entity having primary key id. It uses HTTP GET method.
	 *
	 * @param id the primary key of the java bean.
	 * @return The entity with primary key id.
	 */
	@ApiMethod(name = "getUnrealizedSummaryDbObject")
	public UnrealizedSummaryDbObject getUnrealizedSummaryDbObject(
			@Named("id") Long id) {
		PersistenceManager mgr = getPersistenceManager();
		UnrealizedSummaryDbObject unrealizedsummarydbobject = null;
		try {
			unrealizedsummarydbobject = mgr.getObjectById(
					UnrealizedSummaryDbObject.class, id);
		} finally {
			mgr.close();
		}
		return unrealizedsummarydbobject;
	}

	/**
	 * This inserts a new entity into App Engine datastore. If the entity already
	 * exists in the datastore, an exception is thrown.
	 * It uses HTTP POST method.
	 *
	 * @param unrealizedsummarydbobject the entity to be inserted.
	 * @return The inserted entity.
	 */
	@ApiMethod(name = "insertUnrealizedSummaryDbObject")
	public UnrealizedSummaryDbObject insertUnrealizedSummaryDbObject(
			UnrealizedSummaryDbObject unrealizedsummarydbobject) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			if (containsUnrealizedSummaryDbObject(unrealizedsummarydbobject)) {
				throw new EntityExistsException("Object already exists");
			}
			mgr.makePersistent(unrealizedsummarydbobject);
		} finally {
			mgr.close();
		}
		return unrealizedsummarydbobject;
	}

	/**
	 * This method is used for updating an existing entity. If the entity does not
	 * exist in the datastore, an exception is thrown.
	 * It uses HTTP PUT method.
	 *
	 * @param unrealizedsummarydbobject the entity to be updated.
	 * @return The updated entity.
	 */
	@ApiMethod(name = "updateUnrealizedSummaryDbObject")
	public UnrealizedSummaryDbObject updateUnrealizedSummaryDbObject(
			UnrealizedSummaryDbObject unrealizedsummarydbobject) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			if (!containsUnrealizedSummaryDbObject(unrealizedsummarydbobject)) {
				throw new EntityNotFoundException("Object does not exist");
			}
			mgr.makePersistent(unrealizedsummarydbobject);
		} finally {
			mgr.close();
		}
		return unrealizedsummarydbobject;
	}

	/**
	 * This method removes the entity with primary key id.
	 * It uses HTTP DELETE method.
	 *
	 * @param id the primary key of the entity to be deleted.
	 */
	@ApiMethod(name = "removeUnrealizedSummaryDbObject")
	public void removeUnrealizedSummaryDbObject(@Named("id") Long id) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			UnrealizedSummaryDbObject unrealizedsummarydbobject = mgr
					.getObjectById(UnrealizedSummaryDbObject.class, id);
			mgr.deletePersistent(unrealizedsummarydbobject);
		} finally {
			mgr.close();
		}
	}

	private boolean containsUnrealizedSummaryDbObject(
			UnrealizedSummaryDbObject unrealizedsummarydbobject) {
		PersistenceManager mgr = getPersistenceManager();
		boolean contains = true;
		try {
			mgr.getObjectById(UnrealizedSummaryDbObject.class,
					unrealizedsummarydbobject.getId());
		} catch (javax.jdo.JDOObjectNotFoundException ex) {
			contains = false;
		} finally {
			mgr.close();
		}
		return contains;
	}

	private static PersistenceManager getPersistenceManager() {
		return PMF.get().getPersistenceManager();
	}

}

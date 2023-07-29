package com.filenet.salesforce.cpe;

import java.util.Iterator;
import java.util.Vector;
import javax.security.auth.Subject;
import com.filenet.api.collection.ObjectStoreSet;
import com.filenet.api.core.Connection;
import com.filenet.api.core.Document;
import com.filenet.api.core.Domain;
import com.filenet.api.core.Factory;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.util.UserContext;
import com.filenet.api.util.ConfigurationParameters;
import com.filenet.api.util.Id;

/**
 * This object represents the connection with
 * the Content Engine. Once connection is established
 * it intializes Domain and ObjectStoreSet with
 * available Domain and ObjectStoreSet.
 * 
 */
public class CEConnection 
{
	private Connection con;
	private Domain dom;
	private String domainName;
	private ObjectStoreSet ost;
	private Vector<String> osnames;
	private boolean isConnected;
	private UserContext uc;
		
	/*
	 * constructor
	 */
	public CEConnection()
	{
		con = null;
		uc = UserContext.get();
		dom = null;
		domainName = null;
		ost = null;
		osnames = new Vector<String>();
		isConnected = false;
	}
	
	/*
	 * Establishes connection with Content Engine using
	 * supplied username, password, JAAS stanza and CE Uri.
	 */
	public void establishConnection(String userName, String password, String stanza, String uri)
    {
		ConfigurationParameters cparms = new ConfigurationParameters();
        con = Factory.Connection.getConnection(uri, cparms);
        Subject sub = UserContext.createSubject(con,userName,password,stanza);
        uc.pushSubject(sub);
        dom = fetchDomain();
        domainName = dom.get_Name();
        //ost = getOSSet();
        isConnected = true;
    }

	/*
	 * Returns Domain object.
	 */
	public Domain fetchDomain()
    {
        dom = Factory.Domain.fetchInstance(con, null, null);
        return dom;
    }
    
    /*
     * Returns ObjectStoreSet from Domain
     */
	public ObjectStoreSet getOSSet()
    {
        ost = dom.get_ObjectStores();
        return ost;
    }
    
    /*
     * Returns vector containing ObjectStore
     * names from object stores available in
     * ObjectStoreSet.
     */
	public Vector<String> getOSNames()
    {
    	if(osnames.isEmpty())
    	{
    		Iterator<?> it = ost.iterator();
    		while(it.hasNext())
    		{
    			ObjectStore os = (ObjectStore) it.next();
    			osnames.add(os.get_DisplayName());
    		}
    	}
        return osnames;
    }

	/*
	 * Checks whether connection has established
	 * with the Content Engine or not.
	 */
	public boolean isConnected() 
	{
		return isConnected;
	}
	
	/*
	 * Returns ObjectStore object for supplied
	 * object store name.
	 */
	public ObjectStore fetchOS(String name)
    {
        ObjectStore os = Factory.ObjectStore.fetchInstance(dom, name, null);
        return os;
    }
	
	/*
	 * Returns the domain name.
	 */
	public String getDomainName()
    {
        return domainName;
    }

	public Connection getCon() {
		return con;
	}

	public Document fetchDocument(ObjectStore os, String id) {
		Document doc = Factory.Document.fetchInstance(os, Id.asIdOrNull(id), null);
		return doc;
	}
}

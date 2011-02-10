package org.sagebionetworks.repo.model;

import java.util.Date;

/**
 * This interface defines the methods implemented by all Data Transfer Objects
 * 
 * @author bhoff
 * 
 */
public interface Base {
	public void setId(String id);

	public String getId();

	public void setUri(String uri);

	public String getUri();

	public void setEtag(String etag);

	public String getEtag();

	public void setCreationDate(Date createDate);

	public Date getCreationDate();
}
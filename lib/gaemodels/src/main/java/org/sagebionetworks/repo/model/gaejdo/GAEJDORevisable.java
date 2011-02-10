package org.sagebionetworks.repo.model.gaejdo;

/**
 * Interfaced implemented by all classes which have versions.
 * 
 * @author bhoff <T> the type of the Object 'owning' the revision
 */
public interface GAEJDORevisable<T extends GAEJDORevisable<T>> extends
		GAEJDOBase {
	void setRevision(GAEJDORevision<T> r);

	GAEJDORevision<T> getRevision();
}
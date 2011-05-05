package org.sagebionetworks.repo.model.jdo.aw;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.jdo.JDOObjectNotFoundException;

import org.sagebionetworks.repo.model.Authorizable;
import org.sagebionetworks.repo.model.AuthorizationConstants;
import org.sagebionetworks.repo.model.DatastoreException;
import org.sagebionetworks.repo.model.InvalidModelException;
import org.sagebionetworks.repo.model.UnauthorizedException;
import org.sagebionetworks.repo.model.User;
import org.sagebionetworks.repo.model.UserGroup;
import org.sagebionetworks.repo.model.jdo.JDOExecutor;
import org.sagebionetworks.repo.model.jdo.KeyFactory;
import org.sagebionetworks.repo.model.jdo.persistence.JDOResourceAccess;
import org.sagebionetworks.repo.model.jdo.persistence.JDOUser;
import org.sagebionetworks.repo.model.jdo.persistence.JDOUserGroup;
import org.sagebionetworks.repo.web.NotFoundException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public class JDOUserGroupDAOImpl extends JDOBaseDAOImpl<UserGroup,JDOUserGroup> implements JDOUserGroupDAO {
	
	UserGroup newDTO() {
		return new UserGroup();
	}

	JDOUserGroup newJDO() {
		return new JDOUserGroup();
	}

	void copyToDto(JDOUserGroup jdo, UserGroup dto)
			throws DatastoreException {
		dto.setId(jdo.getId() == null ? null : KeyFactory.keyToString(jdo
				.getId()));
		dto.setName(jdo.getName());
		dto.setCreationDate(jdo.getCreationDate());
	}

	void copyFromDto(UserGroup dto, JDOUserGroup jdo)
		throws InvalidModelException {
		jdo.setName(dto.getName());
		jdo.setCreationDate(dto.getCreationDate());
	}

	Class<JDOUserGroup> getJdoClass() {
		return JDOUserGroup.class;
	}

//	@Override
//	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
//	public String create(UserGroup dto) throws DatastoreException,
//			InvalidModelException, UnauthorizedException {
//		jdoTemplate.makePersistent(g);
//	}
//	
//	@Override 
//	@Transactional
//	public JDOUserGroup get(Long id) {
//		return jdoTemplate.getObjectById(JDOUserGroup.class, id);
//	}
//
//	@Override
//	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
//	public void delete(JDOUserGroup g) {
//		jdoTemplate.deletePersistent(g);
//	}
	

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void addUser(UserGroup userGroup, Long user) throws DatastoreException {
		Long key = KeyFactory.stringToKey(userGroup.getId());

		JDOUserGroup jdo = (JDOUserGroup) jdoTemplate.getObjectById(getJdoClass(), key);

		jdo.getUsers().add(user);
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void removeUser(UserGroup userGroup, Long user) throws DatastoreException {
		Long key = KeyFactory.stringToKey(userGroup.getId());

		JDOUserGroup jdo = (JDOUserGroup) jdoTemplate.getObjectById(getJdoClass(), key);

		jdo.getUsers().remove(user);
	}
	
	@Transactional
	public Collection<Long> getUsers(UserGroup userGroup) throws DatastoreException  {
		Long key = KeyFactory.stringToKey(userGroup.getId());

		JDOUserGroup jdo = (JDOUserGroup) jdoTemplate.getObjectById(getJdoClass(), key);
		return jdo.getUsers();
	}

	@Transactional
	public UserGroup getPublicGroup() throws DatastoreException {
		return getSystemGroup(AuthorizationConstants.PUBLIC_GROUP_NAME, false);
	}
	
	@Transactional
	public UserGroup getIndividualGroup(String userName) throws DatastoreException {
		return getSystemGroup(userName, true);
	}
	
	@Transactional
	public UserGroup getAdminGroup() throws DatastoreException {
		return getSystemGroup(AuthorizationConstants.ADMIN_GROUP_NAME, false);
	}


	@Transactional
	public UserGroup getSystemGroup(String name, boolean isIndividualGroup) throws DatastoreException {
		JDOExecutor<JDOUserGroup> exec = new JDOExecutor<JDOUserGroup>(jdoTemplate, JDOUserGroup.class);
		Collection<JDOUserGroup> ans = exec.execute(
				"isSystemGroup==true && name==pName && isIndividual==pIsIndividual",
				String.class.getName()+" pName, "+Boolean.class.getName()+" pIsIndividual",
				null,
				name, isIndividualGroup);
		if (ans.size() > 1)
			throw new IllegalStateException("Expected 0-1 but found "
					+ ans.size());
		if (ans.size() == 0)
			return null;
		JDOUserGroup jdo = ans.iterator().next();
		UserGroup dto = newDTO();
		copyToDto(jdo, dto);
		return dto;
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public UserGroup createPublicGroup() {
		throw new RuntimeException("Not yet implemented.");
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public UserGroup createAdminGroup() {
		throw new RuntimeException("Not yet implemented.");
	}
	
	@Transactional
	public Collection<String> getCreatableTypes(UserGroup userGroup) throws NotFoundException, DatastoreException {
		Long key = KeyFactory.stringToKey(userGroup.getId());
		try {
			JDOUserGroup jdo = (JDOUserGroup) jdoTemplate.getObjectById(JDOUserGroup.class, key);
			return jdo.getCreatableTypes();
		} catch (JDOObjectNotFoundException e) {
			throw new NotFoundException(e);
		} catch (Exception e) {
			throw new DatastoreException(e);
		}
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void setCreatableTypes(UserGroup userGroup, Collection<String> creatableTypes) throws NotFoundException, DatastoreException {
		Long key = KeyFactory.stringToKey(userGroup.getId());
		try {
			JDOUserGroup jdo = (JDOUserGroup) jdoTemplate.getObjectById(JDOUserGroup.class, key);
			jdo.setCreatableTypes(new HashSet<String>(creatableTypes));
		} catch (JDOObjectNotFoundException e) {
			throw new NotFoundException(e);
		} catch (Exception e) {
			throw new DatastoreException(e);
		}

	}

	// TODO this could be done more quickly by querying directly against JDOResourceAccess
	public void addResource(UserGroup userGroup, Authorizable resource, 
			Collection<AuthorizationConstants.ACCESS_TYPE> accessTypes) 
				throws NotFoundException, DatastoreException {

		Long key = KeyFactory.stringToKey(userGroup.getId());
		Long resourceKey = KeyFactory.stringToKey(resource.getId());
		String type = resource.getType();
		try {
			JDOUserGroup jdo = (JDOUserGroup) jdoTemplate.getObjectById(JDOUserGroup.class, key);
			Set<JDOResourceAccess> ras = jdo.getResourceAccess();
			boolean foundit = false;
			// if you can find the reference resource, then update it...
			for (JDOResourceAccess ra: ras) {
				if (type.equals(ra.getResourceType()) && resourceKey.equals(ra.getResourceId())) {
					foundit = true;
					ra.setAccessTypeByEnum(new HashSet<AuthorizationConstants.ACCESS_TYPE>(accessTypes));
					break;
				}
			}
			// ... else add a new record for the resource, with the specified access types.
			if (!foundit) {
				JDOResourceAccess ra = new JDOResourceAccess();
				ra.setResourceType(type);
				ra.setResourceId(resourceKey);
				ra.setAccessTypeByEnum(new HashSet<AuthorizationConstants.ACCESS_TYPE>(accessTypes));
				jdo.getResourceAccess().add(ra);
			}
		} catch (JDOObjectNotFoundException e) {
			throw new NotFoundException(e);
		} catch (Exception e) {
			throw new DatastoreException(e);
		}
	}

	// TODO this could be done more quickly by querying directly against JDOResourceAccess
	public void removeResource(UserGroup userGroup, Authorizable resource) 
				throws NotFoundException, DatastoreException {

		Long key = KeyFactory.stringToKey(userGroup.getId());
		Long resourceKey = KeyFactory.stringToKey(resource.getId());
		String type = resource.getType();
		try {
			JDOUserGroup jdo = (JDOUserGroup) jdoTemplate.getObjectById(JDOUserGroup.class, key);
			Set<JDOResourceAccess> ras = jdo.getResourceAccess();
			for (JDOResourceAccess ra: ras) {
				if (type.equals(ra.getResourceType()) && resourceKey.equals(ra.getResourceId())) {
					ras.remove(ra);
					break;
				}
			}
		} catch (JDOObjectNotFoundException e) {
			throw new NotFoundException(e);
		} catch (Exception e) {
			throw new DatastoreException(e);
		}
	}

	// TODO this could be done more quickly by querying directly against JDOResourceAccess
	public Collection<AuthorizationConstants.ACCESS_TYPE> getAccessTypes(UserGroup userGroup, Authorizable resource) 
		throws NotFoundException, DatastoreException {

		Long key = KeyFactory.stringToKey(userGroup.getId());
		Long resourceKey = KeyFactory.stringToKey(resource.getId());
		String type = resource.getType();
		try {
			JDOUserGroup jdo = (JDOUserGroup) jdoTemplate.getObjectById(JDOUserGroup.class, key);
			Set<JDOResourceAccess> ras = jdo.getResourceAccess();
			for (JDOResourceAccess ra: ras) {
				if (type.equals(ra.getResourceType()) && resourceKey.equals(ra.getResourceId())) {
					return ra.getAccessTypeAsEnum();
				}
			}
			return new HashSet<AuthorizationConstants.ACCESS_TYPE>();
		} catch (JDOObjectNotFoundException e) {
			throw new NotFoundException(e);
		} catch (Exception e) {
			throw new DatastoreException(e);
		}
	}
}

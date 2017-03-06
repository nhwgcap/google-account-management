package gov.cap.nhwg.useradmin.google;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.api.client.util.ArrayMap;
import com.google.api.services.admin.directory.model.User;
import com.google.api.services.admin.directory.model.UserExternalId;
import com.google.api.services.admin.directory.model.UserName;
import com.google.api.services.admin.directory.model.UserOrganization;

/**
 * Wrapper for a Google directory user with convenience methods for CAP specific
 * properties.
 */
public class GoogleUser {
	private static final String ORGANIZATION = "organization";
	private static final String TYPE = "type";
	private static final String VALUE = "value";
	private static final String PRIMARY = "primary";
	private static final String DESCRIPTION = "description";
	private static final String CUSTOM_TYPE = "customType";

	private final User user;

	/**
	 * Creates a new instance with only a CAPID
	 * 
	 * @param id
	 *            the CAPID (or other numeric ID) of the user
	 */
	public GoogleUser(int id) {
		user = new User();
		setId(id);
		user.setName(new UserName());
	}

	/**
	 * Creates a new instance from a Google directory user instance.
	 * 
	 * @param user
	 *            the non-null user retrieved from the Google directory list
	 *            service
	 */
	public GoogleUser(User user) {
		this.user = user;

		convertExternalIds();
		convertOrganizations();
	}

	private void convertExternalIds() {
		@SuppressWarnings("unchecked")
		List<ArrayMap<String, String>> externalIds = ((List<ArrayMap<String, String>>) user.getExternalIds());
		if (externalIds == null) {
			return;
		}

		List<UserExternalId> newExternalIds = new ArrayList<>(externalIds.size());
		for (ArrayMap<String, String> externalId : externalIds) {
			UserExternalId newExternalId = new UserExternalId();
			newExternalId.setType(externalId.get(TYPE));
			newExternalId.setValue(externalId.get(VALUE));
			newExternalIds.add(newExternalId);
		}
		user.setExternalIds(newExternalIds);
	}

	private UserExternalId getExternalId(String type) {
		@SuppressWarnings("unchecked")
		List<UserExternalId> externalIds = (List<UserExternalId>) user.getExternalIds();
		if (externalIds != null) {
			for (UserExternalId externalId : externalIds) {
				if (externalId.getType().equals(type)) {
					return externalId;
				}
			}
		}

		UserExternalId externalId = new UserExternalId();
		externalId.setType(type);
		return externalId;
	}

	public int getId() {
		String value = getExternalId(ORGANIZATION).getValue();
		if (value != null) {
			return Integer.valueOf(value);
		}
		return -1;
	}

	private void setId(int id) {
		UserExternalId externalId = getExternalId(ORGANIZATION);
		externalId.setValue(Integer.toString(id));
		user.setExternalIds(Collections.singletonList(externalId));
	}

	public String getFirstName() {
		return user.getName().getGivenName();
	}

	public void setFirstName(String name) {
		user.getName().setGivenName(name);
	}

	public String getLastName() {
		return user.getName().getFamilyName();
	}

	public void setLastName(String name) {
		user.getName().setFamilyName(name);
	}

	public String getEmail() {
		return user.getPrimaryEmail();
	}

	public void setEmail(String email) {
		user.setPrimaryEmail(email);
	}

	private void convertOrganizations() {
		@SuppressWarnings("unchecked")
		List<ArrayMap<String, ?>> organizations = ((List<ArrayMap<String, ?>>) user.getOrganizations());
		if (organizations == null) {
			return;
		}

		List<UserOrganization> newOrganizations = new ArrayList<>(organizations.size());
		for (ArrayMap<String, ?> organization : organizations) {
			UserOrganization newOrganization = new UserOrganization();
			newOrganization.setPrimary((Boolean) organization.get(PRIMARY));
			newOrganization.setCustomType((String) organization.get(CUSTOM_TYPE));
			newOrganization.setDescription((String) organization.get(DESCRIPTION));
			newOrganizations.add(newOrganization);
		}
		user.setOrganizations(newOrganizations);
	}

	private UserOrganization getOrCreatePrimaryOrganization() {
		@SuppressWarnings("unchecked")
		List<UserOrganization> organizations = (List<UserOrganization>) user.getOrganizations();
		if (organizations != null) {
			for (UserOrganization organization : organizations) {
				if (organization.getPrimary()) {
					return organization;
				}
			}
		}

		UserOrganization organization = new UserOrganization();
		organization.setPrimary(true);
		user.setOrganizations(Collections.singletonList(organization));

		return organization;
	}

	public String getType() {
		UserOrganization organization = getOrCreatePrimaryOrganization();
		return organization.getDescription();
	}

	public void setType(String type) {
		UserOrganization organization = getOrCreatePrimaryOrganization();
		organization.setDescription(type);
	}

	public String getUnitPath() {
		return user.getOrgUnitPath();
	}

	public void setUnitPath(String unit) {
		user.setOrgUnitPath(unit);
	}
}

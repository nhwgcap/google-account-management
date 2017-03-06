package gov.cap.nhwg.useradmin.google;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.api.services.admin.directory.Directory;
import com.google.api.services.admin.directory.Directory.Users.List;
import com.google.api.services.admin.directory.model.User;
import com.google.api.services.admin.directory.model.Users;

/**
 * Provides a wrapper around the Google user directory list API to load
 * {@code GoogleUser}s from Google Apps.
 */
public class GoogleUserLoader {
	private static final int MAX_RESULTS = 500;

	private final Directory directory;
	private final String domain;

	private Map<String, GoogleUser> users;

	/**
	 * Creates a new instance.
	 * 
	 * @param directory
	 *            the authenticated directory service for the Google Apps
	 *            instance from which to retrieve users
	 * @param domain
	 *            a valid domain managed by the Google Apps instance to which
	 *            the directory service is authenticated
	 */
	public GoogleUserLoader(Directory directory, String domain) {
		this.directory = directory;
		this.domain = domain;
	}

	/**
	 * Retrieves the users from the Google Apps instance and stores
	 * {@code GoogleUser} instances locally for each.
	 * 
	 * @see #getUsersByEmail
	 */
	public void load() throws IOException {
		users = new HashMap<>();

		List list = directory.users().list()
				.setDomain(domain)
				.setMaxResults(MAX_RESULTS);
		load(list);
	}

	private void load(List list) throws IOException {
		Users result = list.execute();
		for (User user : result.getUsers()) {
			GoogleUser googleUser = new GoogleUser(user);
			users.put(googleUser.getEmail(), googleUser);
		}

		String nextPageToken = result.getNextPageToken();
		if (nextPageToken != null) {
			list.setPageToken(nextPageToken);
			load(list);
		}
	}

	/**
	 * Gets all the users as a map of email to {@code GoogleUser} instance.
	 * 
	 * @return a map of users keyed by their primary email address
	 */
	public Map<String, GoogleUser> getUsersByEmail() {
		return users;
	}
}

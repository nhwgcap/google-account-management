package gov.cap.nhwg.useradmin.google;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.client.util.store.MemoryDataStoreFactory;
import com.google.api.services.admin.directory.Directory;
import com.google.api.services.admin.directory.DirectoryScopes;

/**
 * A wrapper providing access to the various Google Apps Admin API services.
 * 
 * @see <a href=
 *      "https://developers.google.com/admin-sdk/directory/v1/get-start/getting-started">Google
 *      Apps Admin API</a>
 */
public class ServiceProvider {
	private static final List<String> SCOPES = Arrays.asList(
			DirectoryScopes.ADMIN_DIRECTORY_USER_READONLY);
	private static final String ACCESS_TYPE = "offline";
	private static final String AUTH_USER_ID = "user";

	private final String applicationName;

	private final DataStoreFactory dataStoreFactory;
	private final JsonFactory jsonFactory;
	private final HttpTransport httpTransport;

	private Credential credential;

	/**
	 * Creates a new instance and authorizes with the API.
	 * 
	 * @param applicationName
	 *            the name of the application to authorize
	 * @param clientSecretPath
	 *            the path to the client secrets file
	 */
	public ServiceProvider(String applicationName, Path clientSecretPath) throws GeneralSecurityException, IOException {
		this.applicationName = applicationName;
		jsonFactory = JacksonFactory.getDefaultInstance();
		httpTransport = GoogleNetHttpTransport.newTrustedTransport();
		dataStoreFactory = new MemoryDataStoreFactory();
		credential = authorize(clientSecretPath);
	}

	/**
	 * Creates an authorized {@code Credential} object.
	 * 
	 * @param clientSecretPath
	 *            the path to the client secret file
	 *
	 * @return an authorized credential object.
	 */
	private Credential authorize(Path clientSecretPath) throws IOException {
		try (Reader reader = Files.newBufferedReader(clientSecretPath)) {
			GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(jsonFactory, reader);

			GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
					httpTransport, jsonFactory, clientSecrets, SCOPES)
							.setDataStoreFactory(dataStoreFactory)
							.setAccessType(ACCESS_TYPE)
							.build();
			return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize(AUTH_USER_ID);
		}
	}

	/**
	 * Build and return an authorized Admin SDK Directory client service.
	 *
	 * @return an authorized Directory client service
	 *
	 * @throws IOException
	 */
	public Directory getDirectoryService() throws IOException {
		return new Directory.Builder(httpTransport, jsonFactory, credential)
				.setApplicationName(applicationName)
				.build();
	}
}

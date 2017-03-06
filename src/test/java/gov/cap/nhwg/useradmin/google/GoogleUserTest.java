package gov.cap.nhwg.useradmin.google;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Before;
import org.junit.Test;

public class GoogleUserTest {
	private static final int NUMBER = 12345;
	private static final String STRING = "Something";

	private GoogleUser user;

	@Before
	public void setup() {
		user = new GoogleUser(NUMBER);
	}

	@Test
	public void testGettingId() {
		assertThat(user.getId(), equalTo(NUMBER));
	}

	@Test
	public void testSettingFirstName() {
		user.setFirstName(STRING);

		assertThat(user.getFirstName(), equalTo(STRING));
	}

	@Test
	public void testSettingLastName() {
		user.setLastName(STRING);

		assertThat(user.getLastName(), equalTo(STRING));
	}

	@Test
	public void testGettingEmail() {
		user.setEmail(STRING);

		assertThat(user.getEmail(), equalTo(STRING));
	}

	@Test
	public void testGettingType() {
		user.setType(STRING);

		assertThat(user.getType(), equalTo(STRING));
	}

	@Test
	public void testGettingUnitPath() {
		user.setUnitPath(STRING);

		assertThat(user.getUnitPath(), equalTo(STRING));
	}
}

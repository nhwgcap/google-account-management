package gov.cap.nhwg.useradmin.capwatch;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class MemberLoaderTest {
	private static final int ONE = 111111;
	private static final int TWO = 222222;

	private MemberLoader loader;

	@Before
	public void setup() {
		loader = new MemberLoader();
	}

	@Test
	public void testLoading() throws URISyntaxException {
		URL url = getClass().getResource("/CAPWATCH/Member.txt");
		assertThat(url, notNullValue());

		Map<Integer, CapMember> members = loader.load(Paths.get(url.toURI()));

		assertThat(members.keySet(), containsInAnyOrder(ONE, TWO));
		verifyMember(members.get(ONE), ONE, "First", "Senior", "SENIOR");
		verifyMember(members.get(TWO), TWO, "Second", "Cadet", "CADET");
	}

	private void verifyMember(CapMember member, int id, String firstName, String lastName, String type) {
		assertThat(member.getId(), equalTo(id));
		assertThat(member.getFirstName(), equalTo(firstName));
		assertThat(member.getLastName(), equalTo(lastName));
		assertThat(member.getType(), equalTo(type));
	}
}

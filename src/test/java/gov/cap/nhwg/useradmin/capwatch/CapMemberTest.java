package gov.cap.nhwg.useradmin.capwatch;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Before;
import org.junit.Test;

public class CapMemberTest {
	private static final int CAPID = 281200;
	private static final String STRING = "Some string value";

	private CapMember member;

	@Before
	public void setup() {
		member = new CapMember(CAPID);
	}

	@Test
	public void capIdShouldBeFromConstructor() {
		assertThat(member.getId(), equalTo(CAPID));
	}

	@Test
	public void testFirstName() {
		member.setFirstName(STRING);
		assertThat(member.getFirstName(), equalTo(STRING));
	}

	@Test
	public void testLastName() {
		member.setLastName(STRING);
		assertThat(member.getLastName(), equalTo(STRING));
	}

	@Test
	public void testType() {
		member.setType(STRING);
		assertThat(member.getType(), equalTo(STRING));
	}
}

package gov.cap.nhwg.useradmin.capwatch;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class RecordTest {
	private enum Columns {
		A, B
	}

	private static final Integer NUMERIC_VALUE = 42;
	private static final String STRING_VALUE = "b,c";

	private static final String INPUT = "\"42\",\"b,c\"";

	private Record<Columns> record;

	@Before
	public void setup() throws IOException {
		record = new Record<Columns>(INPUT);
	}

	@Test
	public void testGettingStringValue() {
		assertThat(record.getString(Columns.B), equalTo(STRING_VALUE));
	}

	@Test
	public void testGettingNumericValue() {
		assertThat(record.getInt(Columns.A), equalTo(NUMERIC_VALUE));
	}
}

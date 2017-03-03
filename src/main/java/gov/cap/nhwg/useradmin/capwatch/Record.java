package gov.cap.nhwg.useradmin.capwatch;

import java.io.IOException;

import com.opencsv.CSVParser;

public class Record<T extends Enum<T>> {
	private String[] values;

	public Record(String line) throws IOException {
		values = new CSVParser().parseLine(line);
	}

	public String getString(T column) {
		return values[column.ordinal()];
	}

	public Integer getInt(T column) {
		return Integer.valueOf(getString(column));
	}
}

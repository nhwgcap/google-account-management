package gov.cap.nhwg.useradmin.capwatch;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Loads member information from a CAPWATCH Members.txt file.
 */
public class MemberLoader {
	private enum Column {
		CAPID, SSN, NameLast, NameFirst, NameMiddle, NameSuffix, Gender, DOB, Profession, EducationLevel, Citizen, ORGID, Wing, Unit, Rank, Joined, Expiration, OrgJoined, UsrID, DateMod, LSCode, Type, RankDate, Region, MbrStatus, PicStatus, PicDate, CdtWaiver
	}

	/**
	 * Loads member information.
	 * 
	 * @param memberTable
	 *            the path to the Members.txt from a CAPWATCH download.
	 * @return a map of CAPID to member
	 */
	public Map<Integer, CapMember> load(Path memberTable) {
		try {
			return Files.lines(memberTable)
					.skip(1)
					.map(this::createMember)
					.filter(member -> member != null) // TODO: Handle failures
														// better
					.collect(Collectors.toConcurrentMap(CapMember::getId, Function.identity()));
		} catch (IOException e) {
			return null;
		}
	}

	private CapMember createMember(String line) {
		try {
			Record<Column> record = new Record<>(line);

			CapMember member = new CapMember(record.getInt(Column.CAPID));
			member.setFirstName(record.getString(Column.NameFirst));
			member.setLastName(record.getString(Column.NameLast));
			member.setType(record.getString(Column.Type));

			return member;
		} catch (IOException e) {
			return null;
		}
	}
}

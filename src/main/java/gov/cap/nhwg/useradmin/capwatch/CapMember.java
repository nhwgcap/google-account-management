package gov.cap.nhwg.useradmin.capwatch;

/**
 * CAP member data loaded from CAPWATCH.
 * 
 * @see MemberLoader
 */
public class CapMember {
	private final int id;

	private String firstName;
	private String lastName;
	private String type;

	public CapMember(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}

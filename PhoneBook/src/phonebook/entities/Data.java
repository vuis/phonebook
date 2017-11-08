package phonebook.entities;

public class Data {
	private int id;
	private String firstname;
	private String lastname;
	private String email;
	private Number number;

	public Data(int id, String firstname, String lastname, String email, Number number) {
		this.id = id;
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.number = number;
	}

	public Data(String firstname, String lastname, String email, Number number) {
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.number = number;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Number getNumber() {
		return number;
	}

	public void setNumber(Number number) {
		this.number = number;
	}

}

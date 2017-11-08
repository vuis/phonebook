package phonebook.entities;

public class Number {
	private int id;
	private String number;
	private Group group;

	public Number(int id, String number, Group group) {
		this.id = id;
		this.number = number;
		this.group = group;
	}
	
	public Number(String number, Group group) {	
		this.number = number;
		this.group = group;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

}

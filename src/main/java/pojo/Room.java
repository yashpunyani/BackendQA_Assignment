package pojo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Room {

	private List<Guest> guest;

	public List<Guest> getGuest() {
		return guest;
	}

	public void setGuest(List<Guest> guest) {
		this.guest = guest;
	}
	
}

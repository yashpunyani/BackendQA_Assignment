package utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import pojo.Dates;
import pojo.Guest;
import pojo.PostHotelSearch;
import pojo.Room;

public class BuildJsonPayload {

	// Will Dynamically create the json request body for PostHotelSearch API using
	// POJO
	public static PostHotelSearch postHotelSearch(Map<String, String> payloadData) {

		PostHotelSearch p = new PostHotelSearch();
		Dates dates = new Dates();
		dates.setCheckin(payloadData.get("checkin"));
		dates.setCheckout(payloadData.get("checkout"));
		p.setDates(dates);

		p.setDestination(payloadData.get("destination"));

		Room room1 = new Room();
		Guest guest1 = new Guest();
		guest1.setType(payloadData.get("type1"));
		if (payloadData.get("age1") != null)
			guest1.setAge(Integer.parseInt(payloadData.get("age1")));

		Guest guest2 = new Guest();
		guest2.setType(payloadData.get("type2"));
		if (payloadData.get("age2") != null)
			guest2.setAge(Integer.parseInt(payloadData.get("age2")));

		Guest guest3 = new Guest();
		guest3.setType(payloadData.get("type3"));
		if (payloadData.get("age3") != null)
			guest3.setAge(Integer.parseInt(payloadData.get("age3")));
		List<Guest> myGuestList1 = new ArrayList<Guest>();
		myGuestList1.add(guest1);
		myGuestList1.add(guest2);
		myGuestList1.add(guest3);

		Room room2 = new Room();
		List<Guest> myGuestList2 = new ArrayList<Guest>();
		Guest guest4 = new Guest();
		guest4.setType(payloadData.get("type4"));
		if (payloadData.get("age4") != null)
			guest4.setAge(Integer.parseInt(payloadData.get("age4")));
		Guest guest5 = new Guest();
		guest5.setType(payloadData.get("type5"));
		if (payloadData.get("age5") != null)
			guest5.setAge(Integer.parseInt(payloadData.get("age5")));
		Guest guest6 = new Guest();
		guest6.setType(payloadData.get("type6"));
		if (payloadData.get("age6") != null)
			guest6.setAge(Integer.parseInt(payloadData.get("age6")));
		myGuestList2.add(guest4);
		myGuestList2.add(guest5);
		myGuestList2.add(guest6);

		List<Room> roomList = new ArrayList<Room>();
		room1.setGuest(myGuestList1);
		room2.setGuest(myGuestList2);
		roomList.add(room1);
		roomList.add(room2);
		p.setRoom(roomList);

		p.setPlaceId(payloadData.get("placeId"));

		return p;
	}

}

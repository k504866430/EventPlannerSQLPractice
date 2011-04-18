package com.semanticbits.ep

class Event {

	String title
	String description
	Date startDate
	Date endDate
	String type
	String url
	
	User user
	
	static hasMany = [locations: EventPlace]
	
    static constraints = {
		title blank: false, unique: true
		description blank: false
		startDate blank: false
		endDate nullable: true
		type nullable: true
		url nullable: true, url: true
    }
	
	@Override
	public String toString() {
		return title+" ("+startDate+")";
	}
}

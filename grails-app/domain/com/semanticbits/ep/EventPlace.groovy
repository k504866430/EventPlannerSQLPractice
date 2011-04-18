package com.semanticbits.ep

class EventPlace extends Address{

	
	
	Date startDate
	Date endDate
	
    static constraints = {
		startDate blank: false
		endDate nullable: true
    }
}

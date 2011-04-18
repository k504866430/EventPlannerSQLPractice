package com.semanticbits.ep

class Address {

	String street
	String country
	String city
	String postalCode
	String state
	
    static constraints = {
		postalCode blank: false
		state nullable: true, minSize: 2, maxSize: 2
		country nullable: true
		city nullable: true
		street nullable: true
    }
}

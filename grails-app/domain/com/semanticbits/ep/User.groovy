package com.semanticbits.ep


class User extends ApplicationUser{

	String firstName
	String lastName
	String email
	String aboutMe
	String website

	Address homeAddress, workAddress
	static embedded = ['homeAddress', 'workAddress']

	static hasMany = [events: Event]

	static constraints = {
		firstName blank: false
		lastName blank: false
		email blank: false, unique: true
		website nullable: true, url: true
		aboutMe nullable: true
		homeAddress nullable: true
		workAddress nullable: true
	}
	
	static mapping = {
		table 'app_user'
	}
	
	@Override
	public String toString() {
		return firstName+" "+lastName+" "+ "("+email+")"
	}
}

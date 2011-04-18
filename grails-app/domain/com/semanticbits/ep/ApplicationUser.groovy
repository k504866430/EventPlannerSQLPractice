package com.semanticbits.ep

class ApplicationUser {

	String username
	String password
	boolean enabled
	boolean accountExpired
	boolean accountLocked
	boolean passwordExpired

	static constraints = {
		username blank: false, unique: true
		password blank: false
	}

	static mapping = {
		password column: '`password`'
	}

	Set<ApplicationRole> getAuthorities() {
		ApplicationUserApplicationRole.findAllByApplicationUser(this).collect { it.applicationRole } as Set
	}
}

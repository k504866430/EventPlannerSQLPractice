package com.semanticbits.ep

class UserService {

    static expose = [ 'gwt:com.semanticbits.ep.client' ]
	
    def String getAuthor() {
		return User.findByEmail("kruttik.aggarwal@semanticbits.com").toString()
    }
}

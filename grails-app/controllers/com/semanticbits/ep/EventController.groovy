package com.semanticbits.ep

import grails.plugins.springsecurity.Secured;

@Secured(['ROLE_USER', 'ROLE_ADMIN'])
class EventController {

    static scaffold = Event
	
	def index = {
	}
}

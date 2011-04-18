import com.semanticbits.ep.ApplicationRole;
import com.semanticbits.ep.ApplicationUserApplicationRole;
import com.semanticbits.ep.EventPlace;
import com.semanticbits.ep.User;
import com.semanticbits.ep.Event;

class BootStrap {
	def springSecurityService
	
    def init = { servletContext ->
		
		def userRole = ApplicationRole.findByAuthority("ROLE_USER")?:new ApplicationRole(authority: "ROLE_USER").save(failOnError: true)
		def adminRole = ApplicationRole.findByAuthority("ROLE_ADMIN")?:new ApplicationRole(authority: "ROLE_ADMIN").save(failOnError: true)
		
		def user1 = new User(firstName: "Patrick", lastName: "McConnell", email: "patrick.mcconnell@semanticbits.com", username: "patrick", aboutMe: "I am smart", password: springSecurityService.encodePassword("password"), enabled: true)
		def user2 = new User(firstName: "Kruttik", lastName: "Aggarwal", email: "kruttik.aggarwal@semanticbits.com", username: "kruttik", password: springSecurityService.encodePassword("password"), enabled: true)
		def user3 = new User(firstName: "Matthew", lastName: "McKinnerey", email: "matthew.mckinnerey@semanticbits.com", username: "matthew", password: springSecurityService.encodePassword("password"), enabled: true)
		user1.save(failOnError: true)
		user2.save(failOnError: true)
		user3.save(failOnError: true)
		
		ApplicationUserApplicationRole.create(user1, userRole).save(failOnError: true)
		ApplicationUserApplicationRole.create(user2, adminRole).save(failOnError: true)
		ApplicationUserApplicationRole.create(user3, userRole).save(failOnError: true)
		
		def eventPlace1 = new EventPlace(postalCode: "20171", country: "USA", startDate: new Date())
		def eventPlace2 = new EventPlace(postalCode: "20171", country: "USA", startDate: new Date())
		def eventPlace3 = new EventPlace(postalCode: "20171", country: "USA", startDate: new Date())
		eventPlace1.save(failOnError: true)
		eventPlace2.save(failOnError: true)
		eventPlace3.save(failOnError: true)
		
		def event1 = new Event(title: "Event 1", description: "test event 1", startDate: new Date(), user: user1, locations: [eventPlace1, eventPlace2])
		def event2 = new Event(title: "Event 2", description: "test event 1", startDate: new Date(), user: user1, locations: [eventPlace3])
		def event3 = new Event(title: "Event 3", description: "test event 1", startDate: new Date(), user: user2)
		def event4 = new Event(title: "Event 4", description: "test event 1", startDate: new Date(), user: user3)
		event1.save(failOnError: true)
		event2.save(failOnError: true)
		event3.save(failOnError: true)
		event4.save(failOnError: true)
    }
    def destroy = {
    }
}

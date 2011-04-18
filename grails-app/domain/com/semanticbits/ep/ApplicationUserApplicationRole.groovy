package com.semanticbits.ep

import org.apache.commons.lang.builder.HashCodeBuilder

class ApplicationUserApplicationRole implements Serializable {

	ApplicationUser applicationUser
	ApplicationRole applicationRole

	boolean equals(other) {
		if (!(other instanceof ApplicationUserApplicationRole)) {
			return false
		}

		other.applicationUser?.id == applicationUser?.id &&
			other.applicationRole?.id == applicationRole?.id
	}

	int hashCode() {
		def builder = new HashCodeBuilder()
		if (applicationUser) builder.append(applicationUser.id)
		if (applicationRole) builder.append(applicationRole.id)
		builder.toHashCode()
	}

	static ApplicationUserApplicationRole get(long applicationUserId, long applicationRoleId) {
		find 'from ApplicationUserApplicationRole where applicationUser.id=:applicationUserId and applicationRole.id=:applicationRoleId',
			[applicationUserId: applicationUserId, applicationRoleId: applicationRoleId]
	}

	static ApplicationUserApplicationRole create(ApplicationUser applicationUser, ApplicationRole applicationRole, boolean flush = false) {
		new ApplicationUserApplicationRole(applicationUser: applicationUser, applicationRole: applicationRole).save(flush: flush, insert: true)
	}

	static boolean remove(ApplicationUser applicationUser, ApplicationRole applicationRole, boolean flush = false) {
		ApplicationUserApplicationRole instance = ApplicationUserApplicationRole.findByApplicationUserAndApplicationRole(applicationUser, applicationRole)
		instance ? instance.delete(flush: flush) : false
	}

	static void removeAll(ApplicationUser applicationUser) {
		executeUpdate 'DELETE FROM ApplicationUserApplicationRole WHERE applicationUser=:applicationUser', [applicationUser: applicationUser]
	}

	static void removeAll(ApplicationRole applicationRole) {
		executeUpdate 'DELETE FROM ApplicationUserApplicationRole WHERE applicationRole=:applicationRole', [applicationRole: applicationRole]
	}

	static mapping = {
		id composite: ['applicationRole', 'applicationUser']
		version false
	}
}

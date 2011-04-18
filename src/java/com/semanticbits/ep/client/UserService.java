package com.semanticbits.ep.client;

import com.google.gwt.user.client.rpc.RemoteService;

public interface UserService extends RemoteService {
	
	String getAuthor();
}

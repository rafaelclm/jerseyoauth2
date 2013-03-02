package com.burgmeier.jerseyoauth2.authsrv.api.client;

import com.burgmeier.jerseyoauth2.api.client.IAuthorizedClientApp;

public interface IClientAuthorization {

	String getCode();

	IAuthorizedClientApp getAuthorizedClient();

}

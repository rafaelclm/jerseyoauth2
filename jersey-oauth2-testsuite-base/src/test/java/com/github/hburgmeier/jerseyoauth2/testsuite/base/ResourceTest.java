package com.github.hburgmeier.jerseyoauth2.testsuite.base;

import org.junit.Assert;
import org.junit.Test;
import org.scribe.model.Token;

import com.github.hburgmeier.jerseyoauth2.testsuite.base.SampleEntity;
import com.github.hburgmeier.jerseyoauth2.testsuite.base.client.ClientException;
import com.github.hburgmeier.jerseyoauth2.testsuite.base.client.ResourceClient;

public class ResourceTest extends BaseTest {

	@Test
	public void testInvalidResourceAccess()
	{
		ResourceClient client = new ResourceClient(clientEntity);
		try {
			client.sendTestRequestSample1(new Token("Invalid",""));
			Assert.fail();
		} catch (ClientException e) {
		}
	}
	
	@Test
	public void testValidResourceAccess() throws ClientException
	{
		String code = authClient.authorizeClient(clientEntity, "test1 test2").getCode();
		Assert.assertNotNull(code);
		restClient.setFollowRedirects(false);
		
		ResourceClient client = new ResourceClient(clientEntity);
		Token tok = client.getAccessToken(code);
		Assert.assertNotNull(tok);
		Assert.assertNotNull(tok.getToken());
		
		client.sendTestRequestSample1(tok);
		
		SampleEntity entity = client.retrieveEntitySample1(tok);
		Assert.assertNotNull(entity);
		Assert.assertEquals("manager", entity.getUsername());
		Assert.assertEquals(clientEntity.getClientId(), entity.getClientApp());
	}	
	
	@Test
	public void testClassAnnotation() throws ClientException
	{
		String code = authClient.authorizeClient(clientEntity, "").getCode();
		Assert.assertNotNull(code);
		restClient.setFollowRedirects(false);
		
		ResourceClient client = new ResourceClient(clientEntity);
		Token oldToken = client.getAccessToken(code);
		Assert.assertNotNull(oldToken);
		Assert.assertNotNull(oldToken.getToken());
		
		client.sendTestRequestSample2(oldToken);		
	}	
	
	@Test
	public void testInvalidScopes()
	{
		String code = authClient.authorizeClient(clientEntity, "test1 invalidScope").getCode();
		Assert.assertNotNull(code);
		restClient.setFollowRedirects(false);
		
		ResourceClient client = new ResourceClient(clientEntity);
		Token tok = client.getAccessToken(code);
		Assert.assertNotNull(tok);
		Assert.assertNotNull(tok.getToken());
		
		try {
			client.sendTestRequestSample1(tok);
			Assert.fail();
		} catch (ClientException cex) {
		}
	}	
	
	@Test
	public void testTokenExpiration() throws ClientException, InterruptedException
	{
		String code = authClient.authorizeClient(clientEntity, "test1 test2").getCode();
		Assert.assertNotNull(code);
		restClient.setFollowRedirects(false);
		
		ResourceClient client = new ResourceClient(clientEntity);
		Token tok = client.getAccessToken(code);
		Assert.assertNotNull(tok);
		Assert.assertNotNull(tok.getToken());
		
		client.sendTestRequestSample1(tok);
		
		Thread.sleep(7000);
		
		try {
			client.sendTestRequestSample1(tok);
			Assert.fail();
		} catch (ClientException e) {
		}
	}		
		
}
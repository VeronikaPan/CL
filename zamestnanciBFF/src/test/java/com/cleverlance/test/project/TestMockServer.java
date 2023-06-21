package com.cleverlance.test.project;

import static org.junit.Assert.assertEquals;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.matchers.Times.exactly;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.model.StringBody.exact;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockserver.client.server.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.Header;
import org.mockserver.verify.VerificationTimes;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestMockServer {

	private static ClientAndServer mockServer;

	@BeforeClass
	public static void startServer() {
		mockServer = startClientAndServer(1080);
	}

	@Test
	public void whenGetAllEmployeesRequestMockServer_thenServerRecieved() {
		new MockServerClient("127.0.0.1", 1080)
				.when(request().withMethod("GET").withPath("/employees")
						.withHeader("\"Content-type\", \"application/json\"").withBody(exact("")), exactly(1))
				.respond(response().withStatusCode(200)
						.withHeaders(new Header("Content-Type", "application/json; charset=utf-8"),
								new Header("Cache-Control", "public, max-age=86400"))
						.withBody("{id: '1', name: 'Josef', surname: 'Novak', email: 'josefNovak@gmail.com'},"
								+ "{id: '2', name: 'Karel', surname: 'Gott', email: 'karel@gmail.com'},")
						.withDelay(TimeUnit.SECONDS, 1));

		org.apache.http.HttpResponse response = hitTheServerWithGetRequest("employees");

		assertEquals(200, response.getStatusLine().getStatusCode());
		new MockServerClient("localhost", 1080).verify(request().withMethod("GET").withPath("/employees"),
				VerificationTimes.exactly(1));
	}

	@Test
	public void whenGetEmployeeByIDRequestMockServer_thenServerRecieved() {
		new MockServerClient("127.0.0.1", 1080)
				.when(request().withMethod("GET").withPath("/employees/1")
						.withHeader("\"Content-type\", \"application/json\"").withBody(exact("")), exactly(1))
				.respond(response().withStatusCode(200)
						.withHeaders(new Header("Content-Type", "application/json; charset=utf-8"),
								new Header("Cache-Control", "public, max-age=86400"))
						.withBody("{id: '1', name: 'Josef', surname: 'Novak', email: 'josefNovak@gmail.com'}")
						.withDelay(TimeUnit.SECONDS, 1));
		org.apache.http.HttpResponse response = hitTheServerWithGetRequest("employees/1");

		assertEquals(200, response.getStatusLine().getStatusCode());
		new MockServerClient("localhost", 1080).verify(request().withMethod("GET").withPath("/employees/1"),
				VerificationTimes.exactly(1));
	}

	@Test
	public void whenAddEmployeeRequestMockServer_thenServerRecieved() {
		// expectations
		new MockServerClient("127.0.0.1", 1080).when(
				request().withMethod("POST").withPath("/employees").withHeader("\"Content-type\", \"application/json\"")
						.withBody(exact("{id: '1', name: 'Josef', surname: 'Novak', email: 'josefNovak@gmail.com'}")),
				exactly(1))
				.respond(response().withStatusCode(200)
						.withHeaders(new Header("Content-Type", "application/json; charset=utf-8"),
								new Header("Cache-Control", "public, max-age=86400"))
						.withBody("{id: '1', name: 'Josef', surname: 'Novak', email: 'josefNovak@gmail.com'}")
						.withDelay(TimeUnit.SECONDS, 1));

		// hit the server
		org.apache.http.HttpResponse response = hitTheServerWithPostRequest();
		// verify
		assertEquals(200, response.getStatusLine().getStatusCode());
		new MockServerClient("localhost", 1080).verify(
				request().withMethod("POST").withPath("/employees")
						.withBody(exact("{id: '1', name: 'Josef', surname: 'Novak', email: 'josefNovak@gmail.com'}")),
				VerificationTimes.exactly(1));
	}

	private org.apache.http.HttpResponse hitTheServerWithPostRequest() {
		String url = "http://127.0.0.1:1080/employees";
		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(url);
		post.setHeader("Content-type", "application/json");
		org.apache.http.HttpResponse response = null;

		try {
			StringEntity stringEntity = new StringEntity(
					"{id: '1', name: 'Josef', surname: 'Novak', email: 'josefNovak@gmail.com'}");
			post.getRequestLine();
			post.setEntity(stringEntity);
			response = client.execute(post);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return response;
	}

	private org.apache.http.HttpResponse hitTheServerWithGetRequest(String page) {
		String url = "http://127.0.0.1:1080/" + page;
		HttpClient client = HttpClientBuilder.create().build();
		org.apache.http.HttpResponse response = null;
		HttpGet get = new HttpGet(url);
		try {
			response = client.execute(get);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return response;
	}

	@AfterClass
	public static void stopServer() {
		mockServer.stop();
	}
}

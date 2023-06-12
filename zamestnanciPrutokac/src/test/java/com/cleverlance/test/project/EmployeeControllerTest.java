//package com.cleverlance.test.project;
//
//import org.junit.jupiter.api.AfterAll;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockserver.client.MockServerClient;
//import org.mockserver.model.HttpRequest;
//import org.mockserver.model.HttpResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.web.server.LocalServerPort;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.test.context.TestPropertySource;
//import org.springframework.web.client.RestTemplate;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockserver.model.HttpResponse.response;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@TestPropertySource(properties = {"mockserver.port=32778"})
//public class EmployeeControllerTest {
//
//    @LocalServerPort
//    private int serverPort = 8080;
//
//    @Value("${mockserver.port}")
//    private static int mockServerPort;
//
//    private static MockServerClient mockServerClient;
//
//    @Autowired
//    private RestTemplate restTemplate;
//
//    @BeforeAll
//    public static void startMockServer() {
//        mockServerClient = new MockServerClient("localhost", mockServerPort);
//    }
//
//	@AfterAll
//    public static void stopMockServer() {
//        mockServerClient.stop();
//    }
//
//    @Test
//    public void testGetAllEmployees() {
//        // Nastavení očekávaného požadavku
//        HttpRequest request = HttpRequest.request()
//                .withMethod("GET")
//                .withPath("/employees");
//
//        // Nastavení odpovědi
//        HttpResponse response = response()
//                .withStatusCode(200)
//                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
//                .withBody("[{\"id\": 1, \"name\": \"John Doe\", \"email\": \"john.doe@example.com\", \"dateBirth\": \"1990-01-01\"}]");
//
//        // Mapování očekávaného požadavku na odpověď
//        mockServerClient.when(request).respond(response);
//
//        // Provedení volání na Spring Boot aplikaci
//        String url = "http://localhost:" + serverPort + "/employees";
//        ResponseEntity<String> apiResponse = restTemplate.getForEntity(url, String.class);
//
//        // Ověření odpovědi
//        assertEquals(HttpStatus.OK, apiResponse.getStatusCode());
//        assertEquals("[{\"id\": 1, \"name\": \"John Doe\", \"email\": \"john.doe@example.com\", \"dateBirth\": \"1990-01-01\"}]", apiResponse.getBody());
//
//        // Ověření volání na MockServer
//        mockServerClient.verify(request);
//    }
//
//    @Test
//    public void testAddEmployee() {
//        // Nastavení očekávaného požadavku
//        HttpRequest request = HttpRequest.request()
//                .withMethod("POST")
//                .withPath("/employees")
//                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
//                .withBody("{\"name\": \"John Doe\", \"email\": \"john.doe@example.com\", \"dateBirth\": \"1990-01-01\"}");
//
//        // Nastavení odpovědi
//        HttpResponse response = response()
//                .withStatusCode(200)
//                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
//                .withBody("{\"id\": 1, \"name\": \"John Doe\", \"email\": \"john.doe@example.com\", \"dateBirth\": \"1990-01-01\"}");
//
//        // Mapování očekávaného požadavku na odpověď
//        mockServerClient.when(request).respond(response);
//
//        // Provedení volání na Spring Boot aplikaci
//        String url = "http://localhost:" + serverPort + "/employees";
//        ResponseEntity<String> apiResponse = restTemplate.postForEntity(url, "{\"name\": \"John Doe\", \"email\": \"john.doe@example.com\", \"dateBirth\": \"1990-01-01\"}", String.class);
//
//        // Ověření odpovědi
//        assertEquals(HttpStatus.OK, apiResponse.getStatusCode());
//        assertEquals("{\"id\": 1, \"name\": \"John Doe\", \"email\": \"john.doe@example.com\", \"dateBirth\": \"1990-01-01\"}", apiResponse.getBody());
//
//        // Ověření volání na MockServer
//        mockServerClient.verify(request);
//    }
//
//    @Test
//    public void testGetEmployeeById() {
//        // Nastavení očekávaného požadavku
//        HttpRequest request = HttpRequest.request()
//                .withMethod("GET")
//                .withPath("/employees/1");
//
//        // Nastavení odpovědi
//        HttpResponse response = response()
//                .withStatusCode(200)
//                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
//                .withBody("{\"id\": 1, \"name\": \"John Doe\", \"email\": \"john.doe@example.com\", \"dateBirth\": \"1990-01-01\"}");
//
//        // Mapování očekávaného požadavku na odpověď
//        mockServerClient.when(request).respond(response);
//
//        // Provedení volání na Spring Boot aplikaci
//        String url = "http://localhost:" + serverPort + "/employees/1";
//        ResponseEntity<String> apiResponse = restTemplate.getForEntity(url, String.class);
//
//        // Ověření odpovědi
//        assertEquals(HttpStatus.OK, apiResponse.getStatusCode());
//        assertEquals("{\"id\": 1, \"name\": \"John Doe\", \"email\": \"john.doe@example.com\", \"dateBirth\": \"1990-01-01\"}", apiResponse.getBody());
//
//        // Ověření volání na MockServer
//        mockServerClient.verify(request);
//    }
//
//    @Test
//    public void testUpdateEmployee() {
//        // Nastavení očekávaného požadavku
//        HttpRequest request = HttpRequest.request()
//                .withMethod("PUT")
//                .withPath("/employees/1")
//                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
//                .withBody("{\"id\": 1, \"name\": \"John Doe\", \"email\": \"john.doe@example.com\", \"dateBirth\": \"1990-01-01\"}");
//
//        // Nastavení odpovědi
//        HttpResponse response = response()
//                .withStatusCode(200)
//                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
//                .withBody("{\"id\": 1, \"name\": \"John Doe\", \"email\": \"john.doe@example.com\", \"dateBirth\": \"1990-01-01\"}");
//
//        // Mapování očekávaného požadavku na odpověď
//        mockServerClient.when(request).respond(response);
//
//        // Provedení volání na  Spring Boot aplikaci
//        String url = "http://localhost:" + serverPort + "/employees/1";
//        ResponseEntity<String> apiResponse = restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>("{\"id\": 1, \"name\": \"John Doe\", \"email\": \"john.doe@example.com\", \"dateBirth\": \"1990-01-01\"}"), String.class);
//
//        // Ověření odpovědi
//        assertEquals(HttpStatus.OK, apiResponse.getStatusCode());
//        assertEquals("{\"id\": 1, \"name\": \"John Doe\", \"email\": \"john.doe@example.com\", \"dateBirth\": \"1990-01-01\"}", apiResponse.getBody());
//
//        // Ověření volání na MockServer
//        mockServerClient.verify(request);
//    }
//
//    @Test
//    public void testDeleteUnknownEmployee() {
//        // Nastavení očekávaného požadavku
//        HttpRequest request = HttpRequest.request()
//                .withMethod("DELETE")
//                .withPath("/employees/111");
//
//        // Nastavení odpovědi
//        HttpResponse response = response()
//                .withStatusCode(404)
//                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
//                .withBody("{\"message\": \"Zaměstnanec nenalezen\"}");
//
//        // Mapování očekávaného požadavku na odpověď
//        mockServerClient.when(request).respond(response);
//
//        // Provedení volání na Spring Boot aplikaci
//        String url = "http://localhost:" + serverPort + "/employees/111";
//        ResponseEntity<String> apiResponse = restTemplate.exchange(url, HttpMethod.DELETE, null, String.class);
//
//        // Ověření odpovědi
//        assertEquals(HttpStatus.NOT_FOUND, apiResponse.getStatusCode());
//        assertEquals("{\"message\": \"Zaměstnanec nenalezen\"}", apiResponse.getBody());
//
//        // Ověření volání na MockServer
//        mockServerClient.verify(request);
//    }
//    
//    @Test
//    public void testDeleteEmployee() {
//        // Nastavení očekávaného požadavku
//        HttpRequest request = HttpRequest.request()
//                .withMethod("DELETE")
//                .withPath("/employees/111");
//
//        // Nastavení odpovědi
//        HttpResponse response = response()
//                .withStatusCode(404)
//                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
//                .withBody("{\"message\": \"Zaměstnanec nenalezen\"}");
//
//        // Mapování očekávaného požadavku na odpověď
//        mockServerClient.when(request).respond(response);
//
//        // Provedení volání na Spring Boot aplikaci
//        String url = "http://localhost:" + serverPort + "/employees/111";
//        ResponseEntity<String> apiResponse = restTemplate.exchange(url, HttpMethod.DELETE, null, String.class);
//
//        // Ověření odpovědi
//        assertEquals(HttpStatus.NOT_FOUND, apiResponse.getStatusCode());
//        assertEquals("{\"message\": \"Zaměstnanec nenalezen\"}", apiResponse.getBody());
//
//        // Ověření volání na MockServer
//        mockServerClient.verify(request);
//    }
//
//    
////    @Test
////    public void testDeleteEmployee() {
////        // Nastavení očekávaného požadavku
////        HttpRequest request = HttpRequest.request()
////                .withMethod("DELETE")
////                .withPath("/employees/1");
////
////        // Nastavení odpovědi
////        HttpResponse response = response()
////                .withStatusCode(200)
////                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
////                .withBody("{\"id\": 1, \"name\": \"John Doe\", \"email\": \"john.doe@example.com\", \"dateBirth\": \"1990-01-01\"}");
////
////        // Mapování očekávaného požadavku na odpověď
////        mockServerClient.when(request).respond(response);
////
////        // Provedení volání na  Spring Boot aplikaci
////        String url = "http://localhost:" + serverPort + "/employees/1";
////        ResponseEntity<String> apiResponse = restTemplate.exchange(url, HttpMethod.DELETE, null, String.class);
////
////        // Ověření odpovědi
////        assertEquals(HttpStatus.OK, apiResponse.getStatusCode());
////        assertEquals("{\"id\": 1, \"name\": \"John Doe\", \"email\": \"john.doe@example.com\", \"dateBirth\": \"1990-01-01\"}", apiResponse.getBody());
////
////        // Ověření volání na MockServer
////        mockServerClient.verify(request);
////    }
//}

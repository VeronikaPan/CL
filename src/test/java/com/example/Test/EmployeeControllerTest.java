package com.example.Test;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static ClientAndServer mockServer;

    @BeforeAll
    public static void setupMockServer() {
        mockServer = ClientAndServer.startClientAndServer(8080);

        mockServer.when(request().withMethod("GET").withPath("/employees"))
                .respond(response().withStatusCode(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("[]"));

        mockServer.when(request().withMethod("GET").withPath("/employees/123"))
                .respond(response().withStatusCode(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"id\": 123, \"name\": \"Adam\"}"));

        mockServer.when(request().withMethod("POST").withPath("/"))
                .respond(response().withStatusCode(201)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"id\": 456, \"name\": \"John\"}"));

        mockServer.when(request().withMethod("PUT").withPath("/456"))
                .respond(response().withStatusCode(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"id\": 456, \"name\": \"Updated John\"}"));

        mockServer.when(request().withMethod("DELETE").withPath("/456"))
                .respond(response().withStatusCode(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"id\": 456, \"name\": \"Deleted John\"}"));
    }

    @AfterAll
    public static void stopMockServer() {
        mockServer.stop();
    }

    @Test
    public void testGetAllEmployees() throws Exception {
        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    public void testGetEmployeeById() throws Exception {
        mockMvc.perform(get("/employees/123"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", equalTo(123)))
                .andExpect(jsonPath("$.name", equalTo("Adam")));
    }

    @Test
    public void testAddEmployee() throws Exception {
        Employee employee = new Employee();
        employee.setName("John");

        mockMvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"John\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", equalTo(456)))
                .andExpect(jsonPath("$.name", equalTo("John")));
    }

    @Test
    public void testUpdateEmployee() throws Exception {
        Employee employee = new Employee();
        employee.setName("Updated John");

        mockMvc.perform(put("/456")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"Updated John\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", equalTo(456)))
                .andExpect(jsonPath("$.name", equalTo("Updated John")));
    }

    @Test
    public void testDeleteEmployee() throws Exception {
        mockMvc.perform(delete("/456"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", equalTo(456)))
                .andExpect(jsonPath("$.name", equalTo("Deleted John")));
    }
}

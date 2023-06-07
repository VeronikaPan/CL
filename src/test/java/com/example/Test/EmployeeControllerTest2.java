//import static org.junit.Assert.assertNotNull;
//
//import org.junit.Test;
//import org.mockserver.client.MockServerClient;
//import org.mockserver.logging.MockServerLogger;
//import org.mockserver.model.Format;
//import org.mockserver.serialization.ExpectationSerializer;
//import org.mockserver.springtest.MockServerTest;
//import org.springframework.beans.factory.annotation.Value;
//
////package com.example.Test;
////
//////import lombok.extern.slf4j.Slf4j;
////import org.junit.jupiter.api.Test;
////import org.junit.jupiter.api.extension.ExtendWith;
////import org.mockserver.client.MockServerClient;
////import org.mockserver.springtest.MockServerTest;
////import org.springframework.beans.factory.annotation.Value;
////import org.springframework.test.context.junit.jupiter.SpringExtension;
////import space.gavinklfong.insurance.quotation.models.Customer;
////
////import java.util.Collections;
////import java.util.List;
////import java.util.Optional;
////
////import static org.junit.jupiter.api.Assertions.assertNotNull;
////import static org.junit.jupiter.api.Assertions.assertTrue;
////import static org.mockserver.mock.OpenAPIExpectation.openAPIExpectation;
////import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
////
//@MockServerTest("server.url=http://localhost:${mockServerPort}")
//@ExtendWith(SpringExtension.class)
//public class EmployeeControllerTest2 {
//
//    @Value("${server.url}")
//    private String serverUrl;
//
//    private MockServerClient mockServerClient;
//
//    @Test
//    void givenRecordExists_getCustomer() {
//
//        // Setup request matcher and response using OpenAPI definition
//        Map<String, String> operationIdStatusMap = new HashMap<>();
//        operationIdStatusMap.put("getCustomerById", "200");
//        mockServerClient
//                .upsert(
//                        openAPIExpectation("employeesApi.yaml")
//                                .withOperationsAndResponses(operationIdStatusMap)
//                );
//        mockServerClient.retrieveActiveExpectations(request().withPath(calculatePath("some_path.*")), Format.JSON),
//        is(new ExpectationSerializer(new MockServerLogger()).serialize(Arrays.asList(
//          new Expectation(request().withPath(calculatePath("some_path.*")), exactly(4), TimeToLive.unlimited())
//
//        // Initialize API client and trigger request
//        CustomerSrvClient customerSrvClient = new CustomerSrvClient(serverUrl);
//        Optional<Employee> customerOptional = customerSrvClient.getCustomer(1l);
//
//        // Assert response
//        assertTrue(customerOptional.isPresent());
//        Employee employee = customerOptional.get();
//        assertNotNull(employee.getId());
//        assertNotNull(employee.getName());
//    }
//}
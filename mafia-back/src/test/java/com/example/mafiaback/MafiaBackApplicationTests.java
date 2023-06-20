package com.example.mafiaback;

import com.example.mafiaback.point.AddressDto;
import com.example.mafiaback.point.PointDto;
import com.example.mafiaback.security.AuthRequest;
import com.example.mafiaback.security.AuthenticationResponse;
import com.example.mafiaback.task.TaskDto;
import com.example.mafiaback.task.TaskStatus;
import com.example.mafiaback.task.TaskType;
import com.example.mafiaback.user.UserDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MafiaBackApplicationTests {
  @Autowired
  private ObjectMapper objectMapper;
  private String token;

  @Container
  static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:15.2")
      .withDatabaseName("bruh")
      .withUsername("bruh")
      .withPassword("bruh");
  
  static {
    postgreSQLContainer.start();
  }
  
  @Autowired
  private MockMvc mockMvc;

  @DynamicPropertySource
  static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
    dynamicPropertyRegistry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
    dynamicPropertyRegistry.add("spring.datasource.username", postgreSQLContainer::getUsername);
    dynamicPropertyRegistry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    dynamicPropertyRegistry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
    dynamicPropertyRegistry.add("spring.jpa.database", () -> "postgresql");
    dynamicPropertyRegistry.add("spring.jpa.database-platform", () -> "org.hibernate.dialect.PostgreSQLDialect");
  }
  
  @Test
  @BeforeAll
  void testAuth() throws Exception {
    MockHttpServletResponse result = mockMvc.perform(
        MockMvcRequestBuilders.post("/api/auth/authenticate")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(new AuthRequest("manager@gmail.com", "manager")))
    ).andReturn().getResponse();
    
    //todo status
    assertEquals(200, result.getStatus());

    AuthenticationResponse resp = objectMapper.readValue(result.getContentAsString(), AuthenticationResponse.class);
    
    //todo resp
    assertNotNull(resp);
    // todo not null
    assertNotNull(resp.getToken());
    token = "Bearer " + resp.getToken();
  }
  
  @Test
  void testTask() throws Exception {
    // Guard Creation
    UserDto dummyGuard = createDummyGuard("jacek.madry@gmail.com", "333444555");
    MockHttpServletResponse createdGuardRes = postGuard(dummyGuard);

    // todo status
    assertEquals(201, createdGuardRes.getStatus());

    Integer guardId = objectMapper.readValue(createdGuardRes.getContentAsString(), Integer.class);

    // Point Creation
    PointDto dummyPoint = createDummyPoint("jan.kowalski@gmail.com", "222333444");
    MockHttpServletResponse createdPointRes = postPoint(dummyPoint);

    // todo status
    assertEquals(200, createdPointRes.getStatus());

    Integer pointId = objectMapper.readValue(createdPointRes.getContentAsString(), Integer.class);

    TaskDto dummyTask = TaskDto.builder()
        .name("Test task 1")
        .description("Someone tries armed robbery on gallery. You need to go there and guard workers, take guns with you")
        .taskStatus(TaskStatus.NEW)
        .taskType(TaskType.CALL)
        .guard(UserDto.builder().id(guardId).build())
        .point(PointDto.builder().id(pointId).build())
        .build();
    
    // create task
    MockHttpServletResponse createdTaskRes = mockMvc.perform(
        MockMvcRequestBuilders.post("/api/task")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dummyTask))
            .header("Authorization", token)
    ).andReturn().getResponse();

    // todo status
    assertEquals(200, createdTaskRes.getStatus());

    Integer taskId = objectMapper.readValue(createdTaskRes.getContentAsString(), Integer.class);

    // Get Created Task By Id and check
    MockHttpServletResponse getTaskByIdRes = getTask(taskId);

    // todo status
    assertEquals(200, getTaskByIdRes.getStatus());
    TaskDto taskDto = objectMapper.readValue(getTaskByIdRes.getContentAsString(), TaskDto.class);
    
    assertEquals(dummyTask.getName(), taskDto.getName());
    assertEquals(dummyTask.getDescription(), taskDto.getDescription());
    assertEquals(dummyTask.getTaskStatus(), taskDto.getTaskStatus());
    assertEquals(dummyTask.getTaskType(), taskDto.getTaskType());
    assertEquals(dummyTask.getGuard().getId(), taskDto.getGuard().getId());
    assertEquals(dummyTask.getPoint().getId(), taskDto.getPoint().getId());

    // Get List of Tasks and check if created exists
    MockHttpServletResponse getListOfTask = mockMvc.perform(
        MockMvcRequestBuilders.get("/api/task")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", token)
    ).andReturn().getResponse();
    // todo status
    assertEquals(200, getListOfTask.getStatus());

    List<TaskDto> tasks = objectMapper.readValue(getListOfTask.getContentAsString(), new TypeReference<List<TaskDto>>() {});

    // todo value
    assertTrue(tasks.size() > 0);
    // todo value
    assertEquals(1, tasks.stream().filter(p -> p.getId().equals(taskDto.getId())).count());

    // delete task
    MockHttpServletResponse deleteTaskRes = mockMvc.perform(
        MockMvcRequestBuilders.delete("/api/task/" + taskId)
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", token)
    ).andReturn().getResponse();

    // todo status
    assertEquals(200, deleteTaskRes.getStatus());

    // check id deleted task doesn't exist
    MockHttpServletResponse getDeletedTaskRes = getTask(taskId);

    //todo status 404
    assertEquals(404, getDeletedTaskRes.getStatus());
  }

  private MockHttpServletResponse getTask(Integer id) throws Exception {
    return mockMvc.perform(
        MockMvcRequestBuilders.get("/api/task/" + id)
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", token)
    ).andReturn().getResponse();
  }
  
  @Test
  void testGuard() throws Exception {
    // Guard Creation
    UserDto dummyGuard = createDummyGuard("jacek.madry@gmail.com", "333444555");
    MockHttpServletResponse createdGuardRes = postGuard(dummyGuard);

    // todo status
    assertEquals(201, createdGuardRes.getStatus());

    Integer id = objectMapper.readValue(createdGuardRes.getContentAsString(), Integer.class);

    // Get Created Guard By Id and check
    MockHttpServletResponse getGuardByIdRes = getGuard(id);

    // todo status
    assertEquals(200, getGuardByIdRes.getStatus());
    UserDto guardDto = objectMapper.readValue(getGuardByIdRes.getContentAsString(), UserDto.class);

    // todo values
    assertEquals(dummyGuard.getFirstName(), guardDto.getFirstName());
    assertEquals(dummyGuard.getLastName(), guardDto.getLastName());
    assertEquals(dummyGuard.getEmail(), guardDto.getEmail());
    assertEquals(dummyGuard.getPhone(), guardDto.getPhone());

    // Get List of Guards and check if created exists
    MockHttpServletResponse getListOfGuard = mockMvc.perform(
        MockMvcRequestBuilders.get("/api/guard")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", token)
    ).andReturn().getResponse();
    // todo status
    assertEquals(200, getListOfGuard.getStatus());

    List<UserDto> guards = objectMapper.readValue(getListOfGuard.getContentAsString(), new TypeReference<List<UserDto>>() {});

    // todo value
    assertTrue(guards.size() > 0);
    // todo value
    assertEquals(1, guards.stream().filter(p -> p.getId().equals(guardDto.getId())).count());

    // delete guard
    MockHttpServletResponse deleteGuardRes = mockMvc.perform(
        MockMvcRequestBuilders.delete("/api/guard/" + id)
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", token)
    ).andReturn().getResponse();

    // todo status
    assertEquals(200, deleteGuardRes.getStatus());

    // check id deleted guard doesn't exist
    MockHttpServletResponse getDeletedGuardRes = getGuard(id);

    //todo status 404
    assertEquals(404, getDeletedGuardRes.getStatus());
  }

  private MockHttpServletResponse getGuard(Integer id) throws Exception {
    return mockMvc.perform(
        MockMvcRequestBuilders.get("/api/user/" + id)
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", token)
    ).andReturn().getResponse();
  }

  private MockHttpServletResponse postGuard(UserDto userDto) throws Exception {
    return mockMvc.perform(
        MockMvcRequestBuilders.post("/api/guard")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userDto))
            .header("Authorization", token)
    ).andReturn().getResponse();
  }

  private UserDto createDummyGuard(String email, String phone) {
    return UserDto.builder()
        .firstName("Jacek")
        .lastName("Madry")
        .email(email)
        .phone(phone)
        .build();
  }
  
  @Test
  void testPoint() throws Exception {
    // Point Creation
    PointDto dummyPoint = createDummyPoint("jan.kowalski@gmail.com", "222333444");
    MockHttpServletResponse createdPointRes = postPoint(dummyPoint);
    
    // todo status
    assertEquals(200, createdPointRes.getStatus());
    
    Integer id = objectMapper.readValue(createdPointRes.getContentAsString(), Integer.class);
    
    // Get Created Point By Id and check
    MockHttpServletResponse getPointByIdRes = getPoint(id);
    
    // todo status
    assertEquals(200, getPointByIdRes.getStatus());
    PointDto pointDto = objectMapper.readValue(getPointByIdRes.getContentAsString(), PointDto.class);

    // todo values
    assertEquals(dummyPoint.getName(), pointDto.getName());
    assertEquals(dummyPoint.getPrice(), pointDto.getPrice());
    assertEquals(dummyPoint.getClient().getFirstName(), pointDto.getClient().getFirstName());
    assertEquals(dummyPoint.getClient().getLastName(), pointDto.getClient().getLastName());
    assertEquals(dummyPoint.getClient().getEmail(), pointDto.getClient().getEmail());
    assertEquals(dummyPoint.getClient().getPhone(), pointDto.getClient().getPhone());
    assertEquals(dummyPoint.getAddress().getCity(), pointDto.getAddress().getCity());
    assertEquals(dummyPoint.getAddress().getDistrict(), pointDto.getAddress().getDistrict());
    assertEquals(dummyPoint.getAddress().getStreet(), pointDto.getAddress().getStreet());
    assertEquals(dummyPoint.getAddress().getHouseNumber(), pointDto.getAddress().getHouseNumber());
    
    // Get List of Points and check if created exists
    MockHttpServletResponse getListOfPoints = mockMvc.perform(
        MockMvcRequestBuilders.get("/api/point")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", token)
    ).andReturn().getResponse();
    // todo status
    assertEquals(200, getListOfPoints.getStatus());
    
    List<PointDto> points = objectMapper.readValue(getListOfPoints.getContentAsString(), new TypeReference<List<PointDto>>() {});

    // todo value
    assertTrue(points.size() > 0);
    // todo value
    assertEquals(1, points.stream().filter(p -> p.getId().equals(pointDto.getId())).count());
    
    // delete point
    MockHttpServletResponse deletePointRes = mockMvc.perform(
        MockMvcRequestBuilders.delete("/api/point/" + id)
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", token)
    ).andReturn().getResponse();

    // todo status
    assertEquals(200, deletePointRes.getStatus());
    
    // check id deleted point doesn't exist
    MockHttpServletResponse getDeletedPointRes = getPoint(id);
    
    //todo status 404
    assertEquals(404, getDeletedPointRes.getStatus());
  }
  
  private MockHttpServletResponse getPoint(Integer id) throws Exception {
    return mockMvc.perform(
        MockMvcRequestBuilders.get("/api/point/" + id)
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", token)
    ).andReturn().getResponse();
  }
  
  private MockHttpServletResponse postPoint(PointDto pointDto) throws Exception {
    return mockMvc.perform(
        MockMvcRequestBuilders.post("/api/point")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(pointDto))
            .header("Authorization", token)
    ).andReturn().getResponse();
  }
  
  private PointDto createDummyPoint(String email, String phone) {
    return PointDto.builder()
        .name("Test Point 1")
        .price(5000)
        .client(
            UserDto.builder()
                .firstName("Jan")
                .lastName("Kowalski")
                .email(email)
                .phone(phone).build()
        )
        .address(
            AddressDto.builder()
                .city("Lublin")
                .district("Dziesiata")
                .street("Jana")
                .houseNumber("2b")
                .build()
        ).build();
  }
}

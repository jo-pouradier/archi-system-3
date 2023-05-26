import com.sp.controller.RestUserCtr;
import com.sp.model.User;
import com.sp.repository.UserRepository;
import com.sp.service.UserService;
import fr.dtos.common.user.UserDTO;
import fr.dtos.common.user.UserRegisterDTO;
import javafx.application.Application;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebMvcTest(RestUserCtr.class)
@AutoConfigureMockMvc
public class TestUserControler {

    @Autowired
    private MockMvc mockMvc;
    private UUID userUUID;
    private User user;

    @MockBean
    private UserService userService;

//    @InjectMocks
//    private RestUserCtr restUserCtr;
//    @BeforeEach
//    public void setup() {
//        MockitoAnnotations.initMocks(this);
//    }

    @Test
    public void testGPTGetUser() throws Exception {
        // Créer un UUID fictif pour les besoins du test
        UUID uuid = UUID.randomUUID();

        // Créer un utilisateur fictif
        User user = new User("John", "password123", "john@example.com");
        user.setUUID(uuid);

        // Configurer le comportement du userService pour retourner l'utilisateur fictif
        Mockito.when(userService.getUser(uuid)).thenReturn(user);

        // Effectuer la requête GET /users/{uuid}
        mockMvc.perform(MockMvcRequestBuilders.get("/users/{uuid}", uuid.toString()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("John"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("john@example.com"));

        // Vérifier que la méthode getUser du userService a été appelée avec le bon UUID
        Mockito.verify(userService, Mockito.times(1)).getUser(uuid);
    }
//
//    @DisplayName("JUnit test for createUser method")
//    @Test
//    public void testCreateUser() {
//        UserRegisterDTO userRegisterDTO = new UserRegisterDTO();
//        // Set userRegisterDTO properties
//        userRegisterDTO.setName("Jo");
//        userRegisterDTO.setEmail("t.p@tp");
//        userRegisterDTO.setPassword("1234");
//        // Set user properties
//        User user = userService.createUser(userRegisterDTO);
//
////        when(userService.createUser(userRegisterDTO)).thenReturn(user);
//        System.out.println(userRegisterDTO);
//        System.out.println(user);
//        assertEquals(user.getName(), userRegisterDTO.getName());
//        assertEquals(user.getEmail(), userRegisterDTO.getEmail());
//        assertEquals(user.getPassword(), userRegisterDTO.getPassword());
//
//        ResponseEntity<?> response = restUserCtr.getUser(userRegisterDTO.getUuid().toString());
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        ResponseEntity<UserDTO> responseEntity = response.hasBody() ? (ResponseEntity<UserDTO>) response : null;
//        assertEquals(user.getUUID().toString(), responseEntity.getBody().getUUID().toString());
//
//        userUUID = user.getUUID();
//        this.user = user;
//        // Assert other properties if necessary
//    }
//
//    @Test
//    public void testCredit() {
//        Float amount = 100.0f;
//
////        when(userService.credit(any(UUID.class), anyFloat())).thenReturn(user);
//
//        ResponseEntity<UserDTO> response = restUserCtr.credit(String.valueOf(userUUID), amount);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(user.getUUID().toString(), response.getBody().getUUID().toString());
//        assertEquals(user.getBalance() + amount, response.getBody().getBalance());
//    }
//
//    @Test
//    public void testDebit() {
//        String uuid = "some_uuid";
//        Float amount = 100.0f;
//
//        User user = new User();
//        // Set user properties
//
//        when(userService.debit(any(UUID.class), anyFloat())).thenReturn(true);
//        when(userService.getUser(any(UUID.class))).thenReturn(user);
//
//        ResponseEntity<UserDTO> response = restUserCtr.debit(uuid, amount);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(user.getUUID().toString(), response.getBody().getUUID().toString());
//        // Assert other properties if necessary
//    }
//
//    @Test
//    public void testGetUser() {
//        String uuid = "some_uuid";
//
//        User user = new User();
//        // Set user properties
//
//        when(userService.getUser(any(UUID.class))).thenReturn(user);
//
//        ResponseEntity<?> response = restUserCtr.getUser(uuid);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(user.getUUID().toString(), ((UserDTO) response.getBody()).getUUID().toString());
//        // Assert other properties if necessary
//    }
//
//    @Test
//    public void testGetUserByEmail() {
//        String email = "test@example.com";
//
//        User user = new User();
//        // Set user properties
//
//        when(userService.getUserByEmail(anyString())).thenReturn(user);
//
//        ResponseEntity<?> response = restUserCtr.getUserByEmail(email);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(user.getUUID().toString(), ((UserDTO) response.getBody()).getUUID().toString());
//        // Assert other properties if necessary
//    }
}

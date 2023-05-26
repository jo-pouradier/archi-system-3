import com.sp.model.User;
import com.sp.repository.UserRepository;
import com.sp.service.UserService;
import fr.dtos.common.user.UserRegisterDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TestUserService {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testCreateUser() {
        // Créer un UserRegisterDTO fictif pour les besoins du test
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO();
        userRegisterDTO.setName("John");
        userRegisterDTO.setPassword("password123");
        userRegisterDTO.setEmail("john@example.com");

        // Créer un utilisateur fictif basé sur le DTO
        User user = new User(userRegisterDTO.getName(), userRegisterDTO.getPassword(), userRegisterDTO.getEmail());

        // Configurer le comportement du userRepository pour retourner l'utilisateur fictif
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Appeler la méthode createUser du service
        User createdUser = userService.createUser(userRegisterDTO);

        // Vérifier que l'utilisateur a été correctement ajouté en appelant le save du userRepository
        verify(userRepository, times(1)).save(any(User.class));

        // Vérifier que la méthode createUser retourne l'utilisateur créé
        assertNotNull(createdUser);
        assertNotNull(createdUser.getUUID());
        assertEquals(userRegisterDTO.getName(), createdUser.getName());
        assertEquals(userRegisterDTO.getPassword(), createdUser.getPassword());
        assertEquals(userRegisterDTO.getEmail(), createdUser.getEmail());
    }

    @Test
    void testGetUser() {
        UUID uuid = UUID.randomUUID();

        // Créer un utilisateur fictif
        User user = new User("John", "password123", "john@example.com");
        user.setUUID(uuid);

        // Configurer le comportement du userRepository pour retourner l'utilisateur fictif
        when(userRepository.findById(uuid)).thenReturn(Optional.of(user));

        // Appeler la méthode getUser du service
        User retrievedUser = userService.getUser(uuid);

        // Vérifier que la méthode getUser du userRepository a été appelée avec le bon UUID
        verify(userRepository, times(1)).findById(uuid);

        // Vérifier que la méthode getUser retourne l'utilisateur fictif
        assertNotNull(retrievedUser);
        assertEquals(uuid, retrievedUser.getUUID());
        assertEquals("John", retrievedUser.getName());
        assertEquals("password123", retrievedUser.getPassword());
        assertEquals("john@example.com", retrievedUser.getEmail());
    }

    // Ajoutez d'autres méthodes de test pour les autres fonctionnalités du service

}

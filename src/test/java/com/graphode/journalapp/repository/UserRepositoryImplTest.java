package com.graphode.journalapp.repository;

import com.graphode.journalapp.entity.User;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class UserRepositoryImplTest {

    @MockBean
    private UserRepositoryImpl userRepository; // Mock the repository if needed

    @Disabled
    @Test
    public void testGetAllSA() {
        List<User> users = userRepository.getAllSA();
        assertNotNull(users);
    }
}

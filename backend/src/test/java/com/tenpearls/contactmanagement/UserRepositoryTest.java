
package com.tenpearls.contactmanagement.repository;

import com.tenpearls.contactmanagement.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldSaveAndFindUserByEmail() {

        User user = new User();
        user.setFirstName("Nimra");
        user.setLastName("Sultan");
        user.setEmail("nimra@example.com");
        user.setPassword("hashed-password");

        userRepository.save(user);

        Optional<User> foundUser =
                userRepository.findByEmailIgnoreCase("NIMRA@example.com");

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getFirstName()).isEqualTo("Nimra");
    }

    @Test
    void shouldFindUserByPhone() {

        User user = new User();
        user.setFirstName("Test");
        user.setLastName("User");
        user.setPhone("03001234567");
        user.setPassword("hashed-password");

        userRepository.save(user);

        Optional<User> foundUser =
                userRepository.findByPhone("03001234567");

        assertThat(foundUser).isPresent();
    }

    @Test
    void shouldReturnFalseWhenEmailDoesNotExist() {

        boolean exists =
                userRepository.existsByEmailIgnoreCase("unknown@example.com");

        assertThat(exists).isFalse();
    }
}

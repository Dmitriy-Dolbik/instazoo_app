package com.example.instazoo_app;

import com.example.instazoo_app.models.User;
import com.example.instazoo_app.repositories.UserRepository;
import com.example.instazoo_app.services.UserService;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import java.security.Principal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

class UserServiceTest {
	private static UserService userService;
	private static UserRepository userRepository;

	@BeforeAll
	public static void setup(){
		userRepository = Mockito.mock(UserRepository.class);
		userService = new UserService(userRepository);
	}
	@Test
	void getCurrentUserTest() {
		// Given
		Principal principal = Mockito.mock(Principal.class);
		Mockito.when(principal.getName()).thenReturn("Jack");

		Optional<User> user = Optional.of(new User(2L, "Jack",
				"JackUsername", "Jacklastname"));
		Mockito.when(userRepository.findUserByUsername(any())).thenReturn(user);

		// When
		User currentUser = userService.getCurrentUser(principal);

		// Then
		Assertions.assertEquals(2L, currentUser.getId());
		Assertions.assertEquals("Jack", currentUser.getName());
	}

}

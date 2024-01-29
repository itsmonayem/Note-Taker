package com.spring.notetaker;

import com.spring.notetaker.dao.NoteRepository;
import com.spring.notetaker.dao.UserRepository;
import com.spring.notetaker.entities.Note;
import com.spring.notetaker.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class NoteTakerApplication implements CommandLineRunner {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private NoteRepository noteRepository;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(NoteTakerApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		if(this.userRepository.findAll().isEmpty()) {
			User user = new User();
			user.setName("Monayem");
			user.setEmail("abc@dsi.com");
			user.setPassword(this.passwordEncoder.encode("1234"));
			user.setRole("ROLE_ADMIN");
			this.userRepository.save(user);

			int noteNo = 1;


			for(int i = 1; i <= 50; i++) {
				user = new User();
				user.setName("DSi" + i);
				user.setEmail("abc" + i +"@dsi.com");
				user.setPassword(this.passwordEncoder.encode("1234"));
				user.setRole("ROLE_USER");
				this.userRepository.save(user);

				for (int j = 1; j <= 20; j++) {
					Note note = new Note();
					note.setTitle("Title"+noteNo);
					note.setDescription("Description"+noteNo++);
					note.setUser(user);
					this.noteRepository.save(note);
				}
			}
		}
	}
}

package com.elevato.elevato.features.authentication.configuration;


import com.elevato.elevato.features.authentication.model.AuthenticationUser;
import com.elevato.elevato.features.authentication.repository.AuthenticationUserRepository;
import com.elevato.elevato.features.authentication.utils.Encoder;
import com.elevato.elevato.features.feed.model.Comment;
import com.elevato.elevato.features.feed.model.Post;
import com.elevato.elevato.features.feed.repository.PostRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

@Configuration
public class LoadDataBaseConfiguration {

    public final Encoder encoder;

    public LoadDataBaseConfiguration(Encoder encoder) {
        this.encoder = encoder;
    }


    @Bean
    public CommandLineRunner initDataBase(AuthenticationUserRepository authenticationUserRepository, PostRepository postRepository){
        AuthenticationUser user = new AuthenticationUser("naveen@gmail.com",encoder.encode("abc123"));

        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                List<AuthenticationUser> users = createUsers(authenticationUserRepository);
                createPosts(postRepository, users);
            }
        };
    }

    private List<AuthenticationUser> createUsers(AuthenticationUserRepository authenticationUserRepository) {
        List<AuthenticationUser> users = List.of(
                createUser("naveen@example.com", "abc123", "Naveen", "Gundavarapu", "Vishnu Institute of Technology",  "Bhimavaram, IN",
                        "https://images.unsplash.com/photo-1633332755192-727a05c4013d?q=80&w=3560&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"),
                createUser("kumar@example.com", "abc123", "Kumar", "Sharma", "KL University",  "Vijayawada, IN",
                        "https://t3.ftcdn.net/jpg/06/53/05/06/360_F_653050611_zjw73tRk6GII71af6GKgTD9VLJZ2byWm.jpg"),
                createUser("ram@example.com", "abc123", "Ram", "Kumar", "Vishnu Institute of Technology",  "Bhimavaram, IN",
                        "https://images.unsplash.com/photo-1640960543409-dbe56ccc30e2?q=80&w=2725&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"),
                createUser("john@example.com", "abc123", "John", "Doe", "KL University",  "Vijayawada, IN",
                        "https://images.unsplash.com/photo-1586297135537-94bc9ba060aa?q=80&w=3560&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"),
                createUser("hari@example.com", "abc123", "Hari", "Rao", "Vishnu Institute of Technology",  "Bhimavaram, IN",
                        "https://images.unsplash.com/photo-1640951613773-54706e06851d?q=80&w=2967&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D")
        );

        authenticationUserRepository.saveAll(users);
        return users;
    }

    private AuthenticationUser createUser(String email, String password, String firstName, String lastName,
                                          String college, String location, String profilePicture) {
        AuthenticationUser user = new AuthenticationUser(email, encoder.encode(password));
        user.setEmailVerified(true);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setCollege(college);
        user.setLocation(location);
        user.setProfilePicture(profilePicture);
        return user;
    }

    private void createPosts(PostRepository postRepository, List<AuthenticationUser> users) {
        List<String> posts = List.of("""
Spring Boot vs Spring Framework
Many beginners confuse Spring with Spring Boot.

Spring Framework → A powerful ecosystem for building Java applications.
Spring Boot → A tool that simplifies Spring setup with auto-configuration and starters.

Example:
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

💡 Takeaway:
Spring Boot removes boilerplate configuration and helps you start faster.
#SpringBoot #Java #BackendDevelopment
""",
                """
                What is Dependency Injection?
                Dependency Injection (DI) is one of the core principles of Spring.
                
                Instead of creating objects manually, Spring provides them for you.
                
                Example without Spring:
                UserService service = new UserService();
                
                Example with Spring:
                Spring manages the object lifecycle and injects dependencies automatically.
                
                💡 Benefits:
                • Loose coupling
                • Easier testing
                • Better maintainability
                #SpringFramework #JavaArchitecture
                """,
                """
                Spring Boot Starters Explained
                Spring Boot starters simplify dependency management.
                
                Instead of adding many libraries manually, you add one starter.
                
                Example:
                spring-boot-starter-web
                
                This includes:
                • Spring MVC
                • Jackson
                • Validation
                • Embedded Tomcat
                
                💡 Takeaway:
                Starters reduce dependency conflicts and speed up development.
                #SpringBoot #JavaDevelopers
                """,
                """
                What is Auto Configuration?
                One of the most powerful features of Spring Boot is auto-configuration.
                
                Spring Boot automatically configures beans based on dependencies in your project.
                
                Example:
                If you add spring-boot-starter-web, Spring Boot automatically configures:
                • DispatcherServlet
                • Jackson
                • Embedded Tomcat
                
                💡 You write less configuration and focus on business logic.
                #SpringBoot #JavaBackend
                """,
                """
                Embedded Servers in Spring Boot
                Traditional Java apps required external servers like Tomcat.
                
                Spring Boot changed this.
                
                It comes with embedded servers such as:
                • Tomcat
                • Jetty
                • Undertow
                
                Just run your application:
                mvn spring-boot:run
                
                💡 Your application becomes a standalone executable jar.
                #SpringBoot #BackendEngineering
                """,
                """
                Understanding Spring Bean Lifecycle
                Spring manages the lifecycle of beans.
                
                Main phases:
                1. Bean Instantiation
                2. Dependency Injection
                3. Initialization
                4. Bean Ready for Use
                5. Destruction
                
                You can hook into the lifecycle using:
                @PostConstruct
                @PreDestroy
                
                💡 Understanding lifecycle helps debug many Spring issues.
                #SpringFramework #Java
                """,
                """
                Why Use application.properties?
                Spring Boot uses application.properties or application.yml for configuration.
                
                Example:
                server.port=8081
                spring.application.name=demo-app
                
                Benefits:
                • Environment-based configuration
                • Clean separation from code
                • Easy deployment configuration
                
                💡 Configuration should live outside the codebase.
                #SpringBoot #DevTips
                """,
                """
                Spring Boot Actuator
                Want to monitor your application easily?
                
                Spring Boot Actuator provides production-ready endpoints.
                
                Examples:
                /actuator/health
                /actuator/metrics
                /actuator/info
                
                Add dependency:
                spring-boot-starter-actuator
                
                💡 Actuator helps with monitoring and observability.
                #SpringBoot #DevOps
                """,
                """
                REST APIs with Spring Boot
                Spring Boot makes building REST APIs incredibly simple.
                
                Example:
                @RestController
                @RequestMapping("/users")
                public class UserController {
                
                @GetMapping
                public List<User> getUsers() {
                    return List.of();
                }
                
                }
                
                💡 In just a few lines, you have a REST endpoint ready.
                #SpringBoot #RESTAPI
                """,
                """
                Spring Profiles for Multiple Environments
                Applications often run in multiple environments:
                • Development
                • Testing
                • Production
                
                Spring Profiles allow environment-specific configurations.
                
                Example:
                spring.profiles.active=dev
                
                You can create:
                application-dev.properties
                application-prod.properties
                
                💡 Profiles make deployments flexible and safe.
                #SpringBoot #JavaDevelopment
                """);
        Random random = new Random();
        for (int j = 1; j <= 10; j++) {
            Post post = new Post(posts.get(j-1),
                    users.get(random.nextInt(users.size())));
            post.setLikes(generateLikes(users, j, random));
            if (j == 1) {
                post.setPicture("https://koevoet.biz/wp-content/uploads/2020/11/Spring-BOOT.jpg");
            }
            if(j==4){
                post.setPicture("https://insource.io/images/posts/spring-boot.png");
            }

            List<Comment> comments = generateComments(users,post);
            post.setComments(comments);
            postRepository.save(post);
        }
    }

    private HashSet<AuthenticationUser> generateLikes(List<AuthenticationUser> users, int postNumber, Random random) {
        HashSet<AuthenticationUser> likes = new HashSet<>();

        if (postNumber == 1) {
            while (likes.size() < 3) {
                likes.add(users.get(random.nextInt(users.size())));
            }
        } else {
            int likesCount = switch (postNumber % 5) {
                case 0 -> 3;
                case 2, 3 -> 2;
                default -> 1;
            };
            for (int i = 0; i < likesCount; i++) {
                likes.add(users.get(random.nextInt(users.size())));
            }
        }
        return likes;
    }

    private List<Comment> generateComments(List<AuthenticationUser> usersAuth, Post post){
        List<Integer> users = new ArrayList<>();
        Random random = new Random();
        int n = random.nextInt(usersAuth.size());
        for(int i = 0 ; i<n;i++){
            users.add(random.nextInt(usersAuth.size()));
        }

        List<Comment> comments = new ArrayList<>();

        for(Integer id : users){
            Comment cmt = new Comment(post,usersAuth.get(id),"Hey there!" );
            comments.add(cmt);
        }
        return comments;

    }

}

package demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
// import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
// import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
// import org.springframework.security.core.userdetails.User;
// import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {


    //  TODO-03: Remove the comment from the @Bean definition below.
    //  Uncomment the imports above.
    //  This method will override Spring Boot's default user configuration.

    // @Bean
    // public ReactiveUserDetailsService userDetails() {
    //     UserDetails adminUser =
    //         User.withUsername("admin")
    //             .password(passwordEncoder()
    //             .encode("password"))
    //             .roles("ADMIN").build();

    //     // TODO-04: Using the code above as an example, define two additional users.
    //     // Use whatever values you like for username, password, and role.        


    //     // TODO-05: Add the newly created users to the MapReactiveUserDetailsService.
    //     // (This is the Reactive equivalent of the InMemoryUserDetailsManager):
    //     return new MapReactiveUserDetailsService(adminUser);
    // }   

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    
}

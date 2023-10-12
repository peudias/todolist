package todolist.phpdias.com.github.todolist.Filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import todolist.phpdias.com.github.todolist.user.InterfaceUserRepository;

@Component
public class FilterTaskAuth extends OncePerRequestFilter{

    @Autowired
    private InterfaceUserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {

        // Get Auth (username and password)
        var authorization = request.getHeader("Authorization");

        var authEncoded = authorization.substring("Basic".length()).trim();

        byte[] authDecoded = Base64.getDecoder().decode(authEncoded);

        var authString = new String(authDecoded);

        String[] credentials = authString.split(":");
        String usernameCredential = credentials[0];
        String passwordCredential = credentials[1];

        // Validate username
        var userValidate = this.userRepository.findByUsername(usernameCredential);
        if(userValidate == null){
            response.sendError(401);
        }else{
            // Validate password
            var passwordVerify = BCrypt.verifyer().verify(passwordCredential.toCharArray(), userValidate.getPassword());
            if(passwordVerify.verified){
                filterChain.doFilter(request, response);
            }else {
                response.sendError(401);
            }
        }
    }
}

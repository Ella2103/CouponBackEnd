package jb.CouponsBack.filters;

import com.auth0.jwt.JWT;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Order(2)
public class TokenFilter extends OncePerRequestFilter {


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path=request.getRequestURI();
        return path.contains("/login");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        try {

            String token=request.getHeader("Authorization").replace("Bearer ","");
            String email = JWT.decode(token).getClaim("email").asString();
            String clientType = JWT.decode(token).getClaim("clientType").asString();
            int clientId=JWT.decode(token).getClaim("id").asInt();

            filterChain.doFilter(request,response);


//        }catch (Exception e){
//            response.setStatus(401);
//            response.getWriter().println("Invalid credentials!");
//        }


    }
}

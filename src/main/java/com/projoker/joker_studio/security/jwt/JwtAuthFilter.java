package com.projoker.joker_studio.security.jwt;

import com.projoker.joker_studio.security.user_details.StudioUserDetails;
import com.projoker.joker_studio.security.user_details.StudioUserDetailsService;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter{

    private final StudioUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String authHeader=request.getHeader("Authorization");
            String token=null;
            String username=null;
            if(authHeader!=null && authHeader.startsWith("Bearer ")){
                token=authHeader.substring(7);
                username=jwtUtil.extractUserName(token);
                System.out.println("Token extracted... And User name Extracted: "+username);
            }

            if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null){
                UserDetails userDetails=userDetailsService.loadUserByUsername(username);
                if(jwtUtil.validateToken(username,token,userDetails)){
                    //here after validated add to securitycontext holder
                    UsernamePasswordAuthenticationToken authToken=new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);

                    System.out.println("JWT token is stored on security context...");
                }
                else{
                    System.out.println("Not an valid user...");
                }
            }
            filterChain.doFilter(request,response);

        } catch (JwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(e.getMessage()+" : Invalid or expired token, you may login and try again!");
            return;
        }
        catch (Exception e){
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(e.getMessage());
            return;
        }

    }
}

package com.tapso.jwt;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.tapso.service.CustomUserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
@Component
public class JwtRequestFilter extends OncePerRequestFilter{
	
	private JwtUtil jwtUtil;
	private CustomUserDetailsService customUserDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		                  
		           String      requestHeader=request.getHeader("Authorization");
		           String jwt=null;
		           String username=null;
		           if(requestHeader!=null && requestHeader.startsWith("Bearer ") ){
		        	           jwt=requestHeader.substring(7);
		        	           username=jwtUtil.extractUsername(jwt);         
		           }
		           if(username !=null && SecurityContextHolder.getContext().getAuthentication()==null) {
		        	  UserDetails userDetails=customUserDetailsService.loadUserByUsername(username);
		        	  UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=new UsernamePasswordAuthenticationToken(userDetails, null,userDetails.getAuthorities());
		          usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		          SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
		           }
		           filterChain.doFilter(request, response);
	                 }
	
}

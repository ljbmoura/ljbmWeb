package br.com.ljbm.fp.interceptador;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

import org.apache.logging.log4j.Logger;


@Logged
@Provider
public class ResponseLoggingFilter implements ContainerResponseFilter {
	@Inject
	Logger log;

    @Override
    public void filter( ContainerRequestContext requestContext, 
                       ContainerResponseContext responseContext) throws IOException {

    	log.info(requestContext.getUriInfo().getAbsolutePath());
    	log.info(requestContext.getHeaders().toString());
    	
    	StringBuilder buffer = new StringBuilder();
    	BufferedReader reader = new BufferedReader(new InputStreamReader(requestContext.getEntityStream()));
    	String line;
    	while ((line = reader.readLine()) != null) {
    		buffer.append(line);
    	}	
    	String requestBody = buffer.toString();
    	log.info(String.format("request  payload body\r%s", requestBody));
    	
//    	log.info(responseContext.getLocation());
    	log.info(responseContext.getHeaders().toString());
    	log.info(String.format("response payload body\r%s", responseContext.getEntity()));
    }
}

	
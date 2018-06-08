package br.com.ljbm.fp.interceptador;

import java.io.IOException;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

import org.apache.logging.log4j.Logger;


@ResponseLogged
@Provider
public class ResponseLoggingFilter implements ContainerResponseFilter {
	@Inject
	Logger log;
   
    @Override
    public void filter( ContainerRequestContext requestContext, 
                       ContainerResponseContext responseContext) throws IOException {

		log.info(requestContext.getHeaders().toString());
    	log.info(requestContext.getUriInfo().getPath());
    	log.info(requestContext.getMethod());
    	log.info("location: " + responseContext.getLocation());
    	log.info("Headers :  " + responseContext.getHeaders().toString());
    	log.info("Body    :\n\t"+ responseContext.getEntity());
    }
}

	
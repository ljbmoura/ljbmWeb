package br.com.ljbm.rest;

import java.io.IOException;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

import org.apache.logging.log4j.Logger;
/*
 *  filter executed after the resource was matched
 */
@Provider
public class PostMatchingRequestFilter implements ContainerRequestFilter {
     
	@Inject
	Logger LOG;

    @Override
    public void filter(ContainerRequestContext ctx) throws IOException {
    	if (ctx.hasEntity()) { 
    		LOG.debug(FiltersHelper.formataBody(FiltersHelper.getBody(ctx, LOG)));
    	}
//    	LOG.debug(ctx.getLanguage());
//        if (ctx.getLanguage() == null || 
//        		"EN".equals(ctx.getLanguage().getLanguage())) {
//            ctx.abortWith(Response.status(Response.Status.FORBIDDEN)
//              .entity("Cannot access")
//              .build());
//        }
    }

}
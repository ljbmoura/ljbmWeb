package br.com.ljbm.rest;
import java.io.IOException;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.ext.Provider;

import org.apache.logging.log4j.Logger;

/*
 *  filter executed before the resource matching
 */
@Provider
@PreMatching
public class PreMatchingRequestFilter implements ContainerRequestFilter {
	@Inject
	Logger LOG;

    @Override
    public void filter(ContainerRequestContext ctxRequest) throws IOException {
    	LOG.debug(ctxRequest.getMethod() + " " + ctxRequest.getUriInfo().getAbsolutePath());
    	LOG.debug(FiltersHelper.formataHeaders(ctxRequest.getHeaders().toString()));
    	LOG.debug("QueryParameters " + ctxRequest.getUriInfo().getQueryParameters().toString());
    }
}
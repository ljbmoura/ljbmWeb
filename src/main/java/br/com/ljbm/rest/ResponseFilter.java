package br.com.ljbm.rest;

import java.io.IOException;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

import org.apache.logging.log4j.Logger;

@Provider
public class ResponseFilter implements ContainerResponseFilter {

	@Inject
	Logger LOG;

	@Override
	public void filter(ContainerRequestContext ctxRequest, ContainerResponseContext ctxResponse)
			throws IOException {
		LOG.debug(FiltersHelper.formataHeaders(ctxResponse.getHeaders().toString()));
		if (ctxResponse.hasEntity()) {
			LOG.debug(FiltersHelper.formataBody(ctxResponse.getEntity()));
		}
	}
}
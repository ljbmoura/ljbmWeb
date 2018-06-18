package br.com.ljbm.fp.rest;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.Logger;


public class MetricasFilter implements Filter {

    @Inject private Logger log;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		long t0 = System.currentTimeMillis();
		HttpServletRequest httpReq = (HttpServletRequest)req;
		chain.doFilter(req, res);
		long t1 = System.currentTimeMillis();
		log.info("%s %s (%dms)", httpReq.getMethod(), httpReq.getPathInfo() , t1 - t0);
	}

	@Override
	public void destroy() {
	}

}

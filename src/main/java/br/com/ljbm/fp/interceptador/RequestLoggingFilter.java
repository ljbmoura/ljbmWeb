package br.com.ljbm.fp.interceptador;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.message.internal.ReaderWriter;
import org.glassfish.jersey.server.ContainerException;


@RequestLogged
@Provider
public class RequestLoggingFilter implements ContainerRequestFilter {
	@Inject
	Logger log;

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {

		log.info(requestContext.getHeaders().toString());
    	log.info(requestContext.getUriInfo().getPath());
    	log.info(requestContext.getMethod());
    	log.info("body " + getBody(requestContext));
    }
    
    protected String getBody(
            ContainerRequestContext containerRequestContext) {
        String body = null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        InputStream in = containerRequestContext.getEntityStream();

        try {
            if (log.isTraceEnabled()) {
                log.trace("Data Available...");
            }
            ReaderWriter.writeTo(in, out);
            containerRequestContext
                    .setEntityStream(new ByteArrayInputStream(out
                            .toByteArray()));
            body = new String(out.toByteArray());
            if (log.isTraceEnabled()) {
                log.trace("Data Read:\n[" + body + "]");
            }
        } catch (IOException ex) {
            log.error("Error while reading JSON body", ex);
            throw new ContainerException(ex);
        } // end try/catch

//        if (log.isDebugEnabled()) {
//            if (MediaType.APPLICATION_JSON.equals(containerRequestContext
//                    .getHeaderString(HttpHeaders.CONTENT_TYPE))) {
//                log.debug("\n[" + containerRequestContext.getMethod()
//                        + "] : "
//                        + containerRequestContext.getUriInfo().getPath()
//                        + "\n" + body);
//            } else {
//                log.debug("\n[" + containerRequestContext.getMethod()
//                        + "] : "
//                        + containerRequestContext.getUriInfo().getPath()
//                        + "\n" + body);
//            } // end if
//        } // end if

        return body;
    }

    
}

	
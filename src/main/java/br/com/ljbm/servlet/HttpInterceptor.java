package br.com.ljbm.servlet;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.Logger;

/*
 * CDI interceptor for the {@linkplain CheckRequest} annotation Created by
 * <a href="mailto:javapapo@mac.com">javapapo</a> on 24/09/15.
 */
@Interceptor
//@IEW
@Priority(100)
public class HttpInterceptor {
	@Inject
	Logger log;

	@Inject
	HttpServletRequest request;
	
//	@Inject
//	ContainerRequestContext requestContext;
	
//	@Inject
//	ContainerResponseContext responseContext;
	
//	@Inject
//	HttpServletResponse response;

//	@Inject
//	HttpSession session;

	@AroundInvoke
	public Object logaRequisicoes(InvocationContext contexto) throws Exception {
		
//		Map<String, Object> data = contexto.getContextData();
//		data.forEach((k, v) -> log.info("data key: " + k + " - value: " + v));
//    	log.info(requestContext.getUriInfo().getAbsolutePath());
//    	log.info(requestContext.getHeaders().toString());
    	
    	StringBuilder buffer = new StringBuilder();
    	BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
    	String line;
    	while ((line = reader.readLine()) != null) {
    		buffer.append(line);
    	}	
    	String requestBody = buffer.toString();
    	log.info(String.format("request  payload body\r%s", requestBody));
    	
//    	log.info(responseContext.getLocation());
//    	log.info(responseContext.getHeaders().toString());
//    	log.info(String.format("response payload body\r%s", responseContext.getEntity()));
    	
		Object retorno = contexto.proceed();
		return retorno;
		
//		Integer idAplicacao = 19;
//		String ip = request.getHeader("X-FORWARDED-FOR");
//
//		if (ip == null) {
//			ip = request.getRemoteAddr();
//		}
//
//		Long idAcesso = null;
//		if (request.getAttribute("idAcesso") != null) {
//			idAcesso = (Long) request.getAttribute("idAcesso");
//		}
//
//		IncluiAcesso inclusaoAcesso = iewRepositorio.geraLogAcesso(ip, idAplicacao, idAcesso);
//
//		if (inclusaoAcesso != null) {
//			request.setAttribute("idAcesso", inclusaoAcesso.getIdAcesso());
//			request.setAttribute("idLogAcesso", inclusaoAcesso.getIdLogAcesso());
//		}
//		return ctx.proceed();
	}
}
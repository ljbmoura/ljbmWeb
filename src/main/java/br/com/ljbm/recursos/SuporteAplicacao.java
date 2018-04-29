package br.com.ljbm.recursos;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Uses CDI to alias Java EE resources to CDI beans, using "resource producers"
 * pattern, allowing for a consistent style throughout our application
 * 
 * <p>
 * Example injection on a managed bean field:
 * </p>
 * 
 * <pre>
 * &#064;Inject
 * private EntityManager em;
 * </pre>
 * 
 */
public class SuporteAplicacao {

	// @Produces
	// public Logger produceLog(InjectionPoint injectionPoint) {
	// return Logger.getLogger(injectionPoint.getMember().getDeclaringClass()
	// .getName());
	// }
	@Produces
	Logger produceLog(InjectionPoint injectionPoint) {
		return LogManager.getFormatterLogger(injectionPoint.getMember().getDeclaringClass().getName());
	}

	@Produces
	@RequestScoped
	public FacesContext produceFacesContext() {
		return FacesContext.getCurrentInstance();
	}

	@Produces
	public Context produceContextoJNDI(InjectionPoint injectionPoint) throws NamingException {
		// TODO: estudar escopo deste recurso
		return new InitialContext();
	}

	// @Produces
	// @ApplicationScoped
	// public FinancasPessoaisDelegate produceFinancasPessoaisDelegate(
	// ) {
	// return FinancasPessoaisDelegate.getInstance();
	// }

}

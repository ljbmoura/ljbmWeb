package br.com.ljbm.recursos;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import javax.naming.Context;
import javax.naming.NamingException;

import br.com.ljbm.fp.servico.AvaliadorInvestimentoRemote;
import br.com.ljbm.fp.servico.FPDominio;
import br.com.ljbm.fp.servico.FPDominioMockImpl;

/**
 * @author luc
 * 
 *         {@linkplain https
 *         ://docs.jboss.org/author/display/AS71/EJB+invocations
 *         +from+a+remote+client+using+JNDI}
 * 
 */
public class EJBFinancasPessoais {
	static final String appName = "ljbmEA";
	static final String moduleName = "ljbmEJB";
	static final String distinctName = "";

	@Inject
	Context contextoJNDI;

//	@Produces
//	public FPDominio produceFPDominio(
//			InjectionPoint injectionPoint) throws NamingException {
//
//		return (FPDominio) lookupServicoRemotoEJBLab(
//				"FPDominioImpl", FPDominio.class);
////		return new FPDominioMockImpl();
//
//	}
//
//	@Produces
//	public AvaliadorInvestimentoRemote produceAvaliadorInvestimento(
//			InjectionPoint injectionPoint) throws NamingException {
//
//		return (AvaliadorInvestimentoRemote) lookupServicoRemotoEJBLab(
//				"AvaliadorInvestimento", AvaliadorInvestimentoRemote.class);
//
//	}

	private Object lookupServicoRemotoEJBLab(String beanName,
			@SuppressWarnings("rawtypes") Class classViewRemota)
			throws NamingException {

		// TODO: estudar  new InitialContext()
		Object o = contextoJNDI.lookup("ejb:" + appName + "/" + moduleName
				+ "/" + distinctName + "/" + beanName + "!" + classViewRemota.getName()); 
		return o;
	}

}

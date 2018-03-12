package br.com.ljbm.recursos;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

public class JNDI {

	public String ListaEJBs() {
		String ret = "vazio";
		try {
			// final Hashtable env = new Hashtable();
			final Properties env = new Properties();

//			env.put(Context.INITIAL_CONTEXT_FACTORY,
//					org.jboss.naming.remote.client.InitialContextFactory.class
//							.getName());
			env.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			env.put(Context.PROVIDER_URL, "remote://localhost:4447");
			env.put(Context.SECURITY_PRINCIPAL, "aplicacao");
			env.put(Context.SECURITY_CREDENTIALS, "dun541vast207");
			env.put(Context.SECURITY_PROTOCOL, "ssl");
			env.put(Context.SECURITY_AUTHENTICATION, "none");

			env.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");

			Context ctx = new InitialContext();
			String raiz = "java:global";
			// String nome = "java:global/EJBLabEAR";
			System.out.println(raiz);

			// showTree(ctx, Integer.MAX_VALUE);
			// showTree(nome, ctx, 0, Integer.MAX_VALUE);
			//
			//
			NamingEnumeration<NameClassPair> nameList = ctx.list(raiz);
			while (nameList.hasMore()) {
				NameClassPair name = (NameClassPair) nameList.next();
				System.out.println("contexto " + raiz + "/" + name.getName());
				showTree(raiz + "/" + name.getName() + "/",
						(Context) ctx.lookup(raiz + "/" + name.getName()), 0,
						Integer.MAX_VALUE);
				// logger.info("{}", name);
			}

			// NamingEnumeration<Binding> lb = ctx.listBindings(nome);
			// while (lb.hasMoreElements()) {
			// Binding b = (Binding) lb.next();
			// System.out.println(b.toString());
			// }

			// java:global/EJBLabEAR/EJBLab/AvaliadorInvestimento!br.com.ljbm.service.AvaliadorInvestimento
			// java:app/EJBLab/AvaliadorInvestimento!br.com.ljbm.service.AvaliadorInvestimento
			// java:module/AvaliadorInvestimento!br.com.ljbm.service.AvaliadorInvestimento
			// java:global/EJBLabEAR/EJBLab/AvaliadorInvestimento!br.com.ljbm.service.AvaliadorInvestimentoLocal
			// java:app/EJBLab/AvaliadorInvestimento!br.com.ljbm.service.AvaliadorInvestimentoLocal
			// java:module/AvaliadorInvestimento!br.com.ljbm.service.AvaliadorInvestimentoLocal
			// java:global/EJBLabEAR/EJBLab/AvaliadorInvestimento!br.com.ljbm.service.AvaliadorInvestimentoRemote
			// java:app/EJBLab/AvaliadorInvestimento!br.com.ljbm.service.AvaliadorInvestimentoRemote
			// java:module/AvaliadorInvestimento!br.com.ljbm.service.AvaliadorInvestimentoRemote
			// java:jboss/exported/EJBLabEAR/EJBLab/AvaliadorInvestimento!br.com.ljbm.service.AvaliadorInvestimentoRemote

		} catch (NamingException e) {
			e.printStackTrace();
			ret = e.getExplanation();
		}
		return ret;
	}
	
	public void showTree(Context ctx) throws NamingException {
		showTree(ctx, Integer.MAX_VALUE);
	}

	public void showTree(Context ctx, int maxDepth)
			throws NamingException {
		System.out.println("----------------------------");
		showTree("java", ctx, 0, maxDepth);
		System.out.println("----------------------------");
	}

	private void showTree(String indent, Context ctx, int depth,
			int maxDepth) throws NamingException {
		if (depth == maxDepth)
			return;
		NamingEnumeration<NameClassPair> rel = ctx.list("");
		while (rel.hasMoreElements()) {
			NameClassPair ncp = (NameClassPair) rel.next();
			System.out.println(indent + ncp);
			if (ncp.getClassName().indexOf("Context") != -1)
				showTree(indent + ncp.getName() + "/",
						(Context) ctx.lookup(ncp.getName()), depth + 1,
						maxDepth);
		}
	}
}

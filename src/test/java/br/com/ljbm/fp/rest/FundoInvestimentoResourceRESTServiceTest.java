package br.com.ljbm.fp.rest;

import static br.com.ljbm.fp.rest.FundoInvestimentoResourceRESTService.FUNDOS_INVESTIMENTO_RESOURCE_BASE;
import static br.com.ljbm.fp.modelo.Corretora.cnpjAgora;
import static br.com.ljbm.fp.modelo.Corretora.cnpjBB;
import static io.restassured.RestAssured.basePath;
import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import br.com.ljbm.fp.modelo.Corretora;
import br.com.ljbm.fp.modelo.FundoInvestimento;
import br.com.ljbm.fp.modelo.TipoFundoInvestimento;

//@formatter:off

//@Ignore
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FundoInvestimentoResourceRESTServiceTest {

	private static Corretora BB;
	private static Corretora AGORA;
	private static List<FundoInvestimento> fundosTeste;	

	private static Logger log;
	private static Map<Long, String> locations;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		log = LogManager.getFormatterLogger(FundoInvestimentoResourceRESTServiceTest.class);
		baseURI = "http://localhost:9080";
		basePath = "/ljbmWeb/rest";
//		com.jayway.restassured.RestAssured.config.getLogConfig().
		BB = new Corretora();
		BB.setIde(1l);
		AGORA = new Corretora();
		AGORA.setIde(2l);
		
		fundosTeste = new ArrayList<FundoInvestimento> (
			Arrays.asList(
				new FundoInvestimento(cnpjAgora, "Agora Prefixado 2019", new BigDecimal("0.15"),
						TipoFundoInvestimento.TesouroDireto, AGORA),
				new FundoInvestimento(cnpjBB   , "BB Prefixado 2023", new BigDecimal("0.15"),
						TipoFundoInvestimento.TesouroDireto, BB),
				new FundoInvestimento(cnpjBB   , "BB NTNB-2024", new BigDecimal("0.15"),
						TipoFundoInvestimento.TesouroDireto, BB),
				new FundoInvestimento(cnpjAgora, "Agora NTNB-2035", new BigDecimal("0.15"),
						TipoFundoInvestimento.TesouroDireto, AGORA)));


	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test01IncluiFundosInvestimento() {
		locations = new HashMap<Long, String>();
		fundosTeste
			.forEach(fundo -> {
				Response response = enviaPost(fundo, FUNDOS_INVESTIMENTO_RESOURCE_BASE);
				log.info("Response Headers\n" + response.headers().toString());
				response.prettyPrint();

				Assert.assertEquals(HttpStatus.SC_CREATED, response.getStatusCode());
				String header = response.getHeader("Location");
				Long ideFundo = Long.parseLong(
					header.substring(header.lastIndexOf("/")+1,header.length()));
				fundo.setIde(ideFundo);
				locations.put(ideFundo, header);
			});
	}
	
	@Test
	@Ignore
	public void test02LeFundosInvestimento() {
		fundosTeste
			.forEach(fundo -> {
				String location = locations.get(fundo.getIde());
				Response response = enviaGet(location, APPLICATION_JSON);
				JsonPath retorno = response.jsonPath();
				log.info(retorno.prettify());
				
				FundoInvestimento resourceLido = retorno.getObject("FundoInvestimento", FundoInvestimento.class);
				assertThat(resourceLido.getIde(), notNullValue());
				assertThat(resourceLido.getIde(), equalTo(fundo.getIde()));
				assertThat(resourceLido.getNome(), equalTo(fundo.getNome()));
				assertThat(resourceLido.getCNPJ(), equalTo(fundo.getCNPJ()));
				assertThat(resourceLido.getTipoFundoInvestimento(),
						equalTo(fundo.getTipoFundoInvestimento()));
				assertThat(resourceLido.getTaxaImpostoRenda(),
						equalTo(fundo.getTaxaImpostoRenda()));
		});
	}


	@Test
	public void test04LeFundosInvestimentoInexistente () {
			String location = FUNDOS_INVESTIMENTO_RESOURCE_BASE + "/0";
			Response response = enviaGet(location, APPLICATION_JSON);
			Assert.assertEquals(HttpStatus.SC_NOT_FOUND, response.getStatusCode());
			log.info("Response Headers\n" + response.headers().toString());
			log.info("Response Body");
			response.getBody().prettyPrint();
	}
	
	@Test
	public void test03LeFundosInvestimentoPorAgente_Titulo() {
			String location = FUNDOS_INVESTIMENTO_RESOURCE_BASE + 
					"?" 
					+ "agente=BB BANCO DE INVESTIMENTO S/A - 1102303"
					+ "&titulo=BB Prefixado 2023";
			Response response = enviaGet(location, APPLICATION_JSON);
			Assert.assertEquals(HttpStatus.SC_OK, response.getStatusCode());
			log.info("Response Headers\n" + response.headers().toString());
			log.info("Response Body");
			response.getBody().prettyPrint();
	}

	
	@Test
	@Ignore
	public void test05ExcluirFundosInvestimento() {
		fundosTeste
			.forEach(fundo -> {
				String location = locations.get(fundo.getIde());
				Response response = enviaDelete(location);
				log.info("Response StatusLine\n" + response.getStatusLine().toString());
				Assert.assertEquals(HttpStatus.SC_NO_CONTENT, response.getStatusCode());
		});		
		
	}

	// FIXME uso de basePath não funcionando
	private static Response enviaPost(FundoInvestimento fundoInvestimento, String destinoPost) {
		
		log.info("enviando post para " + destinoPost);
		return 
			given()
				.contentType(APPLICATION_JSON)
				.body(fundoInvestimento)
			.when()
				.post(destinoPost)
			.andReturn();
	}

	private static Response enviaGet(String location, String tipoRetorno) {
	
		log.info("enviando get para " + location);
		return 
			given()
				.header("Accept", tipoRetorno)
			.when()
				.get(location)
			.andReturn();
				
	}
	
	private static Response enviaDelete(String location) {
		
		log.info("enviando delete para " + location);
		return 
			given()
			.when()
				.delete(location)
			.andReturn();
	}
}
// @formatter:on

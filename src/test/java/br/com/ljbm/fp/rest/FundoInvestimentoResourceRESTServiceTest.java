package br.com.ljbm.fp.rest;

import static br.com.ljbm.fp.modelo.Corretora.cnpjAgora;
import static br.com.ljbm.fp.modelo.Corretora.cnpjBB;
import static com.jayway.restassured.RestAssured.basePath;
import static com.jayway.restassured.RestAssured.baseURI;
import static com.jayway.restassured.RestAssured.given;
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
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;

import br.com.ljbm.fp.modelo.Corretora;
import br.com.ljbm.fp.modelo.FundoInvestimento;
import br.com.ljbm.fp.modelo.TipoFundoInvestimento;

//@formatter:off

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FundoInvestimentoResourceRESTServiceTest {

	private static Corretora BB;
	private static Corretora AGORA;
//	private static FundoInvestimento Agora_NTNB_2035;
	private static List<FundoInvestimento> fundosTeste;	

	private static Logger log;
//	Map(<String>,<String>) locations;
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
//	@Ignore
	public void test01IncluiFundosInvestimento() {
		locations = new HashMap<Long, String>();
		fundosTeste
			.forEach(fundo -> {
				Response response = enviaPost(fundo, "/fundosInvestimento");
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
	public void test02LeFundosInvestimento() {
		fundosTeste
			.forEach(fundo -> {
				String location = locations.get(fundo.getIde());
				JsonPath retorno = enviaGet(location, APPLICATION_JSON);
				log.info(retorno.prettify());
				
				FundoInvestimento resourceLido = retorno.getObject("FundoInvestimento", FundoInvestimento.class);
				assertThat(resourceLido.getIde(), notNullValue());
				assertThat(resourceLido.getNome(), equalTo(fundo.getNome()));
				assertThat(resourceLido.getCNPJ(), equalTo(fundo.getCNPJ()));
				assertThat(resourceLido.getTipoFundoInvestimento(),
						equalTo(fundo.getTipoFundoInvestimento()));
				assertThat(resourceLido.getTaxaImpostoRenda(),
						equalTo(fundo.getTaxaImpostoRenda()));
		});
	}

	@Test
	public void test03ExcluirFundosInvestimento() {
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

	private static JsonPath enviaGet(String location, String tipoRetorno) {
	
		log.info("enviando get para " + location);
		return 
			given()
				.header("Accept", tipoRetorno)
			.when()
				.get(location)
			.andReturn()
				.jsonPath();
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

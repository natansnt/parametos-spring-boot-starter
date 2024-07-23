package br.com.natandev.parametros;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;
import java.util.function.Function;

import org.junit.jupiter.api.Test;

import br.com.natandev.parametros.managers.ParametroDAO;
import br.com.natandev.parametros.managers.ParametroManager;
import br.com.natandev.parametros.test.util.Parametros;

class ParametroManagerTest {

	final ParametroDAO daoMocked;
	final ParametroManager manager;

	public ParametroManagerTest() {
		ParametrosProperties props = new ParametrosProperties();
		props.getAgenciaVirtual().setAgencia("988");
		
		daoMocked = mock(ParametroDAO.class);
		manager = new ParametroManager(props, daoMocked);
	}
	
	@Test
	void adicaoDoPrefixo() {
		when(daoMocked.buscar("SECAO", "988_00_NOME", false))
			.thenReturn(Optional.of("VALOR"));
		
		manager.buscar(Parametro.with("SECAO", "NOME"), Function.identity());
		
		verify(daoMocked).buscar("SECAO", "988_00_NOME", false);
	}
	
	@Test
	void buscaParaOParametroSemPrefixo() {
		Parametro<String> parametro = Parametro.with("SECAO", "NOME");
		when(daoMocked.buscar(parametro, false))
			.thenReturn(parametro.withValor("VALOR"));
		
		assertThat(manager.buscar(parametro, Function.identity()))
		.returns("NOME", Parametro::getNome)	
		.returns("VALOR", param -> param.getValor().get());
		
		verify(daoMocked).buscar("SECAO", "988_00_NOME", false);
	}
	
	@Test
	void utilizandoUtilitario() {
		Parametro<String> parametro = Parametro.with("LOCAL", "DATA_REFERENCIA");
		when(daoMocked.buscar("LOCAL", "988_00_DATA_REFERENCIA", false))
			.thenReturn(Optional.empty());
		when(daoMocked.buscar(parametro, false))
			.thenReturn(parametro);
		
		Parametros parametros = new Parametros(manager);
		assertThat(parametros.local().dataReferencia.get()).returns(Optional.empty(), Parametro::getValor);
		
		verify(daoMocked).buscar("LOCAL", "988_00_DATA_REFERENCIA", false);
		verify(daoMocked).buscar(parametro, false);
	}
	
	@Test
	void conversaoUtilizandoUtilitario() {
		when(daoMocked.buscar("LOCAL", "988_00_DATA_REFERENCIA", false))
			.thenReturn(Optional.of("2024-01-01"));
		
		Parametros parametros = new Parametros(manager);
		assertThat(parametros.local().dataReferencia.get())
			.returns(Optional.of(LocalDate.parse("2024-01-01")), Parametro::getValor);
		
		verify(daoMocked).buscar("LOCAL", "988_00_DATA_REFERENCIA", false);
	}
}

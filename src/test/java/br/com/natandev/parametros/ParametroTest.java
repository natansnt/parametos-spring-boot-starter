package br.com.natandev.parametros;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class ParametroTest {
	
	@Test
	void valoresInvalidos() {
		assertThrows(NullPointerException.class, () -> new Parametro<Void>(null, null, null));
		assertThrows(NullPointerException.class, () -> new Parametro<Void>("SECAO", null, null));
		
		assertThrows(IllegalArgumentException.class, () -> new Parametro<Void>("", "NOME", null));
		assertThrows(IllegalArgumentException.class, () -> new Parametro<Void>("SECAO", "", null));
	}
	
	@Test
	void transformacao() {
		Parametro<Object> emptyParametro = Parametro.with("secao", "nome");
		assertThat(emptyParametro)
			.extracting(Parametro::getSecao, Parametro::getNome)
			.containsExactly("SECAO", "NOME");
		
		assertThat(emptyParametro)
			.isNotEqualTo(emptyParametro.withValor(2));
			
		assertThat(emptyParametro.withValor("2"))
			.isEqualTo(emptyParametro.withValor(2).convert(String::valueOf));
	}
	
}

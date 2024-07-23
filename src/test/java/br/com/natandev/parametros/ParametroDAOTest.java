package br.com.natandev.parametros;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import br.com.natandev.parametros.managers.ParametroDAO;

@DataJpaTest
@ContextConfiguration(classes = ParametroDaoConfig.class)
class ParametroDAOTest {

	@Autowired ParametroDAO dao;
	
	@Test
	void buscar() {
		assertThat(dao.buscar("LOCAL", "DATA_REFERENCIA", false))
			.isPresent()
			.hasValue("2021-01-15");
		
		var parametro = dao.buscar(Parametro.with("LOCAL", "DATA_REFERENCIA"), false);
		assertThat(parametro.getValor())
			.isPresent()
			.hasValue("2021-01-15");
		
		assertThrows(NullPointerException.class, () -> dao.buscar(null, false));
		assertThrows(NullPointerException.class, () -> dao.buscar(null, null, false));
	}
	
	@Test
	void atualizar() {
		String newDate = "2024-04-01";
		
		assertDoesNotThrow(() -> {
			dao.atualizar(Parametro.with("LOCAL", "DATA_REFERENCIA", newDate));
		});
		
		assertThat(dao.buscar("LOCAL", "DATA_REFERENCIA", false))
			.isPresent()
			.hasValue(newDate);
	}
	
	@Test
	void atualizarComNulo() {
		assertThrows(NullPointerException.class, () -> dao.atualizar(null));

		assertDoesNotThrow(() -> dao.atualizar(Parametro.with("LOCAL", "DATA_REFERENCIA")));
		assertThat(dao.buscar("LOCAL", "DATA_REFERENCIA", false)).isEmpty();
	}
	
	
}

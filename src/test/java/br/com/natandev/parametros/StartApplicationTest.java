package br.com.natandev.parametros;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.natandev.parametros.managers.ParametroManager;

@AutoConfigureDataJpa
@EnableAutoConfiguration
@SpringBootTest(classes = ParametrosAutoConfiguration.class)
class StartApplicationTest {

	@Autowired ParametrosProperties props;
	@Autowired ParametroManager parametroManager;
	
	@Test
	void loadContext() {
		assertThat(props).hasNoNullFieldsOrProperties();
		assertThat(parametroManager).hasNoNullFieldsOrProperties();
	}
	
}

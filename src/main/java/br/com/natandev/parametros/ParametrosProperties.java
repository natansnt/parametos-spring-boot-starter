package br.com.natandev.parametros;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Validated
@ConfigurationProperties(prefix = "environments")
public class ParametrosProperties {

	@NotNull @Valid private Mapeamento mapeamento = new Mapeamento();
	@NotNull @Valid	private AgenciaVirtual agenciaVirtual = new AgenciaVirtual();
	
	@Data
	public class AgenciaVirtual {

		@NotNull @Pattern(regexp = "\\d{3}")
		private String agencia;
		
		@NotNull @Pattern(regexp = "\\d{2}")
		private String posto = "00";
		
		@SuppressWarnings("preview")
		public String prefixo() {
			return STR."\{agencia}_\{posto}";
		}
	}
	
	@Data
	public class Mapeamento {
		
		@NotEmpty private String tabela = "TB_PARAMETRO";
		@NotNull @Valid private Colunas colunas = new Colunas();
		
		@Data
		public class Colunas {
			
			@NotEmpty
			private String secao = "CP_NM_SECAO";
			
			@NotEmpty
			private String nome = "CP_NM_PARAMETRO";
			
			@NotEmpty
			private String valor = "VL_PARAMETRO";
		}
	}
}

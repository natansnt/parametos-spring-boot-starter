package br.com.natandev.parametros;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.util.Assert;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class Parametro<R> {

	private String secao;
	private String nome;
	private Optional<R> valor;
	
	public Parametro(String secao, String nome, R valor) {
		
		Objects.requireNonNull(secao, "O nome da seção deve ser fornecido.");
		Objects.requireNonNull(nome, "O nome do parâmetro deve ser fornecido.");
		
		Assert.hasText(secao, "Deve ser fornecido algum texto para o nome da seção.");
		Assert.hasText(nome, "Deve ser fornecido algum texto para o nome do parâmetro.");
		
		this.nome = nome.trim().toUpperCase();
		this.secao = secao.trim().toUpperCase();
		this.valor = Optional.ofNullable(valor);
	}
	
	public boolean exists() {
		return valor.isPresent();
	}
	
	public <T> Parametro<T> convert(final Function<R, T> parser) {
		Objects.requireNonNull(parser);
		return this.withValor(valor.flatMap(v -> Optional.ofNullable(parser.apply(v))));
	}
	
	public <T> Parametro<T> withValor(Optional<T> valor) {
		var newParam = new Parametro<T>(this.secao, this.nome, null);
		
		newParam.valor = Objects.requireNonNull(valor);
		
		return newParam;
	}
	
	public <T> Parametro<T> withValor(T valor) {
		return new Parametro<>(this.secao, this.nome, valor);
	}
	
	public static <T> Parametro<T> with(String secao, String nome) {
		return new Parametro<>(secao, nome, null);
	}
	
	public static <T> Parametro<T> with(String secao, String nome, T valor) {
		return new Parametro<>(secao, nome, valor);
	}
	
}

package br.com.natandev.parametros.util;

import java.util.function.Supplier;

import br.com.natandev.parametros.Parametro;

@FunctionalInterface
public interface ParametroSupplier<T> extends Supplier<Parametro<T>> {}

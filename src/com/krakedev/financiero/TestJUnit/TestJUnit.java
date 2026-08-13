package com.krakedev.financiero.TestJUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.krakedev.financiero.entidades.Cliente;
import com.krakedev.financiero.entidades.Cuenta;
import com.krakedev.financiero.servicios.Banco;

public class TestJUnit {

	// =======================================================
	// PRUEBAS DE COBERTURA: crearCuenta
	// =======================================================

	@Test
	public void testCrearCuenta_AsignacionCodigoPropietarioEIncremento() {
		// Valida que la creación de una cuenta asigne el código actual del banco como ID,
		// vincule correctamente el cliente propietario y retorne la cuenta creada.
		Banco banco = new Banco();
		Cliente cliente = new Cliente("1712345678", "Juan", "Perez");

		Cuenta cuentaCreada = banco.crearCuenta(cliente);

		assertNotNull(cuentaCreada);
		assertEquals("1000", cuentaCreada.getId());
		assertEquals(cliente, cuentaCreada.getPropietario());
		assertEquals("1712345678", cuentaCreada.getPropietario().getCedula());
	}

	@Test
	public void testCrearCuenta_IncrementoUltimoCodigoConsecutivo() {
		// Valida que el banco incremente secuencialmente su ultimoCodigo tras cada cuenta creada.
		Banco banco = new Banco();
		Cliente cliente1 = new Cliente("1712345678", "Juan", "Perez");
		Cliente cliente2 = new Cliente("1787654321", "Maria", "Gomez");

		banco.crearCuenta(cliente1);
		
		// Verificamos que el código interno incrementó a 1001 tras la primera cuenta
		assertEquals(1001, banco.getUltimoCodigo());

		Cuenta segundaCuenta = banco.crearCuenta(cliente2);

		// Verificamos que la segunda cuenta tome el código 1001 y el banco pase a 1002
		assertEquals("1001", segundaCuenta.getId());
		assertEquals(1002, banco.getUltimoCodigo());
	}

	@Test
	public void testCrearCuenta_ConUltimoCodigoModificado() {
		// Valida que si se modifica previamente el ultimoCodigo del banco mediante su setter,
		// la nueva cuenta se cree con dicho código personalizado.
		Banco banco = new Banco();
		banco.setUltimoCodigo(5000);
		Cliente cliente = new Cliente("1799999999", "Carlos", "Lopez");

		Cuenta cuenta = banco.crearCuenta(cliente);

		assertEquals("5000", cuenta.getId());
		assertEquals(5001, banco.getUltimoCodigo());
	}

	// =======================================================
	// PRUEBAS DE COBERTURA: depositar (Lineas y Ramas)
	// =======================================================

	@Test
	public void testDepositar_RamaTrue() {
		// Rama IF: Valida que al depositar un monto mayor a 0 (monto > 0), 
		// el saldo de la cuenta se incremente correctamente y retorne true.
		Banco banco = new Banco();
		Cuenta cuenta = new Cuenta("1000"); // Inicialmente saldoActual es 0.0

		boolean resultado = banco.depositar(150.50, cuenta);

		assertTrue(resultado);
		// Se usa delta (0.001) para la comparación de tipos double
		assertEquals(150.50, cuenta.getSaldoActual(), 0.001);
	}

	@Test
	public void testDepositar_RamaFalse_MontoCero() {
		// Rama ELSE: Valida que al intentar depositar un monto igual a 0, 
		// la operación retorne false y el saldo de la cuenta no se altere.
		Banco banco = new Banco();
		Cuenta cuenta = new Cuenta("1000");

		boolean resultado = banco.depositar(0.0, cuenta);

		assertFalse(resultado);
		assertEquals(0.0, cuenta.getSaldoActual(), 0.001);
	}

	@Test
	public void testDepositar_RamaFalse_MontoNegativo() {
		// Rama ELSE: Valida que al intentar depositar un monto negativo (monto <= 0), 
		// la operación retorne false y el saldo de la cuenta permanezca sin cambios.
		Banco banco = new Banco();
		Cuenta cuenta = new Cuenta("1000");

		boolean resultado = banco.depositar(-50.0, cuenta);

		assertFalse(resultado);
		assertEquals(0.0, cuenta.getSaldoActual(), 0.001);
	}
}
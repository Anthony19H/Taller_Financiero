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
	
	// =======================================================
		// PRUEBAS DE COBERTURA: retirar (Líneas y Ramas)
		// =======================================================

		@Test
		public void testRetirar_RamaTrue_MontoValidoYSaldoSuficiente() {
			// Rama IF: Valida que si monto > 0 Y monto <= saldoActual,
			// resta correctamente el monto del saldo y retorne true (200.0 - 50.0 = 150.0).
			Banco banco = new Banco();
			Cuenta cuenta = new Cuenta("1000");
			cuenta.setSaldoActual(200.0);

			boolean resultado = banco.retirar(50.0, cuenta);

			assertTrue(resultado);
			assertEquals(150.0, cuenta.getSaldoActual(), 0.001);
		}

		@Test
		public void testRetirar_RamaFalse_MontoCero() {
			// Rama ELSE (monto <= 0): Valida que al intentar retirar un monto de 0,
			// no cumpla la condición del IF, retorne false y mantenga el saldo intacto.
			Banco banco = new Banco();
			Cuenta cuenta = new Cuenta("1000");
			cuenta.setSaldoActual(200.0);

			boolean resultado = banco.retirar(0.0, cuenta);

			assertFalse(resultado);
			assertEquals(200.0, cuenta.getSaldoActual(), 0.001);
		}

		@Test
		public void testRetirar_RamaFalse_MontoNegativo() {
			// Rama ELSE (monto <= 0): Valida que al ingresar un monto negativo,
			// retorne false sin modificar el saldo.
			Banco banco = new Banco();
			Cuenta cuenta = new Cuenta("1000");
			cuenta.setSaldoActual(200.0);

			boolean resultado = banco.retirar(-30.0, cuenta);

			assertFalse(resultado);
			assertEquals(200.0, cuenta.getSaldoActual(), 0.001);
		}

		@Test
		public void testRetirar_RamaFalse_SaldoInsuficiente() {
			// Rama ELSE (monto > saldoActual): Valida que si el monto a retirar supera 
			// el saldo disponible en la cuenta, retorne false y no altere el saldo.
			Banco banco = new Banco();
			Cuenta cuenta = new Cuenta("1000");
			cuenta.setSaldoActual(100.0);

			boolean resultado = banco.retirar(150.0, cuenta);

			assertFalse(resultado);
			assertEquals(100.0, cuenta.getSaldoActual(), 0.001);
		}
		
		// =======================================================
		// PRUEBAS DE COBERTURA: transferir (Líneas y Ramas)
		// =======================================================

		@Test
		public void testTransferir_Exito() {
			// Rama ELSE (!estadoTransferencia = false): Valida que si la cuenta origen
			// tiene saldo suficiente y el monto es válido, se descuente el monto de origen,
			// se sume a la cuenta destino y retorne true.
			Banco banco = new Banco();
			Cuenta cuentaOrigen = new Cuenta("1000");
			cuentaOrigen.setSaldoActual(500.0);

			Cuenta cuentaDestino = new Cuenta("1001");
			cuentaDestino.setSaldoActual(100.0);

			boolean resultado = banco.transferir(cuentaOrigen, cuentaDestino, 200.0);

			assertTrue(resultado);
			assertEquals(300.0, cuentaOrigen.getSaldoActual(), 0.001);
			assertEquals(300.0, cuentaDestino.getSaldoActual(), 0.001);
		}

		@Test
		public void testTransferir_FalloPorSaldoInsuficiente() {
			// Rama IF (!estadoTransferencia = true): Valida que si el retiro falla
			// por saldo insuficiente en la origen, la transferencia retorne false
			// y no modifique el saldo de ninguna de las dos cuentas.
			Banco banco = new Banco();
			Cuenta cuentaOrigen = new Cuenta("1000");
			cuentaOrigen.setSaldoActual(50.0);

			Cuenta cuentaDestino = new Cuenta("1001");
			cuentaDestino.setSaldoActual(100.0);

			boolean resultado = banco.transferir(cuentaOrigen, cuentaDestino, 200.0);

			assertFalse(resultado);
			assertEquals(50.0, cuentaOrigen.getSaldoActual(), 0.001);
			assertEquals(100.0, cuentaDestino.getSaldoActual(), 0.001);
		}

		@Test
		public void testTransferir_FalloPorMontoInvalido() {
			// Rama IF (!estadoTransferencia = true): Valida que si se intenta transferir
			// un monto menor o igual a 0, la operación retorne false sin modificar los saldos.
			Banco banco = new Banco();
			Cuenta cuentaOrigen = new Cuenta("1000");
			cuentaOrigen.setSaldoActual(500.0);

			Cuenta cuentaDestino = new Cuenta("1001");
			cuentaDestino.setSaldoActual(100.0);

			boolean resultado = banco.transferir(cuentaOrigen, cuentaDestino, -50.0);

			assertFalse(resultado);
			assertEquals(500.0, cuentaOrigen.getSaldoActual(), 0.001);
			assertEquals(100.0, cuentaDestino.getSaldoActual(), 0.001);
		}
}
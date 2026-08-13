package com.krakedev.financiero.TestJUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import com.krakedev.financiero.entidades.Cliente;
import com.krakedev.financiero.entidades.Cuenta;
import com.krakedev.financiero.servicios.Banco;

public class TestJUnit {

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
}
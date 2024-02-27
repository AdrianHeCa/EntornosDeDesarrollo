/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package gradletests;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GestorArraysTest {
    @Test
    public void testContarElementos() {
        int[] array = {1, 2, 3, 4, 5};
        GestorArrays gestor = new GestorArrays(array);
        assertEquals(5, gestor.contarElementos());
    }

    @Test
    public void testContarElementosArrayVacio() {
        int[] array = {};
        GestorArrays gestor = new GestorArrays(array);
        assertEquals(0, gestor.contarElementos());
    }

    @Test
    public void testDevolverPrimero() {
        int[] array = {1, 2, 3, 4, 5};
        GestorArrays gestor = new GestorArrays(array);
        assertEquals(1, gestor.devolverPrimero());
    }

    @Test
    public void testDevolverPrimeroArrayVacio() {
        int[] array = {};
        GestorArrays gestor = new GestorArrays(array);
        assertThrows(IllegalStateException.class, gestor::devolverPrimero);
    }

    @Test
    public void testDevolverUltimo() {
        int[] array = {1, 2, 3, 4, 5};
        GestorArrays gestor = new GestorArrays(array);
        assertEquals(5, gestor.devolverUltimo());
    }

    @Test
    public void testDevolverUltimoArrayVacio() {
        int[] array = {};
        GestorArrays gestor = new GestorArrays(array);
        assertThrows(IllegalStateException.class, gestor::devolverUltimo);
    }

    @Test
    public void testDevolverTercero() {
        int[] array = {1, 2, 3, 4, 5};
        GestorArrays gestor = new GestorArrays(array);
        assertEquals(3, gestor.devolverTercero());
    }

    @Test
    public void testDevolverTerceroArrayVacio() {
        int[] array = {};
        GestorArrays gestor = new GestorArrays(array);
        assertThrows(IllegalStateException.class, gestor::devolverTercero);
    }

    @Test
    public void testDevolverTerceroArrayMenosDeTresElementos() {
        int[] array = {1, 2};
        GestorArrays gestor = new GestorArrays(array);
        assertThrows(IllegalStateException.class, gestor::devolverTercero);
    }

    @Test
    public void testSumaElementos() {
        int[] array = {1, 2, 3, 4, 5};
        GestorArrays gestor = new GestorArrays(array);
        assertEquals(15, gestor.sumaElementos());
    }

    @Test
    public void testSumaElementosArrayVacio() {
        int[] array = {};
        GestorArrays gestor = new GestorArrays(array);
        assertEquals(0, gestor.sumaElementos());
    }

    @Test
    public void testSumaElementosNegativos() {
        int[] array = {-1, 2, -3, 4, -5};
        GestorArrays gestor = new GestorArrays(array);
        assertEquals(-3, gestor.sumaElementos());
    }

    @Test
    public void testMediaElementos() {
        int[] array = {1, 2, 3, 4, 5};
        GestorArrays gestor = new GestorArrays(array);
        assertEquals(3, gestor.mediaElementos());
    }

    @Test
    public void testMediaElementosArrayVacio() {
        int[] array = {};
        GestorArrays gestor = new GestorArrays(array);
        assertThrows(IllegalStateException.class, gestor::mediaElementos);
    }

    @Test
    public void testMediaElementosConNegativos() {
        int[] array = {-1, 2, -3, 4, -5};
        GestorArrays gestor = new GestorArrays(array);
        assertEquals(-0.6, gestor.mediaElementos(), 0.001);
    }
}
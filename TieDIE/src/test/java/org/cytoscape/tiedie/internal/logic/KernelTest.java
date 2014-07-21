package org.cytoscape.tiedie.internal.logic;

import Jama.Matrix;

import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author SrikanthB
 */
public class KernelTest {
    
    static CyNetwork testNetwork =null;
    
    public KernelTest() {
    }
    
    @BeforeClass
    public static void oneTimeSetUpClass() {
    }
    
    @AfterClass
    public static void oneTimeTearDownClass() {
    }
    
    @Before
    public void setUp() {
        final NetworkTestSupport nts = new NetworkTestSupport();
        testNetwork = nts.getNetwork();
        // Now you have a CyNetwork!
        // Set name for network
        testNetwork.getRow(testNetwork).set(CyNetwork.NAME, "Test Network");
        // Add two nodes to the network
        CyNode node1 = testNetwork.addNode();
        CyNode node2 = testNetwork.addNode();
        // Set name for new nodes
        testNetwork.getRow(node1).set(CyNetwork.NAME, "Node1");
        testNetwork.getRow(node1).set(CyNetwork.NAME, "Node2");
        // Add an edge
        testNetwork.addEdge(node1, node2, true);
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getTime method, of class Kernel.
     */
    @Test
    public void testGetTime() {
        System.out.println("getTime");
        Kernel instance = new Kernel(testNetwork);
        double expResult = 0.1;
        double result = instance.getTime();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getadjacencyMatrixOfNetwork method, of class Kernel.
     */
    @Test
    public void testGetadjacencyMatrixOfNetwork() {
        System.out.println("getadjacencyMatrixOfNetwork");
        Kernel instance = new Kernel(testNetwork);
        double[][] expResult = {{0,1},{1,0}};
        double[][] result = instance.getadjacencyMatrixOfNetwork();
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of getdiffusionKernelOfNetwork method, of class Kernel.
     */
    @Test
    public void testGetdiffusionKernelOfNetwork() {
        System.out.println("getdiffusionKernelOfNetwork");
        Kernel instance = new Kernel(testNetwork);
        double[][] expResult = null;
        double[][] result = instance.getdiffusionKernelOfNetwork();
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of createAdjMatrix method, of class Kernel.
     */
    @Test
    public void testCreateAdjMatrix() {
        System.out.println("createAdjMatrix");
        Kernel instance = new Kernel(testNetwork);
        double[][] expResult = {{0,1},{1,0}};
        double[][] result = instance.createAdjMatrix();
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of createDegMatrix method, of class Kernel.
     */
    @Test
    public void testCreateDegMatrix() {
        System.out.println("createDegMatrix");
        Kernel instance = new Kernel(testNetwork);
        double[][] expResult = {{1,0},{0,1}};
        double[][] result = instance.createDegMatrix();
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of createLapMatrix method, of class Kernel.
     */
    @Test
    public void testCreateLapMatrix() {
        System.out.println("createLapMatrix");
        Kernel instance = new Kernel(testNetwork);
        double[][] resMatrix = {{1,-1},{-1,1}};
        Matrix expResult = new Matrix(resMatrix);
        Matrix result = instance.createLapMatrix();
    }

    /**
     * Test of createRequiredExponential method, of class Kernel.
     */
    @Test
    public void testCreateRequiredExponential() {
        System.out.println("createRequiredExponential");
        Kernel instance = null;
        double[][] expResult = null;
        double[][] result = instance.createRequiredExponential();
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of diffuse method, of class Kernel.
     */
    @Test
    public void testDiffuse() {
        System.out.println("diffuse");
        HeatVector inputVector = null;
        Kernel instance = null;
        DiffusedHeatVector expResult = null;
        DiffusedHeatVector result = instance.diffuse(inputVector);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}

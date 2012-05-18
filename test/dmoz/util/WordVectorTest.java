/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dmoz.util;

import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 *
 * @author hoshun
 */
public class WordVectorTest {

    public WordVectorTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     * Test of innerProduct method, of class WordVector.
     */
    @Test
    public void testInnerProduct() {

        WordVector wv1 = new WordVector(new String[]{"a", "1.5", "c", "2", "e", "5"});
        WordVector wv2 = new WordVector(new String[]{"a", "2.0", "c", "1.3", "e", "5"});
        assertEquals(30.6, wv1.innerProduct(wv2), 0.0);

        wv1 = new WordVector(new String[]{"d", "2", "e", "3", "a", "1"});
        wv2 = new WordVector(new String[]{"a", "1", "d", "2", "e", "3"});
        assertEquals(14, wv1.innerProduct(wv2), 0.0);

        wv1 = new WordVector(new String[]{"c", "1.5", "x", "2", "t", "5"});
        wv2 = new WordVector(new String[]{"a", "2.0", "c", "2", "e", "5"});
        assertEquals(3, wv1.innerProduct(wv2), 0.0);

        wv1 = new WordVector(new String[]{"a", "1.5", "c", "2", "e", "5"});
        wv2 = new WordVector(new String[]{});
        assertEquals(0, wv1.innerProduct(wv2), 0);
        assertEquals(0, wv2.innerProduct(wv1), 0);

        wv1 = new WordVector(new String[]{"a", "1.5", "c", "2", "e", "5"});
        wv2 = new WordVector(new String[]{"e", "1.5"});
        assertEquals(7.5, wv1.innerProduct(wv2), 0);
        assertEquals(7.5, wv2.innerProduct(wv1), 0);

        wv1 = new WordVector(new String[]{"e", "5"});
        wv2 = new WordVector(new String[]{"d", "1.5"});
        assertEquals(0, wv1.innerProduct(wv2), 0);
        assertEquals(0, wv2.innerProduct(wv1), 0);

        wv1 = new WordVector(new String[]{"e", "5"});
        wv2 = new WordVector(new String[]{"e", "1.5"});
        assertEquals(7.5, wv1.innerProduct(wv2), 0);
        assertEquals(7.5, wv2.innerProduct(wv1), 0);

        wv1 = new WordVector(new String[]{});
        wv2 = new WordVector(new String[]{});
        assertEquals(0, wv1.innerProduct(wv2), 0);


    }
}

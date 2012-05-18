/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dmoz;

import java.util.ArrayList;
import java.util.Arrays;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 *
 * @author hoshun
 */
public class ContentFormatTest {

    public ContentFormatTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     * Test of isExternPageStart method, of class ContentFormat.
     */
    @Test
    public void testIsExternPageStart() {
        System.out.println("isExternPageStart");
        assertEquals(true, ContentFormat.isExternPageStart("<ExternalPage about=\"http://www.toonhound.com/\">"));
        assertEquals(false, ContentFormat.isExternPageStart("<ExternalPage>"));
    }

    /**
     * Test of isExternPageEnd method, of class ContentFormat.
     */
    @Test
    public void testIsExternPageEnd() {
        System.out.println("isExternPageEnd");
        assertEquals(true, ContentFormat.isExternPageEnd("   </ExternalPage> "));
        assertEquals(false, ContentFormat.isExternPageEnd("   <ExternalPage> "));
    }

    /**
     * Test of getTitle method, of class ContentFormat.
     */
    @Test
    public void testGetTitle() {
        System.out.println("getTitle");
        assertEquals("About.com: Animation Guide", ContentFormat.getTitle("  <d:Title>About.com: Animation Guide</d:Title>  "));
    }

    /**
     * Test of getDescription method, of class ContentFormat.
     */
    @Test
    public void testGetDescription() {
        
        System.out.println("getDescription");
        assertEquals("British cartoon, animation and comic strip creations - links, reviews and news from the UK.",
                ContentFormat.getDescription("  <d:Description>British cartoon, animation and comic strip creations - "
                + "links, reviews and news from the UK.</d:Description>  "));
    }

    /**
     * Test of getTopic method, of class ContentFormat.
     */
    @Test
    public void testGetTopic() {
        System.out.println("getTopic");
        assertEquals("Top/Arts/Animation", ContentFormat.getTopic("<topic>Top/Arts/Animation</topic>"));
    }

    /**
     * Test of getFirstTwoHeirachy method, of class ContentFormat.
     */
    @Test
    public void testGetFirstTwoHeirachy() {
        System.out.println("getFirstTwoHeirachy");

        assertEquals(new ArrayList<String>(Arrays.asList(new String[]{"Art", "Animation"})),
                ContentFormat.getFirstTwoHeirachy("Top/Art/Animation/"));
        assertEquals(new ArrayList<String>(Arrays.asList(new String[]{"Art", "Animation"})),
                ContentFormat.getFirstTwoHeirachy("Top/Art/Animation"));
        assertEquals(new ArrayList<String>(Arrays.asList(new String[]{"Art", "Animation"})),
                ContentFormat.getFirstTwoHeirachy("Top/Art/Animation/one"));
        assertEquals(new ArrayList<String>(Arrays.asList(new String[]{"Art", "Animation"})),
                ContentFormat.getFirstTwoHeirachy("Top/Art/Animation/one/two"));
        assertEquals(new ArrayList<String>(),
                ContentFormat.getFirstTwoHeirachy("Top/Art/"));
        assertEquals(new ArrayList<String>(),
                ContentFormat.getFirstTwoHeirachy(""));        

    }
}

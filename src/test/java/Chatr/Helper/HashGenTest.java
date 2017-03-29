package Chatr.Helper;

import org.junit.Test;
import static org.junit.Assert.*;

public class HashGenTest {

	@Test
	public void wrongHash(){
		assertEquals("098f6bcd4621d373cade4e832627b4f6", HashGen.hash("test"));
		assertEquals("35f43d73c12ca4c3fef5f7553d385cca", HashGen.hash("teyrzxducfhjblkn"));
		assertEquals("d41d8cd98f00b204e9800998ecf8427e", HashGen.hash(""));
		assertEquals("68b329da9893e34099c7d8ad5cb9c940", HashGen.hash("\n"));
	}
	
	@Test
	public void sameID(){
		String id1, id2;
		id1 = HashGen.getID(true);
		id2 = HashGen.getID(true);
		assertFalse(id1==id2);
	}
	
	@Test
	public void clearCase(){
		assertTrue(HashGen.getID(true).matches("\\d+"));
		assertFalse(HashGen.getID(false).matches("\\d+"));
	}


}

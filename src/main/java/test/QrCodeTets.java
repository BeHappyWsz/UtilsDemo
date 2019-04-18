package test;

import org.junit.Test;

import utils.QrCodeUtils;

/**
 * 
 * @author wsz
 * @date 2019年4月18日
 */
public class QrCodeTets {

	@Test
	public void withoutLogo() {
//		QrCodeUtils.withoutLogo("123");
	}
	
	@Test
	public void withLogo() {
		QrCodeUtils.withLogo("111231231231232312323123123123123123");
	}
}

package net.sf.orassist.jnawechat;

import static org.junit.Assert.*;

import org.junit.Test;

public class JnaWeChatTest {

	@Test
	public void testSend() {
		new JnaWeChat().send("微信发送测试");
	}

}

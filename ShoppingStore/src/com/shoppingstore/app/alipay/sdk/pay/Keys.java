/*
 * Copyright (C) 2010 The MobileSecurePay Project
 * All right reserved.
 * author: shiqun.shi@alipay.com
 * 
 *  提示：如何获取安全校验码和合作身份者id
 *  1.用您的签约支付宝账号登录支付宝网站(www.alipay.com)
 *  2.点击“商家服务”(https://b.alipay.com/order/myorder.htm)
 *  3.点击“查询合作者身份(pid)”、“查询安全校验码(key)”
 */

package com.shoppingstore.app.alipay.sdk.pay;

//
// 请参考 Android平台安全支付服务(msp)应用开发接口(4.2 RSA算法签名)部分，并使用压缩包中的openssl RSA密钥生成工具，生成一套RSA公私钥。
// 这里签名时，只需要使用生成的RSA私钥。
// Note: 为安全起见，使用RSA私钥进行签名的操作过程，应该尽量放到商家服务器端去进行。
public final class Keys {

	//合作身份者id，以2088开头的16位纯数字
	public static final String DEFAULT_PARTNER = "2088021779007742";

	//收款支付宝账号
	public static final String DEFAULT_SELLER = "chicspay@chics-mart.com";

	//商户私钥，自助生成
	public static final String PRIVATE = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAMl9J2ZlzNNPWU5d6KzUjQRkPyouPssn6smtJfC3Pf1wbQNuGedj/EReRXzp4h/7dg93YLbMIPb8uj5jsYs31n45tTjW3mkX9ubrpVO8BZ+sHG1m5kYaWRT6emfwmzV2sHeDfbykdng2CJXm4ept6/Ezs+nZrnfR05Ygu8Jj1wTJAgMBAAECgYEAlZoLawgi8LJ1yK4JWGno2l3kWFlF3mmg9mVNSlGEAW08Q9O4AApJACpOxdSSRNTcb9dfQ9qEj15OmoxDAu9OndCiM9Kbb/7X5XZDb9blkEaM2+QcAcnSA7WSDVxPjRzpjRZzF2u57e1KXeCe9sRWy2kZS4XhoYwSPyQa26e/sAECQQDqo56UayTNxxcuG7zvq/CzGiNktM7aCTbUTt4K0Li563XMA/ZzHTZfruD0CJUxwGU02YDPYNrvaCsd0psJHZuBAkEA29TzkOKTYdHZOEhB6mTMj1dWBQN+7DNCzfyNlsQaewkCpIOc6SPBMxb1r2tsaHsCnvkcNogTdVQlw6m/3hAtSQJAUEIxTxErIJwWCLaCNPT6OzkYnCzgiGIYeByWBNsKJMYun99HIG8Al1AJME/zQHZP5/jx/31gxf06qlPMphyTgQJAFYSXvtPe11FiMNcp1Ja/SzyiR+VatCYkYONavKo/aVGtd4sGfe+pBqY1sFEUkHmR/RunPlM9jFuLQsbH6CWvOQJBALSIPeqZ8/ZMNbmb4A+jY9wkkuxRbtJ0L7j68ArLRWFXBqcwg0QSYcKPNPmuim7UqGLqRvVmOdu/vzRP4B1O/Xw=";

	public static final String PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";

}

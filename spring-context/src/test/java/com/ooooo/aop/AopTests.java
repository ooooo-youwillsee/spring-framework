package com.ooooo.aop;

import org.junit.Test;
import org.springframework.aop.aspectj.autoproxy.AspectJAutoProxyCreatorTests;
import org.springframework.aop.config.AopNamespaceHandlerAdviceTypeTests;
import org.springframework.aop.config.AopNamespaceHandlerTests;

/**
 * @author leizhijie
 * @since 2019-08-05 11:17
 */
public class AopTests {

	/**
	 * aop名称空间处理器
	 */
	private final AopNamespaceHandlerAdviceTypeTests aopNamespaceHandlerAdviceTypeTests = new AopNamespaceHandlerAdviceTypeTests();

	/**
	 * aop标签代理
	 */
	private final AopNamespaceHandlerTests aopNamespaceHandlerTests = new AopNamespaceHandlerTests();


	/**
	 * 基于注解@aspect 代理
	 */
	private final AspectJAutoProxyCreatorTests aspectJAutoProxyCreatorTests = new AspectJAutoProxyCreatorTests();

	/**
	 * 解析aop标签
	 */
	@Test
	public void aopNamespaceHandler_testParsingOfAdviceTypes() {
		aopNamespaceHandlerAdviceTypeTests.testParsingOfAdviceTypes();
	}

	/**
	 * 测试aop标签代理
	 */
	@Test
	public void aopNamespaceHandler_testAdviceInvokedCorrectly() throws Exception {
		aopNamespaceHandlerTests.testAdviceInvokedCorrectly();
	}

	/**
	 * 测试@aspectj代理
	 */
	@Test
	public void aspectJAutoProxyCreatorTests_testAdviceUsingJoinPoint() {
		aspectJAutoProxyCreatorTests.testAdviceUsingJoinPoint();
	}


	/**
	 * 测试aop失败回调，重试机制
	 */
	@Test
	public void aspectJAutoProxyCreatorTests_testRetryAspect() {
		aspectJAutoProxyCreatorTests.testRetryAspect();
	}

}

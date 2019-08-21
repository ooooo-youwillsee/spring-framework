package com.ooooo.aop;

import org.junit.Test;
import org.springframework.aop.aspectj.autoproxy.AspectJAutoProxyCreatorTests;
import org.springframework.aop.config.AopNamespaceHandlerAdviceTypeTests;
import org.springframework.aop.config.AopNamespaceHandlerTests;
import org.springframework.context.annotation.EnableAspectJAutoProxyTests;

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
	 * 注解@enableAspectJAutoProxy 测试类
	 */
	private final EnableAspectJAutoProxyTests enableAspectJAutoProxyTests = new EnableAspectJAutoProxyTests();

	/**
	 * 解析aop标签
	 */
	@Test
	public void aopNamespaceHandler_testParsingOfAdviceTypes() {
		aopNamespaceHandlerAdviceTypeTests.testParsingOfAdviceTypes();
	}

	/**
	 *  在new ClassPathXmlApplicationContext() 就会添加beanProcessor -->org.springframework.aop.aspectj.autoproxy.AspectJAwareAdvisorAutoProxyCreator
	 *
	 * 在 org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator#postProcessBeforeInstantiation 这里打断点调试
	 * 测试aop标签代理
	 *
	 * 这里测试这一句话 ITestBean bean = getTestBean(); --> 可以看出是bean初始化之后才生成代理对象的
	 *
	 * 这里的核心代理类是 ReflectiveMethodInvocation
	 */
	@Test
	public void aopNamespaceHandler_testAdviceInvokedCorrectly() throws Exception {
		aopNamespaceHandlerTests.testAdviceInvokedCorrectly();
	}

	/**
	 * 测试这一句代码 bean.setName("Sally")
	 *
	 * 默认代理是 AspectJAwareAdvisorAutoProxyCreator
	 * @throws Exception
	 */
	@Test
	public void aopNamespaceHandler_testAspectApplied() throws Exception {
		aopNamespaceHandlerTests.testAspectApplied();
	}


	/**
	 * 有个标签 <aop:aspectj-autoproxy/> 默认代理就是 AnnotationAwareAspectJAutoProxyCreator (具有解析aop注解能力)
	 *
	 * 这里的核心代理类是 ReflectiveMethodInvocation
	 * 测试这一句代码 newContext("aspects.xml") --—> xml配置解析<aop:aspectj-autoproxy/>过程
	 *
	 * 测试@aspectj代理
	 */
	@Test
	public void aspectJAutoProxyCreatorTests_testAspectApplied() {
		aspectJAutoProxyCreatorTests.testAspectsAreApplied();
	}


	/**
	 *
	 * 这里的是直接创建 AnnotationAwareAspectJAutoProxyCreator 的bean
	 *
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

	@Test
	public void test_enableAspectJAutoProxyTests(){
		enableAspectJAutoProxyTests.withJdkProxy();
	}

}

package com.ooooo.tx;

import org.junit.Test;
import org.springframework.transaction.TxNamespaceHandlerTests;
import org.springframework.transaction.annotation.AnnotationTransactionNamespaceHandlerTests;
import org.springframework.transaction.annotation.EnableTransactionManagementTests;

/**
 * @author leizhijie
 * @since 2019-08-12 19:19
 */
public class TxTests {

	/**
	 * 标签<tx> 命名空间测试类
	 */
	private final TxNamespaceHandlerTests txNamespaceHandlerTests = new TxNamespaceHandlerTests();

	/**
	 * 注解@transational 测试类
	 */
	private final AnnotationTransactionNamespaceHandlerTests annotationTransactionNamespaceHandlerTests = new AnnotationTransactionNamespaceHandlerTests();

	/**
	 * 注解@enableTransactionManagement 测试类
	 */
	private final EnableTransactionManagementTests enableTransactionManagementTests = new EnableTransactionManagementTests();

	/**
	 * 测试xml配置的事务
	 *
	 *
	 * 对于事务，也是aop来实现的，这里的接口默认aop代理类为  AspectJAwareAdvisorAutoProxyCreator
	 * 这里也是主要看实例化之前的方法(postProcessBeforeInstantiation), 初始化之后的方法(postProcessAfterInitialization)
	 *
	 * 过程：
	 * 	1、每一个标签<tx:advice>都是一个TransactionInterceptor，而这个接口也是advisor的子接口，
	 * 	   所以在调用postProcessBeforeInstantiation()方法中判断条件isInfrastructureClass()方法中加入到advisedBeans集合中
	 *  2、每一个标签<tx:advice>经过getBean()方法创建实例时，再调用populateBean()方法填充属性，每一个txAdvice都必须要填充一个transactionManager(事务管理器)的属性，就是在这个时候，开始又调用getBean()方法来初始化事务管理器，就会被这个postProcessBeforeInstantiation(实例化之前处理)方法中判断条件shouldSkip()方法中，而这个方法中调用了findCandidateAdvisors()方法来初始化所有的advisorBean，再接着调用postProcessAfterInitialization(初始化之后的方法)，但对于transactionmanager(事务管理器)是不会生成代理对象的。
	 *
	 *
	 *  对于标签<tx:attributes>都会被解析为TransactionAttributeSource对象
	 *  对于标签<tx:method>都会被解析为TransactionAttribute对象
	 */
	@Test
	public void txNamespaceHandlerTests_invokeTransactional() {
		txNamespaceHandlerTests.invokeTransactional();
	}


	/**
	 * 测试xml配置的事务的rollback
	 */
	@Test
	public void txNamespaceHandlerTests_rollbackRules() {
		txNamespaceHandlerTests.rollbackRules();
	}


	/**
	 * 测试注解Transactional
	 *
	 * @throws Exception
	 */
	@Test
	public void annotationTransactionNamespaceHandlerTests_invokeTransactional() throws Exception {
		annotationTransactionNamespaceHandlerTests.invokeTransactional();
	}


	/**
	 * 测试事务的传播类型
	 */
	@Test
	public void test_propagation() {
		annotationTransactionNamespaceHandlerTests.get_ooooo_TransactionalTestBean().testTransactional();
	}


	/**
	 * 测试@EnableTransactionManagement注解
	 */
	@Test
	public void test_EnableTransactionManagement(){
		enableTransactionManagementTests.txManagerIsResolvedOnInvocationOfTransactionalMethod();
	}
}

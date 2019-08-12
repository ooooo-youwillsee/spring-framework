package com.ooooo.tx;

import org.junit.Test;
import org.springframework.transaction.TxNamespaceHandlerTests;
import org.springframework.transaction.annotation.AnnotationTransactionNamespaceHandlerTests;

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
	 * 测试xml配置的事务
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
}

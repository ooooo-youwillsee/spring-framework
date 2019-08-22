package com.ooooo.jdbc;

import com.ooooo.jdbc.beans.TransactionalTestBean1;
import com.ooooo.jdbc.beans.TransactionalTestBean2;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * @author leizhijie
 * @see com.ooooo.tx.TxTests
 * @since 2019-08-22 15:10
 * <p>
 * <p>
 * 由于编写com.ooooo.tx.TxTests中的测试类，并不能真正的测试事务的传播类型，主要原因是org.springframework.tests.transaction.CallCountingTransactionManager#doGetTransaction() 方法返回的是一个新的Object，所以在开启事务时，永远都没有一个存在的事务，导致每一次开启事务都是一个新事务
 */
public class PropagationTests {

	private ApplicationContext context;

	private DataSourceTransactionManager transactionManager;

	private TransactionalTestBean1 testBean1;

	private TransactionalTestBean2 testBean2;

	@Before
	public void setup() throws Exception {
		this.context = new ClassPathXmlApplicationContext("com/ooooo/beans/propagationTests.xml");
		this.transactionManager = (DataSourceTransactionManager) context.getBean("transactionManager");
		this.testBean1 = (TransactionalTestBean1) context.getBean("testBean1");
		this.testBean2 = (TransactionalTestBean2) context.getBean("testBean2");
	}


	@Test
	public void test1() {
		// 调试事务对异常的回滚
		assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> testBean1.test1());
	}


}

package com.ooooo.jdbc.beans;

import org.springframework.aop.framework.AopContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author leizhijie
 * @since 2019-08-12 19:32
 */
public class TransactionalTestBean1 {


	private TransactionalTestBean2 testBean2;

	/**
	 * 测试异常
	 * 1、noRollbackFor 表示生成这些异常不会回滚，只会提交
	 * 2、rollbackFor 表示发生这些异常会被回滚，
	 *
	 */
	@Transactional(noRollbackFor=RuntimeException.class)
	public void test1() {
		System.out.println("--1--");
		throw new RuntimeException();
	}

	/**
	 * 测试request传播类型下有request_new传播类型，
	 * 注意在这里调用的是本来的方法，是不会开启一个新的事务，这是因为通过this调用的方法，并没有经过代理,
	 * 注意在里面调用的是私有的方法，哪怕是通过aopContext(aop代理上下文)调用也不会开启事务
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void test2() {
		System.out.println("--2--");
		((TransactionalTestBean1)(AopContext.currentProxy())).test3();
		//testBean2.test3();
	}


	@Transactional(propagation = Propagation.REQUIRED)
	//@Transactional(propagation = Propagation.REQUIRES_NEW)
	//@Transactional(propagation = Propagation.NESTED)
	public void test3() {
		System.out.println("--3--");
		throw new RuntimeException();
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void test4() {
		System.out.println("--4--");
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	public void test5() {
		System.out.println("--5--");
	}


	public void setTestBean2(TransactionalTestBean2 testBean2) {
		this.testBean2 = testBean2;
	}


}


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
	 * 测试request传播类型下有request_new传播类型
	 *
	 * 1、通过this调用自身的方法，是不会开启事务的，这是因为通过this调用的方法，并没有经过代理,
	 * 2、如果调用的是私有方法，哪怕是通过aopContext(aop代理上下文)调用也不会开启事务 （源码有判断）
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void test2() {
		System.out.println("--2--");
		// this调用自身方法  --> 测试 test3 声明为 REQUIRES_NEW
		//test3();

		// 通过当前代理对象调用自身方法  --> 测试 test3 声明为 REQUIRES_NEW
		((TransactionalTestBean1) (AopContext.currentProxy())).test3();
		//
		// 其他bean调用   --> 测试 test3 声明为 REQUIRES_NEW
		//testBean2.test3();

		// 测试SUPPORTS事务 （有没有事务都可以）
		//((TransactionalTestBean1) (AopContext.currentProxy())).test4();

		// 测试NEVER事务 （如果存在事务，则抛出异常，接着外层事务会进行回滚，不存在事务，正常运行）
		//((TransactionalTestBean1) (AopContext.currentProxy())).test5();


	}


	@Transactional(propagation = Propagation.REQUIRED) // request事务，如果存在事务，就不会新建事务，如果没有则会新建事务, 如果在此方法内发生异常，只会标记此事务为rollback，等异常抛到最外层事务时，发现这是个新的事务，就会回滚（jta除外）
	//@Transactional(propagation = Propagation.REQUIRES_NEW)  // 声明为request_new事务，会开启一个新事务，在一个新的事务中，如果没有发生异常，就会commit，不会和外层的事务干扰，如果发生异常了，就会和外层事务一起回滚，因为发生异常后，又会抛出这个异常
	//@Transactional(propagation = Propagation.NESTED)  // 如果是内嵌事务会开启一个保存点，发生异常后会回滚到保存点
	public void test3() {
		System.out.println("--3--");
		throw new RuntimeException();
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	public void test4() {
		System.out.println("--4--");
	}

	@Transactional(propagation = Propagation.NEVER)
	public void test5() {
		System.out.println("--5--");
	}






	public void setTestBean2(TransactionalTestBean2 testBean2) {
		this.testBean2 = testBean2;
	}





}


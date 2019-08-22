package com.ooooo.jdbc.beans;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author leizhijie
 * @since 2019-08-12 19:32
 */
public class TransactionalTestBean2 {


	@Transactional(noRollbackFor=RuntimeException.class)
	public void testTransactional() {
		System.out.println("--TransactionalTestBean2--1--");
		throw new RuntimeException();
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void testTransactional_REQUIRES() {
		System.out.println("--TransactionalTestBean2--2--");
	}

	@Transactional(propagation = Propagation.NESTED)
	public void testTransactional_REQUIRES_NEW() {
		System.out.println("--TransactionalTestBean2--3--");
		throw new RuntimeException();
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void testTransactional_REQUIRES_2_REQUIRES_New() {
		System.out.println("--TransactionalTestBean2--4--");
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	public void testTransactional_REQUIRES_SUPPORTS() {
		System.out.println("--TransactionalTestBean2--5--");
	}


}


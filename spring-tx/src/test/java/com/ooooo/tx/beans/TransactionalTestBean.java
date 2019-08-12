package com.ooooo.tx.beans;

import org.springframework.tests.sample.beans.TestBean;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author leizhijie
 * @since 2019-08-12 19:32
 */
public class TransactionalTestBean extends TestBean {


	@Transactional
	public String testTransactional() {
		return super.getName();
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public int testTransactional_REQUIRES() {

		return super.getAge();
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String testTransactional_REQUIRES_NEW() {
		return super.getSex();
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public String testTransactional_REQUIRES_2_REQUIRES_New() {
		return testTransactional_REQUIRES_NEW();
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	public String testTransactional_REQUIRES_SUPPORTS() {
		return getName();
	}


}


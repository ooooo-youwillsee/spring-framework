package com.ooooo.jdbc.beans;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionStatus;

/**
 * @author leizhijie
 * @since 2019-08-22 15:44
 */
public class MyDataSourceTransactionManager extends DataSourceTransactionManager {


	private int dobegins = 0;

	private int doCommits = 0;

	public int doRollbacks = 0;

	public int doInflight = 0;


	@Override
	protected void doBegin(Object transaction, TransactionDefinition definition) {
		System.out.println("------------doBegin-------------" + ++dobegins);
		System.out.println("------------doInflight-------------" + ++doInflight);
		super.doBegin(transaction, definition);
	}

	@Override
	protected void doCommit(DefaultTransactionStatus status) {
		System.out.println("------------doCommit-------------" + ++doCommits);
		System.out.println("------------doInflight-------------" + ++doInflight);
		super.doCommit(status);
	}


	@Override
	protected void doRollback(DefaultTransactionStatus status) {
		System.out.println("------------doRollback-------------" + ++doRollbacks);
		System.out.println("------------doInflight-------------" + ++doInflight);
		super.doRollback(status);
	}
}

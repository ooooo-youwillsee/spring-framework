/*
 * Copyright 2002-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.transaction.annotation;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.transaction.config.TransactionManagementConfigUtils;
import org.springframework.transaction.interceptor.BeanFactoryTransactionAttributeSourceAdvisor;
import org.springframework.transaction.interceptor.TransactionAttributeSource;
import org.springframework.transaction.interceptor.TransactionInterceptor;

/**
 * {@code @Configuration} class that registers the Spring infrastructure beans
 * necessary to enable proxy-based annotation-driven transaction management.
 *
 * @author Chris Beams
 * @author Sebastien Deleuze
 * @since 3.1
 * @see EnableTransactionManagement
 * @see TransactionManagementConfigurationSelector
 */
@Configuration(proxyBeanMethods = false)
public class ProxyTransactionManagementConfiguration extends AbstractTransactionManagementConfiguration {

	@Bean(name = TransactionManagementConfigUtils.TRANSACTION_ADVISOR_BEAN_NAME)
	@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
	public BeanFactoryTransactionAttributeSourceAdvisor transactionAdvisor(
			TransactionAttributeSource transactionAttributeSource,
			TransactionInterceptor transactionInterceptor) {
		/*
		* 创建BeanFactoryTransactionAttributeSourceAdvisor对象
		*
		* 因为这个类是一个advisor（通知类），
		*   1、应该关注它的切点 --> getPointcut()方法, 该方法返回值是TransactionAttributeSourcePointcut(事务属性源切点对象)，而TransactionAttributeSourcePointcut这个类是一个抽象类，必须要实现getTransactionAttributeSource()方法，在这里返回的就是设置的事务属性源，而对于每一个切点，还需要关注getMethodMatcher(方法匹配器，在这个方法里面判断对该方法是否需要开启事务)和getClassFilter(类匹配器)
		*
		* 	2、应该关注它的advice --> 当对bean中的method匹配切点成功后，就要获得切点对应的拦截器(transactionInterceptor)，开始事务的流程
		*
		* */
		BeanFactoryTransactionAttributeSourceAdvisor advisor = new BeanFactoryTransactionAttributeSourceAdvisor();
		// 设置transactionAttributeSource （事务属性源）
		advisor.setTransactionAttributeSource(transactionAttributeSource);
		// 设置transactionInterceptor （事务拦截器）
		advisor.setAdvice(transactionInterceptor);
		if (this.enableTx != null) {
			advisor.setOrder(this.enableTx.<Integer>getNumber("order"));
		}
		return advisor;
	}

	@Bean
	@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
	public TransactionAttributeSource transactionAttributeSource() {
		// 基于注解的事务属性源， --> 事务切面  --> 用于判断代理对象的方法是否需要开启事务，可以从这个对象中获得事务拦截器
		return new AnnotationTransactionAttributeSource();
	}

	@Bean
	@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
	public TransactionInterceptor transactionInterceptor(
			TransactionAttributeSource transactionAttributeSource) {
		// 事务拦截器 --> 这个bean就是进行事务控制的核心bean
		TransactionInterceptor interceptor = new TransactionInterceptor();
		interceptor.setTransactionAttributeSource(transactionAttributeSource);
		if (this.txManager != null) {
			// 设置事务管理器
			interceptor.setTransactionManager(this.txManager);
		}
		return interceptor;
	}

}

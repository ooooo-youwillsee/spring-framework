/*
 * Copyright 2002-2018 the original author or authors.
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

import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.AdviceModeImportSelector;
import org.springframework.context.annotation.AutoProxyRegistrar;
import org.springframework.transaction.config.TransactionManagementConfigUtils;
import org.springframework.util.ClassUtils;

/**
 * Selects which implementation of {@link AbstractTransactionManagementConfiguration}
 * should be used based on the value of {@link EnableTransactionManagement#mode} on the
 * importing {@code @Configuration} class.
 *
 * @author Chris Beams
 * @author Juergen Hoeller
 * @since 3.1
 * @see EnableTransactionManagement
 * @see ProxyTransactionManagementConfiguration
 * @see TransactionManagementConfigUtils#TRANSACTION_ASPECT_CONFIGURATION_CLASS_NAME
 * @see TransactionManagementConfigUtils#JTA_TRANSACTION_ASPECT_CONFIGURATION_CLASS_NAME
 */
public class TransactionManagementConfigurationSelector extends AdviceModeImportSelector<EnableTransactionManagement> {

	/**
	 * Returns {@link ProxyTransactionManagementConfiguration} or
	 * {@code AspectJ(Jta)TransactionManagementConfiguration} for {@code PROXY}
	 * and {@code ASPECTJ} values of {@link EnableTransactionManagement#mode()},
	 * respectively.
	 */
	@Override
	protected String[] selectImports(AdviceMode adviceMode) {

		// 根据代理模式，来选择注册相应的bean，默认是proxy(jdk动态代理)
		switch (adviceMode) {
			/*
			* 注册AutoProxyRegistrar;
			* 	这个类实现了ImportBeanDefinitionRegistrar接口，registerBeanDefinitions() --> 用来注册BeanDefinition
			* 	主要是注册了autoProxyCreator，是否强制使用cglib代理
			*
			* 注册ProxyTransactionManagementConfiguration：
			* 	这个类是一个configuration类，实现了ImportAware，所以也能读取@EnableTransactionManagement的注解信息
			* 	主要是：
			* 	1、transactionAdvisor （事务通知）  --> 判断代理对象的方法是否需要开始事务，
			* 	2、transactionAttributeSource （事务属性源）  --> 真正的通知，判断对方法开启事务
			* 	3、transactionInterceptor（事务拦截器） --> 执行事务的拦截器
			*	4、transactionalEventListenerFactory （事务事件监听器工厂，在其父类声明了该bean） --< 创建事务监听器
			* 		对事务同步进行注册（默认只对在激活的事务状态情况下） ，也可以通过TransactionSynchronizationManager.registerSynchronization()来注册事务同步
			* */
			case PROXY:
				return new String[] {AutoProxyRegistrar.class.getName(),
						ProxyTransactionManagementConfiguration.class.getName()};
			case ASPECTJ:
				return new String[] {determineTransactionAspectClass()};
			default:
				return null;
		}
	}

	private String determineTransactionAspectClass() {
		return (ClassUtils.isPresent("javax.transaction.Transactional", getClass().getClassLoader()) ?
				TransactionManagementConfigUtils.JTA_TRANSACTION_ASPECT_CONFIGURATION_CLASS_NAME :
				TransactionManagementConfigUtils.TRANSACTION_ASPECT_CONFIGURATION_CLASS_NAME);
	}

}

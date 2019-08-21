/*
 * Copyright 2002-2017 the original author or authors.
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

package org.springframework.context.annotation;

import org.springframework.aop.config.AopConfigUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

/**
 * Registers an {@link org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator
 * AnnotationAwareAspectJAutoProxyCreator} against the current {@link BeanDefinitionRegistry}
 * as appropriate based on a given @{@link EnableAspectJAutoProxy} annotation.
 *
 * @author Chris Beams
 * @author Juergen Hoeller
 * @since 3.1
 * @see EnableAspectJAutoProxy
 */
class AspectJAutoProxyRegistrar implements ImportBeanDefinitionRegistrar {

	/**
	 * Register, escalate, and configure the AspectJ auto proxy creator based on the value
	 * of the @{@link EnableAspectJAutoProxy#proxyTargetClass()} attribute on the importing
	 * {@code @Configuration} class.
	 */
	@Override
	public void registerBeanDefinitions(
			AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

		/*
		* @EnableAspectJAutoProxy --> 这个注解用于启用注解的aop代理
		* 下面这个方法会注册 AnnotationAwareAspectJAutoProxyCreator的beanDefinition （基于注解的aspectJ自动代理创建器）
		*
		* 这个方法里面会去比较其他的autoProxyCreator，例如 InfrastructureAdvisorAutoProxyCreator、AspectJAwareAdvisorAutoProxyCreator
		*
		* 注册了autoproxyCreator会在AbstractAutoProxyCreator#postProcessBeforeInstantiation()中执行shouldSkip()方法，来查找所有的advisor
		* */

		AopConfigUtils.registerAspectJAnnotationAutoProxyCreatorIfNecessary(registry);

		// 获得注解属性
		AnnotationAttributes enableAspectJAutoProxy =
				AnnotationConfigUtils.attributesFor(importingClassMetadata, EnableAspectJAutoProxy.class);
		if (enableAspectJAutoProxy != null) {
			// proxyTargetClass为true，表示强制使用cglib代理
			if (enableAspectJAutoProxy.getBoolean("proxyTargetClass")) {
				// 设置属性proxyTargetClass为true
				AopConfigUtils.forceAutoProxyCreatorToUseClassProxying(registry);
			}
			/*
			* exposeProxy为true，表示强制使用暴露对象，
			* 在JdkDynamicAopProxy#invoke()，会执行AopContext.setCurrentProxy(proxy)来设置当前的代理对象
			* */
			if (enableAspectJAutoProxy.getBoolean("exposeProxy")) {
				// 设置属性exposeProxy为true
				AopConfigUtils.forceAutoProxyCreatorToExposeProxy(registry);
			}
		}
	}

}

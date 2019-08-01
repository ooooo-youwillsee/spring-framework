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

package com.ooooo.context;


import org.junit.Test;
import org.springframework.beans.factory.xml.XmlBeanFactoryTests;

/**
 * Miscellaneous tests for XML bean definitions.
 *
 * @author Juergen Hoeller
 * @author Rod Johnson
 * @author Rick Evans
 * @author Chris Beams
 * @author Sam Brannen
 */
public class XmlBeanFactoryTests_ooooo {

	private final XmlBeanFactoryTests xmlBeanFactoryTests = new XmlBeanFactoryTests();


	/**
	 * 测试单例bean
	 *
	 * 在这个测试中有简单的循环引用
	 */
	@Test
	public void testSingleton() {
		xmlBeanFactoryTests.testRefToSingleton();
	}


	/**
	 * 测试循环引用
	 *
	 * 在这个测试中有多个bean的循环引用
	 */
	@Test
	public void testCircularReferences() {
		xmlBeanFactoryTests.testCircularReferences();
	}

	/**
	 * 测试内嵌属性值， 也就是在当前beanFactory中获取不到，在parentBeanFactory中获取
	 */
	@Test
	public void testNestedPropertyValue() {
		xmlBeanFactoryTests.testNestedPropertyValue();

	}


	/**
	 * 测试autowire，  这个就是按照类型注入
	 */
	@Test
	public void testAutowireWithDefault() {
		xmlBeanFactoryTests.testAutowireWithDefault();
	}


	/**
	 * 测试autowire， 构造器注入
	 */
	@Test
	public void testAutowireByConstructor() {
		xmlBeanFactoryTests.testAutowireByConstructor();
	}


	/**
	 * 测试lookup-method
	 *
	 * lookup-method和replace-method 是在调用instantiate()方法时使用cglib来代理（方法拦截器）
	 */
	@Test
	public void lookupOverrideMethodsWithSetterInjection() {
		xmlBeanFactoryTests.lookupOverrideMethodsWithSetterInjection();
	}


	/**
	 * 测试工厂bean依赖普通bean
	 */
	@Test
	public void testCircularReferenceWithFactoryBeanFirst() {
		xmlBeanFactoryTests.testCircularReferenceWithFactoryBeanFirst();
	}

	/**
	 * 测试工厂bean的循环引用
	 */
	@Test
	public void testFactoryReferenceCircle() {
		xmlBeanFactoryTests.testFactoryReferenceCircle();
	}
}

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

package org.springframework.beans.factory.support;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedExceptionAction;

import org.springframework.beans.BeanInstantiationException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.lang.Nullable;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

/**
 * Simple object instantiation strategy for use in a BeanFactory.
 *
 * <p>Does not support Method Injection, although it provides hooks for subclasses
 * to override to add Method Injection support, for example by overriding methods.
 *
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @since 1.1
 */
public class SimpleInstantiationStrategy implements InstantiationStrategy {

	private static final ThreadLocal<Method> currentlyInvokedFactoryMethod = new ThreadLocal<>();


	/**
	 * Return the factory method currently being invoked or {@code null} if none.
	 * <p>Allows factory method implementations to determine whether the current
	 * caller is the container itself as opposed to user code.
	 */
	@Nullable
	public static Method getCurrentlyInvokedFactoryMethod() {
		return currentlyInvokedFactoryMethod.get();
	}


	@Override
	public Object instantiate(RootBeanDefinition bd, @Nullable String beanName, BeanFactory owner) {
		// Don't override the class with CGLIB if no overrides.

		// 如果有Overrides方法，例如lookup-method和replace-method方法
		if (!bd.hasMethodOverrides()) {
			Constructor<?> constructorToUse;
			synchronized (bd.constructorArgumentLock) {

				// 从缓存中获取
				constructorToUse = (Constructor<?>) bd.resolvedConstructorOrFactoryMethod;

				// 如果constructorToUse为null，则会获得默认的构造方法
				if (constructorToUse == null) {
					final Class<?> clazz = bd.getBeanClass();
					if (clazz.isInterface()) {
						throw new BeanInstantiationException(clazz, "Specified class is an interface");
					}
					try {
						if (System.getSecurityManager() != null) {
							constructorToUse = AccessController.doPrivileged(
									(PrivilegedExceptionAction<Constructor<?>>) clazz::getDeclaredConstructor);
						}
						else {
							constructorToUse = clazz.getDeclaredConstructor();
						}

						// 缓存构造方法
						bd.resolvedConstructorOrFactoryMethod = constructorToUse;
					}
					catch (Throwable ex) {
						throw new BeanInstantiationException(clazz, "No default constructor found", ex);
					}
				}
			}

			// 直接实例化
			return BeanUtils.instantiateClass(constructorToUse);
		}
		else {
			// Must generate CGLIB subclass.

			// 覆盖方法来实例化， 内部使用cglib来实例化
			return instantiateWithMethodInjection(bd, beanName, owner);
		}
	}

	/**
	 * Subclasses can override this method, which is implemented to throw
	 * UnsupportedOperationException, if they can instantiate an object with
	 * the Method Injection specified in the given RootBeanDefinition.
	 * Instantiation should use a no-arg constructor.
	 */
	protected Object instantiateWithMethodInjection(RootBeanDefinition bd, @Nullable String beanName, BeanFactory owner) {
		throw new UnsupportedOperationException("Method Injection not supported in SimpleInstantiationStrategy");
	}

	@Override
	public Object instantiate(RootBeanDefinition bd, @Nullable String beanName, BeanFactory owner,
			final Constructor<?> ctor, Object... args) {

		// bd.hasMethodOverrides()为true  -> 有lookup-method和replace-method
		if (!bd.hasMethodOverrides()) {
			if (System.getSecurityManager() != null) {
				// use own privileged to change accessibility (when security is on)
				AccessController.doPrivileged((PrivilegedAction<Object>) () -> {
					ReflectionUtils.makeAccessible(ctor);
					return null;
				});
			}
			// 实例化
			return BeanUtils.instantiateClass(ctor, args);
		}
		else {
			// 实例化时将lookup-method和replace-method方法替换
			return instantiateWithMethodInjection(bd, beanName, owner, ctor, args);
		}
	}

	/**
	 * Subclasses can override this method, which is implemented to throw
	 * UnsupportedOperationException, if they can instantiate an object with
	 * the Method Injection specified in the given RootBeanDefinition.
	 * Instantiation should use the given constructor and parameters.
	 */
	protected Object instantiateWithMethodInjection(RootBeanDefinition bd, @Nullable String beanName,
			BeanFactory owner, @Nullable Constructor<?> ctor, Object... args) {

		throw new UnsupportedOperationException("Method Injection not supported in SimpleInstantiationStrategy");
	}

	@Override
	public Object instantiate(RootBeanDefinition bd, @Nullable String beanName, BeanFactory owner,
			@Nullable Object factoryBean, final Method factoryMethod, Object... args) {

		try {
			if (System.getSecurityManager() != null) {
				AccessController.doPrivileged((PrivilegedAction<Object>) () -> {
					ReflectionUtils.makeAccessible(factoryMethod);
					return null;
				});
			}
			else {
				// 私有方法访问
				ReflectionUtils.makeAccessible(factoryMethod);
			}

			// 获得当前执行的工厂方法
			Method priorInvokedFactoryMethod = currentlyInvokedFactoryMethod.get();
			try {
				// 设置当前执行的工厂方法
				currentlyInvokedFactoryMethod.set(factoryMethod);
				// invoke()方法执行， 如果是静态工厂方法，factoryBean为null， 如果是factoryBean，直接执行
				Object result = factoryMethod.invoke(factoryBean, args);
				if (result == null) {
					// result为null，返回NullBean对象
					result = new NullBean();
				}
				return result;
			}
			finally {
				// 下面这个finally语句块，是为了确保发生异常了，currentlyInvokedFactoryMethod恢复到之前的状态
				if (priorInvokedFactoryMethod != null) {
					currentlyInvokedFactoryMethod.set(priorInvokedFactoryMethod);
				}
				else {
					currentlyInvokedFactoryMethod.remove();
				}
			}
		}
		catch (IllegalArgumentException ex) {
			throw new BeanInstantiationException(factoryMethod,
					"Illegal arguments to factory method '" + factoryMethod.getName() + "'; " +
					"args: " + StringUtils.arrayToCommaDelimitedString(args), ex);
		}
		catch (IllegalAccessException ex) {
			throw new BeanInstantiationException(factoryMethod,
					"Cannot access factory method '" + factoryMethod.getName() + "'; is it public?", ex);
		}
		catch (InvocationTargetException ex) {
			String msg = "Factory method '" + factoryMethod.getName() + "' threw exception";
			if (bd.getFactoryBeanName() != null && owner instanceof ConfigurableBeanFactory &&
					((ConfigurableBeanFactory) owner).isCurrentlyInCreation(bd.getFactoryBeanName())) {
				msg = "Circular reference involving containing bean '" + bd.getFactoryBeanName() + "' - consider " +
						"declaring the factory method as static for independence from its containing instance. " + msg;
			}
			throw new BeanInstantiationException(factoryMethod, msg, ex.getTargetException());
		}
	}

}

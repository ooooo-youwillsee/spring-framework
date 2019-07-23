package com.ooooo.beans;

import org.junit.Test;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.SimpleBeanDefinitionRegistry;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.tests.sample.beans.TestBean;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * @author leizhijie
 * @since 2019-07-17 13:43
 * <p>
 * 阅读源码 --- XmlBeanDefinitionReader 读取spring配置文件
 */
public class XmlBeanDefinitionReaderTests {


	/**
	 * 读取 com.ooooo.beans 中的test.xml
	 * 		1、new XmlBeanDefinitionReader(registry)
	 * 			注册了resourceLoader（路径匹配资源加载器），environment (标准环境)
	 * 		2、loadBeanDefinitions(resource)
	 * 			加载beanDefinitions
	 * 			2.1  loadBeanDefinitions
	 * 此测试代码会在检测验证模式中抛出异常而结束
	 */
	@Test
	public void withOpenInputStream() {
		SimpleBeanDefinitionRegistry registry = new SimpleBeanDefinitionRegistry();
		Resource resource = new InputStreamResource(getClass().getResourceAsStream("test.xml"));
		assertThatExceptionOfType(BeanDefinitionStoreException.class).isThrownBy(() ->
				new XmlBeanDefinitionReader(registry).loadBeanDefinitions(resource));
	}

	/**
	 * 对比withOpenInputStream，该测试指定了验证模式
	 */
	@Test
	public void withOpenInputStreamAndExplicitValidationMode() {
		SimpleBeanDefinitionRegistry registry = new SimpleBeanDefinitionRegistry();
		Resource resource = new InputStreamResource(getClass().getResourceAsStream("test.xml"));
		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(registry);
		reader.setValidationMode(XmlBeanDefinitionReader.VALIDATION_DTD);
		reader.loadBeanDefinitions(resource);
		testBeanDefinitions(registry);
	}


	/**
	 * 测试import标签
	 */
	@Test
	public void withImport() {
		SimpleBeanDefinitionRegistry registry = new SimpleBeanDefinitionRegistry();
		Resource resource = new ClassPathResource("import.xml", getClass());
		new XmlBeanDefinitionReader(registry).loadBeanDefinitions(resource);
		testBeanDefinitions(registry);
	}





	private void testBeanDefinitions(BeanDefinitionRegistry registry) {
		assertThat(registry.getBeanDefinitionCount()).isEqualTo(24);
		assertThat(registry.getBeanDefinitionNames().length).isEqualTo(24);
		assertThat(Arrays.asList(registry.getBeanDefinitionNames()).contains("rod")).isTrue();
		assertThat(Arrays.asList(registry.getBeanDefinitionNames()).contains("aliased")).isTrue();
		assertThat(registry.containsBeanDefinition("rod")).isTrue();
		assertThat(registry.containsBeanDefinition("aliased")).isTrue();
		assertThat(registry.getBeanDefinition("rod").getBeanClassName()).isEqualTo(TestBean.class.getName());
		assertThat(registry.getBeanDefinition("aliased").getBeanClassName()).isEqualTo(TestBean.class.getName());
		assertThat(registry.isAlias("youralias")).isTrue();
		String[] aliases = registry.getAliases("aliased");
		assertThat(aliases.length).isEqualTo(2);
		assertThat(ObjectUtils.containsElement(aliases, "myalias")).isTrue();
		assertThat(ObjectUtils.containsElement(aliases, "youralias")).isTrue();
	}

}

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

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.core.AliasRegistry;

/**
 * todo:BeanDefinitionRegistry是一个接口，它定义了关于BeanDefinition的注册、移除、查询等一系列的操作
 * @author Juergen Hoeller
 * @see org.springframework.beans.factory.config.BeanDefinition
 * @see AbstractBeanDefinition
 * @see RootBeanDefinition
 * @see ChildBeanDefinition
 * @see DefaultListableBeanFactory
 * @see org.springframework.context.support.GenericApplicationContext
 * @see org.springframework.beans.factory.xml.XmlBeanDefinitionReader
 * @see PropertiesBeanDefinitionReader
 * @since 26.11.2003
 */
public interface BeanDefinitionRegistry extends AliasRegistry {

	//注册BeanDefinition
	void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) throws BeanDefinitionStoreException;

	//移除BeanDefinition
	void removeBeanDefinition(String beanName) throws NoSuchBeanDefinitionException;

	//获取BeanDefinition
	BeanDefinition getBeanDefinition(String beanName) throws NoSuchBeanDefinitionException;

	//根据beanName判断容器是否存在对应的 BeanDefinition
	boolean containsBeanDefinition(String beanName);

	//获取所有的BeanDefinition的name
	String[] getBeanDefinitionNames();

	//获取BeanDefinition数量
	int getBeanDefinitionCount();

	//判断beanName是否被占用
	boolean isBeanNameInUse(String beanName);

}

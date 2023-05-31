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

package org.springframework.beans.factory.config;

import org.springframework.beans.BeansException;

//BeanFactoryPostProcessor是Bean工厂的后置处理器，一般用来修改上下文中的BeanDefinition，修改Bean的属性值
@FunctionalInterface
public interface BeanFactoryPostProcessor {


	// 入参是一个 Bean 工厂：ConfigurableListableBeanFactory。该方法执行时，所有BeanDefinition都已被加载，但还未实例化 Bean。
	// 可以对其进行覆盖或添加属性，甚至可以用于初始化 Bean
	void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException;

}

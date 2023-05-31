/*
 * Copyright 2002-2015 the original author or authors.
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

import org.springframework.lang.Nullable;

/**
 * Interface that defines a registry for shared bean instances.
 * Can be implemented by {@link org.springframework.beans.factory.BeanFactory}
 * implementations in order to expose their singleton management facility
 * in a uniform manner.
 *
 * <p>The {@link ConfigurableBeanFactory} interface extends this interface.
 *
 * @author Juergen Hoeller
 * @since 2.0
 * @see ConfigurableBeanFactory
 * @see org.springframework.beans.factory.support.DefaultSingletonBeanRegistry
 * @see org.springframework.beans.factory.support.AbstractBeanFactory
 */
public interface SingletonBeanRegistry {

	// 注册单例Bean。其实就是将该Bean保存到一个专门存储单例Bean实例的Map中，Key是beanName，Value是对应的单例Bean实例
	void registerSingleton(String beanName, Object singletonObject);

	// 通过beanName获取该单例 Bean 实例
	@Nullable
	Object getSingleton(String beanName);

	//通过beanName判断该单例Bean实例是否存在
	boolean containsSingleton(String beanName);

	// 返回所有单例Bean的名称
	String[] getSingletonNames();


	//返回已注册的单例Bean实例数量
	int getSingletonCount();


	//返回当前使用的单例锁，主要提供给外部协作者使用
	Object getSingletonMutex();

}

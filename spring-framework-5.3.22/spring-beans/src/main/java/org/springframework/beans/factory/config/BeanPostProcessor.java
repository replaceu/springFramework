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
import org.springframework.lang.Nullable;

//BeanPostProcessor和InitializingBean有点类似，也是可以在 Bean 的生命周期执行自定义操作，一般称之为Bean的后置处理器，不同的是，
//BeanPostProcessor可以在Bean初始化前、后执行自定义操作，且针对的目标也不同，InitializingBean针对的是实现InitializingBean接口的 Bean，而BeanPostProcessor针对的是所有的 Bean
public interface BeanPostProcessor {

	//Bean初始化前调用
	@Nullable
	default Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	//Bean初始化后调用
	@Nullable
	default Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

}

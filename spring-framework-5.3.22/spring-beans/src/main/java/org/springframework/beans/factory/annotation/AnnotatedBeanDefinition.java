/*
 * Copyright 2002-2014 the original author or authors.
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

package org.springframework.beans.factory.annotation;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.MethodMetadata;
import org.springframework.lang.Nullable;

/**
 * AnnotatedBeanDefinition 是 BeanDefinition 子接口之一，该接口扩展了 BeanDefinition 的功能，其用来操作注解元数据。
 * 一般情况下，通过注解方式得到的Bea（@Component、@Bean），其 BeanDefinition 类型都是该接口的实现类
 *
 * @author Juergen Hoeller
 * @since 2.5
 * @see AnnotatedGenericBeanDefinition
 * @see org.springframework.core.type.AnnotationMetadata
 */
public interface AnnotatedBeanDefinition extends BeanDefinition {

	//获得当前Bean的注解元数据，AnnotationMetadata：主要对Bean的注解信息进行操作，如：获取当前 Bean 标注的所有注解、判断是否包含指定注解
	AnnotationMetadata getMetadata();

	//获得当前Bean的工厂方法上的元数据，提供获取方法名称、此方法所属类的全类名、是否是抽象方法、判断是否是静态方法、判断是否是final方法等
	@Nullable
	MethodMetadata getFactoryMethodMetadata();
}

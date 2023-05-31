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

package org.springframework.beans.factory.support;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.lang.Nullable;
import org.springframework.util.ObjectUtils;

/**
 * 该类继承自 AbstractBeanDefinition。其相当于一个子类，不可以单独存在，必须依赖一个父 BeanDetintion，构造 ChildBeanDefinition 时，
 * 通过构造方法传入父 BeanDetintion 的名称或通过 setParentName 设置父名称。它可以从父类继承方法参数、属性值，并可以重写父类的方法，
 * 同时也可以增加新的属性或者方法。若重新定义 init 方法，destroy 方法或者静态工厂方法，ChildBeanDefinition 会重写父类的设置。
 * 从 Spring 2.5 开始，以编程方式注册 Bean 定义的首选方法是 GenericBeanDefinition，GenericBeanDefinition 可以有效替代 ChildBeanDefinition 的绝大分部使用场合。
 *
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @see GenericBeanDefinition
 * @see RootBeanDefinition
 */
@SuppressWarnings("serial")
public class ChildBeanDefinition extends AbstractBeanDefinition {

	@Nullable
	private String parentName;


	/**
	 * Create a new ChildBeanDefinition for the given parent, to be
	 * configured through its bean properties and configuration methods.
	 * @param parentName the name of the parent bean
	 * @see #setBeanClass
	 * @see #setScope
	 * @see #setConstructorArgumentValues
	 * @see #setPropertyValues
	 */
	public ChildBeanDefinition(String parentName) {
		super();
		this.parentName = parentName;
	}

	/**
	 * Create a new ChildBeanDefinition for the given parent.
	 * @param parentName the name of the parent bean
	 * @param pvs the additional property values of the child
	 */
	public ChildBeanDefinition(String parentName, MutablePropertyValues pvs) {
		super(null, pvs);
		this.parentName = parentName;
	}

	/**
	 * Create a new ChildBeanDefinition for the given parent.
	 * @param parentName the name of the parent bean
	 * @param cargs the constructor argument values to apply
	 * @param pvs the additional property values of the child
	 */
	public ChildBeanDefinition(
			String parentName, ConstructorArgumentValues cargs, MutablePropertyValues pvs) {

		super(cargs, pvs);
		this.parentName = parentName;
	}

	/**
	 * Create a new ChildBeanDefinition for the given parent,
	 * providing constructor arguments and property values.
	 * @param parentName the name of the parent bean
	 * @param beanClass the class of the bean to instantiate
	 * @param cargs the constructor argument values to apply
	 * @param pvs the property values to apply
	 */
	public ChildBeanDefinition(
			String parentName, Class<?> beanClass, ConstructorArgumentValues cargs, MutablePropertyValues pvs) {

		super(cargs, pvs);
		this.parentName = parentName;
		setBeanClass(beanClass);
	}

	/**
	 * Create a new ChildBeanDefinition for the given parent,
	 * providing constructor arguments and property values.
	 * Takes a bean class name to avoid eager loading of the bean class.
	 * @param parentName the name of the parent bean
	 * @param beanClassName the name of the class to instantiate
	 * @param cargs the constructor argument values to apply
	 * @param pvs the property values to apply
	 */
	public ChildBeanDefinition(
			String parentName, String beanClassName, ConstructorArgumentValues cargs, MutablePropertyValues pvs) {

		super(cargs, pvs);
		this.parentName = parentName;
		setBeanClassName(beanClassName);
	}

	/**
	 * Create a new ChildBeanDefinition as deep copy of the given
	 * bean definition.
	 * @param original the original bean definition to copy from
	 */
	public ChildBeanDefinition(ChildBeanDefinition original) {
		super(original);
	}


	@Override
	public void setParentName(@Nullable String parentName) {
		this.parentName = parentName;
	}

	@Override
	@Nullable
	public String getParentName() {
		return this.parentName;
	}

	@Override
	public void validate() throws BeanDefinitionValidationException {
		super.validate();
		if (this.parentName == null) {
			throw new BeanDefinitionValidationException("'parentName' must be set in ChildBeanDefinition");
		}
	}


	@Override
	public AbstractBeanDefinition cloneBeanDefinition() {
		return new ChildBeanDefinition(this);
	}

	@Override
	public boolean equals(@Nullable Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof ChildBeanDefinition)) {
			return false;
		}
		ChildBeanDefinition that = (ChildBeanDefinition) other;
		return (ObjectUtils.nullSafeEquals(this.parentName, that.parentName) && super.equals(other));
	}

	@Override
	public int hashCode() {
		return ObjectUtils.nullSafeHashCode(this.parentName) * 29 + super.hashCode();
	}

	@Override
	public String toString() {
		return "Child bean with parent '" + this.parentName + "': " + super.toString();
	}

}

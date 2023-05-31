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

package org.springframework.beans.factory;

/**
 * @author Chris Beams
 * @author Juergen Hoeller
 * @since 3.1
 */

//Aware自身是一个顶级接口，它有一系列子接口，在一个Bean中实现这些子接口并重写里面的set方法后，
//Spring容器启动时，就会回调该 set 方法，而相应的对象会通过方法参数传递进去
public interface Aware {

}

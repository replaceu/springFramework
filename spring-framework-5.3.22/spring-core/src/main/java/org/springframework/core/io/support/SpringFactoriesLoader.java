/*
 * Copyright 2002-2020 the original author or authors.
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

package org.springframework.core.io.support;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.core.io.UrlResource;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ConcurrentReferenceHashMap;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

/**
 * General purpose factory loading mechanism for internal use within the framework.
 *
 * <p>{@code SpringFactoriesLoader} {@linkplain #loadFactories loads} and instantiates
 * factories of a given type from {@value #FACTORIES_RESOURCE_LOCATION} files which
 * may be present in multiple JAR files in the classpath. The {@code spring.factories}
 * file must be in {@link Properties} format, where the key is the fully qualified
 * name of the interface or abstract class, and the value is a comma-separated list of
 * implementation class names. For example:
 *
 * <pre class="code">example.MyService=example.MyServiceImpl1,example.MyServiceImpl2</pre>
 *
 * where {@code example.MyService} is the name of the interface, and {@code MyServiceImpl1}
 * and {@code MyServiceImpl2} are two implementations.
 *
 * @author Arjen Poutsma
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @since 3.2
 */

/**
 * SpringFactoriesLoader的主要作用是通过类路径下的META-INF/spring.factories文件获取工厂类接口的实现类，初始化并保存在缓存中
 * 以供SpringBoot启动过程中各个阶段的调用
 */
public final class SpringFactoriesLoader {

	/**
	 * The location to look for factories.
	 * <p>Can be present in multiple JAR files.
	 */
	public static final String FACTORIES_RESOURCE_LOCATION = "META-INF/spring.factories";

	private static final Log logger = LogFactory.getLog(SpringFactoriesLoader.class);

	//ConcurrentReferenceHashMap一种存储的数据结构，可用于缓存
	static final Map<ClassLoader, Map<String, List<String>>> cache = new ConcurrentReferenceHashMap<>();

	private SpringFactoriesLoader() {
	}

	/**
	 * Load and instantiate the factory implementations of the given type from
	 * {@value #FACTORIES_RESOURCE_LOCATION}, using the given class loader.
	 * <p>The returned factories are sorted through {@link AnnotationAwareOrderComparator}.
	 * <p>If a custom instantiation strategy is required, use {@link #loadFactoryNames}
	 * to obtain all registered factory names.
	 * <p>As of Spring Framework 5.3, if duplicate implementation class names are
	 * discovered for a given factory type, only one instance of the duplicated
	 * implementation type will be instantiated.
	 * @param factoryType the interface or abstract class representing the factory
	 * @param classLoader the ClassLoader to use for loading (can be {@code null} to use the default)
	 * @throws IllegalArgumentException if any factory implementation class cannot
	 * be loaded or if an error occurs while instantiating any factory
	 * @see #loadFactoryNames
	 */
	//todo：根据参数factoryClass获取spring.factories下配置的所有实现类实例
	public static <T> List<T> loadFactories(Class<T> factoryType, @Nullable ClassLoader classLoader) {
		Assert.notNull(factoryType, "'factoryType' must not be null");
		ClassLoader classLoaderToUse = classLoader;
		if (classLoaderToUse == null) {
			classLoaderToUse = SpringFactoriesLoader.class.getClassLoader();
		}
		List<String> factoryImplementationNames = loadFactoryNames(factoryType, classLoaderToUse);
		if (logger.isTraceEnabled()) {
			logger.trace("Loaded [" + factoryType.getName() + "] names: " + factoryImplementationNames);
		}
		// 因为loadFactories是返回具体的实现类，又因为spring.factories的配置value可以多个并用,\区分
		// 因此 result的个数等于上面获取到实现类全限定类名的个数
		List<T> result = new ArrayList<>(factoryImplementationNames.size());
		for (String factoryImplementationName : factoryImplementationNames) {
			//todo：instantiateFactory将获取到的全限定类名 加载进jvm并生成对应的类对象
			result.add(instantiateFactory(factoryImplementationName, factoryType, classLoaderToUse));
		}
		//排序
		AnnotationAwareOrderComparator.sort(result);
		return result;
	}

	/**
	 * Load the fully qualified class names of factory implementations of the
	 * given type from {@value #FACTORIES_RESOURCE_LOCATION}, using the given
	 * class loader.
	 * <p>As of Spring Framework 5.3, if a particular implementation class name
	 * is discovered more than once for the given factory type, duplicates will
	 * be ignored.
	 * @param factoryType the interface or abstract class representing the factory
	 * @param classLoader the ClassLoader to use for loading resources; can be
	 * {@code null} to use the default
	 * @throws IllegalArgumentException if an error occurs while loading factory names
	 * @see #loadFactories
	 */

	//todo：是根据参数factoryClass获取spring.factories下配置的所有实现类的全限定类名

	/**
	 * @param factoryType：需要被加载的工厂类的class
	 * @param classLoader：类加载器
	 * @return
	 */
	public static List<String> loadFactoryNames(Class<?> factoryType, @Nullable ClassLoader classLoader) {
		ClassLoader classLoaderToUse = classLoader;
		if (classLoaderToUse == null) {
			//如果没有传入类加载器，使用SpringFactoriesLoader的类加载器
			classLoaderToUse = SpringFactoriesLoader.class.getClassLoader();
		}
		//获取该类的全类限定名字
		String factoryTypeName = factoryType.getName();
		return loadSpringFactories(classLoaderToUse).getOrDefault(factoryTypeName, Collections.emptyList());
	}

	/**
	 * 该方法是最核心部分
	 * 1.从项目中找到所有META—INF/spring。factories
	 * 2.解析所有spring.factories文件，生成一个Map<一个接口类，该接口类所有具体实现类的集合>，即Map<String,List<String>>
	 * @param classLoader
	 * @return
	 */
	private static Map<String, List<String>> loadSpringFactories(ClassLoader classLoader) {
		Map<String, List<String>> result = cache.get(classLoader);
		//如果已经获取过了，那么就直接返回
		if (result != null) { return result; }

		result = new HashMap<>();
		try {
			//todo：加载spring.factories资源并封装为URL对象
			Enumeration<URL> urls = classLoader.getResources(FACTORIES_RESOURCE_LOCATION);
			while (urls.hasMoreElements()) {
				URL url = urls.nextElement();
				// UrlResource 类是spring 对URL资源一种封装而已
				UrlResource resource = new UrlResource(url);
				//解析properties文件，因为spring.factories 本身就是以key-->value来书写的
				Properties properties = PropertiesLoaderUtils.loadProperties(resource);
				for (Map.Entry<?, ?> entry : properties.entrySet()) {
					//key：接口全限定名字
					String factoryTypeName = ((String) entry.getKey()).trim();
					//因为value可以是多个，并且要求各个之间用",\"来隔开，commaDelimitedListToStringArray方法就是把value的值，解析并封装成数组
					String[] factoryImplementationNames = StringUtils.commaDelimitedListToStringArray((String) entry.getValue());
					for (String factoryImplementationName : factoryImplementationNames) {
						result.computeIfAbsent(factoryTypeName, key -> new ArrayList<>()).add(factoryImplementationName.trim());
					}
				}
			}

			// Replace all lists with unmodifiable lists containing unique elements
			//对于Map中的value值做一系列变化操作，比如原来key：5，value：5 可以改成key：5 value：5-5
			result.replaceAll((factoryType, implementations) -> implementations.stream().distinct().collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList)));
			cache.put(classLoader, result);
		} catch (IOException ex) {
			throw new IllegalArgumentException("Unable to load factories from location [" + FACTORIES_RESOURCE_LOCATION + "]", ex);
		}
		return result;
	}

	/**
	 * @param factoryImplementationName:实现类的全限定类名
	 * @param factoryType：接口的类型
	 * @param classLoader：类加载器
	 * @param <T>
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static <T> T instantiateFactory(String factoryImplementationName, Class<T> factoryType, ClassLoader classLoader) {
		try {
			//todo：这就是熟悉的加载字节码文件，spring将一般使用的反射方式class.forName进行了封装
			Class<?> factoryImplementationClass = ClassUtils.forName(factoryImplementationName, classLoader);
			//todo：判断factoryImplementationClass(实现类)是不是factoryType接口的子类
			if (!factoryType.isAssignableFrom(factoryImplementationClass)) { throw new IllegalArgumentException("Class [" + factoryImplementationName + "] is not assignable to factory type [" + factoryType.getName() + "]"); }
			//todo：newInstance通过反射创建（实现类）对象并返回
			return (T) ReflectionUtils.accessibleConstructor(factoryImplementationClass).newInstance();
		} catch (Throwable ex) {
			throw new IllegalArgumentException("Unable to instantiate factory class [" + factoryImplementationName + "] for factory type [" + factoryType.getName() + "]", ex);
		}
	}

}

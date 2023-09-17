/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2023 All Rights Reserved.
 */
package cn.ntopic.param.bootstrap;

import cn.ntopic.param.NTParamUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * 应用上下文初始化器
 *
 * @author obullxl 2023年09月09日: 新增
 */
public class NTParamContextInitializerImpl implements ApplicationContextInitializer<ConfigurableApplicationContext>, BeanDefinitionRegistryPostProcessor {
    /**
     * Initialize the given application context.
     *
     * @param context the application to configure
     */
    @Override
    public void initialize(ConfigurableApplicationContext context) {
        // 注册Bean上下文初始化后处理器，用于手工注册Bean
        context.addBeanFactoryPostProcessor(this);
    }

    /**
     * Modify the application context's internal bean definition registry after its
     * standard initialization. All regular bean definitions will have been loaded,
     * but no beans will have been instantiated yet. This allows for adding further
     * bean definitions before the next post-processing phase kicks in.
     *
     * @param registry the bean definition registry used by the application context
     * @throws BeansException in case of errors
     */
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        // 在ConfigurationClassPostProcessor前手动注册Bean，保障优先于其他Bean初始化
        registry.registerBeanDefinition("ntParamUtils", new RootBeanDefinition(NTParamUtils.class));
    }

    /**
     * Modify the application context's internal bean factory after its standard
     * initialization. All bean definitions will have been loaded, but no beans
     * will have been instantiated yet. This allows for overriding or adding
     * properties even to eager-initializing beans.
     *
     * @param beanFactory the bean factory used by the application context
     * @throws BeansException in case of errors
     */
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
    }
}

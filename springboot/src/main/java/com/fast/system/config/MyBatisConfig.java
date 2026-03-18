package com.fast.system.config;

import org.apache.ibatis.io.VFS;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.*;

/**
 * Mybatis支持*匹配扫描包
 *
 * @author fast
 */
@Configuration
public class MyBatisConfig {
    @jakarta.annotation.Resource
    private Environment env;

    static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";

    public static String setTypeAliasesPackage(String typeAliasesPackage) {
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resolver);
        Set<String> result = new HashSet<>();

        try {
            for (String aliasesPackage : typeAliasesPackage.split(",")) {
                String resourcePattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
                        + ClassUtils.convertClassNameToResourcePath(aliasesPackage.trim()) + "/" + DEFAULT_RESOURCE_PATTERN;
                Resource[] resources = resolver.getResources(resourcePattern);

                for (Resource resource : resources) {
                    if (resource.isReadable()) {
                        MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
                        result.add(Class.forName(metadataReader.getClassMetadata().getClassName()).getPackage().getName());
                    }
                }
            }

            if (!result.isEmpty()) {
                return String.join(",", result);
            } else {
                throw new RuntimeException("mybatis typeAliasesPackage 路径扫描错误,参数typeAliasesPackage:" + typeAliasesPackage + "未找到任何包");
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        String typeAliasesPackage = env.getProperty("mybatis.typeAliasesPackage");
        String mapperLocations = env.getProperty("mybatis.mapperLocations");
        String configLocation = env.getProperty("mybatis.configLocation");

        typeAliasesPackage = setTypeAliasesPackage(typeAliasesPackage);
        VFS.addImplClass(SpringBootVFS.class);

        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setTypeAliasesPackage(typeAliasesPackage);
        sessionFactory.setMapperLocations(resolveMapperLocations(mapperLocations.split(",")));

        // 只有当configLocation配置存在且不为空时才设置
        if (configLocation != null && !configLocation.isEmpty()) {
            sessionFactory.setConfigLocation(new PathMatchingResourcePatternResolver().getResource(configLocation));
        }

        return sessionFactory.getObject();
    }

    public Resource[] resolveMapperLocations(String[] mapperLocations) throws IOException {
        ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
        List<Resource> resources = new ArrayList<>();

        if (mapperLocations != null) {
            for (String mapperLocation : mapperLocations) {
                resources.addAll(Arrays.asList(resourceResolver.getResources(mapperLocation)));
            }
        }
        return resources.toArray(new Resource[0]);
    }
}

package com.debuggeandoideas.erp_lite.configs;

import com.debuggeandoideas.erp_lite.persistence.aws.models.AwsConfigModel;
import com.debuggeandoideas.erp_lite.persistence.rest.models.JsonplaceholderConfigModel;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@EnableConfigurationProperties({
        AwsConfigModel.class,
        JsonplaceholderConfigModel.class
})
@PropertySource(
        value = "classpath:aws/aws.yaml",
        factory = YamlPropertySourceFactory.class
)
@PropertySource(
        value = "classpath:jsonplaceholder/jsonplaceholder.yaml",
        factory = YamlPropertySourceFactory.class
)
public class YmlConfig {
}
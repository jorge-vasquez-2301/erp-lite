package com.debuggeandoideas.erp_lite.configs;

import com.debuggeandoideas.erp_lite.persistence.aws.models.AwsConfigModel;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@EnableConfigurationProperties(AwsConfigModel.class)
@PropertySource(value = "classpath:aws/aws.yaml", factory = YamlPropertySourceFactory.class)
public class YmlConfig {
}
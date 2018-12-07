package testSystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebMvc
public class WebMvcConfig extends WebMvcConfigurerAdapter {


    // Static Resource Config
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Css resource.
        registry.addResourceHandler("/styles/**")
                .addResourceLocations("/resources/css/")
                .addResourceLocations("bower_components/bootstrap/dist/css/");
        //JS resource.
        registry.addResourceHandler("/scripts/**")
                .addResourceLocations("/resources/js/")
                .addResourceLocations("bower_components/jquery/dist/")
                .addResourceLocations("bower_components/bootstrap/dist/js/")
                .addResourceLocations("bower_components/popper.js/");
    }

//    @Bean
//    public CommonsMultipartResolver multipartResolver() {
//        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
//        resolver.setDefaultEncoding("UTF-8");
//        return resolver;
//    }
@Override
public void addViewControllers(ViewControllerRegistry registry) {
    registry.addViewController("/autorisation").setViewName("autorisation");
}

}


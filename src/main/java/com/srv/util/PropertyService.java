package com.srv.util;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.Assert;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class PropertyService implements ApplicationContextAware, InitializingBean, DisposableBean, ResourceLoaderAware {

    private static final Logger logger = LoggerFactory.getLogger("PropertyService.class");


    private PropertiesConfiguration propertiesConfiguration = null;
    private ResourceLoader resourceLoader = null;


    private MessageSource messageSource;
    private Set<?> extFileName;
    private Map<?, ?> properties;


    public Set<?> getExtFileName() {
        return extFileName;
    }

    public void setExtFileName(Set<?> extFileName) {
        this.extFileName = extFileName;
    }

    @Override
    public void destroy() throws Exception {
        propertiesConfiguration = null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        try{
            propertiesConfiguration = new PropertiesConfiguration();

            if(extFileName != null){
                refreashPropertyFiles();
            }


            if(properties != null){
                Iterator<?> it = properties.entrySet().iterator();
                while(it.hasNext()){
                    Map.Entry entry = (Map.Entry)it.next();
                    String key = (String)entry.getKey();
                    String value = (String)entry.getValue();


                    propertiesConfiguration.addProperty(key, value);
                }
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.messageSource = null;//(MessageSource) applicationContext.getBean("mesageSource");

    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }


    private void loadPropertyResource(String location, String encoding) throws Exception {
        if(resourceLoader instanceof ResourcePatternResolver){
            try{
                Resource[] resources = ((ResourcePatternResolver)resourceLoader).getResources(location);
                loadPropertyLoop(resources, encoding);
            }catch (IOException ex){
                ex.printStackTrace();
            }
        }else{
            Resource resource = resourceLoader.getResource(location);
            loadPropertyRes(resource, encoding);
        }
    }

    private void loadPropertyLoop(Resource[] resources, String encoding) throws Exception {
        Assert.notNull(resources, "Resource array must not be null");

        for(int i = 0 ; i < resources.length; i++){
            loadPropertyRes(resources[i], encoding);
        }
    }

    private void loadPropertyRes(Resource resource, String encodding) throws Exception{
        PropertiesConfiguration tProperty = new PropertiesConfiguration();
        InputStreamReader reader = null;
        if(encodding != null){
            reader = new InputStreamReader(resource.getInputStream(), encodding);
        }else{
            reader = new InputStreamReader(resource.getInputStream());
        }

        tProperty.read(reader);

        propertiesConfiguration.clear();
        propertiesConfiguration.append(tProperty);
    }

    public void refreashPropertyFiles(){
        String fileName = null;

        try{
            Iterator<?> it = extFileName.iterator();

            while(it != null && it.hasNext()){
                Object element = it.next();
                String enc = null;

                if(element instanceof Map){
                    Map<?, ?> ele = (Map<?, ?>)element;
                    enc = (String)ele.get("encoding");
                    fileName = (String) ele.get("fileName");
                }else{
                    fileName = (String)element;
                }

                loadPropertyResource(fileName, enc);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private PropertiesConfiguration getPropertiesConfiguration(){
        return propertiesConfiguration;
    }


    public String getString(String key){
        return getPropertiesConfiguration().getString(key);
    }

    public int getInt(String key){
        return getPropertiesConfiguration().getInt(key);
    }

    public boolean getBoolen(String key){
        return getPropertiesConfiguration().getBoolean(key);
    }


}

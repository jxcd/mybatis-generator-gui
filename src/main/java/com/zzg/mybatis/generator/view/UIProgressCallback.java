package com.zzg.mybatis.generator.view;

import com.zzg.mybatis.generator.model.GeneratorConfig;
import freemarker.template.Configuration;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Alert;
import org.mybatis.generator.api.ProgressCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Owen on 6/21/16.
 */
public class UIProgressCallback extends Alert implements ProgressCallback  {
    private static final Logger log = LoggerFactory.getLogger(UIProgressCallback.class);

    private final StringProperty progressText = new SimpleStringProperty();
    private GeneratorConfig generatorConfig = null;

    public UIProgressCallback(AlertType alertType) {
		super(alertType);
		this.contentTextProperty().bindBidirectional(progressText);
    }

    public UIProgressCallback(AlertType information, GeneratorConfig generatorConfig) {
        super(information);
        this.contentTextProperty().bindBidirectional(progressText);
        this.generatorConfig = generatorConfig;
    }

    @Override
    public void introspectionStarted(int totalTasks) {
		progressText.setValue("开始代码检查");
	}

    @Override
    public void generationStarted(int totalTasks) {
		progressText.setValue("开始代码生成");
    }

    @Override
    public void saveStarted(int totalTasks) {
		progressText.setValue("开始保存生成的文件");
    }

    @Override
    public void startTask(String taskName) {
		progressText.setValue("代码生成任务开始");
    }

    @Override
    public void done() {
        createService();
		progressText.setValue("代码生成完成");
    }

    @Override
    public void checkCancel() throws InterruptedException {
    }

    private void createService() {
        if (generatorConfig == null) {
            return;
        }

        String typeManager = "Manager";
        String typeService = "Service";
        String suffix = generatorConfig.getEnableService();
        if (typeService.equals(suffix) || typeManager.equals(suffix)) {
            String suffixLow = suffix.toLowerCase();
            String domainName = generatorConfig.getDomainObjectName();
            String packagePrefix = generatorConfig.getPackagePrefix();
            // 封装参数
            Map<String, String> dataModel = new LinkedHashMap<>();
            dataModel.put("author", System.getProperty("author-name"));
            dataModel.put("packagePrefix", packagePrefix);
            dataModel.put("suffix", suffix);
            dataModel.put("suffixLow", suffixLow);
            dataModel.put("modelTargetPackage", generatorConfig.getModelPackage());
            dataModel.put("daoTargetPackage", generatorConfig.getDaoPackage());
            dataModel.put("Model", domainName);
            dataModel.put("PK", getPk(generatorConfig));
            log.info("service/manager config: {}", dataModel);

            File service = new File(this.generatorConfig.getProjectFolder() + "/" + this.generatorConfig.getDaoTargetFolder() + "/" + (packagePrefix + "." + suffixLow).replaceAll("\\.", "/"), domainName + suffix + ".java");
            File impl = new File(this.generatorConfig.getProjectFolder() + "/" + this.generatorConfig.getDaoTargetFolder() + "/" + (packagePrefix + "." + suffixLow + ".impl").replaceAll("\\.", "/"), domainName + suffix + "Impl.java");
            try {
                this.createFiles(service, impl);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            String encoding = this.generatorConfig.getEncoding();
            try (Writer serviceWrite = new OutputStreamWriter(Files.newOutputStream(service.toPath()), Charset.forName(encoding));
                 Writer implWrite = new OutputStreamWriter(Files.newOutputStream(impl.toPath()), Charset.forName(encoding))) {

                freemarker.template.Configuration conf = new freemarker.template.Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
                conf.setClassForTemplateLoading(this.getClass(), "/template/");
                conf.setDefaultEncoding(encoding);

                conf.getTemplate("ServiceTemplate.ftl").process(dataModel, serviceWrite);
                log.info("{} 生成完成", suffixLow);
                conf.getTemplate("ServiceImplTemplate.ftl").process(dataModel, implWrite);
                log.info("{} impl 生成完成", suffixLow);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private String getPk(GeneratorConfig generatorConfig) {
        String java = this.generatorConfig.getProjectFolder() + "/" + this.generatorConfig.getDaoTargetFolder() + "/";
        String daoDir = (generatorConfig.getPackagePrefix() + "." + generatorConfig.getDaoPackage()).replace(".", "/");
        File mapper = new File(java + daoDir, generatorConfig.getDomainObjectName() + "Mapper.java");
        try {
            if (mapper.exists()) {
                for (String line : Files.readAllLines(mapper.toPath())) {
                    if (line.contains("interface")) {
                        return line.split(",")[1].trim();
                    }
                }
            }
        } catch (Exception e) {
            log.warn("parse PK error, e: {}", e.getMessage(), e);
        }

        return "Integer";
    }

    private void createFiles(File... files) throws IOException {
        for (File file : files) {
            File parentFile = file.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }
            file.createNewFile();
        }
    }
}

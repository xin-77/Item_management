import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;


public class aa {

    public static void main(String[] args) {

        String url = "jdbc:mysql://47.115.220.46:3306/vue";
        String username = "root";
        String password = "123456";

        String author = "Xin";
        String outputJavaDir = "D:\\code\\bkcode\\SpringBoot" + "/src/main/java";
        String outputXmlDir = "D:\\code\\bkcode\\SpringBoot\\src\\main\\resources\\mapper";
        String parentPackageName = "com.example";
        String moduleName = "demo";
        FastAutoGenerator.create(url, username, password)
                .globalConfig(builder -> {
                    builder.author(author) // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            .outputDir(outputJavaDir); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent(parentPackageName) // 设置父包名
                            .moduleName(moduleName) // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, outputXmlDir)); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude("book_with_shelf"); // 设置需要生成的表名
                    builder.entityBuilder().enableLombok(); // 使用 Lombok
                    builder.controllerBuilder().enableRestStyle(); // 使用 RestController
                    builder.serviceBuilder().formatServiceFileName("%sService"); // Service 接口命名格式
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}

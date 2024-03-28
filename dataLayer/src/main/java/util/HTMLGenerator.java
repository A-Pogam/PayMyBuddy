package util;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import java.io.StringWriter;

@Component
public class HTMLGenerator {

    private final SpringTemplateEngine templateEngine;

    @Autowired
    public HTMLGenerator(SpringResourceTemplateResolver templateResolver) {
        this.templateEngine = new SpringTemplateEngine();
        this.templateEngine.setTemplateResolver(templateResolver);
    }

    public String generateHTML() {
        Context context = new Context();
        context.setVariable("title", "Login");


        StringWriter writer = new StringWriter();
        templateEngine.process("login", context, writer); // Utilise le nom du fichier sans extension

        return writer.toString();
    }
}

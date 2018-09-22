package team.dashboard.web.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.io.StringWriter;

/**
 * Created by awconstable on 20/02/2017.
 */
@Service
public class EmailFeedbackRequestSender implements FeedbackRequestSender
    {

    @Autowired
    private JavaMailSender mailSender;
    
    @Value("${from.email}")
    private String fromEmail;

    @Value("${email.subject}")
    private String emailSubject;

    @Override
    public void requestFeedback(final FeedbackRequest request)
        {
        MimeMessagePreparator mimeMessagePreparator = mimeMessage ->
        {
            ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
            templateResolver.setTemplateMode("HTML5");
            templateResolver.setSuffix(".html");

            TemplateEngine engine = new TemplateEngine();
            engine.setTemplateResolver(templateResolver);

            StringWriter writer = new StringWriter();
            Context context = new Context();
            context.setVariable("request", request);

            engine.process("satisfaction", context, writer);

            MimeMessageHelper mimeHelper = new MimeMessageHelper(mimeMessage);
            mimeHelper.setFrom(fromEmail);
            mimeHelper.setTo(request.getToEmail());
            mimeHelper.setSubject(emailSubject);
            mimeHelper.setText(writer.toString(), true);

        };
        this.mailSender.send(mimeMessagePreparator);
        }

    }



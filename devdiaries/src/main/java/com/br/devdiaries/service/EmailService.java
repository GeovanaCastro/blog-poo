package com.br.devdiaries.service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import com.br.devdiaries.exception.EmailServiceException;

@Service
public class EmailService {
    
    @Value("${sendgrid.api.key}")
    private String sendGridApiKey;
    
    @Value("${sendgrid.from.email}")
    private String fromEmail;
    
    public void enviarEmail(String para, String assunto, String mensagem) {
        Email from = new Email(fromEmail);
        Email to = new Email(para);
        Content content = new Content("text/plain", mensagem);
        Mail mail = new Mail(from, assunto, to, content);
        
        SendGrid sendGrid = new SendGrid(sendGridApiKey);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sendGrid.api(request);
            
            System.out.println("Email sent successfully!");
            System.out.println("Status Code: " + response.getStatusCode());
            System.out.println("Body: " + response.getBody());
            System.out.println("Headers: " + response.getHeaders());
        } catch (IOException ex) {
            System.err.println("Error sending email: " + ex.getMessage());
        }
    }
    
    @ExceptionHandler(EmailServiceException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
        public Map<String, String> handleEmailServiceException(EmailServiceException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return error;
    }
}

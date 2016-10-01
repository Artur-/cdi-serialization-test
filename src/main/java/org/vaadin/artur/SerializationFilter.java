package org.vaadin.artur;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.vaadin.server.VaadinSession;

@WebFilter(asyncSupported = true, urlPatterns = "/*")
public class SerializationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    private <T extends Serializable> T serializeDeserialize(T serializable)
            throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ObjectOutputStream oos;
        oos = new ObjectOutputStream(os);
        oos.writeObject(serializable);
        return (T) deserialize(os.toByteArray());
    }

    private Object deserialize(byte[] result) throws IOException {
        ByteArrayInputStream is = new ByteArrayInputStream(result);
        ObjectInputStream ois;
        try {
            ois = new ObjectInputStream(is);
            return ois.readObject();
        } catch (ClassNotFoundException e) {
            throw new IOException("Deserialization failed", e);
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        chain.doFilter(request, response);
        HttpSession httpSession = ((HttpServletRequest) request).getSession();

        String key = "com.vaadin.server.VaadinSession.VaadinServlet";
        VaadinSession s = (VaadinSession) httpSession.getAttribute(key);
        VaadinSession s2 = serializeDeserialize(s);
        System.out.println("Serialized, deserialized " + s + " -> " + s2);
    }

    @Override
    public void destroy() {
    }

}

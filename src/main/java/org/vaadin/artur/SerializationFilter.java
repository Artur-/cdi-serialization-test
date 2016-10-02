package org.vaadin.artur;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.Serializable;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


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
            ois = new ObjectInputStream(is) {
                @Override
                protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
                    // this is a workaround for a known problem that sun.internal latestUserDefinedLoader() often returns shit
                    String name = desc.getName();
                    return Class.forName(name, false, Thread.currentThread().getContextClassLoader());
                }
            };
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

        Enumeration<String> attributeNames = httpSession.getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            String name = attributeNames.nextElement();
            Serializable s = (Serializable) httpSession.getAttribute(name);
            Serializable s2 = serializeDeserialize(s);
            System.out.println("Serialized, deserialized attribute " + name + " " + s + " -> " + s2);
        }
    }

    @Override
    public void destroy() {
    }

}

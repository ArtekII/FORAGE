package map;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class Scanner {
    public static Map<String, Mapping> scanModels(String pkg) throws Exception {
        Map<String, Mapping> result = new HashMap<>();
        String path = pkg.replace('.', '/');
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> resources = cl.getResources(path);

        while (resources.hasMoreElements()) {
            URL url = resources.nextElement();
            String protocol = url.getProtocol();

            if ("file".equals(protocol)) {
                File dir = new File(URLDecoder.decode(url.getFile(), "UTF-8"));
                File[] files = dir.listFiles();
                if (files == null) continue;

                for (File f : files) {
                    String name = f.getName();
                    if (name.endsWith(".class") && !name.contains("$")) {
                        String className = pkg + "." + name.substring(0, name.length() - 6);
                        addClassInfo(result, className);
                    }
                }
            } 
        }

        return result;
    }

    private static void addClassInfo(Map<String, Mapping> result, String className) throws Exception {
        Class<?> clazz = Class.forName(className);
        Field[] fields = clazz.getDeclaredFields();

        String[] cols = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            cols[i] = fields[i].getName();
        }

        Mapping m = new Mapping();
        m.setTableName(clazz.getSimpleName());
        m.setFields(fields);
        m.setColonnes(cols);

        result.put(clazz.getName(), m);
    }
}

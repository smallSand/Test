package util;
import java.lang.annotation.Annotation;  
import java.lang.reflect.Method;  
import java.util.HashMap;  
import java.util.Map;

import jsoup.LinkCheck;
import jsoup.company;
public class AnnotationUtil {  
  
    public static AnnotationUtil anno = null;  
  
    public static AnnotationUtil getInstance() {  
        if (anno == null) {  
            anno = new AnnotationUtil();  
        }  
        return anno;  
    }  
  
    /** 
     * 读取注解值 
     *  
     * @param annotationClasss 处理Annotation类名称 
     * @param annotationField 处理Annotation类属性名称 
     * @param className 处理Annotation的使用类名称 
     * @return 
     * @throws Exception 
     */  
    @SuppressWarnings("all")  
    public Map<String, String> loadVlaue(Class annotationClasss,  
            String annotationField, String className) throws Exception {  
        Map<String, String> map = new HashMap<String, String>();  
        Method[] methods = Class.forName(className).getDeclaredMethods();  
        for (Method method : methods) {  
            if (method.isAnnotationPresent(annotationClasss)) {  
                Annotation p = method.getAnnotation(annotationClasss);  
                Method m = p.getClass()  
                        .getDeclaredMethod(annotationField, null);  
                String[] values = (String[]) m.invoke(p, null);  
                for (String key : values) {  
                    map.put(key, method.getName());  
                }  
            }  
        }  
        return map; 
    } 
    public static void main(String[] args) throws Exception{
		Map<String, String> map= AnnotationUtil.getInstance().loadVlaue(company.class, "value",  
        		LinkCheck.class.getName());
		for(String company:map.keySet()){
			String methodname=map.get(company);
			System.out.println(company+methodname);
		}
    }
 
}  
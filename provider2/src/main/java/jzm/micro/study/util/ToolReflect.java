package jzm.micro.study.util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/** 
 * * 
 * @version 1.0
 * @description  利用反射进行操作的一个工具类
 * @author zheng.shk
 * @created 2014-4-20 下午10:46:22
 */
public  class ToolReflect {
    /**
     * Logger for this class
     */
    private static final Logger logger = LoggerFactory.getLogger(ToolReflect.class);
	
	
	private static final String TYPE_CLASS_NAME_PREFIX = "class ";
	private static final String TYPE_INTERFACE_NAME_PREFIX = "interface ";
	
	
	
	/**
	 * 
	 * @Title: getFiledName 
	 * @Description: 获取属性名数组 
	 * @param o
	 * @return  String[]
	 */
	public static String[] getFiledName(Object o) {
		Field[] fields = o.getClass().getDeclaredFields();
		ArrayList<String> fieldNames = new ArrayList<String>();
		for (int i = 0; i < fields.length; i++) {
			String field = fields[i].getName();
			if(null!=field&&"serialVersionUID".equals(field)){
				continue;
			}
			fieldNames.add(field);
		}
		return (String[]) fieldNames.toArray(new String[fieldNames.size()]);
	}
	
   /**
    * 利用反射获取指定对象的指定属性的value
    * @param obj 目标对象
    * @param fieldName 目标属性
    * @return 目标属性的值
    */
   public static Object getFieldValue(Object obj, String fieldName) {
       Object result = null;
       Field field = ToolReflect.getField(obj, fieldName);
       if (field != null) {
          field.setAccessible(true);
          try {
              result = field.get(obj);
          } catch (IllegalArgumentException e) {
              // TODO Auto-generated catch block
                logger.error("getFieldValue(Object, String)", e); //$NON-NLS-1$
          } catch (IllegalAccessException e) {
              // TODO Auto-generated catch block
                logger.error("getFieldValue(Object, String)", e); //$NON-NLS-1$
          }
       }
       return result;
   }
  
   /**
    * 利用反射获取指定对象里面的指定属性
    * @param obj 目标对象
    * @param fieldName 目标属性
    * @return 目标字段
    */
   public static Field getField(Object obj, String fieldName) {
       Field field = null;
      for (Class<?> clazz=obj.getClass(); clazz != Object.class; clazz=clazz.getSuperclass()) {
          try {
              field = clazz.getDeclaredField(fieldName);
              break;
          } catch (NoSuchFieldException e) {
              //这里不用做处理，子类没有该字段可能对应的父类有，都没有就返回null。
          }
       }
       return field;
   }

   /**
    * 利用反射设置指定对象的指定属性为指定的值
    * @param obj 目标对象
    * @param fieldName 目标属性
     * @param fieldValue 目标值
    */
   public static void setFieldValue(Object obj, String fieldName, Object fieldValue) {
	   if(null!=fieldValue){
	       Field field = ToolReflect.getField(obj, fieldName);
	       if (null!=field) {
	          try {
	              field.setAccessible(true);
	              field.set(obj, fieldValue);
	          } catch (IllegalArgumentException e) {
	              // TODO Auto-generated catch block
                    logger.error("setFieldValue(Object, String, Object)", e); //$NON-NLS-1$
	          } catch (IllegalAccessException e) {
	              // TODO Auto-generated catch block
                    logger.error("setFieldValue(Object, String, Object)", e); //$NON-NLS-1$
	          }
	       }
	   }

    }
   
   
   
   
   
	public static String getClassName(Type type) {
		if (type==null) {
			return "";
		}
		String className = type.toString();
		if (className.startsWith(TYPE_CLASS_NAME_PREFIX)) {
	    	className = className.substring(TYPE_CLASS_NAME_PREFIX.length());
	    } else if (className.startsWith(TYPE_INTERFACE_NAME_PREFIX)) {
	    	className = className.substring(TYPE_INTERFACE_NAME_PREFIX.length());
	    }
	    return className;
	}
	
	
	
	/**
	 * 
	 * @Title: getClass 
	 * @Description: 通过Type获取对象class
	 * @param type
	 * @return
	 * @throws ClassNotFoundException  Class<?>
	 */
	public static Class<?> getClass(Type type) throws ClassNotFoundException {
		String className = getClassName(type);
		if (className==null || className.isEmpty()) {
			return null;
		}
		return Class.forName(className);
	}
	
	
	/**
	 * 
	 * @Title: newInstance 
	 * @Description: 通过Type创建对象
	 * 在这里输入的Type不能是抽象类、接口、数组类型、以及基础类型、Void否则会发生InstantiationException异常。
	 * @param type
	 * @return
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException  Object
	 */
	public static Object newInstance(Type type) 
			throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		Class<?> clazz = getClass(type);
		if (clazz==null) {
			return null;
		}
		return clazz.newInstance();
	}
	
	
	/**
	 * 
	 * @Title: getParameterizedTypes 
	 * @Description: 获取泛型对象的泛型化参数, 当传入的对象为非泛型类型，则会返回空数组形式。 所以：请传入泛型化参数
	 * @param object
	 * @return  Type[]
	 */
	public static Type[] getParameterizedTypes(Object object) {
		Type superclassType = object.getClass().getGenericSuperclass();
		if (!ParameterizedType.class.isAssignableFrom(superclassType.getClass())) {
			return null;
		}
		
		return ((ParameterizedType)superclassType).getActualTypeArguments();
	}
	
	
	/**
	 * 
	 * @Title: hasDefaultConstructor 
	 * @Description: 检查对象是否存在默认构造函数 
	 * @param clazz
	 * @return
	 * @throws SecurityException  boolean
	 */
	public static boolean hasDefaultConstructor(Class<?> clazz) throws SecurityException {
		Class<?>[] empty = {};
		try {
			clazz.getConstructor(empty);
		} catch (NoSuchMethodException e) {
			return false;
		}
		return true;
	}
	
	
	
	/**
	 * 
	 * @Title: getFieldClass 
	 * @Description: 获取指定类型中的特定field类型
	 * @param clazz
	 * @param name
	 * @return  Class<?>
	 */
	public static Class<?> getFieldClass(Class<?> clazz, String name) {
		if (clazz==null || name==null || name.isEmpty()) {
			return null;
		}
		
		Class<?> propertyClass = null;
		
		for (Field field : clazz.getDeclaredFields()) {
		    field.setAccessible(true);
		    if (field.getName().equalsIgnoreCase(name)) {
		    	propertyClass = field.getType();
		    	break;
		    }
		}
		
		return propertyClass;
	}
	
	
	/**
	 * 
	 * @Title: getMethodReturnType 
	 * @Description: 获取指定类型中的特定method返回类型
	 * @param clazz
	 * @param name
	 * @return  Class<?>
	 */
	public static Class<?> getMethodReturnType(Class<?> clazz, String name) {
		if (clazz==null || name==null || name.isEmpty()) {
			return null;
		}
		
		name = name.toLowerCase();
		Class<?> returnType = null;
		
		for (Method method : clazz.getDeclaredMethods()) {
			if (method.getName().equals(name)) {
				returnType = method.getReturnType();
				break;
			}
		}
		
		return returnType;
	}
	
	
	/**
	 * 
	 * @Title: getEnumConstant 
	 * @Description: 根据字符串标示获取枚举常量
	 * @param clazz
	 * @param name
	 * @return  Object
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Object getEnumConstant(Class<?> clazz, String name) {
		if (clazz==null || name==null || name.isEmpty()) {
			return null;
		}
		return Enum.valueOf((Class<Enum>)clazz, name);
	}
	
	
	/**
	 * 
	 * @Title: objectToMap 
	 * @Description: Object 转换成 Map
	 * @param object
	 * @return  Map<String,Object>
	 */
	public static <T> Map<String, Object> objectToMap(T object) {
		String[] arrayOfString;
		HashMap<String, Object> map = new HashMap<String, Object>();
		String[] fieldArr = ToolReflect.getFiledName(object);
		int j = (arrayOfString = fieldArr).length;
		for (int i = 0; i < j;) {
			String field = arrayOfString[i];
			try {
				map.put(field, ToolReflect.getFieldValue(object, field));
			} catch (Exception e) {
                logger.error("objectToMap(T)", e); //$NON-NLS-1$
			}
			++i;
		}
		return map;
	}	
	
}
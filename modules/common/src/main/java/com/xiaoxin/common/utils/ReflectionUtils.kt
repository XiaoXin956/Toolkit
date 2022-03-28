package com.xiaoxin.common.utils

import java.lang.reflect.*
import java.util.*


/**
 * 反射工具类
 * @author: Admin
 * @date: 2021-08-24
 */
class ReflectionUtils {

    /**
     * 获取包名
     *
     * @return 包名【String类型】
     */
    fun getPackage(clazz: Class<*>): String? {
        val pck = clazz.getPackage()
        return if (null != pck) {
            pck.name
        } else {
            "没有包！"
        }
    }

    /**
     * 获取父类
     */
    fun getSuperClassName(classzz: Class<*>): String {
        val superclass: Class<*> = classzz.superclass;
        return superclass.name
    }

    /**
     * 获取全类名
     */
    fun getClassName(classzz: Class<*>): String {
        val superclass: Class<*> = classzz.superclass;
        return superclass.name
    }

    /**
     * 获取实现的接口名
     */
    fun getInterfaces(clazz: Class<*>): MutableList<String> {
        val interfaces = clazz.interfaces
        val len = interfaces.size
        val list: MutableList<String> = ArrayList()
        for (i in 0 until len) {
            val itfc = interfaces[i]

            // 接口名
            val interfaceName = itfc.simpleName
            list.add(interfaceName)
        }
        return list
    }

    /**
     * 获取所有属性
     *
     * @return 所有的属性【每一个属性添加到StringBuilder中，最后保存到一个List集合中】
     */
    fun getFields(clazz: Class<*>): List<StringBuilder> {
        val fields: Array<Field> = clazz.declaredFields
        val len = fields.size
        val list: MutableList<StringBuilder> = ArrayList()
        var sb: StringBuilder? = null
        for (i in 0 until len) {
            val field: Field = fields[i]
            sb = StringBuilder()

            // 修饰符
            val modifier: String = Modifier.toString(field.modifiers)
            sb.append("$modifier ")

            // 数据类型
            val type: Class<*> = field.type
            val typeName = type.simpleName
            sb.append("$typeName ")

            // 属性名
            val fieldName: String = field.name
            sb.append("$fieldName;")
            list.add(sb)
        }
        return list
    }


    /**
     * 获取所有公共的属性
     *
     * @return 所有公共的属性【每一个属性添加到StringBuilder中，最后保存到一个List集合中】
     */
    fun getPublicFields(clazz: Class<*>): List<StringBuilder> {
        val fields = clazz.fields
        val len = fields.size
        val list: MutableList<StringBuilder> = ArrayList()
        var sb: StringBuilder? = null
        for (i in 0 until len) {
            val field = fields[i]
            sb = StringBuilder()

            // 修饰符
            val modifier = Modifier.toString(field.modifiers)
            sb.append("$modifier ")

            // 数据类型
            val type = field.type
            val typeName = type.simpleName
            sb.append("$typeName ")

            // 属性名
            val fieldName = field.name
            sb.append("$fieldName;")
            list.add(sb)
        }
        return list
    }


    /**
     * 获取所有构造方法
     *
     * @return 所有的构造方法【每一个构造方法添加到StringBuilder中，最后保存到一个List集合中】
     */
    fun getConstructors(clazz: Class<*>): List<StringBuilder> {
        val constructors: Array<Constructor<*>> = clazz.declaredConstructors
        val len = constructors.size
        val list: MutableList<StringBuilder> = ArrayList()
        var sb: StringBuilder? = null
        for (i in 0 until len) {
            val constructor: Constructor<*> = constructors[i]
            sb = StringBuilder()

            // 修饰符
            val modifier = Modifier.toString(constructor.modifiers)
            sb.append("$modifier ")

            // 方法名（类名）
            val constructorName = clazz.simpleName
            sb.append("$constructorName (")

            // 形参列表
            val parameterTypes: Array<Class<*>> = constructor.parameterTypes
            val length = parameterTypes.size
            for (j in 0 until length) {
                val parameterType = parameterTypes[j]
                val parameterTypeName = parameterType.simpleName
                if (j < length - 1) {
                    sb.append("$parameterTypeName, ")
                } else {
                    sb.append(parameterTypeName)
                }
            }
            sb.append(") {}")
            list.add(sb)
        }
        return list
    }


    /**
     * 获取所有自身的方法
     *
     * @return 所有自身的方法【每一个方法添加到StringBuilder中，最后保存到一个List集合中】
     */
    fun getMethods(clazz: Class<*>): List<StringBuilder> {
        val methods: Array<Method> = clazz.declaredMethods
        val len = methods.size
        val list: MutableList<StringBuilder> = ArrayList()
        var sb: StringBuilder? = null
        for (i in 0 until len) {
            val method: Method = methods[i]
            sb = StringBuilder()

            // 修饰符
            val modifier = Modifier.toString(method.modifiers)
            sb.append("$modifier ")

            // 返回值类型
            val returnClass: Class<*> = method.returnType
            val returnType = returnClass.simpleName
            sb.append("$returnType ")

            // 方法名
            val methodName: String = method.name
            sb.append("$methodName (")

            // 形参列表
            val parameterTypes: Array<Class<*>> = method.parameterTypes
            val length = parameterTypes.size
            for (j in 0 until length) {
                val parameterType = parameterTypes[j]

                // 形参类型
                val parameterTypeName = parameterType.simpleName
                if (j < length - 1) {
                    sb.append("$parameterTypeName, ")
                } else {
                    sb.append(parameterTypeName)
                }
            }
            sb.append(") {}")
            list.add(sb)
        }
        return list
    }

    /**
     * 获取所有公共的方法
     *
     * @return 所有公共的方法【每一个方法添加到StringBuilder中，最后保存到一个List集合中】
     */
    fun getPublicMethods(clazz: Class<*>): List<StringBuilder> {
        val methods = clazz.methods
        val len = methods.size
        val list: MutableList<StringBuilder> = ArrayList()
        var sb: StringBuilder? = null
        for (i in 0 until len) {
            val method = methods[i]
            sb = StringBuilder()

            // 修饰符
            val modifier = Modifier.toString(method.modifiers)
            sb.append("$modifier ")

            // 返回值类型
            val returnClass = method.returnType
            val returnType = returnClass.simpleName
            sb.append("$returnType ")

            // 方法名
            val methodName = method.name
            sb.append("$methodName (")

            // 形参列表
            val parameterTypes = method.parameterTypes
            val length = parameterTypes.size
            for (j in 0 until length) {
                val parameterType = parameterTypes[j]

                // 形参类型
                val parameterTypeName = parameterType.simpleName
                if (j < length - 1) {
                    sb.append("$parameterTypeName, ")
                } else {
                    sb.append(parameterTypeName)
                }
            }
            sb.append(") {}")
            list.add(sb)
        }
        return list
    }

    /**
     * 获取父类的泛型
     *
     * @return 父类的泛型【Class类型】
     */
    fun getSuperClassGenericParameterizedType(clazz: Class<*>): Class<*> {
        val genericSuperClass: Type = clazz.genericSuperclass
        var superClassGenericParameterizedType: Class<*>? = null

        // 判断父类是否有泛型
        if (genericSuperClass is ParameterizedType) {
            // 向下转型，以便调用方法
            val pt: ParameterizedType = genericSuperClass as ParameterizedType
            // 只取第一个，因为一个类只能继承一个父类
            val superClazz: Type = pt.actualTypeArguments[0]
            // 转换为Class类型
            superClassGenericParameterizedType = superClazz as Class<*>
        }
        return superClassGenericParameterizedType!!
    }

    /**
     * 获取接口的所有泛型
     *
     * @return 所有的泛型接口【每一个泛型接口的类型为Class，最后保存到一个List集合中】
     */
    fun getInterfaceGenericParameterizedTypes(clazz: Class<*>): List<Class<*>>? {
        val genericInterfaces: Array<Type> = clazz.genericInterfaces
        val len = genericInterfaces.size
        val list: MutableList<Class<*>> = ArrayList()
        for (i in 0 until len) {
            val genericInterface: Type = genericInterfaces[i]

            // 判断接口是否有泛型
            if (genericInterface is ParameterizedType) {
                val pt: ParameterizedType = genericInterface

                // 得到所有的泛型【Type类型的数组】
                val interfaceTypes: Array<Type> = pt.actualTypeArguments
                val length = interfaceTypes.size
                for (j in 0 until length) {
                    // 获取对应的泛型【Type类型】
                    val interfaceType: Type = interfaceTypes[j]
                    // 转换为Class类型
                    val interfaceClass = interfaceType as Class<*>
                    list.add(interfaceClass)
                }
            }
        }
        return list
    }


}
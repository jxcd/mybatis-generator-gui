package com.zzg.mybatis.generator.plugins;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.internal.types.JavaTypeResolverDefaultImpl;

import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * @author cwj
 * @since 2025/10/13
 */
public class JavaTypeResolverImpl extends JavaTypeResolverDefaultImpl {

    public static boolean jsr310 = false;

    public JavaTypeResolverImpl() {
        // 特殊的类型, 如 text 也当作普通的 varchar, 避免逆向生成 blob
        typeMap.put(Types.LONGVARCHAR, new JdbcTypeInformation("VARCHAR",
                new FullyQualifiedJavaType(String.class.getName())));
    }

    @Override
    protected FullyQualifiedJavaType overrideDefaultType(IntrospectedColumn column, FullyQualifiedJavaType defaultType) {
        FullyQualifiedJavaType answer = defaultType;

        if (jsr310) {
            switch (column.getJdbcType()) {
                case Types.BIT -> answer = calculateBitReplacement(column, defaultType);
                case Types.DECIMAL, Types.NUMERIC -> answer = calculateBigDecimalReplacement(column, defaultType);
                case Types.DATE -> answer = new FullyQualifiedJavaType(LocalDate.class.getName());
                case Types.TIME -> answer = new FullyQualifiedJavaType(LocalTime.class.getName());
                case Types.TIMESTAMP -> answer = new FullyQualifiedJavaType(LocalDateTime.class.getName());
                default -> {
                }
            }
        }

        return answer;
    }

}

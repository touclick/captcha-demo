package com.touclick.captcha.model;

import java.net.URLEncoder;

public class Parameter implements java.io.Serializable {
    String name;
    String value;

    private static final long serialVersionUID = -8708108746980739212L;

    public Parameter(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public Parameter(String name, long value) {
        this.name = name;
        this.value = String.valueOf(value);
    }

    public Parameter(String name, double value) {
        this.name = name;
        this.value = String.valueOf(value);
    }

    public Parameter(String name, int value) {
        this.name = name;
        this.value = String.valueOf(value);
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public static Parameter[] getParameterArray(String name, String value) {
        return new Parameter[]{new Parameter(name, value)};
    }

    public static Parameter[] getParameterArray(String name, int value) {
        return getParameterArray(name, String.valueOf(value));
    }

    public static Parameter[] getParameterArray(String name1, String value1
            , String name2, String value2) {
        return new Parameter[]{new Parameter(name1, value1)
                , new Parameter(name2, value2)};
    }

    public static Parameter[] getParameterArray(String name1, int value1
            , String name2, int value2) {
        return getParameterArray(name1, String.valueOf(value1), name2, String.valueOf(value2));
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + value.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (obj instanceof Parameter) {
            Parameter that = (Parameter) obj;

            return this.name.equals(that.name) &&
                    this.value.equals(that.value);
        }
        return false;
    }

    @Override
    public String toString() {
        return "Parameter{" +
                "name='" + name + '\'' +
                ", value='" + value +
                '}';
    }

    public int compareTo(Object o) {
        int compared;
        Parameter that = (Parameter) o;
        compared = name.compareTo(that.name);
        if (0 == compared) {
            compared = value.compareTo(that.value);
        }
        return compared;
    }

    public static String encodeParameters(Parameter[] httpParams) {
        if (null == httpParams) {
            return "";
        }
        StringBuffer buf = new StringBuffer();
        for (int j = 0; j < httpParams.length; j++) {
            if (j != 0) {
                buf.append("&");
            }
            try {
                buf.append(URLEncoder.encode(httpParams[j].name, "UTF-8"))
                        .append("=").append(URLEncoder.encode(httpParams[j].value, "UTF-8"));
            } catch (java.io.UnsupportedEncodingException neverHappen) {
            }
        }
        return buf.toString();

    }

}

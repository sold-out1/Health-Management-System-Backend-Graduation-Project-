//package com.kmbeast.pojo.api;
//
///**
// * 响应基类
// * @param <T>
// */
//public class Result<T> {
//    private Integer code; //响应状态码
//    private String message; //响应消息
//
//    @Override
//    public String toString() {
//        return "Result{" +
//                "code=" + code +
//                ", message='" + message + '\'' +
//                '}';
//    }
//
//    public Integer getCode() {
//        return code;
//    }
//
//    public void setCode(Integer code) {
//        this.code = code;
//    }
//
//    public String getmessage() {
//        return message;
//    }
//
//    public void setmessage(String message) {
//        this.message = message;
//    }
//
//    public Result() {
//    }
//
//    public Result(Integer code, String message) {
//        this.code = code;
//        this.message = message;
//    }
//
//}
package com.kmbeast.pojo.api;

/**
 * 响应基类 - 升级版
 * @param <T> 泛型内容
 */
public class Result<T> {
    private Integer code;    // 响应状态码
    private String message;  // 响应消息
    private T data;          // 核心：存放真正的数据内容（比如用户信息、记录列表）

    // 无参构造
    public Result() {
    }

    // 全参构造
    public Result(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // 常用构造（只有码和消息）
    public Result(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    // 获取数据的方法 - 这就是 AI 分析 service 报错要找的方法
    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Result{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
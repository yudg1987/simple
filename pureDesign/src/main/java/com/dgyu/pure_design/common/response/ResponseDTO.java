package com.dgyu.pure_design.common.response;

import com.dgyu.pure_design.common.constants.Constant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 统一结果集
 *
 * @author yinfan
 * @date 2017年3月14日上午10:45:52
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
public class ResponseDTO<T> implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -6351210629803310701L;

    /**
     * 1 成功 0 失败
     */
    private String code;

    /**
     * 错误信息
     */
    private String message;

    /***/
    private int total;
    /**
     * 业务数据
     */
    private T rows;

    private Map<String, Object> data = new HashMap<String, Object>();


    public ResponseDTO() {
        super();
        code = Constant.SUCCESS;
    }

    public ResponseDTO(String code, String message) {
        super();
        this.code = code;
        this.message = message;
    }

    public ResponseDTO(T data) {
        super();
        rows = data;
        code = Constant.SUCCESS;
    }

    public ResponseDTO(String message) {
        super();
        this.message = message;
        code = Constant.FAILURE;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this,
                ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public static <T> ResponseDTO<T>  success(T data){
        ResponseDTO<T> dto = new ResponseDTO<>();
        dto.setCode(Constant.SUCCESS);
        dto.setRows(data);
        return dto;
    }

    /**
     * 设置Data 内部数据
     */
    public ResponseDTO setDataItem(String key, Object val) {
        if (null == data) {
            data = new HashMap<String, Object>();
        }
        data.put(key, val);
        return this;
    }

    public static <T> ResponseDTO<T> failed(Object msg) {
        ResponseDTO<T> jResponse = new ResponseDTO<>();
        jResponse.setCode(Constant.FAILURE)
                .setMessage(String.valueOf(msg))
                .setData(null);
        return jResponse;
    }

    public static <T> ResponseDTO<T> failed(Object msg, T row) {
        ResponseDTO<T> jResponse = new ResponseDTO<>();
        jResponse.setCode(Constant.FAILURE)
                .setMessage(String.valueOf(msg))
                .setRows(row);
        return jResponse;
    }

    public static <T> ResponseDTO<T> failed(Object msg, Map data) {
        ResponseDTO<T> jResponse = new ResponseDTO<>();
        jResponse.setCode(Constant.FAILURE)
                .setMessage(String.valueOf(msg))
                .setData(data);
        return jResponse;
    }

}

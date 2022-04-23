package com.dgyu.pure_design.common.exception;

/**
 * @Auther: dgyu
 * @Date: 2020/2/19
 * @Description: 自定义业务异常
 */
public class BusinessException extends RuntimeException
{
	public BusinessException(String message)
	{
		super(message);
	}
	
	private String code;

    public BusinessException(String code, String msg) {
        super(msg);
        this.code = code;
    }

}

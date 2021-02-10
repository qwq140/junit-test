package com.cos.book.domain;

import lombok.Data;

@Data
public class CommonRespDto<T> {
	private T data;
}

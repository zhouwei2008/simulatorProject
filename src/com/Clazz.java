package com;

import java.lang.reflect.Field;

public class Clazz {

	public static ResponseDeposit Annotation(Class<?> clazz, String fieldname, String value) throws Exception{
		return Clazz.Annotation(clazz, fieldname, value, Validator.class);
	}

	public static ResponseDeposit Annotation(Class<?> clazz, String fieldname, String value,
			Class<Validator> annclazz) throws Exception {
		ResponseDeposit resDto = new ResponseDeposit();
		Field field = null;
		resDto.setRespCode("00000");
		try {
			field = clazz.getDeclaredField(fieldname);
		} catch (Exception e) {
			resDto.setRespCode(EventCode.WEB_PARAM_LOST);
			return resDto;
		}
		if (field != null && field.isAnnotationPresent(annclazz)) {
			if (value == null || "".equals(value) || "null".equals(value)) {
				if (!field.getAnnotation(annclazz).nullable()) {
					resDto.setRespCode(EventCode.WEB_PARAMEMPTY);
					return resDto;
				}
			}
			if (!"".equals(field.getAnnotation(annclazz).defaultregx())
					&& !String.valueOf(value).matches(field.getAnnotation(annclazz).defaultregx())) {
				if (field.getAnnotation(annclazz).throwable() == false) {
					resDto.setRespCode(EventCode.WEB_PARAMFORMAT);
					return resDto;
				}
			}
			int j = value.length();
			int maxlength = field.getAnnotation(annclazz).maxsize();
			int length = field.getAnnotation(annclazz).length();
			if (maxlength != 0 && j > maxlength) {
				if (field.getAnnotation(annclazz).throwable()) {
					resDto.setRespCode(EventCode.WEB_PARAM_LENGTH_OVERFLOW);
					return resDto;
				}
			}
			if (length != 0 && j != length) {
				if (field.getAnnotation(annclazz).throwable()) {
					resDto.setRespCode(EventCode.WEB_PARAM_LENGTH);
					return resDto;
				}
			}
			if (field.getAnnotation(annclazz).regx() != null && !"".equals(field.getAnnotation(annclazz).regx())) {
				if (!String.valueOf(value).matches(field.getAnnotation(annclazz).regx())) {
					if (field.getAnnotation(annclazz).throwable()) {
						resDto.setRespCode(EventCode.WEB_PARAMFORMAT);
						return resDto;
					}
				}
			}
		}
		return resDto;
	}

}

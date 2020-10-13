package com.frizo.ucc.server.utils.converter;

import com.frizo.ucc.server.service.notice.NoticeType;
import org.springframework.core.convert.converter.Converter;

public class String2NoticeTypeEnumConverter implements Converter<String, NoticeType> {
    @Override
    public NoticeType convert(String s) {
        return NoticeType.valueOf(s.toUpperCase());
    }

}

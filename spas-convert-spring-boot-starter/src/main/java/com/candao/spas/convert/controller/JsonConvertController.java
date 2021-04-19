package com.candao.spas.convert.controller;

import com.candao.spas.convert.sdk.utils.JsonCovertUtils;
import com.candao.spas.convert.web.ResponseData;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/")
public class JsonConvertController {

    @Getter
    @Setter
    public static class RequestBean{
        private String source;
        private String agreement;
    }

    @PostMapping(value = "/convert",consumes = "application/json")
    public ResponseData convertJson(@RequestBody RequestBean bean) throws Exception {
        if (StringUtils.isEmpty(bean.getSource()) || StringUtils.isEmpty(bean.getAgreement())){
            return ResponseData.generateFail("请求参数不能为空");
        }

        Map<?,?> result =  JsonCovertUtils.convert(bean.getSource(),bean.getAgreement());

        return ResponseData.generateSuccess(result);
    }
}

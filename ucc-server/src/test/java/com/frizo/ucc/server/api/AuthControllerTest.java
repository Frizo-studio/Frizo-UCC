package com.frizo.ucc.server.api;

import com.frizo.ucc.server.UccServerApplication;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = UccServerApplication.class)
@WebAppConfiguration
public class AuthControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private HttpHeaders httpHeaders;
    private MockMvc mvc; //創建MockMvc類的物件



    @Before
    public void setup(){
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json");
    }

    @Test
    public void testLogin() throws Exception {
        JSONObject request = new JSONObject();
        request.put("email", "jjarvan1110@gmail.com");
        request.put("password", "1234567890");
        String uri = "/auth/login";
        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .post(uri)
                .headers(httpHeaders)
                .content(request.toString())
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        MockHttpServletResponse response = result.getResponse();
        response.setCharacterEncoding("UTF-8");
        int status = response.getStatus();
        System.out.println(status);
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testRegister() throws Exception {
        JSONObject request = new JSONObject();
        request.put("name", "SpringBootTester");
        request.put("email", "SpringBootTester@gmail.com");
        request.put("password", "1234567890");
        String uri = "/auth/signup";
        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .post(uri)
                .headers(httpHeaders)
                .content(request.toString())
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        MockHttpServletResponse response = result.getResponse();
        response.setCharacterEncoding("UTF-8");
        int status = response.getStatus();
        System.out.println(status);
        System.out.println(result.getResponse().getContentAsString());
    }


    public static void main(String[] args) {
        String a = String.format("%%%s%%", "老王");
        System.out.println(a);
    }

}

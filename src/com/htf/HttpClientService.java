package com.htf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.KeyFactory;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipInputStream;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * Title: HttpClientService<br/>
 * Description: 调用汇添富接口<br/>
 * Company: gigold<br/>
 * 
 * @author xiebin
 * @date 2015年11月10日下午4:54:14
 *
 */
public class HttpClientService {
    // 建立http连接超时，单位毫秒
    private static int CONNECT_TIMEOUT = 30 * 1000;
    private static int SO_TIMEOUT = 30 * 1000;
    
    // 私钥 用于我们提交的数据加密
 	public static final String PRIVATE_KEY = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBANJwlPjJaR5XGkx4KcK5UyVi8YAHrZp1xEhMTkvY7PxUX+mzvo5ruaAaPzIEgsgeNqCRJq9B8frzzRgZcQkUrKLMIO1RtNH+98lgP1dGp/gR0RYRxtlcSOzH4r4XdHCELc2WX7Le8Pv+4x4qIspRD5TvnAIARhZg2EavqteK8D8BAgMBAAECgYEAsllV+FACtgMON9bzVaPpUts7X82iFTFQDBIXnXKYzvXXX+LFUkD8TjNQcdye1mm81HKQPaJ/Kbesj4soYJDUCY8XlcEjiRSjTJsEb3euL9DBukBNFL6ffXectnc863PNP6PjAufpHVYjDUtCjj1mJpyHA17mxdTMSwDgBxK6HN0CQQD255rKJeFtyUwnF9frc22rK3pIkh6AeRoLIfKpN/EiiNzoC8TeVZfzBbgofFSWwmI9Qhd0A6NcUdrUQ+urZddvAkEA2jEZvifd4GxQT7E7WpuPRLQTh1rlrZF2Yq6wokVHe/fJICP5PCavIrzbTO4u+E4hBvAnKpkReCNBDCgFo42YjwJANUwrksV4kx1n7exstKu3GcMJ445/PRZnM84BNtwXlm/a19Bqj+AEK/apGTw1elG9qSGSNH3wjqYwya9buSUDywJBAI6qtE5l+uoXaJvzIwAn3xHpPSuv/6XyCxqTZuNzsT4Z0uS1IO53zORF4I743rV1QJssxC9STt3jD1FHighbOXkCQQDtH7f5nhkK4hVOWA2bewQTayg1DRpX3sQmKni4pQt9NdljDUSMqo+iV5rlKqsz9C5l/Y/DoyflJ4LfD2dlj5Tw";
 	// 公钥 用于对返回的数据解密
 	public static final String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDScJT4yWkeVxpMeCnCuVMlYvGAB62adcRITE5L2Oz8VF/ps76Oa7mgGj8yBILIHjagkSavQfH6880YGXEJFKyizCDtUbTR/vfJYD9XRqf4EdEWEcbZXEjsx+K+F3RwhC3Nll+y3vD7/uMeKiLKUQ+U75wCAEYWYNhGr6rXivA/AQIDAQAB";

    /**
     * 
     * Title: setTimeOut<br/>
     * Description: 设置超时<br/>
     * 
     * @author xiebin
     * @date 2015年11月10日上午10:52:47
     *
     * @param httpclient
     */
    public void setTimeOut(HttpClient httpclient) {
        // 请求超时
        httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, CONNECT_TIMEOUT);
        // 读取超时
        httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, SO_TIMEOUT);

    }

    /**
     * 
     * Title: setHeader<br/>
     * Description: 设置请求头<br/>
     * 
     * @author xiebin
     * @date 2015年11月10日上午10:54:56
     *
     * @param httpPost
     */
    public void setHeader(HttpPost httpPost) {
        httpPost.setHeader("accept", "*/*");
        httpPost.setHeader("connection", "Keep-Alive");
        httpPost.setHeader("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
        // httpPost.setHeader("Content-Type", "application/json");
    }

    /**
     * 
     * Title: httpPost<br/>
     * Description: 往汇添富发接口<br/>
     * 
     * @author xiebin
     * @date 2015年11月10日上午10:40:47
     *
     * @param url
     * @param postData
     * @return
     */
    public String httpPost(String url, String postData) {
        String responseData = "";
        HttpClient httpclient = getHttpClient();

        HttpPost httppost = createPostMethed(url);
        // 设置超时
        setTimeOut(httpclient);
        setHeader(httppost);

        try {
            setRequestParams(httppost, postData);
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }

        try {
            HttpResponse response = httpclient.execute(httppost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                /* 读返回数据 */
                responseData = EntityUtils.toString(response.getEntity(), "UTF-8");
            } else {
            }
        } catch (ClientProtocolException e) {
        } catch (IOException e) {
        }

        return responseData;
    }

    /**
     * 
     * Title: getHTFRequstBody<br/>
     * Description: 获取请求HTF的报文 <br/>
     * 
     * @author xiebin
     * @date 2015年11月10日上午10:27:58
     *
     * @param jsonData
     * @return
     */
    @SuppressWarnings("deprecation")
    public String getHTFRequstBody(String jsonData) {
        String[] reqmsgs = getRequestMsg(jsonData, PRIVATE_KEY);
        StringBuffer postMsg = new StringBuffer();
        postMsg.append("sign=").append(URLEncoder.encode(reqmsgs[0])).append("&msg=")
                .append(URLEncoder.encode(reqmsgs[1]));
        return postMsg.toString();

    }

    /**
     * 
     * Title: convertHtfResponseBody<br/>
     * Description: htf返回报文解签并转化成Map<br/>
     * 
     * @author xiebin
     * @date 2015年11月10日上午10:37:57
     *
     * @param responeMsg
     * @return
     */
    public Map<String, Object> convertHtfResponseBody(String responeMsg) {
        Map<String, Object> responseMap = new HashMap<String, Object>();
        // 回复报文
        String sign = responeMsg.substring("sign=".length(), responeMsg.indexOf("&msg="));
        String msg = responeMsg.substring(responeMsg.indexOf("&msg=") + 5).trim();
        String respPlain = decryptDataWithPubkey(PUBLIC_KEY, sign, msg);
        responseMap = jsonToMap(respPlain);
        return responseMap;
    }

    /**
     * 
     * Title: httpClientRequest<br/>
     * POST 请求访问接口 交互数据格式 JSON: <br/>
     * 
     * @author xiebin
     * @date 2015年11月6日下午1:40:14
     *
     * @param url
     * @param dto
     * @return
     */
    public Map<String, Object> httpClientRequest(String url, Map<String, ?> paramMap) {
        

        Map<String, Object> responseMap = new HashMap<String, Object>();
        JSONObject jsonObject = JSONObject.fromObject(paramMap);
        // 获取HTF请求报文
        String htfRequestBody = getHTFRequstBody(jsonObject.toString());
        String htfResponseBody = "";
        // 调用httpClient访问htf接口
        if (StringUtils.isNotBlank(htfRequestBody)) {
        	HttpClientService httpClientService = new HttpClientService();
            htfResponseBody = httpClientService.httpPost(url,htfRequestBody);
        }
        // 解析HFT返回报文
        if (StringUtils.isNotBlank(htfResponseBody)) {
            try {
                responseMap = convertHtfResponseBody(htfResponseBody);
            } catch (Exception e) {
                responseMap = new HashMap<String, Object>();
            }
        }
        return responseMap;
    }

    public HttpPost createPostMethed(String url) {
        HttpPost httpPost = new HttpPost(url);
        return httpPost;
    }

    public HttpGet createGettMethed(String url) {
        return new HttpGet(url);
    }

    public HttpClient getHttpClient() {
        return new DefaultHttpClient();
    }

    /**
     * 
     * Title: setProxy<br/>
     * 有需要则设置代理: <br/>
     * 
     * @author xiebin
     * @date 2015年11月6日下午1:41:54
     *
     * @param httpclient
     * @param ip
     * @param port
     */
    public void setProxy(DefaultHttpClient httpclient, String proxyHost, int proxyPort, String userName,
            String password) {
        httpclient.getCredentialsProvider().setCredentials(new AuthScope(proxyHost, proxyPort),
                new UsernamePasswordCredentials(userName, password));
        HttpHost proxy = new HttpHost(proxyHost, proxyPort);
        httpclient.getParams().setParameter(ConnRouteParams.DEFAULT_PROXY, proxy);

    }

    /**
     * 
     * Title: setRequestParams<br/>
     * 设置请求参数 和请求头: <br/>
     * 
     * @author xiebin
     * @date 2015年11月6日下午1:41:18
     *
     * @param httppost
     * @param dto
     * @throws UnsupportedEncodingException
     */
    public void setRequestParams(HttpPost httppost, String requestData) throws UnsupportedEncodingException {

        StringEntity entity = new StringEntity(requestData, "UTF-8");// 解决中文乱码问题
        entity.setContentEncoding("UTF-8");
        httppost.setEntity(entity);
    }

    /**
     * 
     * Title: getRequestMsg<br/>
     * Description: 获取加密后的报文<br/>
     * 
     * @author xiebin
     * @date 2015年11月10日上午10:43:16
     *
     * @param post
     * @param privateKey
     * @return
     */
    public String[] getRequestMsg(String post, String privateKey) {
        String param[] = new String[2];
        try {
            BASE64Encoder base64encoder = new BASE64Encoder();
            String content = post;
            content = base64encoder.encode(content.getBytes("UTF-8"));
            String econ = envolopData(privateKey, content);
            content = URLEncoder.encode(content, "UTF-8");
//            debug("进行加密后数据，msg=" + content + "\n sign=" + econ);
            param[0] = econ;
            param[1] = content;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return param;
    }

    public String envolopData(String privKey, String dataStr) {
        String plainText = "";
        try {
            BASE64Decoder base64decoder = new BASE64Decoder();
            BASE64Encoder base64encoder = new BASE64Encoder();
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(base64decoder.decodeBuffer(privKey));
            KeyFactory fac = KeyFactory.getInstance("RSA");

            RSAPrivateKey privateKey = (RSAPrivateKey) fac.generatePrivate(keySpec);
            Signature sigEng = Signature.getInstance("SHA1withRSA");
            sigEng.initSign(privateKey);
            sigEng.update(dataStr.getBytes("UTF-8"));
            byte[] signature = sigEng.sign();
            plainText = base64encoder.encodeBuffer(signature);
            plainText = plainText.replaceAll("\r|\n", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return plainText;
    }

    public String decryptDataWithPubkey(String publicKey, String sign, String msg) {
        String plainText = "";
        try {
            boolean verifyFlag = false;
            msg = URLDecoder.decode(msg, "UTF-8");
//            debug("DecryptDataWithPubkey msg=" + msg + "  ,pubkey=" + publicKey);
            verifyFlag = rsaVerify(publicKey, sign, msg, "UTF-8");
            if (verifyFlag) {
                BASE64Decoder base64decoder = new BASE64Decoder();
                plainText = new String(base64decoder.decodeBuffer(msg), "UTF-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        if ("".equals(plainText)) {
            return null;
        }
        return plainText;
    }

    private boolean rsaVerify(String pubKey, String sign, String src, String encoding) {
        boolean rs = false;
        try {
            BASE64Decoder base64decoder = new BASE64Decoder();
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(base64decoder.decodeBuffer(pubKey));
            KeyFactory fac = KeyFactory.getInstance("RSA");
            RSAPublicKey rsaPubKey = (RSAPublicKey) fac.generatePublic(keySpec);

            Signature sigEng = Signature.getInstance("SHA1withRSA");
            sigEng.initVerify(rsaPubKey);
            sigEng.update(src.getBytes(encoding));
            byte[] signature = base64decoder.decodeBuffer(sign);
            rs = sigEng.verify(signature);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
    }

    public byte[] Ectract(byte[] inBytes) throws IOException {
        byte[] retBy = null;
        ByteArrayOutputStream bos = null;
        ZipInputStream zins = new ZipInputStream(new ByteArrayInputStream(inBytes));
        byte[] ch = new byte[256];
        while (zins.getNextEntry() != null) {
            {
                bos = new ByteArrayOutputStream(inBytes.length);
                int i;
                while ((i = zins.read(ch)) != -1)
                    bos.write(ch, 0, i);
                retBy = bos.toByteArray();
                zins.closeEntry();
                bos.close();
            }
        }
        zins.close();
        return retBy;
    }
    
    @SuppressWarnings("unchecked")
	public static Map<String, Object> jsonToMap(String jsonStr)  {
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> maps=null;
        try {
			maps = objectMapper.readValue(jsonStr, Map.class);
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return maps;
	}

    @SuppressWarnings("deprecation")
	public static void main(String[] args) {
         String
         msg="JTdCYXV0aENvZGUlM0Q1Njg0MzklMkMrc3BiaWxsX2NyZWF0ZV9pcCUzRDEyNy4wLjAuMSUyQytz%0D%0AaWduX3R5cGUlM0QlMkMraW5wdXRfY2hhcnNldCUzRFVURi04JTJDK3BhcnRuZXJfYWNjb19ubyUz%0D%0ARFUxNTExMTExNjE1MzcxMTElMkMrYnV5ZXJfbmFtZSUzRFpIT1VXRUklMkMrYmFua19jb2RlJTNE%0D%0ANjUyNDAwMDAwMDIzMTU2Nzk4NSUyQyt0cmFkZV9zdGF0dXMlM0QyJTJDK3NlcnZpY2VfdmVyc2lv%0D%0AbiUzRDEuMCUyQyt0cmFuc2FjdGlvbl9pZCUzRDEyMzQ1Njc4OTAxMjMlMkMrc2lnbiUzRCUyQyti%0D%0AYW5rX2FjY28lM0QxMDMlMkMrcmV0Q29kZSUzRDIwMTAlMkMrbW9iaWxlX251bWJlciUzRDEzNjEx%0D%0AMjM2NzQyJTJDK3NwX3BhcnRuZXIlM0QxMDAwMDAwMDQwJTJDK3JldE1zZyUzREYwMDI1JTVCc3Bi%0D%0AaWxsX2NyZWF0ZV9pcCU1RCUyQytidXllcl9jZXJ0X3R5cGUlM0QxJTJDK2J1eWVyX2NlcnRfbm8l%0D%0AM0QxMzIzMzYxOTgxMDExNjIwMTglMkMrYWNjX3RpbWUlM0QyMDE1MTIzMDE0MjQxNCU3RA%3D%3D";
         String
         sign="QgdgxIrcHGggMIQdND7LKmU3z7fSojDEM/EY/l6FFeKyGd3NYQMP9INgGZo1 E/jWDoJxTv60SmkZwfjZ/CasTRBA1jv7m16iGQ9 c5N0EfriVTwvVBGdKRCEgQ6JBZLkM/bG9HI4RxVtuAsCUZBBy dtUm19UkdAMjH6SVhtGQ=";
         String
         publicKey="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDScJT4yWkeVxpMeCnCuVMlYvGAB62adcRITE5L2Oz8VF/ps76Oa7mgGj8yBILIHjagkSavQfH6880YGXEJFKyizCDtUbTR/vfJYD9XRqf4EdEWEcbZXEjsx+K+F3RwhC3Nll+y3vD7/uMeKiLKUQ+U75wCAEYWYNhGr6rXivA/AQIDAQAB";
         String ss=new HttpClientService().decryptDataWithPubkey(publicKey,URLDecoder.decode(sign),URLDecoder.decode(msg));
         String ss1=new HttpClientService().decryptDataWithPubkey(publicKey,sign,msg);
         System.out.println(ss);
         System.out.println(URLDecoder.decode(ss1));

//        String jsonStr = "{\"attach\":\"\",\"bank_acco\":\"62270721557943495320\",\"bank_code\":\"607\",\"buyer_cert_no\":\"420802198207150315\",\"buyer_cert_type\":\"0\",\"buyer_name\":\"李四\",\"fundAccoNo\":\"1006105011\",\"input_charset\":\"UTF-8\",\"modify_time\":\"20151118113052\",\"notify_type\":\"1\",\"partner_acco_no\":\"U151111161537112\",\"retcode\":\"0\",\"retmsg\":\"开户成功\",\"service_version\":\"1.0\",\"sign_type\":\"RSA\",\"sp_partner\":\"1000000040\",\"trade_status\":\"0\",\"tran_time\":\"20151118113052\",\"transaction_id\":\"2015111811313356645700011001\",\"user_tp\":\"0\"}";
    }

}

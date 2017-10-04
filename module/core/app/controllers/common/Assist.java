package controllers.common;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.qiniu.util.Json;
import com.qiniu.util.StringMap;

import annotations.Api;
import annotations.Param;
import annotations.Return;
import models.core.assist.Attachment;
import models.core.common.ResponseData;
import models.core.common.Simditor;
import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;
import play.Play;
import play.cache.Cache;
import play.db.jpa.Blob;
import play.libs.Images;
import play.libs.MimeTypes;
import play.mvc.Controller;
import plugins.router.Get;
import plugins.router.Post;

public class Assist extends Controller {

	/**
	 * 验证码
	 */
	@Api(name = "验证码", type = Api.HttpType.GET, url = "/captcha", ret={@Return(clazz=InputStream.class)})
	@Get("/captcha")
	public static void captcha() {
		Images.Captcha captcha = Images.captcha();
		captcha.addNoise();
		String code = captcha.getText();
		code = code.toLowerCase();
		Cache.add(code, code, "2mn");
		renderBinary(captcha);
	}

	/**
	 * 生成二维码
	 * 
	 * @param code
	 */
	@Post("/qrcode")
	@Api(name = "生成二维码", type = Api.HttpType.POST, url = "/qrcode", param= {@Param(clazz=String.class, name="code")}, ret={@Return(clazz=InputStream.class)})
	public static void qrcode(String code) {
		response.setContentTypeIfNotSet("image/png");
		try {
			code = URLDecoder.decode(code, "utf-8");
		} catch (UnsupportedEncodingException e) {
			renderJSON(ResponseData.response(false, "转换code失败"));
		}
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(QRCode.from(code).to(ImageType.PNG).withSize(500, 500).stream().toByteArray());
		renderBinary(byteArrayInputStream);
	}

	/**
	 * 文件上传
	 * 
	 * @param file
	 */
	@Post("/upload")
	@Api(name = "文件上传", type = Api.HttpType.POST, url = "/upload", param= {@Param(clazz=File.class, name="file")}, ret={@Return(clazz=Simditor.class)})
	public static void upload(File file) {
		String ATTACHMENTS_TYPE = Play.configuration.getProperty("attachments.type");
		if(ATTACHMENTS_TYPE.equalsIgnoreCase("local")){
			localUpload(file);
		}else if(ATTACHMENTS_TYPE.equalsIgnoreCase("qiniu")){
			qiniuUpload(file);
		}else{
			renderJSON(ResponseData.response(false, "not support other upload store"));
		}
		
		
	}
	
	private static void localUpload(File file){
		String ImageServerDomain = Play.configuration.getProperty("image.server.domain","http://127.0.0.1:9000");
		Attachment upload = new Attachment();
		try {
			upload.file = new Blob();
			upload.name = file.getName();
			upload.file.set(new FileInputStream(file), MimeTypes.getContentType(file.getName()));
			upload.store_type = "local";
			upload.save();
			Gson g = new GsonBuilder().disableHtmlEscaping().create();
			renderJSON(g.toJson(new Simditor(true, "upload success", ImageServerDomain + "/download?id=" + upload.id)));

		} catch (Exception e) {
			e.printStackTrace();
			renderJSON(new Simditor(false, "upload failure", ""));
		}

	}
	
	private static void qiniuUpload(File file){
		String ACCESS_KEY = Play.configuration.getProperty("qiniu.access_key");
		String SECRET_KEY = Play.configuration.getProperty("qiniu.secret_key");
		String BUCKETNAME = Play.configuration.getProperty("qiniu.bucketname");
		String DOMAIN = Play.configuration.getProperty("qiniu.domain");
		
		if (StringUtils.isEmpty(ACCESS_KEY) || StringUtils.isEmpty(SECRET_KEY) || StringUtils.isEmpty(BUCKETNAME) ||  StringUtils.isEmpty(DOMAIN)) {
			renderJSON(ResponseData.response(false, "please config qiniu param"));
		} else {
			Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
			UploadManager uploadManager = new UploadManager(new Configuration(Zone.autoZone()));
			try {
				// 调用put方法上传
				Response res = uploadManager.put(file.getPath(), RandomStringUtils.randomAlphanumeric(6) + "/" + file.getName(), auth.uploadToken(BUCKETNAME));
				
				// 打印返回的信息
				renderJSON(new Simditor(true, "upload success", DOMAIN +"/"+ res.jsonToMap().get("key")));
			} catch (QiniuException e) {
				Response r = e.response;
				// 请求失败时打印的异常的信息
				System.out.println(r.toString());
				try {
					// 响应的文本信息
					System.out.println(r.bodyString());
				} catch (QiniuException e1) {
					// ignore
				}
			} 
			renderJSON(new Simditor(false, "upload failure", ""));
		}
	}
	
	
	public static String qiniuUploadBase64(String base64) {
		String ACCESS_KEY = Play.configuration.getProperty("qiniu.access_key");
		String SECRET_KEY = Play.configuration.getProperty("qiniu.secret_key");
		String BUCKETNAME = Play.configuration.getProperty("qiniu.bucketname");
		String DOMAIN = Play.configuration.getProperty("qiniu.domain");

		if (StringUtils.isEmpty(ACCESS_KEY) || StringUtils.isEmpty(SECRET_KEY) || StringUtils.isEmpty(BUCKETNAME) || StringUtils.isEmpty(DOMAIN)) {
			renderJSON(ResponseData.response(false, "please config qiniu param"));
		} else {
			Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
			String token = auth.uploadToken(BUCKETNAME, null, 3600, new StringMap().put("insertOnly", 1));
			try {
				String url = "http://up-z2.qiniu.com/putb64/-1";
				// 构造post对象
				HttpPost post = new HttpPost(url);
				post.addHeader("Content-Type", "application/octet-stream");
				post.addHeader("Authorization", "UpToken " + token);
				post.setEntity(new StringEntity(base64));

				// 请求与响应
				HttpClient c = HttpClientBuilder.create().build();
				HttpResponse res = c.execute(post);

				// 输出
				System.out.println(res.getStatusLine());
				String responseBody = EntityUtils.toString(res.getEntity(), "UTF-8");
				
				return DOMAIN + Json.decode(responseBody).get("key");

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return "";
	}
	
	@Get("/download")
	public static void download(long id,String p){
		Attachment attachment = Attachment.findById(id);
		try{
			if(attachment.store_type.equals("local")){
				response.setContentTypeIfNotSet(attachment.file.type());
				renderBinary(attachment.file.get(), attachment.name);
			}else if(attachment.store_type.equals("qiniu")){
				redirect(attachment.store_url + (p==null?"":"?"+p));
			}
		}catch(Exception e){
			e.printStackTrace();
			notFound();
		}
	}

	@Get("/qiniu/token")
	public static void qntoken() {
		String ACCESS_KEY = Play.configuration.getProperty("qiniu.access_key");
		String SECRET_KEY = Play.configuration.getProperty("qiniu.secret_key");
		String BUCKETNAME = Play.configuration.getProperty("qiniu.bucketname");

		if (StringUtils.isEmpty(ACCESS_KEY) || StringUtils.isEmpty(SECRET_KEY) || StringUtils.isEmpty(BUCKETNAME)) {
			renderJSON(ResponseData.response(false, "please config qiniu param"));
		} else {
			Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
			Map<String, String> token = new HashMap<String, String>();
			token.put("uptoken", auth.uploadToken(BUCKETNAME));
			renderJSON(token);
		}
	}
	

}

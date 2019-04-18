package utils;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

/**
 * 二维码图片生成
 * @author wsz
 * @date 2019年4月18日
 */
public class QrCodeUtils {

	private static String LOGO_URL= "logo/logo.png";
	private static String IMG_URL = "qrcode/";
	private static String CHARSET = "utf-8";
    private static String FORMAT = "PNG";
    private static String SUFFIX = ".png";
    private static int CODE_WIDTH = 300;
    private static int CODE_HEIGHT = 300;
   
    /**
     * 纯二维码
     * @param content
     * @return
     */
    public static String withoutLogo(String content) {
    	return withoutLogo(content, null);
    }
    
    /**
     * 纯二维码
     * @param content 二维码数据
     * @param path 保存路径
     * @return
     */
    public static String withoutLogo(String content, String path) {
    	String imgName = getImgName();
    	String qrCodeUrl = defaultPath(path) + imgName + SUFFIX;
    	try {
    		BufferedImage image = createBuffered(content);
			ImageIO.write(image, FORMAT, new File(qrCodeUrl));
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return qrCodeUrl;
    }
    
    /**
     * 带有Logo图片的二维码
     * @param content	二维码内容
     * @return
     */
    public static String withLogo(String content) {
    	return withLogo(content, null, null);
    }
    /**
     * 带有Logo图片的二维码
     * @param content	二维码内容
     * @param logoUrl	logo图片路径
     * @return
     */
    public static String withLogo(String content, String logoUrl) {
    	return withLogo(content, null, logoUrl);
    }
    /**
     * 带有Logo图片的二维码
     * @param content 二维码内容
     * @param path 二维码保存路径
     * @param logoUrl logo图片路径
     * @return
     */
    public static String withLogo(String content, String path, String logoUrl) {
    	String imgName = getImgName();
    	String qrCodeUrl = defaultPath(path) + imgName + SUFFIX;
    	try {
    		// 图片数据
    		BufferedImage source = createBuffered(content);
    		// 获取logo宽高
    		BufferedImage logoBuffer = ImageIO.read(new File(defaultLogoPath(logoUrl)));
	        int width = logoBuffer.getWidth(null);
	        int height = logoBuffer.getHeight(null);
	        // 上下左右居中
	        Graphics2D graph = source.createGraphics();
	        int x = (CODE_WIDTH - width) / 2;
	        int y = (CODE_HEIGHT - height) / 2;
	        graph.drawImage(logoBuffer, x, y, width, height, null);
	        graph.setStroke(new BasicStroke(3f));
	        graph.setColor(Color.white);
	        graph.draw(new RoundRectangle2D.Float(x, y, width, height, 2, 2));
	        graph.dispose();
	        source.flush();
	        // 写入
	        ImageIO.write(source, FORMAT, new File(qrCodeUrl));
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return qrCodeUrl;
    }
    
    private static String getImgName() {
    	return UUID.randomUUID().toString().replace("-", "").substring(0, 5);
    }
    
    private static String defaultPath(String path) {
    	return path == null ? IMG_URL : path;
    }
    
    private static String defaultLogoPath(String logoPath) throws Exception {
    	logoPath = logoPath == null ? LOGO_URL : logoPath;
        File file = new File(logoPath);
        if (!file.exists()) {
            throw new Exception("logoPath:文件不存在");
        }
    	return logoPath;
    }
    
    /**
     * 图片数据缓存
     * @param content
     * @return
     * @throws Exception
     */
    private static BufferedImage createBuffered(String content) throws Exception {
    	ConcurrentHashMap<EncodeHintType, Object> hints = new ConcurrentHashMap<EncodeHintType, Object>(4);
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
        hints.put(EncodeHintType.MARGIN, 1);
		BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, CODE_WIDTH, CODE_HEIGHT, hints);
		int width = bitMatrix.getWidth();
		int height = bitMatrix.getHeight();
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
			}
		}
        return image;
    }
}

package com.hali.test;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.imageio.ImageIO;

public class BroYangPic {

	static int MAX_LEN = 1;

	public static void main(String[] args) {
		
		System.out.println("TestPic.main() \001");
		System.out.println("TestPic.main() mm lent:" + mm.length());

		MAX_LEN = mm.length() / 2;

		try {
			BufferedImage src = ImageIO.read(new File("res/羊哥.jpg"));
			BufferedImage dest = greyFilter(src);

			File f = new File("res/羊哥-灰度图.jpg");
			if (!f.exists()) {
				f.createNewFile();
			}
			ImageIO.write(dest, "jpg", f);

			String tt = outStr;
			tt.replace(" ", "&nbsp;");

			String css = "";

			FileReader fr = new FileReader("res/style.css");
			BufferedReader br = new BufferedReader(fr);
			String line;
			while ((line = br.readLine()) != null) {
				css += line;
			}
			br.close();

			BufferedWriter bw = new BufferedWriter(new FileWriter(new File("res/羊哥.html")));
			bw.write(css + tt);
			bw.flush();
			bw.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 根据灰度生成相应字符

	static char toText(int g) {
		char out = ' ';
		if (g <= 30) {
			out = '#';
		} else if (g > 30 && g <= 60) {
			out = '&';
		} else if (g > 60 && g <= 120) {
			out = '$';
		} else if (g > 120 && g <= 150) {
			out = '*';
		} else if (g > 150 && g <= 180) {
			out = 'o';
		} else if (g > 180 && g <= 210) {
			out = '!';
		} else if (g > 210 && g <= 240) {
			out = ';';
		} else {
			out = ' ';
		}

		return out;
	}

	static String outStr = "";
	static String mm = "!;;;;;;;;;;;;;;;;;;;;;;;!!;;;!!!;;;;!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!";

	public static int clamp(int value) {
		return value > 255 ? 255 : (value < 0 ? 0 : value);
	}

	public static BufferedImage greyFilter(BufferedImage src) {
		// 获得源图片长度和宽度
		int width = src.getWidth();
		int height = src.getHeight();
		BufferedImage dest = new BufferedImage(width, height, src.getType());
		int[] inPixels = new int[width * height];
		int[] outPixels = new int[width * height];
		src.getRGB(0, 0, width, height, inPixels, 0, width);

		// 计算一个像素的红，绿，蓝方法
		int index = 0, ta, tr, tg, tb;
		float tgray;

		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				index = row * width + col;

				ta = (inPixels[index] >> 24) & 0xff;
				tr = (inPixels[index] >> 16) & 0xff;
				tg = (inPixels[index] >> 8) & 0xff;
				tb = inPixels[index] & 0xff;

				tgray = 0.299f * tr + 0.578f * tg + 0.114f * tb;
				int val = (int) tgray;
				val = clamp(val);

				outPixels[index] = (ta << 24) | val << 16 | val << 8 | val;

			}
		}
		dest.setRGB(0, 0, width, height, outPixels, 0, width);

		int stride = width / MAX_LEN;
		stride = width % MAX_LEN > 0 ? stride + 1 : stride;

		outStr += "<p>";
		for (int row = 0; row < height; row += stride) {
			for (int col = 0; col < width; col += stride / 2) {
				index = row * width + col;

				ta = (inPixels[index] >> 24) & 0xff;
				tr = (inPixels[index] >> 16) & 0xff;
				tg = (inPixels[index] >> 8) & 0xff;
				tb = inPixels[index] & 0xff;

				tgray = 0.299f * tr + 0.578f * tg + 0.114f * tb;
				int val = (int) tgray;
				val = clamp(val);

				outPixels[index] = (ta << 24) | val << 16 | val << 8 | val;

				outStr += toText(val);
			}
			outStr += "</p><p>";
		}
		outStr += "</p>";

		return dest;
	}

}

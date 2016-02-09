package net.reederhome.colin.mods.JAPTA;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class UpdateCheckThread implements Runnable {

	static String ret = null;
	String ver;
	
	public UpdateCheckThread(String version) {
		ver = version;
	}
	
	@Override
	public void run() {
		try {
			URL url = new URL("http://colin.reederhome.net/mc/modupdatecheck.php/vpzomtrrfrt/JAPTA?v="+ver);
			URLConnection conn = url.openConnection();
			InputStreamReader isr = new InputStreamReader(conn.getInputStream());
			BufferedReader br = new BufferedReader(isr);
			String line;
			while(true) {
				line = br.readLine();
				if(line == null) {
					break;
				}
				else {
					System.out.println(line);
					if(line.equalsIgnoreCase("update")) {
						ret = "update";
					}
				}
			}
			if(ret == null) {
				ret = "OK";
			}
		} catch (MalformedURLException e) {
			ret = e.getMessage();
		} catch (IOException e) {
			ret = e.getMessage();
		}
		System.out.println(ret);
	}

}